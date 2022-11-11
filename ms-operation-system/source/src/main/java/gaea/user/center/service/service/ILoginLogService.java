package gaea.user.center.service.service;

import gaea.user.center.service.model.entity.LoginLogPo;

import java.util.List;

/**
 * @ClassName: ILoginLogService
 * @Description: 登录日志接口
 * @author: jiangchangyuan1
 * @date: 2020/9/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface ILoginLogService {
    /**
     * 保存登录日志
     * @param loginLogPo
     * @return
     */
    Integer saveLoginLog(LoginLogPo loginLogPo);

    /**
     * 查询登录日志列表
     * @return
     */
    List<LoginLogPo> queryLoginLogList(LoginLogPo po);

}
