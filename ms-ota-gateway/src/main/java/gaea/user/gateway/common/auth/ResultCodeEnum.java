package gaea.user.gateway.common.auth;

/**
 * @description: 自定义状态码枚举类
 * @author: jiankang.xia
 * @date: 2019-04-30 15:16
 */
public enum ResultCodeEnum {


    /**
     * 成功状态码
     */
    SUCCESS(1, "SUCCESS"),

    /**
     * 失败状态码
     */
    FAIL(0,"FAIL"),


    /**
     * 系统错误
     */
    SYSTEM_INNER_ERROR(400, " SYSTEM_INNER_ERROR"),
    SERVICE_TRANSFER_ERROR(401,"SERVICE_TRANSFER_ERROR"),
    SERVICE_HYSTRIX_ERROR(402,"SERVICE_HYSTRIX_ERROR");




    private int code;
    private String message;

    ResultCodeEnum() {
    }

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    public ResultCodeEnum fillArgs(Object... args) {
        this.message = String.format(this.message, args);
        return this;
    }

    @Override
    public String toString() {
        return "ResultCodeEnum [code=" + code + ", message=" + message + "]";
    }
}
