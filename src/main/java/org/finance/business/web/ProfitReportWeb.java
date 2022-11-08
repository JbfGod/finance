package org.finance.business.web;

import org.finance.business.convert.ReportConvert;
import org.finance.business.manage.ReportManage;
import org.finance.business.service.ProfitReportService;
import org.finance.business.web.request.QueryProfitReportRequest;
import org.finance.business.web.request.SaveProfitReportRequest;
import org.finance.business.web.vo.ProfitOfMonthVO;
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
 * @author jiangbangfa
 */
@RestController
@RequestMapping("/api/profitReport")
public class ProfitReportWeb {

    @Resource
    private ProfitReportService baseService;
    @Resource
    private ReportManage reportManage;

    @PostMapping("/save")
    public R saveProfitReport(@Valid @RequestBody SaveProfitReportRequest request) {
        baseService.save(request.getYearMonthNum(), ReportConvert.INSTANCE.toProfitReports(request));
        return R.ok();
    }

    @GetMapping("/list")
    public R<List<ProfitOfMonthVO>> listProfitOfMonth(@Valid QueryProfitReportRequest request) {
        return R.ok(reportManage.listProfit(request));
    }
}
