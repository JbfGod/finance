package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Function;
import org.finance.business.entity.UserFunction;
import org.finance.business.mapper.UserFunctionMapper;
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
public class UserFunctionService extends ServiceImpl<UserFunctionMapper, UserFunction> {

    @Transactional(rollbackFor = Exception.class)
    public void grantFunctionsToUser(long userId, List<Long> functionIds) {
        baseMapper.delete(Wrappers.<UserFunction>lambdaQuery().eq(UserFunction::getUserId, userId));
        List<UserFunction> userFunctions = functionIds.stream()
                .map(funcId -> new UserFunction().setUserId(userId).setFunctionId(funcId))
                .collect(Collectors.toList());
        this.saveBatch(userFunctions);
    }

    public List<Function> getFunctionsByUserId(long userId) {
        return baseMapper.listFunctionByUserId(userId);
    }

}
