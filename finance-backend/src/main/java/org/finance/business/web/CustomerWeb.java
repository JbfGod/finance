package org.finance.business.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import freemarker.template.TemplateException;
import org.finance.business.convert.CustomerConvert;
import org.finance.business.convert.FunctionConvert;
import org.finance.business.convert.UserConvert;
import org.finance.business.entity.Customer;
import org.finance.business.entity.Function;
import org.finance.business.entity.User;
import org.finance.business.service.CustomerCategoryService;
import org.finance.business.service.CustomerFunctionService;
import org.finance.business.service.CustomerService;
import org.finance.business.web.request.AddCustomerRequest;
import org.finance.business.web.request.GrantFunctionsToCustomerRequest;
import org.finance.business.web.request.QueryCustomerRequest;
import org.finance.business.web.request.UpdateCustomerRequest;
import org.finance.business.web.vo.CustomerListVO;
import org.finance.business.web.vo.FunctionIdentifiedVO;
import org.finance.business.web.vo.TreeFunctionVO;
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

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
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

    @Resource
    private CustomerService customerService;
    @Resource
    private CustomerCategoryService customerCategoryService;
    @Resource
    private CustomerFunctionService customerFunctionService;

    @GetMapping("/{customerId}/functionIds")
    public R<List<FunctionIdentifiedVO>> functionIdsOfCustomer(@PathVariable("customerId") long customerId) {
        List<FunctionIdentifiedVO> functionIds = customerFunctionService.listFunctionByCustomerId(customerId)
                .stream().map(FunctionConvert.INSTANCE::toFunctionIdentifiedVO)
                .collect(Collectors.toList());
        return R.ok(functionIds);
    }

    @GetMapping("/{customerId}/treeFunction")
    public R<List<TreeFunctionVO>> treeFunctionOfCustomer(@PathVariable("customerId") long customerId) {
        List<Function> functions = customerFunctionService.listFunctionByCustomerId(customerId);
        return R.ok(FunctionConvert.INSTANCE.toTreeFunctionVO(functions));
    }

    @GetMapping("/page")
    public RPage<CustomerListVO> pageCustomer(QueryCustomerRequest request) {
        IPage<CustomerListVO> pages = customerService.page(request.extractPage(), Wrappers.<Customer>lambdaQuery()
                .likeRight(StringUtils.hasText(request.getUserAccount()), Customer::getUserAccount, request.getUserAccount())
                .likeRight(StringUtils.hasText(request.getAccount()), Customer::getAccount, request.getAccount())
                .likeRight(StringUtils.hasText(request.getName()), Customer::getName, request.getName())
                .eq(request.getIndustryId() != null, Customer::getIndustryId, request.getIndustryId())
                .eq(request.getCategoryId() != null, Customer::getCategoryId, request.getCategoryId())
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

    @PostMapping("/grantFunctions")
    public R grantFunctionToCustomer(@RequestBody @Valid GrantFunctionsToCustomerRequest request) {
        customerFunctionService.grantFunctionsToUser(request.getCustomerId(), request.getFunctionIds());
        return R.ok();
    }

    @PostMapping("/add")
    public R addCustomer(@RequestBody @Valid AddCustomerRequest request) throws TemplateException, IOException {
        Customer customer = CustomerConvert.INSTANCE.toCustomer(request);
        User user = UserConvert.INSTANCE.toAdminUser(request.getUser());
        customerService.addCustomerAndUser(customer, user);
        return R.ok();
    }

    @PutMapping("/update")
    public R updateCustomer(@RequestBody @Valid UpdateCustomerRequest request) {
        customerService.updateById(CustomerConvert.INSTANCE.toCustomer(request));
        return R.ok();
    }


    @DeleteMapping("/delete/{id}")
    public R deleteCustomer(@PathVariable("id") long id) {
        customerService.deleteById(id);
        return R.ok();
    }

}
