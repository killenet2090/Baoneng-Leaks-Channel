package com.bnmotor.icv.tsp.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import com.bnmotor.icv.tsp.device.model.request.vehModel.ConfigImgDto;
import com.bnmotor.icv.tsp.device.model.response.vehLevel.*;
import com.bnmotor.icv.tsp.device.model.response.vehModel.*;
import com.bnmotor.icv.tsp.device.model.response.vehModel.VehModelImgVo;

import java.util.List;

/**
 * @ClassName: IVehModelService
 * @Description: 车型管理服务类
 * @author: zhangwei2
 * @date: 2020/8/11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IVehModelService {
    /**
     * 查询车辆全量型号或根据车系查询该车系下面所有型号
     *
     * @param seriesId 车系id
     * @return 车型全量数据
     */
    List<VehModelVo> listModels(Long seriesId);

    /**
     * 查询项目统计信息
     *
     * @param vehType  车辆动力类型
     * @param pageable 分页对象
     * @return 车型统计数据
     */
    IPage<VehModelStatisticsVo> listStatistics(Integer vehType, Pageable pageable);

    /**
     * 查询型号统计详情
     *
     * @return 车辆型号对应数据集合
     */
    List<VehModelConfigVo> listModelConfig(String modelCode, Integer vehType);

    /**
     * 查询车辆配置详细信息
     *
     * @return 车辆型号对应数据集合
     */
    VehConfigDetailVo queryVehConfigDetail(Long configId);

    /**
     * 查询型号统计详情
     *
     * @return 车辆型号对应数据集合
     */
    VehAllLevelVo queryLevel();

    /**
     * 查询车型配置图片列表
     *
     * @return 车型配置图片列表
     */
    VehModelPicVo listAllConfigPic(ConfigImgDto picDto);

    /**
     * 查询某个指定配置下所有图片
     */
    List<PicVo> listConfigPic(Long configId, Integer imgCategory, Integer imgType);

    /**
     * 查询所有车型年款
     */
    List<VehModelImgVo> listAllModel();

    /**
     * 查询车型下年款配置
     *
     * @return 列表
     */
    List<YearVo> listYearConfig(Long modelId);

    /**
     * 根据图片id
     */
    PicVo getRelationImg(Long id);
}
