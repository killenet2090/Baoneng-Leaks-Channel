package gaea.user.center.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import gaea.user.center.service.common.enums.BusinessStatusEnum;
import gaea.user.center.service.mapper.UserProjectMapper;
import gaea.user.center.service.model.entity.UserProjectPo;
import gaea.user.center.service.service.IUserProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: UserProjectServiceImpl
 * @Description: 用户项目中间表业务层处理类
 * @author: jiangchangyuan1
 * @date: 2020/8/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
public class UserProjectServiceImpl extends ServiceImpl<UserProjectMapper, UserProjectPo> implements IUserProjectService {

    private final UserProjectMapper userProjectMapper;

    public UserProjectServiceImpl(UserProjectMapper userProjectMapper) {
        this.userProjectMapper = userProjectMapper;
    }

    /**
     * 批量保存某用户下项目列表
     * @param userId
     * @param projectIds
     * @return
     * @throws AdamException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveBatchUserProjects(Long userId, String projectIds) throws AdamException {
        //保存最新数据之前先清除原有数据，确保入库数据为最新数据
        userProjectMapper.deleteBatchByUserId(userId);
        //插入最新数据
        List<UserProjectPo> userProjectPoList = new ArrayList<>();
        //切割项目ids
        List<String> projectIdList = Arrays.asList(projectIds.split(","));
        for(String proId : projectIdList){
            //提前构造批量插入实体列表
            UserProjectPo userProjectPo = new UserProjectPo();
            userProjectPo.setProjectId(Long.valueOf(proId));
            userProjectPo.setUserId(userId);
            userProjectPoList.add(userProjectPo);
        }
        int status = userProjectMapper.saveAllInBatch(userProjectPoList);
        if(status < projectIdList.size()){
            throw new AdamException(BusinessStatusEnum.DATA_NOT_INSERT.getCode(),BusinessStatusEnum.DATA_NOT_INSERT.getDescription());
        }
        return status;
    }

    /**
     * 批量保存项目拥有者列表信息
     * @param projectId
     * @param userIds
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveBatchProjectUsers(String projectId, String userIds) throws AdamException{
        //新增之前先清除原有数据
        userProjectMapper.deleteBatchByProjectId(projectId);
        //插入最新数据
        List<String> userIdList = Arrays.asList(userIds.split(","));
        List<UserProjectPo> userProjectPoList = new ArrayList<>();
        for(String userId:userIdList){
            //提前构造好实体数据
            UserProjectPo userProjectPo = new UserProjectPo();
            userProjectPo.setUserId(Long.valueOf(userId));
            userProjectPo.setProjectId(Long.valueOf(projectId));
            userProjectPoList.add(userProjectPo);
        }
        int status = userProjectMapper.saveAllInBatch(userProjectPoList);
        if(status < userIdList.size()){
            throw new AdamException(BusinessStatusEnum.DATA_NOT_INSERT.getCode(),BusinessStatusEnum.DATA_NOT_INSERT.getDescription());
        }
        return status;
    }

    /**
     * 根据项目id查询项目的分配列表
     * @param id 项目id
     * @return
     */
    @Override
    public List<UserProjectPo> queryUserProjectListByProjectId(Long id) throws AdamException{
        List<UserProjectPo> userProjectPoList = userProjectMapper.queryUserProjectListByProjectId(id);
        return userProjectPoList;
    }
}
