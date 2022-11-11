package gaea.user.center.service.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.entity.LoginLogPo;
import gaea.user.center.service.model.entity.OrganizationPo;

import java.util.List;

/**
 * @ClassName: LoginLogMapper
 * @Description: 登录用户dao层
 * @author: jiangchangyuan1
 * @date: 2020/9/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface LoginLogMapper extends AdamMapper<LoginLogPo> {
    /**
     * 查询登录记录列表
     * @return
     */
    List<LoginLogPo> queryLoginLogList(LoginLogPo po);
}
