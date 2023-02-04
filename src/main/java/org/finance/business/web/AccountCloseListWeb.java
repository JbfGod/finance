package org.finance.business.web;

import org.finance.business.entity.AccountCloseList;
import org.finance.business.service.AccountCloseListService;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.constants.Constants;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @PostMapping("/closePreviousPeriod")
    public R closePreviousPeriod() {
        Integer currentPeriod = baseService.currentAccountCloseYearMonth();
        Integer enablePeriod = SecurityUtil.getProxyCustomer().getEnablePeriod();
        AssertUtil.isFalse(currentPeriod.equals(enablePeriod), "上一期不存在");
        baseService.cancelClosedAccount(Integer.parseInt(
                YearMonth.parse(currentPeriod.toString(), Constants.YEAR_MONTH_FMT).minusMonths(1)
                        .format(Constants.YEAR_MONTH_FMT)
        ));
        return R.ok();
    }

    @PostMapping("/closeNextPeriod")
    public R closeNextPeriod() {
        Integer currentPeriod = baseService.currentAccountCloseYearMonth();
        baseService.closeAccount(new AccountCloseList(currentPeriod));
        return R.ok();
    }

}
