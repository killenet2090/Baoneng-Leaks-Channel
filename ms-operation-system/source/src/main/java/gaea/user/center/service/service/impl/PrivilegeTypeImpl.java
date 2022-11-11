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
import gaea.user.center.service.mapper.PrivilegeTypeMapper;
import gaea.user.center.service.mapstuct.PrivilegeTypeVoMapper;
import gaea.user.center.service.model.entity.PrivilegeTypePo;
import gaea.user.center.service.model.response.PrivilegeTypeVo;
import gaea.user.center.service.service.IPrivilegeTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: PrivilegeTypeImpl
 * @Description: 项目类型接口实现类
 * @author: jiangchangyuan1
 * @date: 2020/8/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
public class PrivilegeTypeImpl extends ServiceImpl<PrivilegeTypeMapper, PrivilegeTypePo> implements IPrivilegeTypeService {

    private final PrivilegeTypeMapper privilegeTypeMapper;

    public PrivilegeTypeImpl(PrivilegeTypeMapper privilegeTypeMapper) {
        this.privilegeTypeMapper = privilegeTypeMapper;
    }

    /**
     * 分页查询资源类型列表
     * @param pageable
     * @param privilegeTypeVo
     * @return
     * @throws AdamException
     */
    @Override
    public Page<PrivilegeTypeVo> queryPrivilegeTypePage(Pageable pageable, PrivilegeTypeVo privilegeTypeVo) throws AdamException {
        IPage iPage = PageUtil.map(pageable);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<PrivilegeTypePo> privilegeTypePoPage =
                privilegeTypeMapper.queryPrivilegeTypePage(iPage,privilegeTypeVo);
        Page<PrivilegeTypeVo> privilegeTypeVoPage = PrivilegeTypeVoMapper.INSTANCE.map(privilegeTypePoPage);
        return privilegeTypeVoPage;
    }

    /**
     * 插入资源类型信息
     * @param privilegeTypeVo
     * @return
     * @throws AdamException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(PrivilegeTypeVo privilegeTypeVo) throws AdamException {
        PrivilegeTypePo privilegeTypePo = PrivilegeTypeVoMapper.INSTANCE.revertMap(privilegeTypeVo);
        if(null!= CurrentUserUtils.getCurrentUser()){
            privilegeTypePo.setUpdateBy(CurrentUserUtils.getCurrentUser().getUserName());
            privilegeTypePo.setCreateBy(CurrentUserUtils.getCurrentUser().getUserName());
        }else{
            throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
        }
        privilegeTypePo.setCreateTime(LocalDateTime.now());
        privilegeTypePo.setUpdateTime(LocalDateTime.now());
        privilegeTypePo.setDelFlag(0);
        int status = privilegeTypeMapper.insert(privilegeTypePo);
        Long result = privilegeTypePo.getId();
        return result;
    }

    /**
     * 更新资源类型信息
     * @param id
     * @param privilegeTypeVo
     * @return
     * @throws AdamException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long update(String id, PrivilegeTypeVo privilegeTypeVo) throws AdamException {
        PrivilegeTypePo privilegeTypePo = privilegeTypeMapper.selectById(id);
        if(null == privilegeTypePo){
            throw new AdamException(BusinessStatusEnum.DATA_NOT_FOUND.getCode(),BusinessStatusEnum.DATA_NOT_FOUND.getDescription());
        }
        privilegeTypePo = PrivilegeTypeVoMapper.INSTANCE.revertMap(privilegeTypeVo);
        privilegeTypePo.setUpdateTime(LocalDateTime.now());
        if(null!= CurrentUserUtils.getCurrentUser()){
            privilegeTypePo.setUpdateBy(CurrentUserUtils.getCurrentUser().getUserName());
        }else{
            throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
        }
        privilegeTypePo.setId(Long.valueOf(id));
        int status = privilegeTypeMapper.updateById(privilegeTypePo);
        Long result = privilegeTypePo.getId();
        return result;
    }

    /**
     * 删除资源类型信息
     * @param id
     * @return
     * @throws AdamException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long deletePrivilegeType(String id) throws AdamException {
        PrivilegeTypePo entity = new PrivilegeTypePo();
        entity.setId(Long.valueOf(id));
        if(null!= CurrentUserUtils.getCurrentUser()){
            entity.setUpdateBy(CurrentUserUtils.getCurrentUser().getUserName());
        }else{
            throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
        }
        int status = privilegeTypeMapper.deleteByIdWithFill(entity);
        Long result = entity.getId();
        return result;
    }

    /**
     * 查询资源类型列表
     * @param privilegeTypeVo
     * @return
     */
    @Override
    public List<PrivilegeTypeVo> queryList(PrivilegeTypeVo privilegeTypeVo) {
        PrivilegeTypePo privilegeTypePo = PrivilegeTypeVoMapper.INSTANCE.revertMap(privilegeTypeVo);
        List<PrivilegeTypePo> privilegeTypePoList = privilegeTypeMapper.queryPrivilegeTypeList(privilegeTypePo);
        List<PrivilegeTypeVo> privilegeTypeVoList = PrivilegeTypeVoMapper.INSTANCE.map(privilegeTypePoList);
        return privilegeTypeVoList;
    }
}
