package org.finance.infrastructure.constants;

/**
 * @author jiangbangfa
 */
public enum MessageEnum {

    OK("200", "服务器成功返回请求的数据。", 0),
    SUCESS("201", "新建或修改数据成功。", 0),
    SERVICE_ERROR("500", "服务器发生错误，请检查服务器。", 2),
    BIZ_ERROR("500", "业务处理异常", 2),
    GATEWAY_ERROR("502", "网关错误。", 2),
    SERVICE_UNAVAILABLE("503", "服务不可用，服务器暂时过载或维护。", 2),
    GATEWAY_TIMEOUT("504", "网关超时。", 2),
    BAD_REQUEST("400", "发出的请求有错误，服务器没有进行新建或修改数据的操作。", 2),
    NO_AUTHENTICATION("401", "用户没有权限（令牌、用户名、密码错误）。", 2),
    ACCESS_FORBIDDEN("403", "用户得到授权，但是访问是被禁止的。", 2),
    NOT_FOUND_RESOURCE("404", "发出的请求针对的是不存在的记录，服务器没有进行操作。", 2),
    REQUEST_FORMAT_INVALID("406", "请求的格式不可得。", 2),
    RESOUCE_ALREADY_REMOVE("410", "请求的资源被永久删除，且不会再得到的。", 2),
    ;

    private String code;
    private String message;
    /**
     *  0 silent; 1 message.warn; 2 message.error; 4 notification; 9 page
     */
    private int showType;

    MessageEnum(String code, String message, int showType) {
        this.code = code;
        this.message = message;
        this.showType = showType;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getShowType() {
        return showType;
    }
}
