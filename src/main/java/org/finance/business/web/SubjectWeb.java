package org.finance.business.web;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
        List<Subject> subjects = this.listSubjectByRequest(request);
        List<TreeSubjectVO> treeSubjectVOList = SubjectConvert.INSTANCE.toTreeSubjectVO(subjects, (sub) -> {
            sub.setIndustry(industryService.getById(sub.getIndustryId()).getName());
        });
        return R.ok(treeSubjectVOList);
    }

    @GetMapping("/list")
    public R<List<SubjectVO>> listSubject(@Valid QuerySubjectRequest request) {
        List<SubjectVO> list = this.listSubjectByRequest(request).stream()
                .map(SubjectConvert.INSTANCE::toSubjectVO)
                .collect(Collectors.toList());
        return R.ok(list);
    }

    @PostMapping("/add")
    public R addSubject(@RequestBody @Valid AddSubjectRequest request) {
        boolean numberExists = subjectService.count(Wrappers.<Subject>lambdaQuery()
                .eq(Subject::getIndustryId, request.getIndustryId())
                .eq(Subject::getNumber, request.getNumber())
        ) > 0;
        AssertUtil.isFalse(numberExists, String.format("科目编号：%s，在当前行业下已存在！", request.getNumber()));
        subjectService.add(SubjectConvert.INSTANCE.toSubject(request));
        return R.ok();
    }

    @PutMapping("/update")
    public R updateSubject(@RequestBody @Valid UpdateSubjectRequest request) {
        subjectService.updateById(SubjectConvert.INSTANCE.toSubject(request));
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R deleteSubject(@PathVariable("id") long id) {
        subjectService.delete(id);
        return R.ok();
    }

    private List<Subject> listSubjectByRequest(QuerySubjectRequest request) {
        List<Long> industryWithChildrenIds = new ArrayList<>();
        if (request.getIndustryId() != null) {
            industryWithChildrenIds = industryService.listChildrenIdsById(request.getIndustryId());
        }
        return subjectService.list(Wrappers.<Subject>lambdaQuery()
                .in(request.getIndustryId() != null, Subject::getIndustryId, industryWithChildrenIds)
                .likeRight(StringUtils.isNotBlank(request.getNumber()), Subject::getNumber,request.getNumber())
                .likeRight(StringUtils.isNotBlank(request.getName()), Subject::getName, request.getName())
                .orderByAsc(Subject::getNumber)
        );
    }
}
