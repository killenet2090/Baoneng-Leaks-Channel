package com.bnmotor.icv.tsp.ota.mapper;

import com.bnmotor.icv.tsp.ota.model.entity.FotaDownloadProcessPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: FotaDownloadProcessPo
* @Description: OTA升级下载进度
记录下载的当前进度信息。一个升级对象的一个软件的升级记录对应一条下载进度信息
                                             -&#& dao层
 * @author xxc
 * @since 2020-07-23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface FotaDownloadProcessMapper extends BaseMapper<FotaDownloadProcessPo> {

}
