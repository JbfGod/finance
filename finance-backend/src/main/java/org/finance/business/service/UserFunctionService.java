package org.finance.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.UserFunction;
import org.finance.business.mapper.UserFunctionMapper;
import org.springframework.stereotype.Service;

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

}
