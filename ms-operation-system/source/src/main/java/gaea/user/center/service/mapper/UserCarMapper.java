package gaea.user.center.service.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.entity.UserCarPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: UserCarMapper
 * @Description: 用户车辆关系Dao
 * @author: jiangchangyuan1
 * @date: 2020/10/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface UserCarMapper extends AdamMapper<UserCarPo> {
    /**
     * 批量插入用户-车辆关系
     * @param userCarPoList
     */
    void insertUserCarBatchList(@Param("userCarPoList") List userCarPoList);

    /**
     * 根据vin码清除原有用户-车辆关系
     * @param vin
     */
    void deleteUserCarBatchByVin(@Param("vin") String vin);

    /**
     * 根据用户id批量删除用户-车辆关系
     * @param userId
     */
    void deleteUserCarBatchByUserId(Long userId);

    /**
     * 根據
     * @param userId
     * @param vin
     */
    void deleteUserCarBatchByUserIdAndVin(@Param("userId") Long userId, @Param("vin") String vin);
}
