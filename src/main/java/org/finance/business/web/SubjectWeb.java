package org.finance.business.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.SubjectConvert;
import org.finance.business.entity.Customer;
import org.finance.business.entity.Subject;
import org.finance.business.service.IndustryService;
import org.finance.business.service.SubjectService;
import org.finance.business.web.request.AddSubjectRequest;
import org.finance.business.web.request.QuerySubjectRequest;
import org.finance.business.web.request.UpdateSubjectRequest;
import org.finance.business.web.vo.SubjectVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.common.RPage;
import org.finance.infrastructure.config.security.util.SecurityUtil;
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
import java.util.function.Function;
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

    @GetMapping("/list")
    public R<List<SubjectVO>> listSubject(@Valid QuerySubjectRequest request) {
        List<Long> industryWithChildrenIds = new ArrayList<>();
        if (request.getIndustryId() != null) {
            industryWithChildrenIds = industryService.listChildrenIdsById(request.getIndustryId());
        }
        Function<Long, String> industryNameFunc = industryService.geNameFunction();
        List<SubjectVO> subjects = subjectService.list(Wrappers.<Subject>lambdaQuery()
                .in(request.getIndustryId() != null, Subject::getIndustryId, industryWithChildrenIds)
                .likeRight(StringUtils.isNotBlank(request.getNumber()), Subject::getNumber,request.getNumber())
                .likeRight(StringUtils.isNotBlank(request.getName()), Subject::getName, request.getName())
                .orderByAsc(Subject::getRootNumber, Subject::getLeftValue)
        ).stream().map(subject -> {
            SubjectVO subjectVO = SubjectConvert.INSTANCE.toSubjectVO(subject);
            subjectVO.setIndustry(industryNameFunc.apply(subject.getIndustryId()));
            return subjectVO;
        }).collect(Collectors.toList());
        return R.ok(subjects);
    }

    @GetMapping("/page")
    public RPage<SubjectVO> pageSubject(@Valid QuerySubjectRequest request) {
        List<Long> industryWithChildrenIds = new ArrayList<>();
        if (request.getIndustryId() != null) {
            industryWithChildrenIds = industryService.listChildrenIdsById(request.getIndustryId());
        }
        Function<Long, String> industryNameFunc = industryService.geNameFunction();
        IPage<SubjectVO> subjects = subjectService.page(request.extractPage(), Wrappers.<Subject>lambdaQuery()
                .in(request.getIndustryId() != null, Subject::getIndustryId, industryWithChildrenIds)
                .likeRight(StringUtils.isNotBlank(request.getNumber()), Subject::getNumber,request.getNumber())
                .likeRight(StringUtils.isNotBlank(request.getName()), Subject::getName, request.getName())
                .orderByAsc(Subject::getIndustryId, Subject::getRootNumber, Subject::getLeftValue)
        ).convert(subject -> {
            SubjectVO subjectVO = SubjectConvert.INSTANCE.toSubjectVO(subject);
            subjectVO.setIndustry(industryNameFunc.apply(subject.getIndustryId()));
            return subjectVO;
        });
        return RPage.build(subjects);
    }

    @PostMapping("/add")
    public R addSubject(@RequestBody @Valid AddSubjectRequest request) {
        Customer proxyCustomer = SecurityUtil.getProxyCustomer();
        if (proxyCustomer.isSuperCustomer()) {
            AssertUtil.isTrue(request.getIndustryId() != null, "请选择行业！");
        } else {
            request.setIndustryId(proxyCustomer.getIndustryId());
        }
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

}
