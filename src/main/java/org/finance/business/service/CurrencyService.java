package org.finance.business.service;

import org.finance.business.entity.Currency;
import org.finance.business.mapper.CurrencyMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 货币汇率列表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-05-25
 */
@Service
public class CurrencyService extends ServiceImpl<CurrencyMapper, Currency> {

}
