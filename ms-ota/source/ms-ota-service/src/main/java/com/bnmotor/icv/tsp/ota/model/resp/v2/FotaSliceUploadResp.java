package com.bnmotor.icv.tsp.ota.model.resp.v2;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: R.java 
 * @Description: V2版本文件上传定制响应参数
 * @author E.YanLonG
 * @since 2021-3-15 17:13:08
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class FotaSliceUploadResp<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer code;

    private T data;

    private String msg;


    public static <T> FotaSliceUploadResp<T> ok(T data){
        FotaSliceUploadResp<T> r = new FotaSliceUploadResp<>();
        r.setCode(200);
        r.setData(data);
        r.setMsg("ok");
        return r;
    }

    public static <T> FotaSliceUploadResp<T> ok(Integer code,T data){
        FotaSliceUploadResp<T> r = new FotaSliceUploadResp<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg("ok");
        return r;
    }

    public static <T> FotaSliceUploadResp<T> ok(String msg){
        FotaSliceUploadResp<T> r = new FotaSliceUploadResp<>();
        r.setCode(200);
        r.setMsg(msg);
        return r;
    }
    
    public static <T> FotaSliceUploadResp<T> ok(){
        FotaSliceUploadResp<T> r = new FotaSliceUploadResp<>();
        r.setCode(200);
        r.setMsg("succ");
        return r;
    }

    public static <T> FotaSliceUploadResp<T> failed(String msg){
        FotaSliceUploadResp<T> r = new FotaSliceUploadResp<>();
        r.setCode(-1);
        r.setMsg(msg);
        return r;
    }

}
