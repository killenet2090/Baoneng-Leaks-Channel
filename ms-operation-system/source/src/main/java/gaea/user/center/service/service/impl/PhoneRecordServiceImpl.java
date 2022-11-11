package gaea.user.center.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import gaea.user.center.service.mapper.PhoneRecordMapper;
import gaea.user.center.service.model.entity.PhoneRecordPo;
import gaea.user.center.service.service.IPhoneRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: PhoneRecordServiceImpl
 * @Description: 手机验证码发送记录实现类
 * @author: jiangchangyuan1
 * @date: 2020/11/3
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
public class PhoneRecordServiceImpl extends ServiceImpl<PhoneRecordMapper, PhoneRecordPo> implements IPhoneRecordService {
    private final PhoneRecordMapper phoneRecordMapper;

    public PhoneRecordServiceImpl(PhoneRecordMapper phoneRecordMapper) {
        this.phoneRecordMapper = phoneRecordMapper;
    }

    /**
     * 新增手机验证码发送记录
     * @param phoneRecordPo
     */
    @Override
    public void insertPhoneRecord(PhoneRecordPo phoneRecordPo) throws AdamException {
        phoneRecordPo.setCreateTime(LocalDateTime.now());
        phoneRecordPo.setUpdateTime(LocalDateTime.now());
        phoneRecordPo.setDelFlag(0);
        phoneRecordPo.setCreateBy("admin");
        phoneRecordPo.setUpdateBy("admin");
        phoneRecordMapper.insert(phoneRecordPo);
    }

    /**
     * 获取有效验证码发送记录
     * @param phone
     * @param type
     * @return
     */
    @Override
    public PhoneRecordPo getPhoneRecordEffective(String phone,Integer type) {
        PhoneRecordPo po = new PhoneRecordPo();
        po.setPhone(phone);
        po.setType(type);
        List<PhoneRecordPo> phoneRecordPoList = phoneRecordMapper.getphoneRecordEffective(po);
        PhoneRecordPo result = null;
        if(null != phoneRecordPoList && phoneRecordPoList.size() > 0){
            result = phoneRecordPoList.get(0);
        }
        return result;
    }
}
