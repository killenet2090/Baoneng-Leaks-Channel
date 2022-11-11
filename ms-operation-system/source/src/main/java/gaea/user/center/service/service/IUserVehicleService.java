package gaea.user.center.service.service;

import com.alibaba.fastjson.JSONArray;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import gaea.user.center.service.model.entity.UserVehiclePo;
import gaea.user.center.service.model.response.UserVehicleVo;

import java.util.List;

/**
 * @ClassName: IUserVehicleService
 * @Description: 用户车辆关系接口
 * @author: yuhb1
 * @date: 2021/03/12
 * @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IUserVehicleService {

    /**
     *
     * @return
     */
    public Page<UserVehicleVo> queryUseVehicleList(Pageable pageable, String vin);
    /**
     * 删除user_id字段中包含指定的用户ID
     *
     * @param userId
     * @param vin
     * @return
     */
    public int deleteUserId(String userId, String vin);
    /**
     * 根据vin码新增指定用户ID
     * @param userId
     * @param vins
     * @return
     */
    public int insertUserId(String userId, List vins);
    /**
     * 根据条件查询车辆关系数据
     *
     * @param userId
     * @param vin
     * @return
     */
    public List<UserVehiclePo> queryUserVehicles(String userId, String vin);
    /**
     * 根据vin码删除掉用户车辆关系记录
     * @param vin
     * @return
     */
    public int deleteUserVehicleByVin(String vin);
    /**
     * 根据vin码删除掉所有用户ID
     *
     * @param vin
     * @return
     */
    public int deleteAllUserIdByVin(String vin);
    /**
     * 批量新增用户车辆关系数据
     *
     * @param userId
     * @param type
     * @param vin
     * @param configId
     * @param tagIdList
     * @throws AdamException
     */
    public void insertUserVehicleBatchList(Long userId, Integer type, String vin, Long configId, JSONArray tagIdList) throws AdamException;
}
