package com.bnmotor.icv.tsp.cpsp.service;

import com.bnmotor.icv.tsp.cpsp.pojo.output.AccountBindStatusOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.AccountQrCodeOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.AccountUnBindOutput;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;

/**
 * @ClassName: IAccountService
 * @Description: 账户管理接口
 * @author: jiangchangyuan1
 * @date: 2021/3/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IAccountService {
    /**
     * 生成二维码
     * @param vin 车架号
     * @return
     */
    AccountQrCodeOutput createQrcode(HttpServletRequest request, String vin);

    /**
     * 查询绑定状态
     * @param request
     * @param vin 车架号
     * @return
     */
    AccountBindStatusOutput getAccountBindStatus(HttpServletRequest request, String vin);

    /**
     * 账户解除绑定
     * @param request
     * @param vin 车架号
     * @return
     */
    AccountUnBindOutput accountUnbind(HttpServletRequest request, String vin);
}
