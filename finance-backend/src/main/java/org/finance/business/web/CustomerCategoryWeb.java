package org.finance.business.web;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.CustomerCategoryConvert;
import org.finance.business.entity.CustomerCategory;
import org.finance.business.service.CustomerCategoryService;
import org.finance.business.web.request.AddCustomerCategoryRequest;
import org.finance.business.web.request.UpdateCustomerCategoryRequest;
import org.finance.business.web.vo.CustomerCategoryVO;
import org.finance.business.web.vo.TreeCustomerCategoryVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.util.AssertUtil;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户分类表 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@RestController
@RequestMapping("/api/customerCategory")
public class CustomerCategoryWeb {

    @Resource
    private CustomerCategoryService customerCategoryService;

    @GetMapping("/tree")
    public R<List<TreeCustomerCategoryVO>> treeCustomerCategory() {
        return R.ok(CustomerCategoryConvert.INSTANCE.toTreeCustomerCategoryVO(customerCategoryService.list()));
    }

    @GetMapping("/list")
    public R<List<CustomerCategoryVO>> listCustomerCategory() {
        List<CustomerCategoryVO> list = customerCategoryService.list().stream()
                .map(CustomerCategoryConvert.INSTANCE::toCustomerCategoryVO)
                .collect(Collectors.toList());
        return R.ok(list);
    }

    @PostMapping("/add")
    public R addCustomerCategory(@RequestBody @Valid AddCustomerCategoryRequest request) {
        boolean numberExists = customerCategoryService.count(Wrappers.<CustomerCategory>lambdaQuery()
                .eq(CustomerCategory::getNumber, request.getNumber())
        ) > 0;
        AssertUtil.isFalse(numberExists, String.format("类别编号：%s，已存在！", request.getNumber()));
        boolean nameExists = customerCategoryService.count(Wrappers.<CustomerCategory>lambdaQuery()
                .eq(CustomerCategory::getParentId, request.getParentId())
                .eq(CustomerCategory::getName, request.getName())
        ) > 0;
        AssertUtil.isFalse(nameExists, "同一级目录下不能定义相同的类别名称！");
        customerCategoryService.add(CustomerCategoryConvert.INSTANCE.toCustomerCategory(request));
        return R.ok();
    }

    @PutMapping("/update")
    public R updateCustomerCategory(@RequestBody @Valid UpdateCustomerCategoryRequest request) {
        customerCategoryService.updateById(CustomerCategoryConvert.INSTANCE.toCustomerCategory(request));
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R deleteCustomerCategory(@PathVariable("id") long id) {
        customerCategoryService.delete(id);
        return R.ok();
    }

}
