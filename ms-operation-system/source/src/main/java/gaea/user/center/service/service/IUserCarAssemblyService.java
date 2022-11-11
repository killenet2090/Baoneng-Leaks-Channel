package gaea.user.center.service.service;

import com.alibaba.fastjson.JSONArray;
import gaea.user.center.service.model.entity.UserCarAssemblyPo;
import gaea.user.center.service.model.response.UserCarAssemblyVO;

import java.util.List;

/**
 * @ClassName: IUserCarAssemblyService
 * @Description: 用户-车辆集
 * @author: jiangchangyuan1
 * @date: 2020/10/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IUserCarAssemblyService {
    /**
     * 批量保存用户-车辆集关系
     * @param userCarAssemblyPoList
     */
    void saveBatchUserCarAssembly(List<UserCarAssemblyPo> userCarAssemblyPoList);

    /**
     * 批量删除用户-车辆集关系
     * @param id
     */
    void deleteBatchUserAssembly(Long id);

    /**
     * 查询用户-车辆集列表
     * @param userId
     * @return
     */
    List<UserCarAssemblyVO> queryUserCarAssemblyListByUserId(Long userId);

    /**
     * 根据配置、标签列表查询出用户-车辆集关系列表
     * @param config
     * @param tagIdList
     * @return
     */
    List<UserCarAssemblyPo> queryUserCarAssemblyList(Long config, JSONArray tagIdList);
}
