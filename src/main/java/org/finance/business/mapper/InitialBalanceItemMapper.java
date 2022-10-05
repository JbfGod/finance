package org.finance.business.mapper;

import org.apache.ibatis.annotations.Param;
import org.finance.business.entity.InitialBalanceItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 初始余额条目 Mapper 接口
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-07-21
 */
public interface InitialBalanceItemMapper extends BaseMapper<InitialBalanceItem> {

    List<InitialBalanceItem> summaryGroupBySubject(@Param("initialBalanceId") long initialBalanceId);

}
