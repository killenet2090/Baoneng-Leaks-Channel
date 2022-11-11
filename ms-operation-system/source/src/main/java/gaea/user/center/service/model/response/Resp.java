package gaea.user.center.service.model.response;
import gaea.user.center.service.common.enums.Enums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
public class Resp<T>
{
    /**
     * <pre>
     * 是否成功,提供微服务之间调用判断：
     * true-成功
     * false-失败
     * </pre>
     */
    @ApiModelProperty("是否成功")
    private boolean status;
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
    public String getMessage() {
        return message;
    }

    public Resp<T> setMessage(String message)
    {
        this.message = message;
        if (Objects.nonNull(message) && !message.isEmpty()) {
            this.setCode(Enums.Status.Success.getValue());
        }
        return this;
    }

    @ApiModelProperty("返回信息")
    private String message;

    /**
     * 默认构造函数,默认创建,赋值是否成功为true,状态码=200
     */
    public Resp()
    {
        this.status = true;
        this.code = Enums.Status.Success.getValue();
    }

    /**
     * <pre>
     * 数据构造函数,说明是成功的,
     * </pre>
     *
     * @param data 返回成功数据模型
     */
    public Resp(T data)
    {
        this.data = data;
        this.status = true;
        this.code = Enums.Status.Success.getValue();
    }



    public boolean isSuccess() {
        return status;
    }

    public Resp<T> setStatus(boolean success)
    {
        this.status = success;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Resp<T> setCode(int code)
    {
        this.code = code;
        if (Enums.Status.Success.getValue() == code) {
            this.setStatus(true);
        } else {
            this.setStatus(false);
        }
        return this;
    }

    public T getData() {
        return data;
    }

    public Resp<T> setData(T data)
    {
        this.data = data;
        this.setCode(Enums.Status.Success.getValue());
        return this;
    }

    /**
     *
     * @return

    public Map<String, String> getErrsMsg() {
        return errsMsg;
    }

    public Resp<T> setErrsMsg(String errsMsg) {
        this.errsMsg = errsMsg;
        if (Objects.nonNull(errsMsg) && !errsMsg.isEmpty()) {
            this.setCode(Enums.Status.BizFailure.getValue());
        }
        return this;
    }
     */




    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
