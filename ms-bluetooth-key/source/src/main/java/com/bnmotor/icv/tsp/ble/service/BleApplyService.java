package com.bnmotor.icv.tsp.ble.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthPo;
import com.bnmotor.icv.tsp.ble.model.entity.BleUserTypePo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.*;
import com.bnmotor.icv.tsp.ble.model.response.ble.*;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: BleApplyService
 * @Description: 蓝牙钥匙申请接口
 * @author: shuqi1
 * @date: 2020/6/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface BleApplyService extends IService<UserBleKeyPo> {

    /**
     * 检车数据库里是否已经存在记录
     * @param projectId
     * @param userId
     * @param keyApplyDto
     */
    void queryHasApply(String projectId,String userId, BleKeyApplyDto keyApplyDto);

    /**
     *
     * @param projectId
     * @param uid
     * @param bleKeyApplyDto
     * @return
     */
    UserBleKeyPo assembleCreateBleKey(String projectId,String uid,BleKeyApplyDto bleKeyApplyDto);

    /**
     *在线授权接口
     * @param bleAuthPo
     * @param userBleKeyPo
     * @return
     */
    BleAuthBleKeyVo saveAuthBleKeyPerms(String userName, VehicleInfoVo vehicleInfoVo, BleDevicePinVo bleDevicePinVo,
                                        UserBleKeyPo userBleKeyPo, BleAuthPo bleAuthPo, BleKeyGeneratorDto bleKeyGeneratorDto);
    /**
     * 根据项目ID、设备ID、蓝牙钥匙ID、蓝牙钥匙状态查询数据
     * @param userBleKeyPo
     * @return
     */
    UserBleKeyPo getDbBlekeyData(UserBleKeyPo userBleKeyPo);

    /**
     * 借车人从DB查询蓝牙钥匙
     * @param  projectId,  deviceId, user_id
     * @return
     */
    List<UserBleKeyVo> getBorryDbBlekeyInfo(String projectId, String userId,String usedMobileDeviceId);

    /**
     * 车主查询已经授权的蓝牙钥匙
     * @param projectId
     * @param userId
     * @return
     */
    List<UserBleKeyVo> ownerQueryBorryDbBlekeyInfo(String projectId, String deviceId,String userId);

    /**
     * 车主查询已经授权的蓝牙钥匙
     * @param userBleKeyPo
     * @return
     */
    List<UserBleKeyPo> queryBlekeyInfoById(UserBleKeyPo userBleKeyPo);

    /**
     * 根据传入的实体类查询单个蓝牙钥匙PO
     * @param bleKeyDelDto
     * @param projectId
     * @return
     */
    UserBleKeyPo queryBlekeyInfo(BleKeyDelDto bleKeyDelDto,String projectId);
    /**
     * 查询车辆下有多少把钥匙
     * @param projectId
     * @param bleAuthDto
     * @return
     */
    void checkDeviceBleOverflowLimited(String projectId, BleAuthDto bleAuthDto);

    /**
     * 根据用户授权
     * @return
     */
    UserBleKeyPo queryUserAuthBlekey(BleUserAuthDelDto userAuthDelDto);

    /**
     * 查询车下有多少把蓝牙钥匙
     * @param projectId
     * @param deviceId
     * @return
     */
    List<UserBlekeyHisVo> checkDeviceBleCount(String projectId, String deviceId);
    /**
     * 校验车主蓝牙钥匙在当前手机上是否已经生效
     * @param userId
     * @param deviceId
     * @param mobileDeviceId
     */
    void checkVehOwnerBleKey(String projectId, String userId, String deviceId, String mobileDeviceId);

    /**
     *
     * @param bleAuthPo
     * @param dt
     */
    void checkInputTimeValid(BleAuthPo bleAuthPo, LocalDateTime dt);

    /**
     * 检查钥匙是否超出10把
     * @param projectId
     * @param userId
     * @param keyApplyDto
     */
    void checkVehBleKeyOverflowLimited(String projectId, String userId, BleKeyApplyDto keyApplyDto);


    /**
     * 修改蓝牙钥匙权限
     * @param bleKeyModifyDateDto
     * @param projectId
     * @param uid
     * @return
     * @throws ParseException
     */
    boolean updateBleKeyExpireDate(BleKeyModifyDateDto bleKeyModifyDateDto, String projectId, String  uid) throws ParseException;

    /**
     * 注销已经授权但是还没确认的数据
     * @param bleAuthCancelDto
     * @param projectId
     * @param uid
     * @return
     */
    int cancelBleKey(BleAuthCancelDto bleAuthCancelDto, String projectId, String  uid);

    /**
     * 查询撤销授权的数据
     * @param projectId
     * @param deviceId
     * @param user_id
     * @param phoneNumber
     * @return
     */
    Optional<UserBleKeyPo> getCancelBlekeyInfo(String projectId, String deviceId,String user_id,String phoneNumber);

    /**
     * 保存蓝牙钥匙
     * @param userBleKeyPo
     * @return
     */
    int saveBleKeyInfo(UserBleKeyPo userBleKeyPo);


      /**
     * 数据库记录蓝牙钥匙信息、申请记录、蓝牙钥匙关联授权服务记录
     * @param userBleKeyPo
     * @return
     */
    BleKeyApplyVo saveBleKeyAndPerms(UserBleKeyPo userBleKeyPo);
    /**
     *保存蓝牙到数据库
     * @param userBleKeyPo
     * @param now
     */
    void saveBlekeyAndRight(UserBleKeyPo userBleKeyPo, Date now);

    /**
     * 发送蓝牙信息到tbox端
     * @param userBleKeyPo
     * @param now
     * @param response
     * @param bleKey
     */
    void sendBlekeyAndRightKafka(UserBleKeyPo userBleKeyPo,Date now,UserPhoneVo response,BleKeyGeneratorDto bleKey);

    /**
     * 更新授权ID
     * @param userBleKeyPo
     */
    int updateApplyBleAuthId(UserBleKeyPo userBleKeyPo);

}
