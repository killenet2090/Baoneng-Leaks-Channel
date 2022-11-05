package com.bnmotor.icv.tsp.ble.model.response;

import java.io.Serializable;

public class ErrorResponseVo implements Serializable {
    private int code;
    private String message;
    private String field;
    private String objectName;

    public ErrorResponseVo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorResponseVo() {
    }

    public ErrorResponseVo(String message, String field, String objectName) {
        this.message = message;
        this.field = field;
        this.objectName = objectName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    @Override
    public String toString() {
        return "ErrorResponseVo{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", field='" + field + '\'' +
                ", objectName='" + objectName + '\'' +
                '}';
    }
}
