package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Customer;
import org.finance.business.entity.Resource;
import org.finance.business.entity.User;
import org.finance.business.mapper.CustomerMapper;
import org.finance.business.mapper.UserResourceMapper;
import org.finance.business.mapper.UserMapper;
import org.finance.infrastructure.config.security.CustomerUserService;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.finance.infrastructure.exception.HxException;
import org.finance.infrastructure.util.AssertUtil;
import org.finance.infrastructure.util.CacheAttr;
import org.finance.infrastructure.util.CacheKeyUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

    @javax.annotation.Resource
    private CustomerMapper customerMapper;
    @javax.annotation.Resource
    private UserResourceMapper userResourceMapper;
    @javax.annotation.Resource
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
            throw new UsernameNotFoundException("用户名或密码不正确！");
        }
        if (user.getCustomerId() != 0) {
            Customer customer = customerMapper.findByAccountName(customerAccount);
            if (customer == null) {
                throw new UsernameNotFoundException("用户名或密码不正确！");
            }
            if (!customer.getEnabled()) {
                throw new DisabledException("客户已被禁用！");
            }
            Customer.Status status = customer.getStatus();
            if (status == Customer.Status.INITIALIZING) {
                throw new HxException("客户数据初始化中，请稍后尝试...");
            }
            user.setCustomer(customer);
        }
        redisTemplate.opsForValue().set(cacheAttr.getKey(), user, cacheAttr.getTimeout());
        return user;
    }

    @Override
    @Cacheable(value = "User", unless = "#result == null")
    public User loadUserById(Long userId) {
        User user = baseMapper.selectById(userId);
        Customer customer = customerMapper.findByAccountName(user.getCustomerAccount());
        user.setCustomer(customer);
        return user;
    }

    @Override
    @Cacheable("User")
    public List<GrantedAuthority> loadAuthoritiesByUserId(Long userId) {
        return userResourceMapper.listResourceByUserId(userId)
                .stream().map(Resource::getPermitCode)
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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

    public Function<Long, String> getUserNameFunction() {
        Map<Long, String> nameById = new HashMap<>(10);
        return (Long userId) -> {
            if (nameById.containsKey(userId)) {
                return nameById.get(userId);
            }
            User user = baseMapper.selectOne(Wrappers.<User>lambdaQuery()
                    .select(User::getName)
                    .eq(User::getId, userId)
            );
            if (user == null) {
                return "用户已被删除";
            }
            return user.getName();
        };
    }

    public String getUserNameById(long id) {
        User user = baseMapper.selectOne(Wrappers.<User>lambdaQuery()
                .select(User::getName)
                .eq(User::getId, id)
        );
        return user.getName();
    }
}
