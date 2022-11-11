package com.bnmotor.icv.tsp.ota.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareListPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: FotaFirmwareListPo
* @Description: OTA升级固件清单
 dao层
 * @author xxc
 * @since 2020-07-06
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface FotaFirmwareListMapper extends AdamMapper<FotaFirmwareListPo> {
}
