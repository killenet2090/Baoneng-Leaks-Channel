package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.mapper.FotaVersionCheckVerifyMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanObjListPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaVersionCheckVerifyPo;
import com.bnmotor.icv.tsp.ota.model.query.FotaUpgradeLogQuery;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanObjListDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaVersionCheckVerifyDbService;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.bnmotor.icv.tsp.ota.util.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author xxc
 * @ClassName: FotaVersionCheckVerifyServiceImpl
 * @Description: OTA升级版本结果确认表服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-15
 */

@Service
@Slf4j
public class FotaVersionCheckVerifyDbServiceImpl extends ServiceImpl<FotaVersionCheckVerifyMapper, FotaVersionCheckVerifyPo> implements IFotaVersionCheckVerifyDbService {
    @Autowired
    private IFotaPlanObjListDbService fotaPlanObjListService;

    /**
     * 获取升级事务关联的升级车辆对象
     * 添加了一些验证信息
     * @param transId
     * @return
     */
    @Override
    public FotaPlanObjListPo findFotaPlanObjListPoWithTransId(Long transId){
        /*
            数据校验：
            1、升级事务是否存在
            2、升级对象是否存在
         */
        FotaVersionCheckVerifyPo existEntity = getById(transId);
        MyAssertUtil.notNull(existEntity, OTARespCodeEnum.FOTA_PLAN_OBJ_LIST_NOT_EXIST);

        FotaPlanObjListPo entity = fotaPlanObjListService.getById(existEntity.getOtaPlanObjectId());
        MyAssertUtil.notNull(entity, OTARespCodeEnum.FOTA_CHECK_VERIFY_NOT_EXIST);
        return entity;
    }

    @Override
    public FotaVersionCheckVerifyPo findCurFotaVersionCheckVerifyPo(Long planId, Long planObjectId){
        QueryWrapper<FotaVersionCheckVerifyPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_plan_id", planId);
        queryWrapper.eq("ota_plan_object_id", planObjectId);
        queryWrapper.orderByDesc("create_time");
        Page page = new Page(0, 1);
        IPage<FotaVersionCheckVerifyPo> fotaVersionCheckVerifyPos = getBaseMapper().selectPage(page, queryWrapper);
        if(Objects.nonNull(fotaVersionCheckVerifyPos) && MyCollectionUtil.isNotEmpty(fotaVersionCheckVerifyPos.getRecords())){
            log.info("fotaVersionCheckVerifyPos.getRecords().size={}", fotaVersionCheckVerifyPos.getRecords().size());
            return fotaVersionCheckVerifyPos.getRecords().get(0);
        }
        return null;
    }

    @Override
    public FotaVersionCheckVerifyPo findCurFotaVersionCheckVerifyPoByVin(String vin) {
        QueryWrapper<FotaVersionCheckVerifyPo> wrapper = new QueryWrapper<FotaVersionCheckVerifyPo>();
        wrapper.eq("vin", vin);
        wrapper.orderByDesc("create_time");
        List<FotaVersionCheckVerifyPo> fotaVersionCheckVerifyPos = getBaseMapper().selectList(wrapper);
        return MyCollectionUtil.isNotEmpty(fotaVersionCheckVerifyPos) ? fotaVersionCheckVerifyPos.get(0) : null;
    }

    @Override
    public IPage<FotaVersionCheckVerifyPo> queryPage4UpgradeLog(FotaUpgradeLogQuery fotaUpgradeLogQuery) {
        Page<FotaVersionCheckVerifyPo> page = new Page<>(fotaUpgradeLogQuery.getCurrent(), fotaUpgradeLogQuery.getPageSize());
        QueryWrapper<FotaVersionCheckVerifyPo> queryWrapper = new QueryWrapper<>();

        Long planId = fotaUpgradeLogQuery.getPlanId();
        String vin = fotaUpgradeLogQuery.getVin();
        Long transId = fotaUpgradeLogQuery.getTransId();
        if (Objects.nonNull(transId)) {
            queryWrapper.eq("id", transId);
        }

        if (Objects.nonNull(planId)) {
            queryWrapper.eq("ota_plan_id", planId);
        }

        if (StringUtils.isNotBlank(vin)) {
            queryWrapper.eq("vin", vin);
        }

        Date startTime = dateString2Date(fotaUpgradeLogQuery.getStartTime());
        Date endTime = dateString2Date(fotaUpgradeLogQuery.getEndTime());

        if (Objects.nonNull(startTime)) {
            queryWrapper.ge("create_time", startTime);
        }

        if (Objects.nonNull(endTime)) {
            queryWrapper.lt("create_time", endTime);
        }

        queryWrapper.orderByDesc("create_time");

        return getBaseMapper().selectPage(page, queryWrapper);
    }

    @Override
    public UpdateWrapper<FotaVersionCheckVerifyPo> build4InstalledVerifyBookedTime(Long id, Date installedVerifyBookedTime) {
        UpdateWrapper<FotaVersionCheckVerifyPo> updateWrapper = new UpdateWrapper<>();
        //确认安装
        updateWrapper.eq("id", id);
        updateWrapper.set("installed_verify_booked_time", installedVerifyBookedTime);
        return updateWrapper;
    }

    public Date dateString2Date(String datetime) {
    	if (StringUtils.isBlank(datetime) ) {
    		return null;
    	}
    	try {
			return MyDateUtil.parseDateTime(datetime);
		} catch (ParseException e) {
			log.info("转换Date失败|{}", datetime);
		}
    	return null;
    }
}
