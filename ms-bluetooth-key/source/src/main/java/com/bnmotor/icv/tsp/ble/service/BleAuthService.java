package com.bnmotor.icv.tsp.ble.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ble.model.entity.*;
import com.bnmotor.icv.tsp.ble.model.request.ble.*;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleAuthBleKeyVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleAuthingVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyActiveVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.VehicleInfoVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserVo;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BleAuthService
 * @Description: 蓝牙钥匙授权接口
 * @author: shuqi1
 * @date: 2020/6/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface BleAuthService extends IService<BleAuthPo> {

    /**
     * 根据授权表查询出所有授权的权限记录
     * @param bleAuthPo
     * @return
     */
    List<BleKeyServicePo> assembleBleKeyServicePo(BleAuthPo bleAuthPo,UserBleKeyPo userBleKeyPo);
    /**
     * 生成授权码
     * @return
     */
    String generateAuthCode();


    /**
     * 查询tsp用户信息
     * @param phoneNumber
     * @param uid
     * @return
     */
    UserVo queryTspUserVo(String phoneNumber, String uid);

    /**
     *根据DB查询是否重复申请
     * @param projectId,uid,bleAuthDto
     * @return
     */
    void getDbHasAuthCondition(LocalDateTime dt, Date nowParam, String projectId, UserVo userVo, BleAuthDto bleAuthDto);

    /**
     * 根据手机号码查询已经确认的记录
     * @return
     */
    void getDbHasConfirmCondition(String projectId, String uid, BleAuthConfirmDto bleAuthConfirmDto);

    /**
     * 检查用户名是否存在重复
     * @param projectId
     * @param uid
     * @param bleAuthDto
     * @param userPhoneVo
     */
    void queryUserNameLimit(String projectId, String uid, BleAuthDto bleAuthDto, UserPhoneVo userPhoneVo);

    /**
     * 车主授权第一阶段，授权码生成并存库
     * @param projectId
     * @param bleAuthDto
     * @param uid
     * @param authCode
     * @return
     * @throws ParseException
     */
    BleAuthBleKeyVo saveBleAuthRight(LocalDateTime dt, Date now, String projectId, BleAuthDto bleAuthDto,
                                     VehicleInfoVo vehicleInfoVo, UserPhoneVo userPhoneVo, UserVo userVo,
                                     String authCode, String uid);

    /**
     * 根据传入的参数组合成授权信息
     * @param dt
     * @param now
     * @param projectId
     * @param bleAuthDto
     * @param vehicleInfoVo
     * @param userPhoneVo
     * @param userVo
     * @param authCode
     * @param bleKeyGeneratorDto
     * @return
     */
    BleAuthPo assembleBleAuth(LocalDateTime dt,Date now ,String projectId, BleAuthDto bleAuthDto,
                    VehicleInfoVo vehicleInfoVo,UserPhoneVo userPhoneVo, UserVo userVo,
                    String authCode,BleKeyGeneratorDto bleKeyGeneratorDto);

    /**
     * 根据传入的参数组合成授权信息
     * @param projectId
     * @param bleAuthDto
     * @param userPhoneVo
     * @return
     */
    void assemblePushMessageBleAuth(String projectId, BleAuthDto bleAuthDto, UserPhoneVo userPhoneVo,
                                    VehicleInfoVo vehicleInfoVo,String ownerUid,Long uid,String userName);

    BleAuthPo assembleTspBleAuthPo(BleAuthConfirmDto bleAuthConfirmDto, String projectId, String uid, String userName, UserPhoneVo userPhoneVo);

    /**
     * 根据传入的参数组合成钥匙信息
     * @param projectId
     * @param bleAuthDto
     * @param vehicleInfoVo
     * @param userPhoneVo
     * @param userVo
     * @param authId
     * @return
     */
    UserBleKeyPo assembleBleKey(String projectId, BleAuthDto bleAuthDto, VehicleInfoVo vehicleInfoVo,
                                UserPhoneVo userPhoneVo, UserVo userVo, Long authId,Date now);


    /**
     * 授权第二阶段， 借车人查询没有确认的记录
     * @param projectId 项目ID
     * @param Uid 用户ID
     * @param phoneNumber 手机号码
     * @return 没有授权记录实体类
     */
    List<BleAuthingVo> blekeyAuthorisingQueryService(String projectId, String Uid, String phoneNumber);

    /**
     * 授权第三阶段（确认阶段）,检查授权码是否合法
     * @param bleAuthConfirmDto
     * @param projectId
     * @param uid
     * @return
     */
    void checkAuthCodeInfo(BleAuthConfirmDto bleAuthConfirmDto,String projectId,String uid);

    /**
     * 用户换手机登录，重新生成蓝牙钥匙
     * @return
     */
    BleAuthBleKeyVo createNewBle(String projectId,String uid,BleAuthConfirmDto bleAuthConfirmDto);
    /**
     * 保存合成的数据到数据库
     * @param bleAuthPo
     * @param bleKeyInfoPo
     */
    List<BleKeyServicePo> saveAuthBleData(BleAuthPo bleAuthPo,UserBleKeyPo bleKeyInfoPo);

    /**
     * 离线激活更新
     * @param bleKeyInfoParam
     * @return
     */
    void activeOfflineAuthBleKey(UserBleKeyPo bleKeyInfoParam);
    /**
     * 取消授权
     * @param bleAuthPo
     * @param projectId
     * @param uid
     * @return
     */
    int  cancelAuth(BleAuthPo bleAuthPo, String projectId, String uid);

    /**
     * 检查取消的蓝牙钥匙是否存在
     * @param bleAuthCancelDto
     * @param projectId
     * @param uid
     * @return
     */
    BleAuthPo cancelCheck(BleAuthCancelDto bleAuthCancelDto, String projectId, String  uid);

}
