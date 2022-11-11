package gaea.user.center.service.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import gaea.user.center.service.model.entity.PhoneRecordPo;

import java.util.List;

/**
 * @ClassName: PhoneRecordMapper
 * @Description: 手机验证码发送Dao
 * @author: jiangchangyuan1
 * @date: 2020/11/3
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface PhoneRecordMapper extends AdamMapper<PhoneRecordPo> {
    List<PhoneRecordPo> getphoneRecordEffective(PhoneRecordPo po);
}
