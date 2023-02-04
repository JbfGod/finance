package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.AccountCloseList;
import org.finance.business.mapper.AccountCloseListMapper;
import org.finance.business.service.event.AccountEvent;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.constants.Constants;
import org.finance.infrastructure.util.SpringContextUtil;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Collection;

/**
 * <p>
 * 关账列表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-07-12
 */
@Service
public class AccountCloseListService extends ServiceImpl<AccountCloseListMapper, AccountCloseList> {

    public Integer currentAccountCloseYearMonth() {
        AccountCloseList accountCloseList = baseMapper.selectOne(Wrappers.<AccountCloseList>lambdaQuery()
                .orderByDesc(AccountCloseList::getYearMonthNum)
                .last("limit 1")
        );
        if (accountCloseList == null) {
            return SecurityUtil.getProxyCustomer().getEnablePeriod();
        }
        YearMonth yearMonth = YearMonth.parse(accountCloseList.getYearMonthNum().toString(), Constants.YEAR_MONTH_FMT);
        return Integer.valueOf(Constants.YEAR_MONTH_FMT.format(yearMonth.plusMonths(1)));
    }

    public void closeAccount(AccountCloseList accountClose) {
        baseMapper.insert(accountClose);

        Collection<AccountEvent> accountEvents = SpringContextUtil.getBeansOfType(AccountEvent.class);
        accountEvents.forEach(accountEvent -> {
            accountEvent.onAccountClosed(YearMonth.parse(accountClose.getYearMonthNum().toString(), Constants.YEAR_MONTH_FMT));
        });
    }

    public void cancelClosedAccount(int yearMonthNum) {
        baseMapper.delete(Wrappers.<AccountCloseList>lambdaQuery()
            .eq(AccountCloseList::getYearMonthNum, yearMonthNum)
        );

        Collection<AccountEvent> accountEvents = SpringContextUtil.getBeansOfType(AccountEvent.class);
        accountEvents.forEach(accountEvent -> {
            accountEvent.onCancelClosedAccount(YearMonth.parse(String.valueOf(yearMonthNum), Constants.YEAR_MONTH_FMT));
        });
    }

}
