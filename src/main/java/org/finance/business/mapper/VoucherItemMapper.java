package org.finance.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.finance.business.entity.VoucherItem;
import org.finance.business.mapper.dto.DailyVoucherItemDTO;
import org.finance.business.mapper.param.QueryVoucherItemOfSubLegerParam;

import java.time.LocalDate;
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

    List<VoucherItem> summaryMonthGroupBySubject(@Param("yearMonthNum") int yearMonthNum);

    List<VoucherItem> summaryMonthGroupBySubjectAndCurrency(@Param("yearMonthNum") int yearMonthNum);

    List<VoucherItem> summaryByCurrencyGroupBySubject(@Param("yearMonthNum") int yearMonthNum, @Param("currencyName") String currencyName);

    List<VoucherItem> listByMonthAndCurrency(@Param("param") QueryVoucherItemOfSubLegerParam param);

    List<VoucherItem> summaryGroupBySubjectAndCurrency(@Param("yearMonthNum") int yearMonthNum, @Param("voucherDate") LocalDate voucherDate, @Param("currency") String currency);

    List<DailyVoucherItemDTO> summaryDailyGroupBySubjectAndCurrency(@Param("voucherDate") LocalDate voucherDate, @Param("currency") String currency);
}
