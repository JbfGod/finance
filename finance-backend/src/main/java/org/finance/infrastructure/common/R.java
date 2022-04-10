package org.finance.infrastructure.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import org.finance.infrastructure.constants.MessageEnum;

import java.io.Serializable;

import static org.finance.infrastructure.constants.MessageEnum.BAD_REQUEST;
import static org.finance.infrastructure.constants.MessageEnum.BIZ_ERROR;

/**
 * @author jiangbangfa
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public static <T> R<T> ok() {
        return new R<T>(true).setMessage("操作成功！").setShowType(SHOW_TYPE_NOTIFICATION);
    }

    public static <T> R<T> ok(T data) {
        return new R<T>(true).setShowType(SHOW_TYPE_SILENT).setData(data);
    }

    public static <T> R<T> ok(T data, String message) {
        return new R<T>(true).setData(data)
                .setShowType(SHOW_TYPE_NOTIFICATION).setMessage(message);
    }

    public static <T> R<T> error(String errorMsg) {
        return R.error(BIZ_ERROR, errorMsg);
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

    public static <T> R<T> warn(String errorMsg) {
        return R.warn(BAD_REQUEST, errorMsg);
    }

    public static <T> R<T> warn(String errorCode, String errorMsg) {
        return new R<T>(false).setShowType(SHOW_TYPE_WARN)
                .setErrorCode(errorCode).setMessage(errorMsg);
    }

    public static <T> R<T> warn(MessageEnum messageEnum) {
        return new R<T>(false).setShowType(SHOW_TYPE_WARN)
                .setErrorCode(messageEnum.getCode()).setMessage(messageEnum.getMessage());
    }

    public static <T> R<T> warn(MessageEnum messageEnum, String message) {
        return new R<T>(false).setShowType(SHOW_TYPE_WARN)
                .setErrorCode(messageEnum.getCode()).setMessage(message);
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
