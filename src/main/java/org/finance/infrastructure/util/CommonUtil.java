package org.finance.infrastructure.util;

import java.time.YearMonth;

import static org.finance.infrastructure.constants.Constants.YEAR_MONTH_FMT;

/**
 * @author jiangbangfa
 */
public class CommonUtil {

    public static int getYearMonthNum(YearMonth yearMonth) {
        return Integer.parseInt(yearMonth.format(YEAR_MONTH_FMT));
    }
}
