package gaea.vehicle.basic.service.models.page;


import gaea.vehicle.basic.service.models.constant.Enums;
import gaea.vehicle.basic.service.models.domain.Msg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <pre>
 *  统一返回结果对象,这里不提供 Resp<T>(String errMsg) 构造函数，防止跟数据构造函数冲突
 * </pre>
 *
 * @param <T>
 * @author jiankang.xia
 * @version 1.0.0
 */
@ApiModel("返回结果")
public class Resp<T> {
    /**
     * <pre>
     * 是否成功,提供微服务之间调用判断：
     * true-成功
     * false-失败
     * </pre>
     */
    @ApiModelProperty("是否成功")
    private boolean success;
    /**
     * <pre>
     * 状态码,主要提供前端使用:
     * 200-成功
     * 500-异常
     * 700-校验异常
     * 等等
     * </pre>
     */
    @ApiModelProperty("状态码")
    private int code;
    /**
     * <pre>
     *  返回数据信息.
     * </pre>
     */
    @ApiModelProperty("返回数据")
    private T data;
    /**
     * <pre>
     * 异常信息,这里提供是异常的,如果要提示正常,请使用{@link Resp#data}
     * </pre>
     */
    @ApiModelProperty("错误信息")
    private Map<String, String> errsMsg;

    /**
     * 默认构造函数,默认创建,赋值是否成功为true,状态码=200
     */
    public Resp() {
        this.success = true;
        this.code = Enums.Status.Success.getValue();
    }

    /**
     * <pre>
     * 数据构造函数,说明是成功的,
     * </pre>
     *
     * @param data 返回成功数据模型
     */
    public Resp(T data) {
        this.data = data;
        this.success = true;
        this.code = Enums.Status.Success.getValue();
    }

    /**
     * <pre>
     *  根据错误信息，构造对象
     * </pre>
     *
     * @param msg 返回错误信息
     */
    public Resp(Msg msg) {
        this.setErrMsg(msg);
    }

    public boolean isSuccess() {
        return success;
    }

    public Resp<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Resp<T> setCode(int code) {
        this.code = code;
        if (Enums.Status.Success.getValue() == code) {
            this.setSuccess(true);
        } else {
            this.setSuccess(false);
        }
        return this;
    }

    public T getData() {
        return data;
    }

    public Resp<T> setData(T data) {
        this.data = data;
        this.setCode(Enums.Status.Success.getValue());
        return this;
    }

    public Map<String, String> getErrsMsg() {
        return errsMsg;
    }

    public Resp<T> setErrsMsg(Map<String, String> errsMsg) {
        this.errsMsg = errsMsg;
        if (Objects.nonNull(errsMsg) && !errsMsg.isEmpty()) {
            this.setCode(Enums.Status.BizFailure.getValue());
        }
        return this;
    }

    /**
     * <pre>
     *  扩展方法,提供便利方法,方法名要分开,防止反射出问题
     * </pre>
     *
     * @param code 异常码
     * @param msg  错误信息
     * @return 返回数据
     */
    public Resp<T> setErrMsg(String code, String msg) {
        if (StringUtils.isEmpty(code)) {
            return this;
        }
        return setErrMsg(new Msg(code, msg));
    }

    /**
     * <pre>
     *  扩展便利方法,单条错误信息新增
     * </pre>
     *
     * @param msg 提示信息
     * @return 返回数据
     */
    public Resp<T> setErrMsg(Msg msg) {
        if (Objects.isNull(this.errsMsg)) {
            this.errsMsg = new HashMap<>(16, 0.8f);
        }
        this.errsMsg.put(msg.getCode(), msg.getMsg());
        this.setCode(Enums.Status.BizFailure.getValue());
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
