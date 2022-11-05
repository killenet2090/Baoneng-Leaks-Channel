package com.bnmotor.icv.tsp.ble.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleKeyServicePo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: BleReviseMapper
 * @Description: 蓝牙钥匙属性修改业务，用mybatis访问数据库的mapper文件
 * @author: shuqi1
 * @date: 2020/5/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Mapper
public interface BleKeyMapMapper extends BaseMapper<BleKeyServicePo> {
    /**
     * 查询蓝牙钥匙信息
     * @param bleKeyInfoPo
     * @return
     */
    UserBleKeyPo queryBleKeyInfo(UserBleKeyPo bleKeyInfoPo);
    /**
     * 查询,返回list
     * @param bleKeyServicePo
     * @return
     */
    List<Long> queryBleService(BleKeyServicePo bleKeyServicePo);
    /**
     * 根据项目ID和蓝牙钥匙ID查询serviceId
     * @param projectId
     * @param bleId
     * @return
     */
    List<Long>  queryServiceByBleId(String projectId,String bleId);
    /**
     * 添加车主申请蓝牙钥匙权限
     * @param bleKeyServicePo
     * @return
     */
    int addBleKeyServiceMap(BleKeyServicePo bleKeyServicePo);
    /**
     * 根据删除蓝牙钥匙ID删除蓝牙钥匙
     * @param projectId
     * @param bleId
     * @return
     */
    int deleteBleKeyServiceById(@Param("projectId") String projectId,@Param("bleId") String bleId,@Param("serviceId") Long serviceId);
    /**
     * 批量删除
     * @param bleKeyServicePoList
     * @param projectId
     * @return
     */
    int deleteByBleKeyServicesByIds(@Param("projectId") String projectId,@Param("bleKeyServicePoList") List<UserBleKeyPo> bleKeyServicePoList);
    /**
     * 修改蓝牙钥匙权限
     * @param bleKeyInfoPo
     * @return
     */
    Integer updateBleKeyService(UserBleKeyPo bleKeyInfoPo);
}
