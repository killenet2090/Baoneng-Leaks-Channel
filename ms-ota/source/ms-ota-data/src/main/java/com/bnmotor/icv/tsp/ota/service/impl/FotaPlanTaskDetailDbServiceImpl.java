package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.mapper.FotaPlanTaskDetailMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanTaskDetailPo;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanTaskDetailDbService;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xxc
 * @ClassName: FotaPlanTaskDetailPo
 * @Description: OTA升级任务明细定义的是一辆车的一个零部件的一款软件所涉及的一个或多个软件的升级
 * 在创建升级计划时创建升级 服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-08-08
 */
@Service
@Slf4j
public class FotaPlanTaskDetailDbServiceImpl extends ServiceImpl<FotaPlanTaskDetailMapper, FotaPlanTaskDetailPo> implements IFotaPlanTaskDetailDbService {
    @Override
    public void deleteByOtaPlanObjId(Long otaPlanId, Long otaPlanObjId) {
        getBaseMapper().deleteByOtaPlanObjIdPhysical(otaPlanId, otaPlanObjId);
    }

    @Override
    public List<FotaPlanTaskDetailPo> listByOtaPlanObjIdWithPlanFirmwareId(Long otaPlanObjectId, List<Long> planFirmwareIds) {
        MyAssertUtil.notEmpty(planFirmwareIds, OTARespCodeEnum.DATA_NOT_FOUND);
        QueryWrapper<FotaPlanTaskDetailPo> queryWrapper = new QueryWrapper<FotaPlanTaskDetailPo>();
        queryWrapper.eq("ota_plan_obj_id", otaPlanObjectId);
        queryWrapper.in("ota_plan_firmware_id", planFirmwareIds);
        List<FotaPlanTaskDetailPo> fotaPlanTaskDetailPos = list(queryWrapper);
        return fotaPlanTaskDetailPos;
    }

    @Override
    public List<FotaPlanTaskDetailPo> listByOtaPlanObjId(Long otaPlanObjectId) {
        QueryWrapper<FotaPlanTaskDetailPo> queryWrapper = new QueryWrapper<FotaPlanTaskDetailPo>();
        queryWrapper.eq("ota_plan_obj_id", otaPlanObjectId);
        List<FotaPlanTaskDetailPo> fotaPlanTaskDetailPos = list(queryWrapper);
        return fotaPlanTaskDetailPos;
    }
}