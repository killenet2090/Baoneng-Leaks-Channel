package com.bnmotor.icv.tsp.cpsp.service;

import com.bnmotor.icv.tsp.cpsp.domain.request.RemoteVehControlVo;
import com.bnmotor.icv.tsp.cpsp.domain.request.RemoteVehInfoVo;
import com.bnmotor.icv.tsp.cpsp.pojo.output.RemoteVehControlOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.RemoteVehInfoOutput;

/**
* @ClassName: IRemoteVehService
* @Description: 家控车服务接口设计
* @author: liuhuaqiao1
* @date: 2021/3/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public interface IRemoteVehService {

    /**
     * 查询车辆状态信息
     * @param vo 车辆状态查询vo
     * @return
     */
    RemoteVehInfoOutput getVehInfo(RemoteVehInfoVo vo);

    /**
     * 设置空调开关
     * @param vo
     * @return
     */
    RemoteVehControlOutput airConditionerSetOffOn(RemoteVehControlVo vo);

    /**
     * 设置空调温度
     * @param vo
     * @return
     */
    RemoteVehControlOutput airConditionerSetTemperature(RemoteVehControlVo vo);

    /**
     * 设置车窗状态
     * @param vo
     * @return
     */
    RemoteVehControlOutput vehWindowSet(RemoteVehControlVo vo);

}
