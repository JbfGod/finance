package org.finance.business.service;

import cn.hutool.core.lang.func.Supplier2;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.AggregateFormula;
import org.finance.business.entity.Report;
import org.finance.business.entity.ReportFormula;
import org.finance.business.entity.enums.Symbol;
import org.finance.business.mapper.AggregateFormulaMapper;
import org.finance.business.mapper.ReportFormulaMapper;
import org.finance.business.mapper.ReportMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 报表条目 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2023-01-20
 */
@Service
public class ReportService extends ServiceImpl<ReportMapper, Report> {

    @Resource
    private ReportFormulaMapper reportFormulaMapper;
    @Resource
    private AggregateFormulaMapper aggregateFormulaMapper;


    public List<Report> listWithFormula(Wrapper<Report> queryWrapper) {
        List<Report> reports = baseMapper.selectList(queryWrapper);
        reports.forEach(report -> {
            report.setReportFormulas(reportFormulaMapper.selectList(Wrappers.<ReportFormula>lambdaQuery()
                    .eq(ReportFormula::getReportId, report.getId())
            ));
            report.setAggregateFormulas(aggregateFormulaMapper.selectList(Wrappers.<AggregateFormula>lambdaQuery()
                    .eq(AggregateFormula::getAggregateReportId, report.getId())
            ));
        });
        return reports;
    }

    public void recursion(List<Report> reports, Map<String, AccountBalance> balanceBySubNumber) {
        Map<Integer, Report> reportById = reports.stream().collect(Collectors.toMap(Report::getId, v -> v));
        reports.stream().filter(report -> report.getFormulaType() > 0)
                .forEach(report -> {
                    calcReport(report, reportById, balanceBySubNumber);
                });
    }

    public void calcReport(Report report, Map<Integer, Report> reportById, Map<String, AccountBalance> balanceBySubNumber) {
        if (report.getFlag() != null && report.getFlag()) {
            return;
        }
        report.setBeginBalance(BigDecimal.ZERO)
                .setEndBalance(BigDecimal.ZERO)
                .setAnnualAmount(BigDecimal.ZERO)
                .setCurrentPeriodAmount(BigDecimal.ZERO);
        report.setFlag(true);
        Integer formulaType = report.getFormulaType();
        if (Report.REPORT_FORMULA == formulaType) {
            List<ReportFormula> reportFormulas = report.getReportFormulas();
            if (reportFormulas == null) {
                return;
            }
            reportFormulas.forEach(next -> {
                Symbol symbol = next.getSymbol();
                Supplier2<BigDecimal, BigDecimal, BigDecimal> calc = symbol == Symbol.ADD ? BigDecimal::add : BigDecimal::subtract;
                String subNumber = next.getNumber();
                ReportFormula.NumberRule numberRule = next.getNumberRule();
                AccountBalance balance = balanceBySubNumber.get(subNumber);
                if (balance == null) {
                    return;
                }
                switch (numberRule) {
                    case BALANCE:
                        report.setBeginBalance(calc.get(report.getBeginBalance(), balance.getBeginningBalance()));
                        report.setEndBalance(calc.get(report.getEndBalance(), balance.getLocalClosingBalance()));
                        break;
                    case DEBIT_BALANCE:
                        report.setBeginBalance(calc.get(report.getBeginBalance(), balance.getBeginningBalance()));
                        report.setEndBalance(calc.get(report.getEndBalance(), balance.getDebitClosingAmount()));
                        break;
                    case CREDIT_BALANCE:
                        report.setBeginBalance(calc.get(report.getBeginBalance(), balance.getBeginningBalance()));
                        report.setEndBalance(calc.get(report.getEndBalance(), balance.getCreditClosingAmount()));
                        break;
                    case AMOUNT:
                        report.setAnnualAmount(calc.get(report.getAnnualAmount(), balance.getAnnualAmount()));
                        report.setCurrentPeriodAmount(calc.get(report.getCurrentPeriodAmount(), balance.getCreditClosingAmount()));
                        break;
                }
            });
        } else if (Report.AGGREGATE_FORMULA == formulaType || Report.TOTAL_FORMULA == formulaType ) {
            List<AggregateFormula> aggregateFormulas = report.getAggregateFormulas();
            if (aggregateFormulas == null) {
                return;
            }
            aggregateFormulas.forEach(formula -> {
                Symbol symbol = formula.getSymbol();
                Supplier2<BigDecimal, BigDecimal, BigDecimal> calc = symbol == Symbol.ADD ? BigDecimal::add : BigDecimal::subtract;
                Report part = reportById.get(formula.getReportId());
                calcReport(part, reportById, balanceBySubNumber);
                report.setBeginBalance(calc.get(report.getBeginBalance(), part.getBeginBalance()));
                report.setEndBalance(calc.get(report.getEndBalance(), part.getEndBalance()));
            });
        }
    }

}
