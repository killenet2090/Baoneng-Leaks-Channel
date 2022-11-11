package gaea.user.center.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gaea.user.center.service.mapper.LoginLogMapper;
import gaea.user.center.service.model.entity.LoginLogPo;
import gaea.user.center.service.service.ILoginLogService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: LoginLogServiceImpl
 * @Description: 登录日志实现类
 * @author: jiangchangyuan1
 * @date: 2020/9/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLogPo> implements ILoginLogService {

    private final LoginLogMapper loginLogMapper;

    public LoginLogServiceImpl(LoginLogMapper loginLogMapper) {
        this.loginLogMapper = loginLogMapper;
    }

    /**
     * 保存用户登录记录
     * @param loginLogPo
     * @return
     */
    @Override
    public Integer saveLoginLog(LoginLogPo loginLogPo) {
        Integer status = loginLogMapper.insert(loginLogPo);
        return Integer.valueOf(loginLogPo.getId());
    }

    /**
     * 查询登录日志列表
     * @param po
     * @return
     */
    @Override
    public List<LoginLogPo> queryLoginLogList(LoginLogPo po) {
        List<LoginLogPo> loginLogPoList = loginLogMapper.queryLoginLogList(po);
        return loginLogPoList;
    }
}
