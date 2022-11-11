package com.bnmotor.icv.tsp.device.mapper;

import com.bnmotor.icv.tsp.device.model.entity.VehicleConfigImgPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.device.model.response.vehModel.PicVo;
import com.bnmotor.icv.tsp.device.model.response.vehModel.VehConfigPicVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: VehicleConfigImgPo
 * @Description: dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-11-16
 */

@Mapper
public interface VehicleConfigImgMapper extends BaseMapper<VehicleConfigImgPo> {
    /**
     * 查询某个配置下所有图片
     *
     * @param configId    配置id
     * @param imgCategory 分类id
     * @param imgType     图片类型
     * @return 图片列表
     */
    List<PicVo> listConfigPic(@Param("configId") Long configId,
                              @Param("imgCategory") Integer imgCategory,
                              @Param("imgType") Integer imgType,
                              @Param("size") Integer size);

    /**
     * 统计单个配置的图片张数
     *
     * @param configId    配置id
     * @param imgCategory 图片分类
     * @param imgType     图片类型
     * @return 图片张数
     */
    int countConfigPic(@Param("configId") Long configId,
                       @Param("imgCategory") Integer imgCategory,
                       @Param("imgType") Integer imgType);

    /**
     * 统计每个配置对应的图片数目
     *
     * @param configIds   配置列表
     * @param imgCategory 图片分类
     * @param imgType     图片类型
     * @return 统计列表
     */
    List<VehConfigPicVo> countAllConfigPic(@Param("configIds") List<Long> configIds,
                                           @Param("imgCategory") Integer imgCategory,
                                           @Param("imgType") Integer imgType);

    /**
     * 统计共有多少配置
     *
     * @param configIds    配置id
     * @param categoryType 图片类型
     * @return 总计条目
     */
    int sumConfigs(@Param("configIds") Collection configIds, @Param("categoryType") Integer categoryType);
}
