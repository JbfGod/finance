package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.ProfitReport;
import org.finance.business.mapper.ProfitReportMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * <p>
 * 利润报表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-10-25
 */
@Service
public class ProfitReportService extends ServiceImpl<ProfitReportMapper, ProfitReport> {

    public List<ProfitReport> list(int yearMonth) {
        return baseMapper.selectList(Wrappers.<ProfitReport>lambdaQuery()
                .eq(ProfitReport::getYearMonthNum, yearMonth)
                .orderByAsc(ProfitReport::getSerialNumber)
        );
    }

    public Map<Integer, ProfitReport.ProfitParam> calcProfit(List<ProfitReport> profits, Map<String, AccountBalance> balanceBySubjectNumber) {
        Map<Integer, String> formulaByRowNum = profits.stream()
                .filter(profitReport -> StringUtils.isNotBlank(profitReport.getFormula()))
                .collect(Collectors.toMap(ProfitReport::getRowNumber, ProfitReport::getFormula));

        Map<Integer, ProfitReport.ProfitParam> valueByRowNum = new HashMap<>();
        for (ProfitReport profit : profits) {
            Integer rowNumber = profit.getRowNumber();
            if (rowNumber == null || rowNumber == 0) {
                valueByRowNum.put(rowNumber, new ProfitReport.ProfitParam());
                continue;
            }
            calcByRowNum(rowNumber, formulaByRowNum, balanceBySubjectNumber, valueByRowNum);
        }

        return valueByRowNum;
    }

    public ProfitReport.ProfitParam calcByRowNum(Integer rowNumber, Map<Integer, String> formulaByRowNum,
                                                 Map<String, AccountBalance> balanceBySubjectNumber,
                                                 Map<Integer, ProfitReport.ProfitParam> valueByRowNum) {
        ProfitReport.ProfitParam profitParam = valueByRowNum.get(rowNumber);
        if (profitParam != null) {
            return profitParam;
        }
        profitParam = new ProfitReport.ProfitParam();
        String formula = formulaByRowNum.get(rowNumber);
        List<String> parts = ProfitReport.splitFormula(formula);
        if (parts.isEmpty()) {
            valueByRowNum.put(rowNumber, profitParam);
            return profitParam;
        }
        int partLen = parts.size();
        boolean isRowNumExpression = ProfitReport.isRowNumFormula(formula);
        for (int i = 0; i < partLen; i++) {
            String part = parts.get(i);
            String operationalSymbol = part.substring(0, 1);
            String partValue = part.substring(1);
            BiFunction<BigDecimal, BigDecimal, BigDecimal> calcFunc = "+".equals(operationalSymbol)
                    ? BigDecimal::add
                    // "-"
                    : BigDecimal::subtract;

            if (isRowNumExpression) {
                ProfitReport.ProfitParam profitParamByRowNum = calcByRowNum(Integer.valueOf(partValue), formulaByRowNum, balanceBySubjectNumber, valueByRowNum);
                profitParam.setMonthlyAmount(calcFunc.apply(profitParam.getMonthlyAmount(), profitParamByRowNum.getMonthlyAmount()))
                        .setAnnualAmount(calcFunc.apply(profitParam.getAnnualAmount(), profitParamByRowNum.getAnnualAmount()));
            } else {
                AccountBalance accountBalance = Optional.ofNullable(balanceBySubjectNumber.get(partValue)).orElseGet(AccountBalance::newInstance);
                profitParam.setMonthlyAmount(calcFunc.apply(profitParam.getMonthlyAmount(), accountBalance.getCurrentAmount()))
                        .setAnnualAmount(calcFunc.apply(profitParam.getAnnualAmount(), accountBalance.getAnnualAmount()));
            }
        }
        valueByRowNum.put(rowNumber, profitParam);
        return profitParam;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(int yearMonth, List<ProfitReport> profitReports) {
        baseMapper.delete(Wrappers.<ProfitReport>lambdaQuery()
                .eq(ProfitReport::getYearMonthNum, yearMonth)
        );
        this.saveBatch(profitReports);
    }
}
