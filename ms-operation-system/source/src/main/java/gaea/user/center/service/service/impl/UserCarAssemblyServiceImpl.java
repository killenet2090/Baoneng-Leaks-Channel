package gaea.user.center.service.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import gaea.user.center.service.mapper.UserCarAssemblyMapper;
import gaea.user.center.service.mapstuct.UserCarAssemblyVoMapper;
import gaea.user.center.service.model.entity.UserCarAssemblyPo;
import gaea.user.center.service.model.response.UserCarAssemblyVO;
import gaea.user.center.service.service.IUserCarAssemblyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @ClassName: UserCarAssemblyServiceImpl
 * @Description: 用户-车辆集实现类
 * @author: jiangchangyuan1
 * @date: 2020/10/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
public class UserCarAssemblyServiceImpl extends ServiceImpl<UserCarAssemblyMapper, UserCarAssemblyPo> implements IUserCarAssemblyService {

    private final UserCarAssemblyMapper userCarAssemblyMapper;

    public UserCarAssemblyServiceImpl(UserCarAssemblyMapper userCarAssemblyMapper) {
        this.userCarAssemblyMapper = userCarAssemblyMapper;
    }

    /**
     * 批量保存用户车辆集信息
     * @param userCarAssemblyPoList
     */
    @Override
    public void saveBatchUserCarAssembly(List<UserCarAssemblyPo> userCarAssemblyPoList) throws AdamException {
        userCarAssemblyMapper.saveAllInBatch(userCarAssemblyPoList);
    }

    /**
     * 批量删除用户-车辆集关系信息
     * @param userId
     */
    @Override
    public void deleteBatchUserAssembly(Long userId) throws AdamException{
        userCarAssemblyMapper.deleteBatchUserAssembly(userId);
    }

    /**
     * 查询用户-车辆集列表
     * @param userId
     * @return
     */
    @Override
    public List<UserCarAssemblyVO> queryUserCarAssemblyListByUserId(Long userId) throws AdamException {
        List<UserCarAssemblyPo> userCarAssemblyPoList = userCarAssemblyMapper.queryUserCarAssemblyListByUserId(userId);
        List<UserCarAssemblyVO> userCarAssemblyVoList = UserCarAssemblyVoMapper.INSTANCE.map(userCarAssemblyPoList);
        return userCarAssemblyVoList;
    }

    /**
     * 根据配置、标签查询出所有符合条件的用户-车辆集关系列表
     * @param configId 配置id
     * @param tagIdList 标签列表
     * @return
     */
    @Override
    public List<UserCarAssemblyPo> queryUserCarAssemblyList(Long configId, JSONArray tagIdList) {
        UserCarAssemblyPo userCarAssemblyPo = new UserCarAssemblyPo();
        //查询配置对应的用户-车辆集列表
        List<UserCarAssemblyPo> userCarAssemblyPoConfigList = new ArrayList<>();
        if(null != configId){
            userCarAssemblyPo.setType(1);
            userCarAssemblyPo.setCid(configId);
            userCarAssemblyPoConfigList = userCarAssemblyMapper.getCarAssemblyListByConfig(userCarAssemblyPo);
        }

        //查询标签数组对应的用户-车辆集列表
        UserCarAssemblyVO param = new UserCarAssemblyVO();
        //综合配置和标签列表对应的用户-车辆集结果集
        List<UserCarAssemblyPo> result = new ArrayList<>();
        result.addAll(userCarAssemblyPoConfigList);
        if(null != tagIdList && tagIdList.size() > 0){
            List<Long> cids = JSONObject.parseArray(tagIdList.toJSONString(), Long.class);
            param.setCids(cids);
            param.setType(2);
            List<UserCarAssemblyPo> userCarAssemblyPoTagsList = userCarAssemblyMapper.getCarAssemblyListByTags(param);
            result.addAll(userCarAssemblyPoTagsList);
        }
        return result;

    }
}
