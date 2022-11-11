package gaea.user.center.service.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.entity.UserVehiclePo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserVehicleMapper extends AdamMapper<UserVehiclePo> {
    /**
     *  分页查询 数据
     * @param iPage 分页数据
     * @param vin 查询条件
     * @return 数据集合
     */
    Page<UserVehiclePo> queryUserVehiclePage(IPage iPage, @Param("vin") String vin);
    /**
     * 根据vin码删除掉所有用户ID
     *
     * @param vin
     * @return
     */
    int deleteAllUserIdByVin(String vin);

    /**
     * 根据vin码删除掉用户车辆关系记录
     * @param vin
     * @return
     */
    int deleteUserVehicleByVin(String vin);
    /**
     * 删除user_id字段中包含指定的用户ID
     *
     * @param userId
     * @return
     */
    int deleteUserId(@Param("userId")String userId, @Param("vin")String vin);
    /**
     * 根据vin码新增指定用户ID
     *
     * @param userId
     * @param vins
     * @return
     */
    int insertUserId(@Param("userId")String userId, @Param("vins") List vins);
    /**
     * 插入车辆用户关系数据
     *
     * @param userVehiclePoList
     * @return
     */
    int insertUserVehicles(@Param("userVehiclePoList") List userVehiclePoList);
    /**
     * 更新车辆关系中关联的车辆用户ID
     *
     * @param userVehiclePo
     * @return
     */
    int updateUserId(@Param("query")UserVehiclePo userVehiclePo);
    /**
     * 根据条件查询车辆关系数据
     *
     * @param userId
     * @param vin
     * @return
     */
    List<UserVehiclePo> queryUserVehicles(@Param("userId")String userId, @Param("vin") String vin);
}
