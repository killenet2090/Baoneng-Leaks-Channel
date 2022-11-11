package com.bnmotor.icv.tsp.ota.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareVersionPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: FotaFirmwareVersionDo
* @Description: 软件版本,即软件坂本树上的一个节点
定义软件所生成的各个不同的版本 dao层
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface FotaFirmwareVersionMapper extends BaseMapper<FotaFirmwareVersionPo> {
    /**
     * 物理删除
     * @param versionIds
     */
    void deleteBatchIdsPhysical(List<Long> versionIds);
}
