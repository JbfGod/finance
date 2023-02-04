package org.finance.business.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.entity.AccountCloseList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 关账列表 Mapper 接口
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-07-12
 */
public interface AccountCloseListMapper extends BaseMapper<AccountCloseList> {

    default boolean alreadyAccountClose(int yearMonthNum) {
        return this.exists(Wrappers.<AccountCloseList>lambdaQuery()
                .eq(AccountCloseList::getYearMonthNum, yearMonthNum)
        );
    }
}
