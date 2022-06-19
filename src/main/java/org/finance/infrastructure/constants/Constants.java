package org.finance.infrastructure.constants;

import java.time.format.DateTimeFormatter;

/**
 * @author jiangbangfa
 */
public class Constants {

    public final static String LOGIN_URL = "/api/login";
    public final static String ROLE_PREFIX = "ROLE_";

    public final static DateTimeFormatter YEAR_MONTH_FMT = DateTimeFormatter.ofPattern("yyyyMM");
}
