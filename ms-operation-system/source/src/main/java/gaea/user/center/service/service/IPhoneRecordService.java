package gaea.user.center.service.service;

import gaea.user.center.service.model.entity.PhoneRecordPo;

/**
 * @ClassName: IPhoneRecordService
 * @Description: 手机验证码发送记录接口
 * @author: jiangchangyuan1
 * @date: 2020/11/3
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IPhoneRecordService {
    /**
     * 新增手机验证码发送记录
     * @param phoneRecordPo
     */
    void insertPhoneRecord(PhoneRecordPo phoneRecordPo);

    /**
     * 查询手机号是否存在有效验证码
     * @param phone
     * @return
     */
    PhoneRecordPo getPhoneRecordEffective(String phone,Integer type);
}
