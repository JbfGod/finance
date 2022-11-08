package org.finance.business.web;

import org.finance.business.convert.ReportConvert;
import org.finance.business.manage.ReportManage;
import org.finance.business.service.CashFlowReportService;
import org.finance.business.web.request.QueryCashFlowReportRequest;
import org.finance.business.web.request.SaveCashFlowReportRequest;
import org.finance.business.web.vo.CashFlowOfMonthVO;
import org.finance.infrastructure.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 现金流量报表 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-11-04
 */
@RestController
@RequestMapping("/api/cashFlowReport")
public class CashFlowReportWeb {

    @Resource
    private CashFlowReportService baseService;
    @Resource
    private ReportManage reportManage;

    @PostMapping("/save")
    public R saveCashFlowReport(@Valid @RequestBody SaveCashFlowReportRequest request) {
        baseService.save(request.getYearMonthNum(), ReportConvert.INSTANCE.toCashFlowReports(request));
        return R.ok();
    }

    @GetMapping("/list")
    public R<List<CashFlowOfMonthVO>> listCashFlowOfMonth(@Valid QueryCashFlowReportRequest request) {
        return R.ok(reportManage.listCashFlow(request));
    }

}
