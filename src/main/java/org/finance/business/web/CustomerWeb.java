package org.finance.business.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.CustomerConvert;
import org.finance.business.convert.ResourceConvert;
import org.finance.business.entity.Customer;
import org.finance.business.entity.Resource;
import org.finance.business.service.ApprovalInstanceApproverService;
import org.finance.business.service.CustomerCategoryService;
import org.finance.business.service.CustomerService;
import org.finance.business.service.ResourceService;
import org.finance.business.web.request.AddCustomerRequest;
import org.finance.business.web.request.QueryCustomerCueRequest;
import org.finance.business.web.request.QueryCustomerRequest;
import org.finance.business.web.request.UpdateCustomerRequest;
import org.finance.business.web.vo.CustomerCueVO;
import org.finance.business.web.vo.CustomerListVO;
import org.finance.business.web.vo.OwnedApprovalCustomerVO;
import org.finance.business.web.vo.TreeResourceWithOperateVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.common.RPage;
import org.finance.infrastructure.config.security.util.SecurityUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户表 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerWeb {

    @javax.annotation.Resource
    private CustomerService baseService;
    @javax.annotation.Resource
    private CustomerCategoryService customerCategoryService;
    @javax.annotation.Resource
    private ApprovalInstanceApproverService instanceApproverService;
    @javax.annotation.Resource
    private ResourceService resourceService;

    @GetMapping("/owned/approval")
    public R<List<OwnedApprovalCustomerVO>> ownedApprovalCustomers() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        List<Long> customerIds = instanceApproverService.customerIdsByApproverId(currentUserId);
        if (customerIds.isEmpty()) {
            return R.ok(Collections.emptyList());
        }
        return R.ok(
            baseService.list(Wrappers.<Customer>lambdaQuery()
                .in(Customer::getId, customerIds)
            ).stream().map(CustomerConvert.INSTANCE::toOwnedApprovalCustomerVO)
            .collect(Collectors.toList())
        );
    }

    @GetMapping("/{customerId}/treeResourceWithOperate")
    public R<List<TreeResourceWithOperateVO>> treeResourceWithOperate(@PathVariable("customerId") long customerId) {
        List<Long> canGrantResourceIds = SecurityUtil.getCurrentUser().getResources().stream()
                .map(org.finance.business.entity.Resource::getId)
                .collect(Collectors.toList());
        List<Resource> resources = resourceService.list(Wrappers.<Resource>lambdaQuery()
                .orderByAsc(Resource::getModule, Resource::getSortNum)
        ).stream().filter(r -> {
            if (!Customer.DEFAULT_ID.equals(customerId) && r.getSuperCustomer()) {
                return false;
            }
            if (!canGrantResourceIds.contains(r.getId())) {
                r.setDisabled(true);
            }
            return true;
        }).collect(Collectors.toList());
        return R.ok(ResourceConvert.INSTANCE.toTreeResourceWithOperateVO(resources));
    }

    @GetMapping("/page")
    public RPage<CustomerListVO> pageCustomer(QueryCustomerRequest request) {
        List<Long> categoryIds = new ArrayList<>();
        if (request.getCategoryId() != null) {
            categoryIds = customerCategoryService.listChildrenIdsById(request.getCategoryId());
        }

        IPage<CustomerListVO> pages = baseService.page(request.extractPage(), Wrappers.<Customer>lambdaQuery()
                .gt(Customer::getId, 0)
                .likeRight(StringUtils.hasText(request.getNumber()), Customer::getNumber, request.getNumber())
                .likeRight(StringUtils.hasText(request.getName()), Customer::getName, request.getName())
                .in(request.getCategoryId() != null, Customer::getCategoryId, categoryIds)
                .likeRight(request.getType() !=null, Customer::getType, request.getType())
                .likeRight(StringUtils.hasText(request.getTelephone()), Customer::getTelephone, request.getTelephone())
                .likeRight(StringUtils.hasText(request.getBankAccount()), Customer::getBankAccount, request.getBankAccount())
                .likeRight(StringUtils.hasText(request.getBankAccountName()), Customer::getBankAccountName, request.getBankAccountName())
                .likeRight(request.getUseForeignExchange() != null, Customer::getUseForeignExchange, request.getUseForeignExchange())
                .orderByDesc(Customer::getCreateBy)
        ).convert(customer -> {
            CustomerListVO customerListVO = CustomerConvert.INSTANCE.toCustomerListVO(customer);
            String categoryName = customerCategoryService.getById(customer.getCategoryId()).getName();
            customerListVO.setCategory(categoryName);
            return customerListVO;
        });
        return RPage.build(pages);
    }


    @GetMapping("/searchCustomerCue")
    public R<List<CustomerCueVO>> searchCustomerCue(QueryCustomerCueRequest request) {
        List<CustomerCueVO> cues = baseService.list(Wrappers.<Customer>lambdaQuery()
                .select(Customer::getId, Customer::getNumber, Customer::getName)
                .gt(Customer::getId, 0)
                .likeRight(StringUtils.hasText(request.getKeyword()), Customer::getNumber, request.getKeyword())
                .last(request.getNum() != null, String.format("limit %d", request.getNum()))
        ).stream().map(CustomerConvert.INSTANCE::toCustomerCueVO).collect(Collectors.toList());
        return R.ok(cues);
    }

    @PostMapping("/add")
    public R addCustomer(@Valid AddCustomerRequest request) {
        Customer customer = CustomerConvert.INSTANCE.toCustomer(request);
        baseService.addCustomerAndUser(customer);
        return R.ok();
    }

    @PutMapping("/update")
    public R updateCustomer(@Valid UpdateCustomerRequest request) {
        baseService.updateById(CustomerConvert.INSTANCE.toCustomer(request));
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R deleteCustomer(@PathVariable("id") long id) {
        baseService.deleteById(id);
        return R.ok();
    }

}
