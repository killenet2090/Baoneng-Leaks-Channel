package gaea.user.center.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import gaea.user.center.service.common.enums.BusinessStatusEnum;
import gaea.user.center.service.mapper.UserPrivilegeMapper;
import gaea.user.center.service.model.dto.RolePrivilege;
import gaea.user.center.service.model.entity.UserPrivilegePo;
import gaea.user.center.service.service.IUserPrivilegeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: UserPrivilegeServiceImpl
 * @Description: 用户权限(资源)实现类
 * @author: jiangchangyuan1
 * @date: 2020/8/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
public class UserPrivilegeServiceImpl extends ServiceImpl<UserPrivilegeMapper, UserPrivilegePo> implements IUserPrivilegeService {

    private final UserPrivilegeMapper userPrivilegeMapper;

    public UserPrivilegeServiceImpl(UserPrivilegeMapper userPrivilegeMapper) {
        this.userPrivilegeMapper = userPrivilegeMapper;
    }

    /**
     * 批量保存用户权限信息
     * @param userId
     * @param privilegePoList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveBatchUserPrivilege(Long userId, List<RolePrivilege> privilegePoList) throws AdamException {
        //先批量删除原有数据
        userPrivilegeMapper.deleteBatchByUserId(userId);
        List<UserPrivilegePo> userPrivilegePoList = new ArrayList<>();
        for(RolePrivilege rolePrivilege : privilegePoList) {
            UserPrivilegePo userPrivilegePo = new UserPrivilegePo();
            userPrivilegePo.setPrivilegeId(rolePrivilege.getPrivilegeId().longValue());
            userPrivilegePo.setUserId(userId);
            userPrivilegePoList.add(userPrivilegePo);
        }
        Integer status = userPrivilegeMapper.saveAllInBatch(userPrivilegePoList);
        if(status < privilegePoList.size()){
            throw new AdamException(BusinessStatusEnum.DATA_NOT_INSERT.getCode(),BusinessStatusEnum.DATA_NOT_INSERT.getDescription());
        }
        return status;
    }

    /**
     * 根据权限id查询权限分配的用户列表
     * @param privilegeId 权限id
     * @return
     */
    @Override
    public List<UserPrivilegePo> queryUserPrivilegeByPrivilegeId(Long privilegeId) {
        List<UserPrivilegePo> userPrivilegePoList = userPrivilegeMapper.queryUserPrivilegeByPrivilegeId(privilegeId);
        return userPrivilegePoList;
    }
}
