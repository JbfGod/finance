package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.AccountCloseList;
import org.finance.business.mapper.AccountCloseListMapper;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;

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

    public void assertAccountNotClosed(int yearMonthNum) {
        boolean alreadyClosedAccount = baseMapper.exists(Wrappers.<AccountCloseList>lambdaQuery()
            .eq(AccountCloseList::getYearMonthNum, yearMonthNum)
        );
        AssertUtil.isFalse(alreadyClosedAccount, String.format("(%s)月份已经关账！", yearMonthNum));
    }
}
