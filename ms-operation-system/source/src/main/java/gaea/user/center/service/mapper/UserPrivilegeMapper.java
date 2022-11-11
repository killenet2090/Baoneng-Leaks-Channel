package gaea.user.center.service.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.entity.UserPrivilegePo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: UserPrivilegeMapper
 * @Description: 用户权限Mapper
 * @author: jiangchangyuan1
 * @date: 2020/8/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public interface UserPrivilegeMapper extends AdamMapper<UserPrivilegePo> {
    /**
     * 根据用户id批量删除权限信息
     * @param userId
     */
    void deleteBatchByUserId(Long userId);

    /**
     * 根据权限id查询权限列表
     * @param id
     * @return
     */
    List<UserPrivilegePo> queryUserPrivilegeByPrivilegeId(Long id);
}
