package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Resource;
import org.finance.business.entity.UserResource;
import org.finance.business.mapper.UserResourceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户的功能列表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-04
 */
@Service
public class UserResourceService extends ServiceImpl<UserResourceMapper, UserResource> {

    @Transactional(rollbackFor = Exception.class)
    public void grantResourcesToUser(long userId, List<Long> resourceIds) {
        baseMapper.delete(Wrappers.<UserResource>lambdaQuery().eq(UserResource::getUserId, userId));
        List<UserResource> userResources = resourceIds.stream()
                .map(funcId -> new UserResource().setUserId(userId).setResourceId(funcId))
                .collect(Collectors.toList());
        this.saveBatch(userResources);
    }

    public List<Resource> getResourcesByUserId(long userId) {
        return baseMapper.listResourceByUserId(userId);
    }

}
