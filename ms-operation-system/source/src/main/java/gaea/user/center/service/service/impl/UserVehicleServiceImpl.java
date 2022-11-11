package gaea.user.center.service.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import com.bnmotor.icv.adam.data.mysql.utils.PageUtil;
import gaea.user.center.service.mapper.UserVehicleMapper;
import gaea.user.center.service.mapstuct.UserVehicleVoMapper;
import gaea.user.center.service.model.entity.UserCarAssemblyPo;
import gaea.user.center.service.model.entity.UserVehiclePo;
import gaea.user.center.service.model.response.UserVehicleVo;
import gaea.user.center.service.service.IUserCarAssemblyService;
import gaea.user.center.service.service.IUserVehicleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * @ClassName: UserVehicleServiceImpl
 * @Description: 用户车辆接口
 * @author: yuhb1
 * @date: 2021/03/12
 * @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class UserVehicleServiceImpl extends ServiceImpl<UserVehicleMapper, UserVehiclePo> implements IUserVehicleService {

    @Autowired
    IUserCarAssemblyService userCarAssemblyService;

    private final UserVehicleMapper userVehicleMapper;

    public UserVehicleServiceImpl(UserVehicleMapper userVehicleMapper) {
        this.userVehicleMapper = userVehicleMapper;
    }

    public Page<UserVehicleVo> queryUseVehicleList(Pageable pageable, String vin) {
        IPage iPage = PageUtil.map(pageable);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserVehiclePo> userVehiclePoPage = userVehicleMapper.queryUserVehiclePage(iPage, vin);
        Page<UserVehicleVo> page = UserVehicleVoMapper.INSTANCE.map(userVehiclePoPage);
        return page;
    }

    /**
     * 插入车辆用户关系数据
     *
     * @param userVehiclePoList
     * @return
     */
    public int insertUserVehicles(List userVehiclePoList) {
        return userVehicleMapper.insertUserVehicles(userVehiclePoList);
    }

    /**
     * 删除user_id字段中包含指定的用户ID
     *
     * @param userId
     * @return
     */
    public int deleteUserId(String userId, String vin) {
        return userVehicleMapper.deleteUserId(userId,vin);
    }
    /**
     * 根据vin码新增指定用户ID
     * @param userId
     * @param vins
     * @return
     */
    public int insertUserId(String userId, List vins){
        return userVehicleMapper.insertUserId(userId, vins);
    }
    /**
     * 根据条件查询车辆关系数据
     *
     * @param userId
     * @param vin
     * @return
     */
    public List<UserVehiclePo> queryUserVehicles(String userId, String vin){
        return userVehicleMapper.queryUserVehicles(userId, vin);
    }
    /**
     * 根据vin码删除掉所有用户ID
     *
     * @param vin
     * @return
     */
    public int deleteAllUserIdByVin(String vin){
        return userVehicleMapper.deleteAllUserIdByVin(vin);
    }
    /**
     * 根据vin码删除掉用户车辆关系记录
     * @param vin
     * @return
     */
    public int deleteUserVehicleByVin(String vin){
        return userVehicleMapper.deleteUserVehicleByVin(vin);
    }
    /**
     * 更新车辆关系中关联的车辆用户ID
     *
     * @param userVehiclePo
     * @return
     */
    int updateUserId(UserVehiclePo userVehiclePo){
        return userVehicleMapper.updateUserId(userVehiclePo);
    }

    /**
     * 新增用户-车辆关系记录
     * 考虑去重问题，Kafka消费消息为逐条消费，数据量不大
     * @param userId 用户id
     * @param type 数据修改类型：1-全量，2-增量
     * @param vin 车辆vin码
     * @param configId 车辆配置id
     * @param tagIdList 车辆标签列表ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertUserVehicleBatchList(Long userId, Integer type, String vin, Long configId, JSONArray tagIdList) throws AdamException {
        if(StringUtils.isNotEmpty(vin)) {
            List<UserVehiclePo> userVehiclePoList = new ArrayList<>();
            if(type == 1){
                List<UserVehiclePo> list = this.queryUserVehicles(null, vin);
                if (CollectionUtils.isEmpty(list)) {
                    UserVehiclePo userVehiclePo = new UserVehiclePo();
                    userVehiclePo.setVin(vin);
                    Set userIds = new HashSet();
                    if(null!=userId) {
                        userIds.add(String.valueOf(userId));
                    }
                    userVehiclePo.setUserIds(userIds);
                    userVehiclePoList.add(userVehiclePo);
                }else if(null!=userId){
                    this.deleteUserId(String.valueOf(userId), vin);
                    UserVehiclePo userVehiclePo = list.get(0);
                    Set userIds = userVehiclePo.getUserIds();
                    userIds.add(String.valueOf(userId));
                    userVehiclePo.setUserIds(userIds);
                    this.updateUserId(userVehiclePo);
                }
            }else{
                //根据车辆vin删除原有的用户-车辆关系列表
                this.deleteUserVehicleByVin(vin);
                /**
                 * 增量更新
                 * 1、根据配置、标签列表，vin码查询出相应的用户-车辆集记录
                 * 2、如果用户-车辆集关系已经存在，则获取用户id，构造用户车辆实体列表，去重后批量更新即可
                 * 3、如果用户-车辆集关系不存在，则丢弃消息不做处理
                 */
                List<UserCarAssemblyPo> userCarAssemblyPoList = userCarAssemblyService.queryUserCarAssemblyList(configId,tagIdList);
                log.info("userCarAssemblyPoList :{}",userCarAssemblyPoList);
                Set userIds = new HashSet();
                for(UserCarAssemblyPo userCarAssemblyPo : userCarAssemblyPoList){
                    userIds.add(String.valueOf(userCarAssemblyPo.getUserId()));
                }
                UserVehiclePo userVehiclePo = new UserVehiclePo();
                userVehiclePo.setVin(vin);
                userVehiclePo.setUserIds(userIds);
                userVehiclePoList.add(userVehiclePo);
            }
            //插入用户车辆关系数据
            if (CollectionUtils.isNotEmpty(userVehiclePoList)) {
                this.insertUserVehicles(userVehiclePoList);
            }
        }
    }
}
