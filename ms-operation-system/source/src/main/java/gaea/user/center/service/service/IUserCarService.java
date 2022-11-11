package gaea.user.center.service.service;

import com.alibaba.fastjson.JSONArray;

/**
 * @ClassName: IUserCarService
 * @Description: 用户车辆关系接口
 * @author: jiangchangyuan1
 * @date: 2020/10/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IUserCarService {
    /**
     * 批量保存用户车辆关系
     * @param userId
     * @param type
     * @param vin
     * @param config
     * @param tagIdList
     */
    void insertUserCarBatchList(Long userId, Integer type, String vin, Long config, JSONArray tagIdList);

    /**
     * 批量删除用户id
     * @param userId
     */
    void deleteBatchUserCarByUserId(Long userId);
}
