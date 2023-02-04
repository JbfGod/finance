package org.finance.business.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import freemarker.template.Configuration;
import org.finance.business.convert.SubjectConvert;
import org.finance.business.entity.AggregateFormula;
import org.finance.business.entity.Customer;
import org.finance.business.entity.Report;
import org.finance.business.entity.ReportFormula;
import org.finance.business.entity.Subject;
import org.finance.business.entity.enums.AccountingSystem;
import org.finance.business.entity.templates.accountting.system.AbstractSystem;
import org.finance.business.mapper.AggregateFormulaMapper;
import org.finance.business.mapper.CustomerMapper;
import org.finance.business.mapper.ReportFormulaMapper;
import org.finance.business.mapper.ReportMapper;
import org.finance.business.mapper.SubjectMapper;
import org.finance.infrastructure.util.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    @Resource
    private ReportMapper reportMapper;
    @Resource
    private ReportFormulaMapper reportFormulaMapper;
    @Resource
    private AggregateFormulaMapper aggregateFormulaMapper;
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

        this.initializationData(customer);
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

    private void initializationData(Customer customer) {
        AccountingSystem accountingSystem = customer.getAccountingSystem();
        AbstractSystem system = accountingSystem.getSystem();
        List<Subject> subjects = system.getSubjects();
        List<Subject> rootSubjects = subjects.stream().filter(sub -> sub.getLevel() == 1).collect(Collectors.toList());
        recursion(rootSubjects, customer.getId(), null, null, null, new ArrayList<>());

        List<Report> reports = system.getReports();
        for (Report report : reports) {
            List<ReportFormula> reportFormulas = report.getReportFormulas();
            List<AggregateFormula> aggregateFormulas = report.getAggregateFormulas();
            report.setCustomerId(customer.getId());
            if (reportFormulas != null) {
                reportFormulas.stream().peek(formula -> formula.setCustomerId(customer.getId()))
                        .forEach(reportFormulaMapper::insert);
            } else if (aggregateFormulas != null) {
                aggregateFormulas.stream().peek(formula -> formula.setCustomerId(customer.getId()))
                        .forEach(aggregateFormulaMapper::insert);
            }
            report.setCustomerId(customer.getId());
            reportMapper.insert(report);
        }
    }

    private void recursion(List<Subject> subjects, Long customerId, Long parentId, Long rootId, String rootNumber, List<String> path) {
        for (Subject children : subjects) {
            children.setCustomerId(customerId);
            children.setParentId(Optional.ofNullable(parentId).orElse(0L));
            children.setRootId(rootId);
            String newRootNumber = Optional.ofNullable(rootNumber).orElse(children.getNumber());
            children.setRootNumber(newRootNumber);

            List<String> newPath = new ArrayList<>(path);
            newPath.add(children.getNumber());
            children.setPath(String.join(",", newPath));

            boolean noHasLeaf = CollectionUtils.isEmpty(children.getChildren());
            children.setHasLeaf(!noHasLeaf);

            subjectMapper.insert(children);
            if (noHasLeaf) {
                continue;
            }
            recursion(children.getChildren(), customerId, children.getId(),
                    Optional.ofNullable(rootId).orElse(children.getId()),
                    newRootNumber, newPath);
        }
    }

    private void recursionInsertSubject(
            long customerId, Subject sub, Map<Long, Subject> subjectById, Map<String, Subject> subjectByNumber
    ) {
        if (subjectByNumber.get(sub.getNumber()) != null) {
            return;
        }
        subjectByNumber.put(sub.getNumber(), sub);
        Long parentId = sub.getParentId();
        if (parentId == 0L) {
            subjectMapper.insert(sub.setId(null).setCustomerId(customerId));
            return;
        }
        Subject pSubject = subjectByNumber.get(sub.getParentNumber());
        if (pSubject == null) {
            pSubject =  SubjectConvert.INSTANCE.clone(subjectById.get(parentId))
                    .setBeginningBalance(BigDecimal.ZERO)
                    .setOpeningBalance(BigDecimal.ZERO)
                    .setDebitAnnualAmount(BigDecimal.ZERO)
                    .setCreditAnnualAmount(BigDecimal.ZERO);
            recursionInsertSubject(customerId, pSubject, subjectById, subjectByNumber);
        }
        subjectMapper.insert(sub.setId(null).setCustomerId(customerId).setParentId(pSubject.getParentId()));
    }

}
