package org.finance.business.web;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.SubjectConvert;
import org.finance.business.entity.Subject;
import org.finance.business.service.IndustryService;
import org.finance.business.service.SubjectService;
import org.finance.business.web.request.AddSubjectRequest;
import org.finance.business.web.request.QuerySubjectRequest;
import org.finance.business.web.request.UpdateSubjectRequest;
import org.finance.business.web.vo.SubjectVO;
import org.finance.business.web.vo.TreeSubjectVO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 科目表 前端控制器
 * </p>
 *
 * @author jiangbangfa
 * @since 2022-04-02
 */
@RestController
@RequestMapping("/api/subject")
public class SubjectWeb {

    @Resource
    private SubjectService subjectService;
    @Resource
    private IndustryService industryService;

    @GetMapping("/tree")
    public R<List<TreeSubjectVO>> treeSubject(@Valid QuerySubjectRequest request) {
        List<Long> industryWithChildrenIds = new ArrayList<>();
        if (request.getIndustryId() != null) {
            industryWithChildrenIds = industryService.listChildrenIdsById(request.getIndustryId());
        }
        List<Subject> subjects = subjectService.list(Wrappers.<Subject>lambdaQuery()
            .in(request.getIndustryId() != null, Subject::getIndustryId, industryWithChildrenIds)
        );
        List<TreeSubjectVO> treeSubjectVOList = SubjectConvert.INSTANCE.toTreeSubjectVO(subjects, (sub) -> {
            sub.setIndustry(industryService.getById(sub.getIndustryId()).getName());
        });
        return R.ok(treeSubjectVOList);
    }

    @GetMapping("/list")
    public R<List<SubjectVO>> listSubject(@Valid QuerySubjectRequest request) {
        List<SubjectVO> list = subjectService.list(Wrappers.<Subject>lambdaQuery()
                        .eq(Subject::getIndustryId, request.getIndustryId()))
                .stream()
                .map(SubjectConvert.INSTANCE::toSubjectVO)
                .collect(Collectors.toList());
        return R.ok(list);
    }

    @PostMapping("/add")
    public R addSubject(@RequestBody @Valid AddSubjectRequest request) {
        boolean numberExists = subjectService.count(Wrappers.<Subject>lambdaQuery()
                .eq(Subject::getNumber, request.getNumber())
        ) > 0;
        AssertUtil.isFalse(numberExists, String.format("科目编号：%s，已存在！", request.getNumber()));
        boolean nameExists = subjectService.count(Wrappers.<Subject>lambdaQuery()
                .eq(Subject::getParentId, request.getParentId())
                .eq(Subject::getName, request.getName())
        ) > 0;
        AssertUtil.isFalse(nameExists, "同一级目录下不能定义相同的科目名称！");
        subjectService.add(SubjectConvert.INSTANCE.toSubject(request));
        return R.ok();
    }

    @PutMapping("/update")
    public R updateSubject(@RequestBody @Valid UpdateSubjectRequest request) {
        Subject dbCategory = subjectService.getById(request.getId());
        Subject anyCategory = subjectService.getOne(Wrappers.<Subject>lambdaQuery()
                .eq(Subject::getParentId, dbCategory.getParentId())
                .eq(Subject::getName, request.getName())
                .last("limit 1")
        );
        AssertUtil.isTrue(anyCategory == null || anyCategory.getId().equals(dbCategory.getId())
                , "同一级目录下不能定义相同的科目名称！");
        subjectService.updateById(SubjectConvert.INSTANCE.toSubject(request));
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R deleteSubject(@PathVariable("id") long id) {
        subjectService.delete(id);
        return R.ok();
    }
    
}
