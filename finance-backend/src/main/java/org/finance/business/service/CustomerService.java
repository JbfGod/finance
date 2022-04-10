package org.finance.business.service;

import org.finance.business.entity.Customer;
import org.finance.business.entity.User;
import org.finance.business.mapper.CustomerMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@Service
public class CustomerService extends ServiceImpl<CustomerMapper, Customer> {

    @Resource
    private UserMapper userMapper;

    public void addCustomerAndUser(Customer customer, User user) {
        baseMapper.insert(customer);
        userMapper.insert(user.setCustomerId(user.getCustomerId()));
    }
}
