package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.CustomerFunction;
import org.finance.business.mapper.CustomerFunctionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户的功能列表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-09
 */
@Service
public class CustomerFunctionService extends ServiceImpl<CustomerFunctionMapper, CustomerFunction> {

    @Transactional(rollbackFor = Exception.class)
    public void grantFunctionsToUser(long customerId, List<Long> functionIds) {
        baseMapper.delete(Wrappers.<CustomerFunction>lambdaQuery().eq(CustomerFunction::getCustomerId, customerId));
        List<CustomerFunction> customerFunctions = functionIds.stream()
                .map(funcId -> new CustomerFunction().setCustomerId(customerId).setFunctionId(funcId))
                .collect(Collectors.toList());
        this.saveBatch(customerFunctions);
    }

}
