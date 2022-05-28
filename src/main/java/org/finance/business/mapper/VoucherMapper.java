package org.finance.business.mapper;

import org.apache.ibatis.annotations.Param;
import org.finance.business.entity.Voucher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 凭证 Mapper 接口
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-25
 */
public interface VoucherMapper extends BaseMapper<Voucher> {

    void resetSerialNumber(@Param("yearMonthNum") Integer yearMonthNum);

    Integer getMaxSerialNumber(@Param("yearMonthNum") Integer yearMonthNum);
}
