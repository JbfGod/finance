package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.CustomerResource;
import org.finance.business.entity.Resource;
import org.finance.business.mapper.CustomerResourceMapper;
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
public class CustomerResourceService extends ServiceImpl<CustomerResourceMapper, CustomerResource> {

    @Transactional(rollbackFor = Exception.class)
    public void grantResourcesToUser(long customerId, List<Long> resourceIds) {
        baseMapper.delete(Wrappers.<CustomerResource>lambdaQuery().eq(CustomerResource::getCustomerId, customerId));
        List<CustomerResource> customerResources = resourceIds.stream()
                .map(funcId -> new CustomerResource().setCustomerId(customerId).setResourceId(funcId))
                .collect(Collectors.toList());
        this.saveBatch(customerResources);
    }

    public List<Resource> listResourceByCustomerId(long customerId) {
        return baseMapper.listResourceByCustomerId(customerId);
    }

}
