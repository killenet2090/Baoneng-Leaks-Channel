package com.bnmotor.icv.tsp.ota.common;

import com.bnmotor.icv.adam.core.enums.BaseEnum;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.view.BaseView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;

/**
 * @ClassName: MyRestResponse
 * @Description:
 * @author: xuxiaochang1
 * @date: 2021/2/20 11:17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class MyRestResponse<T>{
    @JsonProperty("respCode")
    @JsonView({BaseView.class})
    private String respCode;
    @JsonProperty("respMsg")
    @JsonView({BaseView.class})
    private String respMsg;
    @JsonProperty("respData")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonView({BaseView.class})
    private T respData;
    @JsonProperty("respTime")
    @JsonView({BaseView.class})
    private long responseTime = System.currentTimeMillis() / 1000L;

    public MyRestResponse(BaseEnum<String> respEnum) {
        this.respCode = (String)respEnum.getValue();
        this.respMsg = respEnum.getDescription();
    }

    public MyRestResponse(T body, String respMsg, String respCode) {
        this.respCode = respCode;
        this.respMsg = respMsg;
        this.respData = body;
    }

    public MyRestResponse() {
    }

    private MyRestResponse(String respCode, String respMsg, T body, Long responseTime) {
        this.respCode = respCode;
        this.respMsg = respMsg;
        this.respData = body;
        this.responseTime = responseTime;
    }

    @JsonIgnore
    public Boolean isSuccess() {
        return RespCode.SUCCESS.getValue().equals(this.respCode);
    }

    public static <T> ResponseEntity<MyRestResponse<T>> ok(T data) {
        MyRestResponse myRestResponse = builder().respCode(RespCode.SUCCESS.getValue()).respMsg(RespCode.SUCCESS.getDescription()).responseTime(System.currentTimeMillis() / 1000L).respData(data).build();
        return ResponseEntity.ok(myRestResponse);
    }

    public static <T> ResponseEntity<MyRestResponse<T>> error(BaseEnum<String> resp) {
        return error((String)resp.getValue(), resp.getDescription());
    }

    public static <T> ResponseEntity<MyRestResponse<T>> error(BaseEnum<String> resp, String message) {
        return error((String)resp.getValue(), message);
    }

    public static <T> ResponseEntity<MyRestResponse<T>> error(String respCode, String message) {
        MyRestResponse myRestResponse = builder().respCode(respCode).respMsg(message).responseTime(System.currentTimeMillis() / 1000L).build();
        return ResponseEntity.ok(myRestResponse);
    }

    public static <T> MyRestResponse.MyRestResponseBuilder<T> builder() {
        return new MyRestResponse.MyRestResponseBuilder();
    }

    public String getRespCode() {
        return this.respCode;
    }

    public String getRespMsg() {
        return this.respMsg;
    }

    public T getRespData() {
        return this.respData;
    }

    public long getResponseTime() {
        return this.responseTime;
    }

    @JsonProperty("respCode")
    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    @JsonProperty("respMsg")
    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    @JsonProperty("respData")
    public void setRespData(T respData) {
        this.respData = respData;
    }

    @JsonProperty("respTime")
    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof MyRestResponse)) {
            return false;
        } else {
            MyRestResponse<?> other = (MyRestResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$respCode = this.getRespCode();
                Object other$respCode = other.getRespCode();
                if (this$respCode == null) {
                    if (other$respCode != null) {
                        return false;
                    }
                } else if (!this$respCode.equals(other$respCode)) {
                    return false;
                }

                Object this$respMsg = this.getRespMsg();
                Object other$respMsg = other.getRespMsg();
                if (this$respMsg == null) {
                    if (other$respMsg != null) {
                        return false;
                    }
                } else if (!this$respMsg.equals(other$respMsg)) {
                    return false;
                }

                Object this$respData = this.getRespData();
                Object other$respData = other.getRespData();
                if (this$respData == null) {
                    if (other$respData != null) {
                        return false;
                    }
                } else if (!this$respData.equals(other$respData)) {
                    return false;
                }

                if (this.getResponseTime() != other.getResponseTime()) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof MyRestResponse;
    }

    @Override
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $respCode = this.getRespCode();
        result = result * 59 + ($respCode == null ? 43 : $respCode.hashCode());
        Object $respMsg = this.getRespMsg();
        result = result * 59 + ($respMsg == null ? 43 : $respMsg.hashCode());
        Object $respData = this.getRespData();
        result = result * 59 + ($respData == null ? 43 : $respData.hashCode());
        long $responseTime = this.getResponseTime();
        result = result * 59 + (int)($responseTime >>> 32 ^ $responseTime);
        return result;
    }

    @Override
    public String toString() {
        String var10000 = this.getRespCode();
        return "MyRestResponse(respCode=" + var10000 + ", respMsg=" + this.getRespMsg() + ", respData=" + this.getRespData() + ", responseTime=" + this.getResponseTime() + ")";
    }

    public static class MyRestResponseBuilder<T> {
        private String respCode;
        private String respMsg;
        private T respData;
        private long responseTime;

        MyRestResponseBuilder() {
        }

        @JsonProperty("respCode")
        public MyRestResponse.MyRestResponseBuilder<T> respCode(String respCode) {
            this.respCode = respCode;
            return this;
        }

        @JsonProperty("respMsg")
        public MyRestResponse.MyRestResponseBuilder<T> respMsg(String respMsg) {
            this.respMsg = respMsg;
            return this;
        }

        @JsonProperty("respData")
        public MyRestResponse.MyRestResponseBuilder<T> respData(T respData) {
            this.respData = respData;
            return this;
        }

        @JsonProperty("respTime")
        public MyRestResponse.MyRestResponseBuilder<T> responseTime(long responseTime) {
            this.responseTime = responseTime;
            return this;
        }

        public MyRestResponse<T> build() {
            return new MyRestResponse(this.respCode, this.respMsg, this.respData, this.responseTime);
        }

        @Override
        public String toString() {
            return "MyRestResponse.RestResponseBuilder(respCode=" + this.respCode + ", respMsg=" + this.respMsg + ", respData=" + this.respData + ", responseTime=" + this.responseTime + ")";
        }
    }
}
