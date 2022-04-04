package org.finance.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.finance.business.entity.Function;
import org.finance.business.entity.UserFunction;

import java.util.List;

/**
 * <p>
 * 用户的功能列表 Mapper 接口
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-04
 */
public interface UserFunctionMapper extends BaseMapper<UserFunction> {

    List<Function> listFunctionByUserId(@Param("userId") Long userId);

}
