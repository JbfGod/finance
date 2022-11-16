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

    public Map<Integer, BigDecimal> calcCashFlow(List<CashFlowReport> cashFlows,
                                                 Map<Long, VoucherItem> voucherItemById) {
        Map<Integer, String> formulaByRowNum = cashFlows.stream()
                .filter(cashFlow -> StringUtils.isNotBlank(cashFlow.getFormula()))
                .collect(Collectors.toMap(CashFlowReport::getRowNum, CashFlowReport::getFormula));

        Map<Integer, BigDecimal> valueByRowNum = new HashMap<>();
        for (CashFlowReport cashFlow : cashFlows) {
            cashFlow.setAmount(calcByRowNum(cashFlow.getRowNum(), cashFlow.getFormula(), formulaByRowNum, voucherItemById, valueByRowNum));
        }

        return valueByRowNum;
    }

    public BigDecimal calcByRowNum(Integer rowNumber, String formula,
                                   Map<Integer, String> formulaByRowNum,
                                   Map<Long, VoucherItem> voucherItemById,
                                   Map<Integer, BigDecimal> valueByRowNum) {
        BigDecimal amount = valueByRowNum.get(rowNumber);
        if (amount != null) {
            return amount;
        }
        amount = new BigDecimal("0");
        List<String> parts = CashFlowReport.splitFormula(formula);
        if (parts.isEmpty()) {
            if (rowNumber > 0) {
                valueByRowNum.put(rowNumber, amount);
            }
            return amount;
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
                int rowNum = Integer.parseInt(partValue);
                BigDecimal rowByRowNum = Optional.ofNullable(valueByRowNum.get(rowNum)).orElseGet(() ->
                    calcByRowNum(
                        rowNum, formulaByRowNum.get(rowNum) , formulaByRowNum, voucherItemById, valueByRowNum
                    )
                );
                amount = calcFunc.apply(amount, rowByRowNum);
            } else {
                VoucherItem voucherItem = Optional.ofNullable(voucherItemById.get(Long.valueOf(partValue)))
                        .orElseGet(VoucherItem::new);
                amount = calcFunc.apply(amount, voucherItem.getLocalDebitAmount().subtract(voucherItem.getCreditAmount()));
            }
        }
        if (rowNumber > 0) {
            valueByRowNum.put(rowNumber, amount);
        }
        return amount;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(int yearMonth, List<CashFlowReport> cashFlowReports) {
        baseMapper.delete(Wrappers.<CashFlowReport>lambdaQuery()
                .eq(CashFlowReport::getYearMonthNum, yearMonth)
        );
        this.saveBatch(cashFlowReports);
    }

}
