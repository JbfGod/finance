package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.finance.business.entity.CashFlowReport;
import org.finance.business.entity.VoucherItem;
import org.finance.business.mapper.CashFlowReportMapper;
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
 * 现金流量报表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-11-04
 */
@Service
public class CashFlowReportService extends ServiceImpl<CashFlowReportMapper, CashFlowReport> {

    public List<CashFlowReport> list(int yearMonth) {
        return baseMapper.selectList(Wrappers.<CashFlowReport>lambdaQuery()
                .eq(CashFlowReport::getYearMonthNum, yearMonth)
                .orderByAsc(CashFlowReport::getSerialNumber)
        );
    }

    public Map<Integer, CashFlowReport.Row> calcBalanceSheet(List<CashFlowReport> cashFlows,
                                                             Map<Long, VoucherItem> balanceByVoucherItemId) {
        Map<Integer, String> formulaByRowNum = cashFlows.stream()
                .filter(cashFlow -> StringUtils.isNotBlank(cashFlow.getFormula()))
                .collect(Collectors.toMap(CashFlowReport::getRowNumber, CashFlowReport::getFormula));

        Map<Integer, CashFlowReport.Row> valueByRowNum = new HashMap<>();
        for (CashFlowReport cashFlow : cashFlows) {
            calcByRowNum(cashFlow.getRowNumber(), formulaByRowNum, balanceByVoucherItemId, valueByRowNum);
        }

        return valueByRowNum;
    }

    public CashFlowReport.Row calcByRowNum(Integer rowNumber, Map<Integer, String> formulaByRowNum,
                                               Map<Long, VoucherItem> balanceByVoucherItemId,
                                               Map<Integer, CashFlowReport.Row> valueByRowNum) {
        CashFlowReport.Row row = valueByRowNum.get(rowNumber);
        if (row != null) {
            return row;
        }
        row = new CashFlowReport.Row();
        String formula = formulaByRowNum.get(rowNumber);
        List<String> parts = CashFlowReport.splitFormula(formula);
        if (parts.isEmpty()) {
            valueByRowNum.put(rowNumber, row);
            return row;
        }
        int partLen = parts.size();
        boolean isRowNumExpression = CashFlowReport.isRowNumFormula(formula);
        for (int i = 0; i < partLen; i++) {
            String part = parts.get(i);
            String operationalSymbol = part.substring(0, 1);
            String partValue = part.substring(1);
            BiFunction<BigDecimal, BigDecimal, BigDecimal> calcFunc = "+".equals(operationalSymbol)
                    ? BigDecimal::add
                    // "-"
                    : BigDecimal::subtract;

            if (isRowNumExpression) {
                CashFlowReport.Row rowByRowNum = calcByRowNum(
                        Integer.valueOf(partValue), formulaByRowNum, balanceByVoucherItemId, valueByRowNum
                );
                row.setMonthlyAmount(calcFunc.apply(row.getMonthlyAmount(), rowByRowNum.getMonthlyAmount()))
                    .setAnnualAmount(calcFunc.apply(row.getAnnualAmount(), rowByRowNum.getAnnualAmount()));
            } else {
                VoucherItem voucherItem = Optional.ofNullable(balanceByVoucherItemId.get(Long.valueOf(partValue)))
                        .orElseGet(VoucherItem::new);
                row.setMonthlyAmount(calcFunc.apply(row.getMonthlyAmount(), voucherItem.getCreditAmount()))
                    .setAnnualAmount(calcFunc.apply(row.getAnnualAmount(), voucherItem.getCreditAmount()));
            }
        }
        valueByRowNum.put(rowNumber, row);
        return row;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(int yearMonth, List<CashFlowReport> cashFlowReports) {
        baseMapper.delete(Wrappers.<CashFlowReport>lambdaQuery()
                .eq(CashFlowReport::getYearMonthNum, yearMonth)
        );
        this.saveBatch(cashFlowReports);
    }

}
