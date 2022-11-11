package com.bnmotor.icv.tsp.ota.common;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: TboxAdamException
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/29 11:56
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class TboxAdamException extends RuntimeException {
    private Integer code;

    private boolean callback2Tbox = true;
    private boolean push2App = true;

    public TboxAdamException(String message) {
        super(message);
    }

    public TboxAdamException(String message, Throwable cause) {
        super(message, cause);
    }

    public TboxAdamException(BaseEnum<Integer> error) {
        this((Integer)error.getValue(), error.getDescription());
    }

    public TboxAdamException(BaseEnum<Integer> error, String message) {
        this((Integer)error.getValue(), message);
    }

    public TboxAdamException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public TboxAdamException(Throwable cause) {
        super(cause);
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean getCallback2Tbox() {
        return this.callback2Tbox;
    }

    public void setCallback2Tbox(boolean callback2Tbox) {
        this.callback2Tbox = callback2Tbox;
    }

    public boolean getPush2App() {
        return this.push2App;
    }

    public void setPush2App(boolean push2App) {
        this.push2App = push2App;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TboxAdamException)) {
            return false;
        } else {
            TboxAdamException other = (TboxAdamException)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$code = this.getCode();
                Object other$code = other.getCode();
                if (this$code == null) {
                    if (other$code != null) {
                        return false;
                    }
                } else if (!this$code.equals(other$code)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof TboxAdamException;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "TboxAdamException(code=" + this.getCode() + ")";
    }
}
