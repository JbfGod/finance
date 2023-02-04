package org.finance.infrastructure.exception;

import lombok.Getter;
import lombok.Setter;
import org.finance.infrastructure.common.R;

/**
 * @author jiangbangfa
 */
public class HxException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private Integer showType = R.SHOW_TYPE_ERROR;

    public HxException(String message) {
        super(message);
    }

    public HxException(Throwable throwable) {
        super(throwable);
    }

    public HxException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public static HxException warn(String message) {
        HxException hxException = new HxException(message);
        hxException.setShowType(R.SHOW_TYPE_WARN);
        return hxException;
    }
}
