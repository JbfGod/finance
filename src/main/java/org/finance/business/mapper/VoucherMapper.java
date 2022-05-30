package org.finance.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;
import org.finance.business.entity.Voucher;
import org.finance.business.entity.enums.AuditStatus;
import org.finance.business.web.vo.VoucherBookVO;

import java.util.List;
import java.util.stream.Collectors;

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

    default List<Long> selectIdsOfToBeConfirmed(Integer yearMonth, Integer beginSerialNum, Integer endSerialNum) {
        return this.selectList(Wrappers.<Voucher>lambdaQuery()
                .select(Voucher::getId)
                .eq(Voucher::getYearMonthNum, yearMonth)
                .eq(Voucher::getAuditStatus, AuditStatus.AUDITED)
                .between(beginSerialNum != null && endSerialNum != null,
                        Voucher::getSerialNumber, beginSerialNum, endSerialNum)
        ).stream().map(Voucher::getId).collect(Collectors.toList());
    }

    IPage<VoucherBookVO> selectVoucherBookVO(IPage<?> page);
}
