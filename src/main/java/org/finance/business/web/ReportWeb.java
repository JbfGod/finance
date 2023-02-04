package org.finance.business.web;

import org.finance.business.entity.templates.accountting.system.AssetsLiability;
import org.finance.business.entity.templates.accountting.system.CashFlow;
import org.finance.business.entity.templates.accountting.system.Profit;
import org.finance.business.manage.ReportManage;
import org.finance.business.manage.report.AssetsLiabilityManage;
import org.finance.business.manage.report.CashFlowManage;
import org.finance.business.manage.report.ProfitManage;
import org.finance.business.web.request.QueryAccountBalanceRequest;
import org.finance.business.web.request.QueryAssetsLiabilityRequest;
import org.finance.business.web.request.QueryDailyBankRequest;
import org.finance.business.web.request.QueryDailyCashRequest;
import org.finance.business.web.request.QueryGeneralLedgerRequest;
import org.finance.business.web.request.QuerySubLedgerRequest;
import org.finance.business.web.vo.AccountBalanceVO;
import org.finance.business.web.vo.DailyBankVO;
import org.finance.business.web.vo.DailyCashVO;
import org.finance.business.web.vo.GeneralLedgerVO;
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
    @Resource
    private AssetsLiabilityManage assetsLiabilityManage;
    @Resource
    private ProfitManage profitManage;
    @Resource
    private CashFlowManage cashFlowManage;

    @GetMapping("/assetsLiabilities")
    public R<List<AssetsLiability>> assetsLiabilities(@Valid QueryAssetsLiabilityRequest request) {
        return R.ok(assetsLiabilityManage.list(request.getYearMonth()));
    }

    @GetMapping("/profit")
    public R<List<Profit>> profit(@Valid QueryAssetsLiabilityRequest request) {
        return R.ok(profitManage.list(request.getYearMonth()));
    }

    @GetMapping("/cashFlow")
    public R<List<CashFlow>> cashFlow(@Valid QueryAssetsLiabilityRequest request) {
        return R.ok(cashFlowManage.list(request.getYearMonth()));
    }

    @GetMapping("/accountBalance")
    public R<List<AccountBalanceVO>> accountBalance(@Valid QueryAccountBalanceRequest request) {
        return R.ok(reportManage.listAccountBalance(request.getYearMonth()));
    }

    @GetMapping("/generalLedger")
    public R<List<GeneralLedgerVO>> generalLedger(@Valid QueryGeneralLedgerRequest request) {
        return R.ok(reportManage.listGeneralLedger(request.getStartMonth(), request.getEndMonth(), request.getCurrencyName()));
    }

    @GetMapping("/subLedger")
    public R subLedger(@Valid QuerySubLedgerRequest request) {
        return R.ok(reportManage.listSubLedger(request));
    }

    @GetMapping("/dailyCash")
    public R<List<DailyCashVO>> dailyCash(@Valid QueryDailyCashRequest request) {
        return R.ok(reportManage.listDailyCash(request));
    }

    @GetMapping("/dailyBank")
    public R<List<DailyBankVO>> dailyBank(@Valid QueryDailyBankRequest request) {
        return R.ok(reportManage.listDailyBank(request));
    }

}
