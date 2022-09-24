package org.finance.business.web;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.AccountCloseListConvert;
import org.finance.business.entity.AccountCloseList;
import org.finance.business.entity.InitialBalance;
import org.finance.business.entity.Voucher;
import org.finance.business.service.AccountCloseListService;
import org.finance.business.service.InitialBalanceService;
import org.finance.business.service.VoucherService;
import org.finance.business.web.request.CancelClosedAccountRequest;
import org.finance.business.web.request.CloseAccountRequest;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.YearMonth;

/**
 * <p>
 * 关账列表 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-07-12
 */
@RestController
@RequestMapping("/api/account")
public class AccountCloseListWeb {

    @Resource
    private AccountCloseListService baseService;
    @Resource
    private InitialBalanceService initialBalanceService;
    @Resource
    private VoucherService voucherService;

    @PostMapping("/close")
    public R closeAccount(@Valid CloseAccountRequest request) {
        AccountCloseList accountCloseList = AccountCloseListConvert.INSTANCE.toAccountCloseList(request);
        assertCanClosed(accountCloseList);
        baseService.closeAccount(accountCloseList);
        return R.ok();
    }

    @PostMapping("/cancel")
    public R cancelClosedAccount(@Valid CancelClosedAccountRequest request) {
        baseService.cancelClosedAccount(request.getYearMonthNum());
        return R.ok();
    }

    private void assertCanClosed(AccountCloseList accountClose) {
        InitialBalance initialBalance = initialBalanceService.getOne(null);
        AssertUtil.notNull(initialBalance, "关账失败，尚未创建初始余额！");

        Integer closedYearMonth = accountClose.getYearMonthNum();
        boolean alreadyClosedAccount = baseService.count(Wrappers.<AccountCloseList>lambdaQuery()
                .eq(AccountCloseList::getYearMonthNum, closedYearMonth)
        ) > 0;
        AssertUtil.isFalse(alreadyClosedAccount, "关账失败，该月已经关账！");

        AccountCloseList previousMonthRecord = baseService.getOne(Wrappers.<AccountCloseList>lambdaQuery()
                .eq(AccountCloseList::getYearMonthNum, YearMonth.from(accountClose.getBeginDate()).minusMonths(1))
        );
        AssertUtil.notNull(previousMonthRecord, "关账失败，该月份的前一个月尚未关账！");

        boolean hasUnBookkeepingVoucher = voucherService.count(Wrappers.<Voucher>lambdaQuery()
                .eq(Voucher::getBookkeeping, false)
                .eq(Voucher::getYearMonthNum, closedYearMonth)
        ) > 0;
        AssertUtil.isFalse(hasUnBookkeepingVoucher, "关账失败，该月份存在未记账的凭证！");
    }
}
