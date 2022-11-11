package gaea.user.center.service.service;

import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import gaea.user.center.service.model.response.PrivilegeTypeVo;

import java.util.List;

public interface IPrivilegeTypeService {
    /**
     * 分页查询资源类型列表
     * @param pageable
     * @param privilegeTypeVo
     * @return
     */
    Page<PrivilegeTypeVo> queryPrivilegeTypePage(Pageable pageable, PrivilegeTypeVo privilegeTypeVo);

    /**
     * 插入资源类型信息
     * @param privilegeTypeVo
     * @return
     */
    Long insert(PrivilegeTypeVo privilegeTypeVo);

    /**
     * 更新资源类型信息
     * @param id
     * @param privilegeTypeVo
     * @return
     */
    Long update(String id, PrivilegeTypeVo privilegeTypeVo);

    /**
     * 删除资源类型
     * @param id
     * @return
     */
    Long deletePrivilegeType(String id);

    /**
     * 查询资源类型列表
     * @param privilegeTypeVo
     * @return
     */
    List<PrivilegeTypeVo> queryList(PrivilegeTypeVo privilegeTypeVo);
}
