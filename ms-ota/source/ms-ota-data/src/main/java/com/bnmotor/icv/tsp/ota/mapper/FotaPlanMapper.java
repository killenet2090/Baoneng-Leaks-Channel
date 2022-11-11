package com.bnmotor.icv.tsp.ota.mapper;

import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: FotaPlanPo
* @Description: OTA升级计划表 dao层
 * @author xxc
 * @since 2020-07-07
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface FotaPlanMapper extends BaseMapper<FotaPlanPo> {

    /**
     * 物理删除数据
     * @param id
     * @return
     */
    int deleteByIdPhysical(Long id);
}
