package org.finance.business.manage;

import org.finance.business.convert.AccountBalanceConvert;
import org.finance.business.service.AccountBalanceService;
import org.finance.business.service.SubjectService;
import org.finance.business.web.vo.AccountBalanceVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.YearMonth;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
@Service
public class ReportManage {

    @Resource
    private AccountBalanceService accountBalanceService;
    @Resource
    private SubjectService subjectService;

    public List<AccountBalanceVO> listAccountBalance(YearMonth yearMonth) {
        Function<Long, String> nameBySubjectId = subjectService.getNameFunction();
        return accountBalanceService.summary(yearMonth)
            .stream().map(accountBalance -> {
                AccountBalanceVO accountBalanceVO = AccountBalanceConvert.INSTANCE.toAccountBalance(accountBalance);
                accountBalanceVO.setSubjectName(nameBySubjectId.apply(accountBalance.getSubjectId()));
                return accountBalanceVO;
            })
            .collect(Collectors.toList());
    }

}
