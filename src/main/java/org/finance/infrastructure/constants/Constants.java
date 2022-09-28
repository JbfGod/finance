package org.finance.infrastructure.constants;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * @author jiangbangfa
 */
public class Constants {

    public final static BigDecimal ZERO = new BigDecimal("0");

    public final static String LOGIN_URL = "/api/login";
    public final static String ROLE_PREFIX = "ROLE_";

    public final static DateTimeFormatter YEAR_MONTH_FMT = DateTimeFormatter.ofPattern("yyyyMM");

    public final static DateTimeFormatter YMD_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

}
