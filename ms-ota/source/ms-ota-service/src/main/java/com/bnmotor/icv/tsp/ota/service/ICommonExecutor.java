package com.bnmotor.icv.tsp.ota.service;

/**
 * @ClassName: ICommonExecutor
 * @Description:    基本执行器接口
 * @author: xuxiaochang1
 * @date: 2020/9/8 11:50
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface ICommonExecutor {
    /**
     * 定义一个通用的执行器，其中捕获了异常信息
     * @param r
     * @param msgDesc
     */
    void execute(Runnable r, String msgDesc);
}
