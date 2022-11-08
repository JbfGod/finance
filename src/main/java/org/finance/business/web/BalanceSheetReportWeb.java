package org.finance.business.web;

import org.finance.business.convert.ReportConvert;
import org.finance.business.manage.ReportManage;
import org.finance.business.service.BalanceSheetReportService;
import org.finance.business.web.request.QueryBalanceSheetReportRequest;
import org.finance.business.web.request.SaveBalanceSheetReportRequest;
import org.finance.business.web.vo.BalanceSheetOfMonthVO;
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
 * 利润报表 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-11-04
 */
@RestController
@RequestMapping("/api/balanceSheetReport")
public class BalanceSheetReportWeb {

    @Resource
    private BalanceSheetReportService baseService;
    @Resource
    private ReportManage reportManage;

    @PostMapping("/save")
    public R saveBalanceSheetReport(@Valid @RequestBody SaveBalanceSheetReportRequest request) {
        baseService.save(request.getYearMonthNum(), ReportConvert.INSTANCE.toBalanceSheetReports(request));
        return R.ok();
    }

    @GetMapping("/list")
    public R<List<BalanceSheetOfMonthVO>> listBalanceSheetOfMonth(@Valid QueryBalanceSheetReportRequest request) {
        return R.ok(reportManage.listBalanceSheet(request));
    }
}
