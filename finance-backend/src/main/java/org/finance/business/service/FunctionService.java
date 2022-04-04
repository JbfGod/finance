package org.finance.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.finance.business.entity.Function;
import org.finance.business.mapper.FunctionMapper;
import org.finance.business.mapper.UserFunctionMapper;
import org.finance.infrastructure.util.CacheKeyUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 功能表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@Service
public class FunctionService extends ServiceImpl<FunctionMapper, Function> {

}
