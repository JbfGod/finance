package org.finance.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.finance.business.entity.VoucherItem;

import java.util.List;

/**
 * <p>
 * 凭证项 Mapper 接口
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-25
 */
public interface VoucherItemMapper extends BaseMapper<VoucherItem> {

    List<VoucherItem> summaryGroupBySubjectId(@Param("yearMonthNum") int yearMonthNum);

}
