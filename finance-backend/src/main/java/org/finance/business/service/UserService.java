package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Customer;
import org.finance.business.entity.Function;
import org.finance.business.entity.User;
import org.finance.business.mapper.CustomerMapper;
import org.finance.business.mapper.FunctionMapper;
import org.finance.business.mapper.UserFunctionMapper;
import org.finance.business.mapper.UserMapper;
import org.finance.infrastructure.config.security.CustomerUserService;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.util.AssertUtil;
import org.finance.infrastructure.util.CacheAttr;
import org.finance.infrastructure.util.CacheKeyUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements CustomerUserService {

    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private UserFunctionMapper userFunctionMapper;
    @Resource
    private FunctionMapper functionMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public User loadUserByCustomerAndAccount(String customerAccount, String account) throws UsernameNotFoundException {
        CacheAttr cacheAttr = CacheKeyUtil.getUser(customerAccount, account);
        Object cacheUser = redisTemplate.opsForValue().get(cacheAttr.getKey());
        if (cacheUser != null) {
            return (User) cacheUser;
        }
        User user = baseMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getCustomerAccount, customerAccount)
                .eq(User::getAccount, account)
        );
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (user.getCustomerId() != 0) {
            Customer customer = customerMapper.findByAccountName(customerAccount);
            if (customer == null) {
                throw new UsernameNotFoundException("用户不存在");
            }
            user.setCustomer(customer);
        }
        redisTemplate.opsForValue().set(cacheAttr.getKey(), user, cacheAttr.getTimeout());
        return user;
    }

    @Override
    @Cacheable(value = "User", unless = "#result == null")
    public User loadUserById(Long userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    @Cacheable("User")
    public List<GrantedAuthority> loadAuthoritiesByUserId(Long userId) {
        return userFunctionMapper.listFunctionByUserId(userId)
                .stream().map(Function::getPermitCode)
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Cacheable("User")
    public List<Function> getFunctionsByUserId(Long userId) {
        return userFunctionMapper.listFunctionByUserId(userId);
    }

    public void updatePassword(long userId, String password) {
        baseMapper.updateById(new User().setId(userId).setPassword(SecurityUtil.encodePassword(password)));
    }

    public void addUser(User user) {
        boolean exists = this.baseMapper.exists(Wrappers.<User>lambdaQuery()
                .eq(User::getCustomerId, user.getCustomerId())
                .eq(User::getAccount, user.getAccount())
        );
        AssertUtil.isFalse(exists, "用户账号已存在！");
        baseMapper.insert(user);
    }
}
