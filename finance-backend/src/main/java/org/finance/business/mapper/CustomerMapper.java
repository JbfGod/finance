package org.finance.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.entity.Customer;

/**
 * <p>
 * 客户表 Mapper 接口
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
public interface CustomerMapper extends BaseMapper<Customer> {

    default Customer findByAccountName(String accountName) {
        return selectOne(Wrappers.<Customer>lambdaQuery().eq(Customer::getAccount, accountName));
    }
}
