package org.finance.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.finance.business.entity.Currency;

import java.util.List;

/**
 * <p>
 * 货币汇率列表 Mapper 接口
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-25
 */
public interface CurrencyMapper extends BaseMapper<Currency> {

    List<String> listGroupByCurrencyName();
}
