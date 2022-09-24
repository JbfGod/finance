package org.finance.business.web;

import org.finance.business.manage.ReportManage;
import org.finance.business.web.request.QueryAccountBalanceRequest;
import org.finance.business.web.vo.AccountBalanceVO;
import org.finance.infrastructure.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author jiangbangfa
 */
@RestController
@RequestMapping("/api/report")
public class ReportWeb {

    @Resource
    private ReportManage reportManage;

    @GetMapping("/accountBalance")
    public R<List<AccountBalanceVO>> accountBalance(@Valid QueryAccountBalanceRequest request) {
        return R.ok(reportManage.listAccountBalance(request.getYearMonth()));
    }

    @GetMapping("/generalLedger")
    public R generalLedger() {
        return R.ok();
    }

    @GetMapping("/subLedger")
    public R subLedger() {
        return R.ok();
    }

    @GetMapping("/dailyCash")
    public R dailyCash() {
        return R.ok();
    }

    @GetMapping("/dailyBank")
    public R dailyBank() {
        return R.ok();
    }

}