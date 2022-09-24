package org.finance.business.service.event;

import java.time.YearMonth;

/**
 * @author jiangbangfa
 */
public interface AccountEvent {

    void onAccountClosed(YearMonth yearMonth);

    void onCancelClosedAccount(YearMonth yearMonth);

}
