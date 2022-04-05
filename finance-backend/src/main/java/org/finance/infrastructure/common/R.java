package org.finance.infrastructure.common;

import lombok.Data;
import lombok.experimental.Accessors;
import org.finance.infrastructure.constants.MessageEnum;

import java.io.Serializable;

/**
 * @author jiangbangfa
 */
@Data
@Accessors(chain = true)
public class R<T> implements Serializable {

    /**
     *  0 silent; 1 message.warn; 2 message.error; 4 notification; 9 page
     */
    public final static int SHOW_TYPE_SILENT = 0;
    public final static int SHOW_TYPE_WARN = 1;
    public final static int SHOW_TYPE_ERROR = 2;
    public final static int SHOW_TYPE_NOTIFICATION = 4;
    public final static int SHOW_TYPE_PAGE = 9;

    private boolean success;
    private T data;
    private String errorCode;
    private String message;
    private int showType;
    private String traceId;
    private String host;

    public R() {
    }

    public R(boolean success) {
        this.success = success;
    }

    public static <T> R<T> ok(T data) {
        return new R<T>(true).setData(data);
    }

    public static <T> R<T> ok(T data, String message) {
        return new R<T>(true).setData(data)
                .setShowType(SHOW_TYPE_NOTIFICATION).setMessage(message);
    }

    public static <T> R<T> page(T data) {
        return new R<T>(true).setData(data).setShowType(SHOW_TYPE_PAGE);
    }

    public static <T> R<T> error(String errorCode, String errorMsg) {
        return new R<T>(false).setShowType(SHOW_TYPE_ERROR)
                .setErrorCode(errorCode).setMessage(errorMsg);
    }

    public static <T> R<T> error(MessageEnum messageEnum) {
        return new R<T>(false).setShowType(messageEnum.getShowType())
                .setErrorCode(messageEnum.getCode()).setMessage(messageEnum.getMessage());
    }

    public static <T> R<T> error(MessageEnum messageEnum, String message) {
        return new R<T>(false).setShowType(messageEnum.getShowType())
                .setErrorCode(messageEnum.getCode()).setMessage(message);
    }

    public static <T> R<T> warn(String errorCode, String errorMsg) {
        return new R<T>(false).setShowType(SHOW_TYPE_WARN)
                .setErrorCode(errorCode).setMessage(errorMsg);
    }

    public R<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public R<T> setData(T data) {
        this.data = data;
        return this;
    }

    public R<T> setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public R<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public R<T> setShowType(int showType) {
        this.showType = showType;
        return this;
    }

    public R<T> setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public R<T> setHost(String host) {
        this.host = host;
        return this;
    }
}
