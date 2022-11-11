package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.model.entity.FotaDownloadProcessPo;
import com.bnmotor.icv.tsp.ota.mapper.FotaDownloadProcessMapper;
import com.bnmotor.icv.tsp.ota.service.IFotaDownloadProcessDbService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @ClassName: FotaDownloadProcessPo
 * @Description: OTA升级下载进度
记录下载的当前进度信息。一个升级对象的一个软件的升级记录对应一条下载进度信息
                                             -&#& 服务实现类
 * @author xxc
 * @since 2020-07-23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
public class FotaDownloadProcessDbServiceImpl extends ServiceImpl<FotaDownloadProcessMapper, FotaDownloadProcessPo> implements IFotaDownloadProcessDbService {

}
