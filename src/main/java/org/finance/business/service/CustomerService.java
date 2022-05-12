package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.finance.business.convert.IndustryConvert;
import org.finance.business.convert.SubjectConvert;
import org.finance.business.entity.Customer;
import org.finance.business.entity.Industry;
import org.finance.business.entity.Sequence;
import org.finance.business.entity.Subject;
import org.finance.business.entity.User;
import org.finance.business.mapper.CustomerMapper;
import org.finance.business.mapper.IndustryMapper;
import org.finance.business.mapper.SequenceMapper;
import org.finance.business.mapper.SubjectMapper;
import org.finance.business.mapper.UserFunctionMapper;
import org.finance.business.mapper.UserMapper;
import org.finance.business.task.CustomerTask;
import org.finance.infrastructure.constants.Constants;
import org.finance.infrastructure.exception.HxException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Resource
    private SubjectMapper subjectMapper;
    @Resource
    private IndustryMapper industryMapper;

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
    public void addCustomerAndUser(Customer customer, User user) {
        // 生成 客户的表标识
        Sequence sequence = new Sequence().setUseCategory(Sequence.CATEGORY_CUSTOMER_TBL_ID);
        sequenceMapper.insert(sequence);

        customer.setTableIdentified(sequence.getId().toString());
        // TODO 数据初始化成功,以后如果出现分表的情况，需要等表初始化才设置为success
        customer.setStatus(Customer.Status.SUCCESS);
        baseMapper.insert(customer);

        user.setCustomerAccount(customer.getAccount())
                .setCustomerId(customer.getId());
        userMapper.insert(user);

        // 初始化客户数据
        // CustomerTask.addInitialCustomer(customer);
        this.initializationData(customer.getId());
    }

    /**
     * 加载未初始化的客户
     */
    public void loadInitializationCustomer() {
        baseMapper.selectList(Wrappers.<Customer>lambdaQuery().eq(Customer::getStatus, Customer.Status.INITIALIZING))
                .forEach(CustomerTask::addInitialCustomer);
    }

    /**
     * 初始化客户相关表
     * @param tableId
     */
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

    public boolean existsByIndustryId(long industryId) {
        return baseMapper.exists(Wrappers.<Customer>lambdaQuery().eq(Customer::getIndustryId, industryId));
    }

    public boolean existsByCategoryId(long categoryId) {
        return baseMapper.exists(Wrappers.<Customer>lambdaQuery().eq(Customer::getCategoryId, categoryId));
    }

    private void initializationData(long customerId) {
        Map<Long, Industry> industryById = industryMapper.selectList(
                Wrappers.<Industry>lambdaQuery().eq(Industry::getCustomerId, Constants.DEFAULT_CUSTOMER_ID)
        ).stream().collect(Collectors.toMap(Industry::getId, i -> i));
        Map<Long, Subject> subjectById = subjectMapper.selectList(
                Wrappers.<Subject>lambdaQuery().eq(Subject::getCustomerId, Constants.DEFAULT_CUSTOMER_ID)
        ).stream().collect(Collectors.toMap(Subject::getId, s -> s));

        // Copy industry data to new customer
        Map<String, Industry> industryByNumber = new HashMap<>(20);
        industryById.values().stream().filter(industry -> industry.getParentId() == Constants.DEFAULT_CUSTOMER_ID).forEach(industry -> {
            recursionInsertIndustry(customerId, IndustryConvert.INSTANCE.clone(industry), industryById, industryByNumber);
        });

        // Copy subject data to new customer
        Map<String, Subject> subjectByNumber = new HashMap<>(20);
        subjectById.values().stream().filter(sub -> sub.getParentId() == Constants.DEFAULT_CUSTOMER_ID).forEach(sub -> {
            recursionInsertSubject(customerId, SubjectConvert.INSTANCE.clone(sub), subjectById, subjectByNumber, industryById, industryByNumber);
        });
    }

    private void recursionInsertSubject(
            long customerId, Subject sub, Map<Long, Subject> subjectById, Map<String, Subject> subjectByNumber,
            Map<Long, Industry> industryById, Map<String, Industry> industryByNumber
    ) {
        Industry industry = industryById.get(sub.getIndustryId());
        if (industry == null) {
            return;
        }
        industry = industryByNumber.get(industry.getNumber());
        if (industry == null) {
            return;
        }
        Long parentId = sub.getParentId();
        if (parentId == Constants.DEFAULT_CUSTOMER_ID) {
            subjectMapper.insert(sub.setId(null).setCustomerId(customerId).setIndustryId(industry.getId()));
            return;
        }
        subjectByNumber.put(sub.getNumber(), sub);
        Subject pSubject = subjectByNumber.get(sub.getParentNumber());
        if (pSubject == null) {
            pSubject =  SubjectConvert.INSTANCE.clone(subjectById.get(parentId));
            recursionInsertSubject(customerId, pSubject, subjectById, subjectByNumber, industryById, industryByNumber);
        }
        subjectMapper.insert(sub.setId(null).setCustomerId(customerId).setParentId(pSubject.getParentId()));
    }

    private void recursionInsertIndustry(long customerId, Industry industry, Map<Long, Industry> industryById, Map<String, Industry> industryByNumber) {
        industryByNumber.put(industry.getNumber(), industry);
        Long parentId = industry.getParentId();
        if (parentId == Constants.DEFAULT_CUSTOMER_ID) {
            industryMapper.insert(industry.setId(null).setCustomerId(customerId));
            return;
        }
        Industry pIndustry = industryByNumber.get(industry.getParentNumber());
        if (pIndustry == null) {
            pIndustry =  IndustryConvert.INSTANCE.clone(industryById.get(parentId));
            recursionInsertIndustry(customerId, pIndustry, industryById, industryByNumber);
        }
        industryMapper.insert(industry.setId(null).setCustomerId(customerId).setParentId(pIndustry.getParentId()));
    }
}
