package gaea.user.center.service.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import gaea.user.center.service.mapper.UserCarMapper;
import gaea.user.center.service.model.entity.UserCarAssemblyPo;
import gaea.user.center.service.model.entity.UserCarPo;
import gaea.user.center.service.service.IUserCarAssemblyService;
import gaea.user.center.service.service.IUserCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @ClassName: UserCarServiceImpl
 * @Description: 用户车辆接口
 * @author: jiangchangyuan1
 * @date: 2020/10/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class UserCarServiceImpl extends ServiceImpl<UserCarMapper, UserCarPo> implements IUserCarService {

    @Autowired
    IUserCarAssemblyService userCarAssemblyService;

    private final UserCarMapper userCarMapper;

    public UserCarServiceImpl(UserCarMapper userCarMapper) {
        this.userCarMapper = userCarMapper;
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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserCarBatchList(Long userId, Integer type, String vin, Long configId, JSONArray tagIdList) throws AdamException {
        List<UserCarPo> userCarPoList = new ArrayList();
        if(type == 1){
            //先清除原有数据
            userCarMapper.deleteUserCarBatchByUserIdAndVin(userId,vin);
            UserCarPo userCarPo = new UserCarPo();
            userCarPo.setUserId(userId);
            userCarPo.setVin(vin);
            userCarPo.setCreateBy("admin");
            userCarPo.setCreateTime(LocalDateTime.now());
            userCarPo.setUpdateBy("admin");
            userCarPo.setUpdateTime(LocalDateTime.now());
            userCarPo.setDelFlag(0);
            userCarPoList.add(userCarPo);
        }else{
            /**
             * 增量更新
             * 1、根据配置、标签列表，vin码查询出相应的用户-车辆集记录
             * 2、如果用户-车辆集关系已经存在，则获取用户id，构造用户车辆实体列表，去重后批量更新即可
             * 3、如果用户-车辆集关系不存在，则丢弃消息不做处理
             */
            List<UserCarAssemblyPo> userCarAssemblyPoList = userCarAssemblyService.queryUserCarAssemblyList(configId,tagIdList);
            log.info("userCarAssemblyPoList :{}",userCarAssemblyPoList);
            //根据车辆vin删除原有的用户-车辆关系列表
            userCarMapper.deleteUserCarBatchByVin(vin);
            for(UserCarAssemblyPo userCarAssemblyPo : userCarAssemblyPoList){
                //用户-车辆集关系存在,循环获取用户id，构造最新用户-车辆列表，插入数据即可
                UserCarPo userCarPo = new UserCarPo();
                userCarPo.setUserId(userCarAssemblyPo.getUserId());
                userCarPo.setVin(vin);
                userCarPo.setCreateBy("admin");
                userCarPo.setCreateTime(LocalDateTime.now());
                userCarPo.setUpdateBy("admin");
                userCarPo.setUpdateTime(LocalDateTime.now());
                userCarPo.setDelFlag(0);
                userCarPoList.add(userCarPo);
            }
        }
        log.info("userCarPoList :{}",userCarPoList);
        List<UserCarPo> distinctResult = userCarPoList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getUserId() + ";" + o.getVin()))), ArrayList::new));
        log.info("distinctResult :{}",distinctResult);
        if(null != distinctResult ||distinctResult.size() > 0 ){
            log.info("符合条件结果集不为空");
            userCarMapper.insertUserCarBatchList(distinctResult);
        }
    }

    @Override
    public void deleteBatchUserCarByUserId(Long userId) {
        userCarMapper.deleteUserCarBatchByUserId(userId);
    }
}
