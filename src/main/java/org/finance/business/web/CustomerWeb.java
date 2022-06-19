package org.finance.business.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.CustomerConvert;
import org.finance.business.convert.ResourceConvert;
import org.finance.business.entity.Customer;
import org.finance.business.entity.Resource;
import org.finance.business.service.CustomerCategoryService;
import org.finance.business.service.CustomerResourceService;
import org.finance.business.service.CustomerService;
import org.finance.business.web.request.AddCustomerRequest;
import org.finance.business.web.request.GrantResourcesToCustomerRequest;
import org.finance.business.web.request.QueryCustomerCueRequest;
import org.finance.business.web.request.QueryCustomerRequest;
import org.finance.business.web.request.UpdateCustomerRequest;
import org.finance.business.web.vo.CustomerCueVO;
import org.finance.business.web.vo.CustomerListVO;
import org.finance.business.web.vo.ResourceIdentifiedVO;
import org.finance.business.web.vo.TreeResourceVO;
import org.finance.business.web.vo.TreeResourceWithOperateVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.common.RPage;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
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
    private CustomerService customerService;
    @javax.annotation.Resource
    private CustomerCategoryService customerCategoryService;
    @javax.annotation.Resource
    private CustomerResourceService customerResourceService;

    @GetMapping("/{customerId}/resourceIds")
    public R<List<ResourceIdentifiedVO>> resourceIdsOfCustomer(@PathVariable("customerId") long customerId) {
        List<ResourceIdentifiedVO> resourceIds = customerResourceService.listResourceByCustomerId(customerId)
                .stream().map(ResourceConvert.INSTANCE::toResourceIdentifiedVO)
                .collect(Collectors.toList());
        return R.ok(resourceIds);
    }

    @GetMapping("/{customerId}/treeResource")
    public R<List<TreeResourceVO>> treeResourceOfCustomer(@PathVariable("customerId") long customerId) {
        List<Resource> resources = customerResourceService.listResourceByCustomerId(customerId);
        return R.ok(ResourceConvert.INSTANCE.toTreeResourceVO(resources));
    }

    @GetMapping("/{customerId}/treeResourceWithOperate")
    public R<List<TreeResourceWithOperateVO>> treeResourceWithOperate(@PathVariable("customerId") long customerId) {
        List<Resource> resources = customerResourceService.listResourceByCustomerId(customerId);
        return R.ok(ResourceConvert.INSTANCE.toTreeResourceWithOperateVO(resources));
    }

    @GetMapping("/page")
    public RPage<CustomerListVO> pageCustomer(QueryCustomerRequest request) {
        List<Long> categoryIds = new ArrayList<>();
        if (request.getCategoryId() != null) {
            categoryIds = customerCategoryService.listChildrenIdsById(request.getCategoryId());
        }

        IPage<CustomerListVO> pages = customerService.page(request.extractPage(), Wrappers.<Customer>lambdaQuery()
                .gt(Customer::getId, 0)
                .likeRight(StringUtils.hasText(request.getNumber()), Customer::getNumber, request.getNumber())
                .likeRight(StringUtils.hasText(request.getName()), Customer::getName, request.getName())
                .eq(request.getIndustryId() != null, Customer::getIndustryId, request.getIndustryId())
                .in(request.getCategoryId() != null, Customer::getCategoryId, categoryIds)
                .likeRight(request.getType() !=null, Customer::getType, request.getType())
                .likeRight(request.getStatus() != null, Customer::getStatus, request.getStatus())
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
        List<CustomerCueVO> cues = customerService.list(Wrappers.<Customer>lambdaQuery()
                .select(Customer::getId, Customer::getNumber, Customer::getName)
                .likeRight(Customer::getNumber, request.getKeyword())
                .last(String.format("limit %d", request.getNum()))
        ).stream().map(CustomerConvert.INSTANCE::toCustomerCueVO).collect(Collectors.toList());
        return R.ok(cues);
    }

    @PostMapping("/grantResources")
    public R grantResourceToCustomer(@RequestBody @Valid GrantResourcesToCustomerRequest request) {
        customerResourceService.grantResourcesToUser(request.getCustomerId(), request.getResourceIds());
        return R.ok();
    }

    @PostMapping("/add")
    public R addCustomer(@Valid AddCustomerRequest request) {
        Customer customer = CustomerConvert.INSTANCE.toCustomer(request);
        customerService.addCustomerAndUser(customer);
        return R.ok();
    }

    @PutMapping("/update")
    public R updateCustomer(@Valid UpdateCustomerRequest request) {
        customerService.updateById(CustomerConvert.INSTANCE.toCustomer(request));
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R deleteCustomer(@PathVariable("id") long id) {
        customerService.deleteById(id);
        return R.ok();
    }

}
