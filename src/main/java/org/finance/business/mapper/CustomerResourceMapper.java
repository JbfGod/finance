package org.finance.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.finance.business.entity.CustomerResource;
import org.finance.business.entity.Resource;

import java.util.List;

/**
 * <p>
 * 客户的功能列表 Mapper 接口
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-09
 */
public interface CustomerResourceMapper extends BaseMapper<CustomerResource> {

    List<Resource> listResourceByCustomerId(@Param("customerId") long customerId);

}
