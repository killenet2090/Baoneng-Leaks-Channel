package gaea.user.center.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gaea.user.center.service.common.enums.EmailEnums;
import gaea.user.center.service.common.utils.EmailUtils;
import gaea.user.center.service.mapper.EmailRecordMapper;
import gaea.user.center.service.model.dto.User;
import gaea.user.center.service.model.entity.EmailRecordPo;
import gaea.user.center.service.service.IEmailRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: EmailRecordServiceImpl
 * @Description: 邮件发送记录实现类
 * @author: jiangchangyuan1
 * @date: 2020/11/2
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
public class EmailRecordServiceImpl extends ServiceImpl<EmailRecordMapper,EmailRecordPo> implements IEmailRecordService {
    private final EmailRecordMapper emailRecordMapper;

    public EmailRecordServiceImpl(EmailRecordMapper emailRecordMapper) {
        this.emailRecordMapper = emailRecordMapper;
    }

    @Autowired
    EmailUtils emailUtils;

    /**
     * 保存邮件发送记录
     * @param emailRecordPo
     */
    @Override
    public void insertEmailRecord(EmailRecordPo emailRecordPo) {
        emailRecordPo.setCreateTime(LocalDateTime.now());
        emailRecordPo.setUpdateTime(LocalDateTime.now());
        emailRecordPo.setDelFlag(0);
        emailRecordPo.setCreateBy("admin");
        emailRecordPo.setUpdateBy("admin");
        emailRecordMapper.insert(emailRecordPo);
    }

    /**
     * 定时任务-重试发送失败的邮件
     */
    @Override
    public void retryEmailSendTask() {
        EmailRecordPo po = new EmailRecordPo();
        po.setIsSend(0);
        List<EmailRecordPo> emailRecordPoList = emailRecordMapper.getEmailRecordList(po);
        for(EmailRecordPo emailRecordPo : emailRecordPoList){
            Boolean result= false;
            if(emailRecordPo.getType() == EmailEnums.Status.REGISTER.getValue()){
                result = emailUtils.sendEmail(emailRecordPo.getDisplayName(),emailRecordPo.getEmail(),emailRecordPo.getSubject(),emailRecordPo.getKeyWord());
            }else{
                result = emailUtils.sendActivitionEmail(emailRecordPo.getEmail(),emailRecordPo.getSubject(),emailRecordPo.getKeyWord());
            }
            emailRecordPo.setIsSend(result ? 1 : 0);
            emailRecordPo.setUpdateTime(LocalDateTime.now());
            emailRecordPo.setUpdateBy("admin");
            emailRecordMapper.updateById(emailRecordPo);
        }

    }
}
