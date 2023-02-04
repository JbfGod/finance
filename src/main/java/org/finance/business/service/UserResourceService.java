package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Resource;
import org.finance.business.entity.UserResource;
import org.finance.business.entity.enums.ResourceModule;
import org.finance.business.entity.enums.ResourceOperate;
import org.finance.business.mapper.UserResourceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public void grantResourcesToUser(long userId, List<String> resourceWithOperateIds) {
        Map<Long, List<String>> permitCodesByResourceId = new HashMap<>(20);
        resourceWithOperateIds.forEach(resourceOperateId -> {
            boolean isResourceOperateId = resourceOperateId.startsWith(ResourceOperate.ID_PREFIX);
            if (isResourceOperateId) {
                ResourceOperate.consumerResourceOperateId(resourceOperateId, (resourceId, permitCode) -> {
                    List<String> permitCodes = permitCodesByResourceId.computeIfAbsent(resourceId, (r) -> new ArrayList<>());
                    permitCodes.add(permitCode);
                });
                return;
            }
            permitCodesByResourceId.putIfAbsent(Long.valueOf(resourceOperateId), new ArrayList<>());
        });
        baseMapper.delete(Wrappers.<UserResource>lambdaQuery().eq(UserResource::getUserId, userId));
        List<UserResource> userResources = permitCodesByResourceId.keySet().stream()
                .map(rId -> new UserResource().setUserId(userId).setResourceId(rId)
                        .setPermitCode(String.join(",", permitCodesByResourceId.get(rId)))
                )
                .collect(Collectors.toList());
        this.saveBatch(userResources);
    }

    public List<Resource> getResourcesByUserId(long userId) {
        return baseMapper.listResourceByUserId(userId);
    }

    public List<Resource> getResourcesByUserIdAndModule(long userId, ResourceModule module) {
        return baseMapper.listResourceByUserIdAndModule(userId, module);
    }

}
