package com.bnmotor.icv.tsp.ota.aop.aspect;

/**
 * @ClassName: MyRedisException
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/7 15:25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class MyRedisException extends Exception{
    /*MyRedisException(Exception e){
        super(e);
    }*/

    MyRedisException(String errorMsg){
        super(errorMsg);
    }

    MyRedisException(/*Exception e, */String errorMsg, Throwable cause){
        super(errorMsg, cause);
    }
}
