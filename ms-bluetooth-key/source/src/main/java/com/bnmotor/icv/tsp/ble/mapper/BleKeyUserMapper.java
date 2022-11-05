package com.bnmotor.icv.tsp.ble.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleUidKeyDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleUidKey;
import com.bnmotor.icv.tsp.ble.model.response.ble.UserBleKeyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: UserBleKey
* @Description: 用户蓝牙钥匙
记录用户申请到的蓝牙钥匙 dao层
 * @author shuqi1
 * @since 2020-07-20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface BleKeyUserMapper extends BaseMapper<UserBleKeyPo> {
    /**
     * 判断数据库中是否存在
     * @param userBleKeyPo
     * @return
     */
    int isExistInDB(UserBleKeyPo userBleKeyPo);

    /**
     * 根据蓝牙实体类查询蓝牙信息
     * @param userBleKeyPo
     * @return
     */
    UserBleKeyPo queryBleKeyInfoByPrimary(UserBleKeyPo userBleKeyPo);

    /**
     * 根据蓝牙实体类查询蓝牙信息
     * @param userBleKeyPo
     * @return
     */
    List<UserBleKeyPo> queryBleKeyListByPrimary(UserBleKeyPo userBleKeyPo);
    /**
     * 根据蓝牙实体类查询蓝牙信息
     * @param userBleKeyPo
     * @return
     */
    UserBleKeyPo queryBleKeyInfo(UserBleKeyPo userBleKeyPo);
    /**
     * 从数据库查询车主的蓝牙钥匙记录
     * @param userBleKeyPo
     * @return
     */
    List<UserBleKeyPo> queryBleOwnerKeyInfo(UserBleKeyPo userBleKeyPo);

    /**
     * 借车人查询自己的记录
     * @param userBleKeyPo
     * @return
     */
    List<UserBleKeyVo>  queryBleBorrykeyInfo(UserBleKeyPo userBleKeyPo);

    /**
     * 车主人查询自己的记录
     * @param userBleKeyPo
     * @return
     */
    List<UserBleKeyVo>  queryOwnerBleBorrykeyInfo(UserBleKeyPo userBleKeyPo);

    /**
     * 根据授权ID查询蓝牙信息
     * @param userBleKeyPo
     * @return
     */
    UserBleKeyPo queryBleKeyInfoNoAuth(UserBleKeyPo userBleKeyPo);

    /**
     * 检查钥匙数量是否超出10把
     * @param userBleKeyPo
     * @return
     */
    List<UserBleKeyPo> checkVehBleKeyOverflowLimited(UserBleKeyPo userBleKeyPo);
    /**
     * 检查钥匙数量是否超出10把
     * @param userBleKeyPo
     * @return
     */
    List<UserBleKeyPo> queryDeviceAllBles(UserBleKeyPo userBleKeyPo);

    /**
     * 根据多个用户Id查询蓝牙钥匙
     * @param bleUidKeyDto
     * @return
     */
    List<BleUidKey> queryBleKeysByUids(BleUidKeyDto bleUidKeyDto);

    /**
     * 根据bleId更新蓝牙钥匙名字
     * @param userBleKeyPo
     * @return
     */
    int updateBleNameByBleId(UserBleKeyPo userBleKeyPo);
    /**
     * 根据bleId更新车辆牌照
     * @param userBleKeyPo
     * @return
     */
    int updateBleDeviceNameBleId(UserBleKeyPo userBleKeyPo);

    /**
     * 查询最近取消的记录
     * @param userBleKeyPo
     * @return
     */
    UserBleKeyPo getCancelBlekeyInfo(UserBleKeyPo userBleKeyPo);

    /**
     *
     */
    List<UserBleKeyPo>  queryExpiredMonthNeedTip();


    /**
     * 添加蓝牙钥匙授权
     * @param userBleKeyPo
     * @return
     */
    int addBleKeyUser(UserBleKeyPo userBleKeyPo);

    /**
     * 更新蓝牙有效期
     * @param userBleKeyPo
     * @return
     */
    long updateBleKeyExpireDate(UserBleKeyPo userBleKeyPo);

    /**
     * 更新蓝牙状态
     * @param userBleKeyPo
     * @return
     */
    Integer updateBleKeyStatus(UserBleKeyPo userBleKeyPo);

    /**
     * 批量激活蓝牙钥匙
     * @param bleKeyIdList
     * @param status
     * @return
     */
    Integer batchUpdateBleKeyStatusBleIds(@Param("status") int status,@Param("bleKeyIdList") List<Long> bleKeyIdList);

    /**
     * 批量注销蓝牙钥匙
     * @param bleKeyIdList
     * @return
     */
    Integer batchUpdateBleKeyDeRegByBleIds(List<Long> bleKeyIdList);

    /**
     * 更新蓝牙钥匙权限
     * @param userBleKeyPo
     * @return
     */
    Integer updateBleKeyAuthrity(UserBleKeyPo userBleKeyPo);

    /**
     * 更新蓝牙钥匙刷新时间
     * @param userBleKeyPo
     * @return
     */
    Integer updateBleKeyRefreshTime(UserBleKeyPo userBleKeyPo);

    /**
     * 修改有效期
     * @param userBleKeyPo
     * @return
     */
    Integer updateBleKeyLimitDate(UserBleKeyPo userBleKeyPo);
    /**
     * 注销蓝牙钥匙
     * @param userBleKeyPo
     * @return
     */
    int updateBleKeyData(UserBleKeyPo userBleKeyPo);
    /**
     * 更新所有字段数据
     * @param userBleKeyPo
     * @return
     */
    int updateAllFieldById(UserBleKeyPo userBleKeyPo);

    /**
     * 根据bleId更新
     * @param userBleKeyPo
     * @return
     */
    int updateAppEncryptFieldByBleId(UserBleKeyPo userBleKeyPo);

    /**
     * 移动蓝牙钥匙历史数据
     * @return
     */
    int moveBlekeyHisData(UserBleKeyPo userBleKeyPo);

    /**
     * 更新要删除的蓝牙钥匙
     * @param userBleKeyPo
     * @return
     */
    int updateBleKeyDestroy(UserBleKeyPo userBleKeyPo);

    /**
     * tbox存在的钥匙，数据库里不存在，进行还原
     * @param userBleKeyPo
     * @return
     */
    int moveBlekeyFromHisData(UserBleKeyPo userBleKeyPo);

    /**
     * 还原历史数据到实时表后，删除历史数据
     * @param userBleKeyPo
     * @return
     */
    int deleteBlekeyFromHisData(UserBleKeyPo userBleKeyPo);

    /**
     * 删除历史数据
     * @param userBleKeyPo
     * @return
     */
    int deleteBlekeyHisData(UserBleKeyPo userBleKeyPo);

    /**
     * 修改蓝牙钥匙授权ID
     * @param userBleKeyPo
     */
    int updateApplyBleAuthId(UserBleKeyPo userBleKeyPo);
}
