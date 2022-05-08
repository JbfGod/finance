package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.finance.business.entity.Customer;
import org.finance.business.entity.Sequence;
import org.finance.business.entity.User;
import org.finance.business.mapper.CustomerMapper;
import org.finance.business.mapper.SequenceMapper;
import org.finance.business.mapper.UserFunctionMapper;
import org.finance.business.mapper.UserMapper;
import org.finance.business.task.CustomerTask;
import org.finance.infrastructure.exception.HxException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@Service
public class CustomerService extends ServiceImpl<CustomerMapper, Customer> {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserFunctionMapper userFunctionMapper;
    @Resource
    private SequenceMapper sequenceMapper;

    private final Configuration CUSTOMER_FTL_CONFIG;

    public CustomerService() {
        CUSTOMER_FTL_CONFIG = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        CUSTOMER_FTL_CONFIG.setDefaultEncoding("UTF-8");
        CUSTOMER_FTL_CONFIG.setClassForTemplateLoading(CustomerService.class, "/freemarker");
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(long id) {
        Customer customer = baseMapper.selectById(id);
        baseMapper.deleteById(id);
        baseMapper.dropRelatedTblByTableIdentified(customer.getTableIdentified());
    }

    @Transactional(rollbackFor = Exception.class)
    public void addCustomerAndUser(Customer customer, User user) throws IOException, TemplateException {
        // 生成 客户的表标识
        Sequence sequence = new Sequence().setUseCategory(Sequence.CATEGORY_CUSTOMER_TBL_ID);
        sequenceMapper.insert(sequence);

        customer.setTableIdentified(sequence.getId().toString());
        baseMapper.insert(customer);

        user.setCustomerAccount(customer.getAccount())
                .setCustomerId(customer.getId());
        userMapper.insert(user);

        // 初始化客户数据
        CustomerTask.addInitialCustomer(customer);
    }

    /**
     * 加载未初始化的客户
     */
    public void loadInitializationCustomer() {
        baseMapper.selectList(Wrappers.<Customer>lambdaQuery().eq(Customer::getStatus, Customer.Status.INITIALIZING))
                .forEach(CustomerTask::addInitialCustomer);
    }

    public void initializationRelatedTable(String tableId) {
        Map<String, String> map = new HashMap<>(1);
        map.put("tableId", tableId);

        Template template = null;
        try {
            template = CUSTOMER_FTL_CONFIG.getTemplate("customerRelatedTable.sql.ftl");
            String sql = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            baseMapper.executeSql(sql);
        } catch (IOException | TemplateException e) {
            throw new HxException(e.getMessage(), e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void initializationData(long customerId) {
        Customer customer = baseMapper.selectById(customerId);
        baseMapper.initializationData(customer.getTableIdentified());

        customer.setStatus(Customer.Status.SUCCESS);
        baseMapper.updateById(customer);
    }

    public boolean existsByIndustryId(long industryId) {
        return baseMapper.exists(Wrappers.<Customer>lambdaQuery().eq(Customer::getIndustryId, industryId));
    }

    public boolean existsByCategoryId(long categoryId) {
        return baseMapper.exists(Wrappers.<Customer>lambdaQuery().eq(Customer::getCategoryId, categoryId));
    }
}
