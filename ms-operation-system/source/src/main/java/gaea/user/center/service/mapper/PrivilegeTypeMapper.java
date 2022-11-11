package gaea.user.center.service.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.entity.PrivilegeTypePo;
import gaea.user.center.service.model.response.PrivilegeTypeVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: PrivilegeTypeMapper
 * 资源类型Mapper接口类
 * @author: jiangchangyuan1
 * @date: 2020/8/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 * 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public interface PrivilegeTypeMapper extends AdamMapper<PrivilegeTypePo> {
    /**
     * 分页查询资源类型列表
     * @param iPage
     * @param query
     * @return
     */
    Page<PrivilegeTypePo> queryPrivilegeTypePage(IPage iPage, @Param("query") PrivilegeTypeVo query);

    /**
     * 查询资源类型列表
     * @param query
     * @return
     */
    List<PrivilegeTypePo> queryPrivilegeTypeList(@Param("query")PrivilegeTypePo query);
}
