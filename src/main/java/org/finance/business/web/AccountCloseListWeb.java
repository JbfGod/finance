package org.finance.business.web;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.AccountCloseListConvert;
import org.finance.business.entity.AccountCloseList;
import org.finance.business.service.AccountCloseListService;
import org.finance.business.web.request.CancelClosedAccountRequest;
import org.finance.business.web.request.CloseAccountRequest;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

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

    @PostMapping("/close")
    public R closeAccount(@Valid CloseAccountRequest request) {
        baseService.assertAccountNotClosed(request.getYearMonthNum());
        baseService.save(AccountCloseListConvert.INSTANCE.toAccountCloseList(request));
        return R.ok();
    }

    @PostMapping("/cancel")
    public R cancelClosedAccount(@Valid CancelClosedAccountRequest request) {
        Long proxyCustomerId = SecurityUtil.getProxyCustomerId();
        baseService.remove(Wrappers.<AccountCloseList>lambdaQuery()
                .eq(AccountCloseList::getCustomerId, proxyCustomerId)
                .eq(AccountCloseList::getYearMonthNum, request.getYearMonthNum())
        );
        return R.ok();
    }

}
