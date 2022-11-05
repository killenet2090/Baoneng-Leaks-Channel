package com.bnmotor.icv.tsp.ble.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: TspBleuthPoMapper
 * @Description: 蓝牙钥匙授权业务用mybatis访问数据库的mapper文件
 * @author: shuqi1
 * @date: 2020/5/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Mapper
public interface BleAuthMapper extends BaseMapper<BleAuthPo> {
    /**
     * 更新授权记录
     * @param bleAuthPo
     * @return
     */
    int updateBleAuth(BleAuthPo bleAuthPo);
    /**
     * 确认更新授权记录
     * @param bleAuthPo
     * @return
     */
    int confirmUpdateBleAuth(BleAuthPo bleAuthPo);

    /**
     * 授权记录通用查询
     * @param bleAuthPo
     * @return
     */
    BleAuthPo queryBleAuth(BleAuthPo bleAuthPo);

    /**
     * 授权记录通用查询列表
     * @param bleAuthPo
     * @return
     */
    List<BleAuthPo> queryDeviceBleAuth(BleAuthPo bleAuthPo);

    /**
     * 授权记录通用查询
     * @param bleAuthPo
     * @return
     */
    BleAuthPo queryBleAuthToActive(BleAuthPo bleAuthPo);

    /**
     * 查询授权记录是否存在
     * @param bleAuthPo
     * @return
     */
    BleAuthPo queryExistBleAuth(BleAuthPo bleAuthPo);

    /**
     * 查询没有授权记录
     * @param bleAuthPo
     * @return
     */
    List<BleAuthPo> queryBleNoAuth(BleAuthPo bleAuthPo);

    /**
     * 查询修改的授权记录
     * @param bleAuthPo
     * @return
     */
    BleAuthPo queryBleAuthModify(BleAuthPo bleAuthPo);

    /**
     *查询用户名是否有重复
     * @param bleAuthPo
     */
    BleAuthPo queryUserNameLimit(BleAuthPo bleAuthPo);

    /**
     * 查询授权码超时和授权有效期超时的记录
     * @return
     */
    List<BleAuthPo> queryExpiredBleAuthCode(BleAuthPo bleAuthPo);

    /**
     * 查询授权有效期超时的记录
     * @return
     */
    List<BleAuthPo> queryExpiredBleAuthLimit(BleAuthPo bleAuthPo);

    /**
     * 查询哪些信息只剩下一天时间，需要提醒的记录
     * @return
     */
    List<BleAuthPo> queryExpiredDayNeedTip();

    /**
     * 更新有效期
     * @param bleAuthPo
     * @return
     */
    int updateAuthExpireDate(BleAuthPo bleAuthPo);

    /**
     * 创建授权
     * @param bleAuthPo
     * @return
     */
    int cancelAuth(BleAuthPo bleAuthPo);

    /**
     * 取消授权检测
     * @param bleAuthPo
     * @return
     */
    BleAuthPo cancelBleKeyCheck(BleAuthPo bleAuthPo);

    /**
     * 移动授权历史数据
     * @return
     */
    int moveAuthHisData(BleAuthPo bleAuthPo);

    /**
     * 删除授权历史数据
     * @return
     */
    int deleteAuthHisData(BleAuthPo bleAuthPo);
}
