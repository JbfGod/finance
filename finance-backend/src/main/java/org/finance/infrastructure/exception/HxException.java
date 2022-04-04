package org.finance.infrastructure.exception;

/**
 * @author jiangbangfa
 */
public class HxException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public HxException(String message) {
        super(message);
    }

    public HxException(Throwable throwable) {
        super(throwable);
    }

    public HxException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
