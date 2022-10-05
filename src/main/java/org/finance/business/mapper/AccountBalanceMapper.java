package org.finance.business.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;
import org.finance.business.entity.AccountBalance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 科目余额 Mapper 接口
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-09-16
 */
public interface AccountBalanceMapper extends BaseMapper<AccountBalance> {

    List<AccountBalance> summaryGroupBySubjectId(@Param("yearMonthNum") int yearMonthNum);

    default List<AccountBalance> listByYearMonth(int yearMonthNum) {
        return  this.selectList(Wrappers .<AccountBalance>lambdaQuery().eq(AccountBalance::getYearMonthNum, yearMonthNum));
    }

}
