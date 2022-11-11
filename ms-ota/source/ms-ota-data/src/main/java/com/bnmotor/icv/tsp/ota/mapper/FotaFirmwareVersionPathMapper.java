package com.bnmotor.icv.tsp.ota.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareVersionPathPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: FotaFirmwareVersionPathPo
* @Description: 软件版本升级路径记录全量包、补丁包和差分包的升级条件信息记录从适应的版本到当前版本的升级路径 dao层
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface FotaFirmwareVersionPathMapper extends BaseMapper<FotaFirmwareVersionPathPo> {
    /**
     * 物理删除
     * @param ids
     */
    void deleteBatchIdsPhysical(List<Long> ids);
}
