package gaea.user.center.service.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.entity.UserCarAssemblyPo;
import gaea.user.center.service.model.response.UserCarAssemblyVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: UserCarAssemblyMapper
 * @Description: 用户-车辆集Dao层
 * @author: jiangchangyuan1
 * @date: 2020/10/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface UserCarAssemblyMapper extends AdamMapper<UserCarAssemblyPo> {
    /**
     * 批量删除用户-车辆集数据
     * @param userId
     */
    void deleteBatchUserAssembly(Long userId);

    /**
     * 查询用户-车辆集列表
     * @param userId
     * @return
     */
    List<UserCarAssemblyPo> queryUserCarAssemblyListByUserId(@Param("userId") Long userId);

    /**
     * 根据配置获取用户-车辆集列表
     * @param userCarAssemblyPo
     * @return
     */
    List<UserCarAssemblyPo> getCarAssemblyListByConfig(@Param("param")UserCarAssemblyPo userCarAssemblyPo);

    /**
     * 根据标签列表获取用户-车辆集列表
     * @param param
     * @return
     */
    List<UserCarAssemblyPo> getCarAssemblyListByTags(@Param("param")UserCarAssemblyVO param);
}
