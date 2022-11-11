package gaea.user.center.service.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import com.bnmotor.icv.adam.data.mysql.utils.PageUtil;
import gaea.user.center.service.common.CurrentUser;
import gaea.user.center.service.common.CurrentUserUtils;
import gaea.user.center.service.common.enums.BusinessStatusEnum;
import gaea.user.center.service.mapper.ProjectMapper;
import gaea.user.center.service.mapstuct.ProjectVoMapper;
import gaea.user.center.service.model.entity.ProjectPo;
import gaea.user.center.service.model.entity.UserProjectPo;
import gaea.user.center.service.model.request.ProjectQuery;
import gaea.user.center.service.model.response.ProjectVo;
import gaea.user.center.service.service.IProjectService;
import gaea.user.center.service.service.IUserProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: ProjectServiceImpl
 * @Description: 项目信息接口实现类
 * @author: jiangchangyuan1
 * @date: 2020/8/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectPo> implements IProjectService {

    private final ProjectMapper projectMapper;

    @Autowired
    private IUserProjectService userProjectService;

    public ProjectServiceImpl(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    /**
     * 分页查询项目列表
     * @param query 查询条件,继承分页信息
     * @return
     */
    @Override
    public Page<ProjectVo> queryProjectPage(Pageable pageable, ProjectQuery query) throws  AdamException{
        IPage iPage = PageUtil.map(pageable);
        if(null!= CurrentUserUtils.getCurrentUser() && CurrentUserUtils.getCurrentUser().getUserId().equals(1L)){
            query.setUserId(CurrentUserUtils.getCurrentUser().getUserId());
        }
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ProjectPo> projectPoPage = projectMapper.queryProjectPage(iPage,query);
        Page<ProjectVo> projectVoPage = ProjectVoMapper.INSTANCE.map(projectPoPage);
        return projectVoPage;
    }

    /**
     * 新增项目信息
     * @param projectVo
     * @return
     * @throws AdamException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(ProjectVo projectVo) throws  AdamException{
        ProjectPo projectPo = ProjectVoMapper.INSTANCE.revertMap(projectVo);
        if(null!= CurrentUserUtils.getCurrentUser()){
            projectPo.setCreateBy(CurrentUserUtils.getCurrentUser().getUserName());
            projectPo.setUpdateBy(CurrentUserUtils.getCurrentUser().getUserName());
        }else{
            throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
        }
        projectPo.setCreateTime(LocalDateTime.now());
        projectPo.setUpdateTime(LocalDateTime.now());
        projectPo.setDelFlag(0);
        //校验code是否重复
        ProjectPo projectPoExist = projectMapper.queryProjectByCode(projectPo.getCode());
        if(null != projectPoExist){
            throw new AdamException(BusinessStatusEnum.PROJECT_CODE_REPEAT.getCode(),BusinessStatusEnum.PROJECT_CODE_REPEAT.getDescription());
        }
        projectMapper.insert(projectPo);
        Long projectId = projectPo.getId();
        return projectId;
    }

    /**
     * 更新项目信息
     * @param projectVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectPo updateProject(String id,ProjectVo projectVo) throws AdamException{
        //判断code是否存在
        ProjectPo projectPoExist = projectMapper.queryProjectByCode(projectVo.getCode());
        if(null != projectPoExist && !projectPoExist.getId().equals(Long.parseLong(id))){
            throw new AdamException(BusinessStatusEnum.PROJECT_CODE_REPEAT.getCode(),BusinessStatusEnum.PROJECT_CODE_REPEAT.getDescription());
        }
        //判断项目信息是否存在
        ProjectPo projectPo = projectMapper.queryProjectById(Long.valueOf(id));
        if(null == projectPo){
            throw new AdamException(BusinessStatusEnum.DATA_NOT_FOUND.getCode(),BusinessStatusEnum.DATA_NOT_FOUND.getDescription());
        }
        projectPo = ProjectVoMapper.INSTANCE.revertMap(projectVo);
        if(null!= CurrentUserUtils.getCurrentUser()){
            projectPo.setUpdateBy(CurrentUserUtils.getCurrentUser().getUserName());
        }else{
            throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
        }
        projectPo.setId(Long.valueOf(id));
        projectPo.setUpdateTime(LocalDateTime.now());
        int status = projectMapper.updateById(projectPo);
        return projectPo;
    }

    /**
     * 删除项目信息
     * @param id
     * @return
     * @throws AdamException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long deleteProject(String id) throws AdamException {
        //判断项目是否已被分配，如果被分配，则不允许删除
        List<UserProjectPo> userProjectPoList = userProjectService.queryUserProjectListByProjectId(Long.valueOf(id));
        if(null != userProjectPoList && userProjectPoList.size() > 0){
            throw new AdamException(BusinessStatusEnum.DATA_IS_ALLOCATED.getCode(),BusinessStatusEnum.DATA_IS_ALLOCATED.getDescription());
        }
        //未被分配，可删除
        ProjectPo projectPo = new ProjectPo();
        projectPo.setId(Long.valueOf(id));
        if(null!= CurrentUserUtils.getCurrentUser()){
            projectPo.setUpdateBy(CurrentUserUtils.getCurrentUser().getUserName());
        }else{
            throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
        }
        int status = projectMapper.deleteByIdWithFill(projectPo);
        Long result = projectPo.getId();
        return result;
    }

    /**
     * 查询项目信息列表
     * @param projectVo
     * @return
     * @throws AdamException
     */
    @Override
    public List<ProjectVo> queryList(ProjectVo projectVo) throws AdamException {
        if(null!= CurrentUserUtils.getCurrentUser() && CurrentUserUtils.getCurrentUser().getUserId().equals(1L)){
            projectVo.setUserId(CurrentUserUtils.getCurrentUser().getUserId());
        }else{
            throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
        }
        List<ProjectPo> projectPoList = projectMapper.queryProjectList(projectVo);
        List<ProjectVo> projectVoList = ProjectVoMapper.INSTANCE.map(projectPoList);
        return projectVoList;
    }

    /**
     * 根据项目id查询项目信息
     * @param id
     * @return
     * @throws AdamException
     */
    @Override
    public ProjectVo queryById(Long id) throws AdamException{
        ProjectPo projectPo = projectMapper.selectById(id);
        if(null == projectPo){
            throw new AdamException(BusinessStatusEnum.DATA_NOT_FOUND.getCode(),BusinessStatusEnum.DATA_NOT_FOUND.getDescription());
        }
        ProjectVo projectVo = ProjectVoMapper.INSTANCE.map(projectPo);
        return projectVo;
    }

    /**
     * 根据用户id查询项目列表
     * @param userId
     * @return
     * @throws AdamException
     */
    @Override
    public List<ProjectVo> queryListByUserId(Long userId) throws AdamException {
        List<ProjectPo> projectPoList = projectMapper.queryListByUserId(userId);
        List<ProjectVo> projectVoList = ProjectVoMapper.INSTANCE.map(projectPoList);
        return projectVoList;
    }

    /**
     * 批量插入项目下拥有者信息
     * @param projectVo
     * @return
     * @throws AdamException
     */
    @Override
    public Integer saveBatchProjectUsers(ProjectVo projectVo) throws AdamException {
        int status = userProjectService.saveBatchProjectUsers(projectVo.getId(),projectVo.getUserIds());
        return status;
    }
}
