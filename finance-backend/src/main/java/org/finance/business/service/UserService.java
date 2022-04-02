package org.finance.business.service;

import org.finance.business.entity.User;
import org.finance.business.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

}
