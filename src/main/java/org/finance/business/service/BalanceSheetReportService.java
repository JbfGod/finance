package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.BalanceSheetReport;
import org.finance.business.mapper.BalanceSheetReportMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * <p>
 * 利润报表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-11-04
 */
@Service
public class BalanceSheetReportService extends ServiceImpl<BalanceSheetReportMapper, BalanceSheetReport> {


    public List<BalanceSheetReport> list(int yearMonth) {
        return baseMapper.selectList(Wrappers.<BalanceSheetReport>lambdaQuery()
                .eq(BalanceSheetReport::getYearMonthNum, yearMonth)
                .orderByAsc(BalanceSheetReport::getSerialNumber)
        );
    }

    public Map<Integer, BalanceSheetReport.Row> calcBalanceSheet(List<BalanceSheetReport> balanceSheets,
                                                                 Map<String, AccountBalance> balanceBySubjectNumber) {
        Map<Integer, String> formulaByRowNum = new HashMap<>();
        balanceSheets.forEach(balanceSheet -> {
            String assetsFormula = balanceSheet.getAssetsFormula();
            String equityFormula = balanceSheet.getEquityFormula();
            if (StringUtils.isNotBlank(assetsFormula)) {
                formulaByRowNum.put(balanceSheet.getAssetsRowNum(), assetsFormula);
            }
            if (StringUtils.isNotBlank(equityFormula)) {
                formulaByRowNum.put(balanceSheet.getEquityRowNum(), equityFormula);
            }
        });

        Map<Integer, BalanceSheetReport.Row> valueByRowNum = new HashMap<>();
        for (BalanceSheetReport balanceSheet : balanceSheets) {
            Integer assetsRowNumber = balanceSheet.getAssetsRowNum();
            Integer equityRowNumber = balanceSheet.getEquityRowNum();
            balanceSheet.setAssetsRow(calcByRowNum(assetsRowNumber, formulaByRowNum, balanceBySubjectNumber, valueByRowNum));
            balanceSheet.setEquityRow(calcByRowNum(equityRowNumber, formulaByRowNum, balanceBySubjectNumber, valueByRowNum));
        }

        return valueByRowNum;
    }

    public BalanceSheetReport.Row calcByRowNum(Integer rowNumber, Map<Integer, String> formulaByRowNum,
                                               Map<String, AccountBalance> balanceBySubjectNumber,
                                               Map<Integer, BalanceSheetReport.Row> valueByRowNum) {
        BalanceSheetReport.Row row = valueByRowNum.get(rowNumber);
        if (row != null) {
            return row;
        }
        row = new BalanceSheetReport.Row();
        String formula = formulaByRowNum.get(rowNumber);
        List<String> parts = BalanceSheetReport.splitFormula(formula);
        if (parts.isEmpty()) {
            valueByRowNum.put(rowNumber, row);
            return row;
        }
        int partLen = parts.size();
        boolean isRowNumExpression = BalanceSheetReport.isRowNumFormula(formula);
        for (int i = 0; i < partLen; i++) {
            String part = parts.get(i);
            String operationalSymbol = part.substring(0, 1);
            String partValue = part.substring(1);
            BiFunction<BigDecimal, BigDecimal, BigDecimal> calcFunc = "+".equals(operationalSymbol)
                    ? BigDecimal::add
                    // "-"
                    : BigDecimal::subtract;

            if (isRowNumExpression) {
                BalanceSheetReport.Row rowByRowNum = calcByRowNum(
                        Integer.valueOf(partValue), formulaByRowNum, balanceBySubjectNumber, valueByRowNum
                );
                row.setOpeningAmount(calcFunc.apply(row.getOpeningAmount(), rowByRowNum.getOpeningAmount()))
                    .setClosingAmount(calcFunc.apply(row.getClosingAmount(), rowByRowNum.getClosingAmount()));
            } else {
                AccountBalance accountBalance = Optional.ofNullable(balanceBySubjectNumber.get(partValue)).orElseGet(AccountBalance::newInstance);
                row.setOpeningAmount(calcFunc.apply(row.getOpeningAmount(), accountBalance.getLocalOpeningBalance()))
                    .setClosingAmount(calcFunc.apply(row.getClosingAmount(), accountBalance.getLocalClosingBalance()));
            }
        }
        valueByRowNum.put(rowNumber, row);
        return row;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void save(int yearMonth, List<BalanceSheetReport> balanceSheetReports) {
        baseMapper.delete(Wrappers.<BalanceSheetReport>lambdaQuery()
                .eq(BalanceSheetReport::getYearMonthNum, yearMonth)
        );
        this.saveBatch(balanceSheetReports);
    }

}
