package org.finance.business.manage.report;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.finance.business.convert.ReportConvert;
import org.finance.business.entity.AccountBalance;
import org.finance.business.entity.Report;
import org.finance.business.entity.Subject;
import org.finance.business.entity.templates.accountting.system.AssetsLiability;
import org.finance.business.service.AccountBalanceService;
import org.finance.business.service.ReportService;
import org.finance.business.service.SubjectService;
import org.finance.business.service.VoucherItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
@Service
public class AssetsLiabilityManage {

    @Resource
    private AccountBalanceService accountBalanceService;
    @Resource
    private VoucherItemService voucherItemService;
    @Resource
    private SubjectService subjectService;
    @Resource
    private ReportService reportService;

    public List<AssetsLiability> list(YearMonth period) {
        List<Subject> subjects = subjectService.list();
        List<Report> reports = reportService.listWithFormula(Wrappers.<Report>lambdaQuery()
            .eq(Report::getCategory, Report.Category.ASSETS)
        );

        Map<Long, AccountBalance> balanceBySubId = accountBalanceService.summaryBySubjectId(period, subjects, voucherItemService::summaryByMonthGroupBySubject);
        Map<String, AccountBalance> balanceBySubNumber = balanceBySubId.values().stream().collect(Collectors.toMap(AccountBalance::getSubjectNumber, v -> v));
        reportService.recursion(reports, balanceBySubNumber);
        return ReportConvert.INSTANCE.toAssetsLiabilities(reports);
    }
}
