package org.finance.domain.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.domain.system.pojo.User;

/**
 * @author jiangbangfa
 */
public interface UserMapper extends BaseMapper<User> {

    default User selectByConsumerIdAndUsername(String customerId, String password) {
        return null;
    }
}
