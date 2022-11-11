package com.bnmotor.icv.tsp.ota.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePkgPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: FotaFirmwarePkgPo
* @Description: 升级包信息
包括升级包原始信息以及升级包发布信息 dao层
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface FotaFirmwarePkgMapper extends BaseMapper<FotaFirmwarePkgPo> {
    /**
     * 物理删除
     * @param ids
     */
    void deleteBatchIdsPhysical(List<Long> ids);
}
