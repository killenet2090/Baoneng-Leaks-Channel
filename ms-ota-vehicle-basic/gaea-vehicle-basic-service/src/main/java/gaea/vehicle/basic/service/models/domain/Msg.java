package gaea.vehicle.basic.service.models.domain;

/**
 * <pre>
 * 错误信息提示,包括编码和系统给的提示.
 * </pre>
 *
 * @author jiankang.xia
 * @version 1.0.0
 */
public final class Msg {
    /**
     * <pre>
     * 编码,建议使用数字
     * </pre>
     */
    private final String code;
    /**
     * <pre>
     *  默认提示
     * </pre>
     */
    private final String msg;

    /**
     * <pre>
     *  构造函数
     * </pre>
     *
     * @param code 错误码
     * @param msg  错误信息
     */
    public Msg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
