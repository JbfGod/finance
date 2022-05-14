package org.finance.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.finance.business.entity.Resource;
import org.finance.business.entity.UserResource;

import java.util.List;

/**
 * <p>
 * 用户的功能列表 Mapper 接口
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-04
 */
public interface UserResourceMapper extends BaseMapper<UserResource> {

    List<Resource> listResourceByUserId(@Param("userId") Long userId);

}
