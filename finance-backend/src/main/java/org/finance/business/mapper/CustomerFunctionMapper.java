package org.finance.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.finance.business.entity.CustomerFunction;
import org.finance.business.entity.Function;

import java.util.List;

/**
 * <p>
 * 客户的功能列表 Mapper 接口
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-09
 */
public interface CustomerFunctionMapper extends BaseMapper<CustomerFunction> {

    List<Function> listFunctionByCustomerId(@Param("customerId") long customerId);

}
