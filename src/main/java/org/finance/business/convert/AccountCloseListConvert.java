package org.finance.business.convert;

import org.finance.business.entity.AccountCloseList;
import org.finance.business.web.request.CloseAccountRequest;
import org.finance.infrastructure.constants.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.YearMonth;

/**
 * @author jiangbangfa
 */

@Mapper
public interface AccountCloseListConvert {

    AccountCloseListConvert INSTANCE = Mappers.getMapper( AccountCloseListConvert.class );

    AccountCloseList toAccountCloseList(CloseAccountRequest request);

    default AccountCloseList toAccountCloseList(YearMonth yearMonth) {
        return new AccountCloseList().setYear(yearMonth.getYear())
                .setYearMonthNum(Integer.parseInt(Constants.YEAR_MONTH_FMT.format(yearMonth)))
                .setBeginDate(YearMonth.from(yearMonth).atDay(1))
                .setEndDate(YearMonth.from(yearMonth).atEndOfMonth());
    }
}
