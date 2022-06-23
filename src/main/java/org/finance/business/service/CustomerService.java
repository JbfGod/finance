package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.finance.business.convert.SubjectConvert;
import org.finance.business.entity.Customer;
import org.finance.business.entity.Subject;
import org.finance.business.mapper.CustomerMapper;
import org.finance.business.mapper.IndustryMapper;
import org.finance.business.mapper.SequenceMapper;
import org.finance.business.mapper.SubjectMapper;
import org.finance.business.task.CustomerTask;
import org.finance.infrastructure.exception.HxException;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
        // Customer customer = baseMapper.selectById(id);
        baseMapper.deleteById(id);
        // baseMapper.dropRelatedTblByTableIdentified(customer.getTableIdentified());
    }

    @Transactional(rollbackFor = Exception.class)
    public void addCustomerAndUser(Customer customer) {
        boolean existsCustomer = baseMapper.exists(Wrappers.<Customer>lambdaQuery()
                .eq(Customer::getNumber, customer.getNumber())
        );
        AssertUtil.isFalse(existsCustomer, "客户编号已存在！");

        // TODO 数据初始化成功,以后如果出现分表的情况，需要等表初始化才设置为success
        customer.setStatus(Customer.Status.SUCCESS);
        baseMapper.insert(customer);

        // 初始化客户数据
        // CustomerTask.addInitialCustomer(customer);
        this.initializationData(customer.getId(), customer.getIndustryId());
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

    public List<Long> getIdsLikeByName(String name) {
        return baseMapper.selectList(Wrappers.<Customer>lambdaQuery()
                .select(Customer::getId)
                .likeRight(Customer::getName, name)
        ).stream().map(Customer::getId).collect(Collectors.toList());
    }

    public Function<Long, String> getCustomerNameFunction() {
        Map<Long, String> nameById = new HashMap<>(10);
        return (Long customerId) -> {
            if (nameById.containsKey(customerId)) {
                return nameById.get(customerId);
            }
            Customer customer = baseMapper.selectOne(Wrappers.<Customer>lambdaQuery()
                    .select(Customer::getName)
                    .eq(Customer::getId, customerId)
            );
            if (customer == null) {
                return "客户已被删除";
            }
            return customer.getName();
        };
    }

    public String getCustomerNameById(long customerId) {
        if (customerId == Customer.DEFAULT_ID) {
            return Customer.DEFAULT_NAME;
        }
        Customer customer = baseMapper.selectOne(Wrappers.<Customer>lambdaQuery()
                .select(Customer::getName)
                .eq(Customer::getId, customerId)
        );
        if (customer == null) {
            return "客户已被删除";
        }
        return customer.getName();
    }

    private void initializationData(long customerId, long industryId) {
        Map<Long, Subject> subjectById = subjectMapper.selectList(Wrappers.<Subject>lambdaQuery()
                .eq(Subject::getCustomerId, Customer.DEFAULT_ID)
                .eq(Subject::getIndustryId, industryId)
        ).stream().collect(Collectors.toMap(Subject::getId, s -> s));

        // Copy subject data to new customer
        Map<String, Subject> subjectByNumber = new HashMap<>(20);
        subjectById.values().forEach(sub -> {
            recursionInsertSubject(customerId, SubjectConvert.INSTANCE.clone(sub), subjectById, subjectByNumber, industryId);
        });
    }

    private void recursionInsertSubject(
            long customerId, Subject sub, Map<Long, Subject> subjectById, Map<String, Subject> subjectByNumber,
            long industryId
    ) {
        if (subjectByNumber.get(sub.getNumber()) != null) {
            return;
        }
        subjectByNumber.put(sub.getNumber(), sub);
        Long parentId = sub.getParentId();
        if (parentId == 0L) {
            subjectMapper.insert(sub.setId(null).setCustomerId(customerId).setIndustryId(industryId));
            return;
        }
        Subject pSubject = subjectByNumber.get(sub.getParentNumber());
        if (pSubject == null) {
            pSubject =  SubjectConvert.INSTANCE.clone(subjectById.get(parentId));
            recursionInsertSubject(customerId, pSubject, subjectById, subjectByNumber, industryId);
        }
        subjectMapper.insert(sub.setId(null).setCustomerId(customerId).setParentId(pSubject.getParentId()));
    }

}
