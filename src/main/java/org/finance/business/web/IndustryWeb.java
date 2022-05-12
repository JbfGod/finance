package org.finance.business.web;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.IndustryConvert;
import org.finance.business.entity.Industry;
import org.finance.business.service.CustomerService;
import org.finance.business.service.IndustryService;
import org.finance.business.service.SubjectService;
import org.finance.business.web.request.AddIndustryRequest;
import org.finance.business.web.request.UpdateIndustryRequest;
import org.finance.business.web.vo.IndustryVO;
import org.finance.business.web.vo.TreeIndustryVO;
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
 * 行业分类表 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@RestController
@RequestMapping("/api/industry")
public class IndustryWeb {

    @Resource
    private IndustryService industryService;
    @Resource
    private CustomerService customerService;
    @Resource
    private SubjectService subjectService;

    @GetMapping("/tree")
    public R<List<TreeIndustryVO>> treeIndustry() {
        return R.ok(IndustryConvert.INSTANCE.toTreeIndustryVO(industryService.list()));
    }

    @GetMapping("/list")
    public R<List<IndustryVO>> listIndustry() {
        List<IndustryVO> list = industryService.list().stream()
                .map(IndustryConvert.INSTANCE::toIndustryVO)
                .collect(Collectors.toList());
        return R.ok(list);
    }

    @PostMapping("/add")
    public R addIndustry(@RequestBody @Valid AddIndustryRequest request) {
        boolean numberExists = industryService.count(Wrappers.<Industry>lambdaQuery()
                .eq(Industry::getNumber, request.getNumber())
        ) > 0;
        AssertUtil.isFalse(numberExists, String.format("行业编号：%s，已存在！", request.getNumber()));

        boolean nameExists = industryService.count(Wrappers.<Industry>lambdaQuery()
                .eq(Industry::getParentId, request.getParentId())
                .eq(Industry::getName, request.getName())
        ) > 0;
        AssertUtil.isFalse(nameExists, "同一级目录下不能定义相同的行业名称！");

        if (request.getParentId() > 0) {
            assertNotHasIndustryRelatedData(request.getParentId(), "新增子级失败");
        }

        industryService.add(IndustryConvert.INSTANCE.toIndustry(request));
        return R.ok();
    }

    @PutMapping("/update")
    public R updateIndustry(@RequestBody @Valid UpdateIndustryRequest request) {
        Industry dbCategory = industryService.getById(request.getId());
        Industry anyCategory = industryService.getOne(Wrappers.<Industry>lambdaQuery()
                .eq(Industry::getParentId, dbCategory.getParentId())
                .eq(Industry::getName, request.getName())
                .last("limit 1")
        );
        AssertUtil.isTrue(anyCategory == null || anyCategory.getId().equals(dbCategory.getId())
                , "同一级目录下不能定义相同的行业名称！");
        industryService.updateById(IndustryConvert.INSTANCE.toIndustry(request));
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R deleteIndustry(@PathVariable("id") long id) {
        assertNotHasIndustryRelatedData(id, "删除失败");
        industryService.delete(id);
        return R.ok();
    }

    private void assertNotHasIndustryRelatedData(long industryId, String operateMsg) {
        boolean hasCustomer = customerService.existsByIndustryId(industryId);
        AssertUtil.isFalse(hasCustomer, String.format("%s，当前行业存在关联的客户！", operateMsg));

        boolean hasSubject = subjectService.existsByIndustryId(industryId);
        AssertUtil.isFalse(hasSubject, String.format("%s，当前行业存在关联的科目！", operateMsg));
    }
}
