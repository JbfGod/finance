package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import freemarker.template.Configuration;
import org.finance.business.convert.SubjectConvert;
import org.finance.business.entity.Customer;
import org.finance.business.entity.Subject;
import org.finance.business.mapper.CustomerMapper;
import org.finance.business.mapper.SubjectMapper;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    private SubjectMapper subjectMapper;

    private final Configuration CUSTOMER_FTL_CONFIG;

    public CustomerService() {
        CUSTOMER_FTL_CONFIG = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        CUSTOMER_FTL_CONFIG.setDefaultEncoding("UTF-8");
        CUSTOMER_FTL_CONFIG.setClassForTemplateLoading(CustomerService.class, "/freemarker");
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(long id) {
        baseMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addCustomerAndUser(Customer customer) {
        boolean existsCustomer = baseMapper.exists(Wrappers.<Customer>lambdaQuery()
                .eq(Customer::getNumber, customer.getNumber())
        );
        AssertUtil.isFalse(existsCustomer, "客户编号已存在！");

        baseMapper.insert(customer);

        this.initializationData(customer.getId(), customer.getIndustryId());
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
