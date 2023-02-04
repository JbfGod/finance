package org.finance.business.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.SubjectConvert;
import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.Subject;
import org.finance.business.service.AccountBalanceService;
import org.finance.business.service.AccountCloseListService;
import org.finance.business.service.SubjectService;
import org.finance.business.service.VoucherItemService;
import org.finance.business.web.request.AddSubjectRequest;
import org.finance.business.web.request.QuerySubjectRequest;
import org.finance.business.web.request.UpdateSubjectInitialBalanceRequest;
import org.finance.business.web.request.UpdateSubjectRequest;
import org.finance.business.web.vo.SubjectVO;
import org.finance.infrastructure.common.R;
import org.finance.infrastructure.common.RPage;
import org.finance.infrastructure.constants.Constants;
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
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private AccountBalanceService accountBalanceService;
    @Resource
    private VoucherItemService voucherItemService;
    @Resource
    private AccountCloseListService accountCloseListService;


    @GetMapping("/list")
    public R<List<SubjectVO>> listSubject(@Valid QuerySubjectRequest request) {
        List<Subject> subjects = subjectService.list(Wrappers.<Subject>lambdaQuery()
                .eq(request.getCategory() != null, Subject::getCategory, request.getCategory())
                .likeRight(StringUtils.isNotBlank(request.getNumber()), Subject::getNumber, request.getNumber())
                .likeRight(StringUtils.isNotBlank(request.getName()), Subject::getName, request.getName())
                .orderByAsc(Subject::getRootNumber, Subject::getNumber)
        );
        Integer yearMonthNum = accountCloseListService.currentAccountCloseYearMonth();
        Map<Long, AccountBalance> balanceBySubject = accountBalanceService.summaryBySubjectId(YearMonth.parse(yearMonthNum.toString(), Constants.YEAR_MONTH_FMT), subjects, voucherItemService::summaryByMonthGroupBySubject);
        List<SubjectVO> subjectVOs = SubjectConvert.INSTANCE.sumInitialBalance(subjects).stream().map(subject -> {
            SubjectVO subjectVO = SubjectConvert.INSTANCE.toSubjectVO(subject);
            subjectVO.setBalance(Optional.ofNullable(balanceBySubject.get(subject.getId())).map(AccountBalance::getLocalClosingBalance).orElse(BigDecimal.ZERO));
            return subjectVO;
        }).collect(Collectors.toList());
        return R.ok(subjectVOs);
    }

    @GetMapping("/page")
    public RPage<SubjectVO> pageSubject(@Valid QuerySubjectRequest request) {
        IPage<SubjectVO> subjects = subjectService.page(request.extractPage(), Wrappers.<Subject>lambdaQuery()
                .likeRight(StringUtils.isNotBlank(request.getNumber()), Subject::getNumber,request.getNumber())
                .likeRight(StringUtils.isNotBlank(request.getName()), Subject::getName, request.getName())
        ).convert(subject -> {
            SubjectVO subjectVO = SubjectConvert.INSTANCE.toSubjectVO(subject);
            return subjectVO;
        });
        return RPage.build(subjects);
    }

    @PostMapping("/add")
    public R addSubject(@RequestBody @Valid AddSubjectRequest request) {
        boolean numberExists = subjectService.count(Wrappers.<Subject>lambdaQuery()
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

    @PutMapping("/updateSubjectInitialBalance")
    public R updateSubjectInitialBalance(@RequestBody @Valid UpdateSubjectInitialBalanceRequest request) {
        subjectService.updateById(SubjectConvert.INSTANCE.toSubject(request));
        return R.ok();
    }

    @DeleteMapping("/delete/{id}")
    public R deleteSubject(@PathVariable("id") long id) {
        subjectService.delete(id);
        return R.ok();
    }

}
