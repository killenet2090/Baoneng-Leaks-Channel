package com.bnmotor.icv.tsp.apigateway.exception;


import com.bnmotor.icv.tsp.apigateway.common.base.BaseEnum;

/**
 * @ClassName: BusinessException
 * @Description: 通用基础类
 * @author: shuqi1
 * @date: 2020/4/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class AdamException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private String msg;
    private String code;

    public AdamException(String msg)
    {
        super(msg);
        this.msg = msg;
    }

    public AdamException(String msg, Throwable e)
    {
        super(msg, e);
        this.msg = msg;
    }

    public AdamException(String code, String msg)
    {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public AdamException(String code, String msg, Throwable e)
    {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public AdamException(BaseEnum<String> error) {
        this(error.getValue(), error.getDescription());
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}
