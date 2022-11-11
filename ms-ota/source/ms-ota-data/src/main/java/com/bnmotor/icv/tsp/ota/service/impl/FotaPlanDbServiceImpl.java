package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.common.enums.PublishStateEnum;
import com.bnmotor.icv.tsp.ota.mapper.FotaPlanMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanFirmwareListPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.model.query.FotaPlanDelayQuery;
import com.bnmotor.icv.tsp.ota.model.query.FotaPlanQuery;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanFirmwareListDbService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.bnmotor.icv.tsp.ota.util.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * @author xxc
 * @ClassName: FotaPlanPo
 * @Description: OTA升级计划表 服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-07
 */

@Slf4j
@Service
public class FotaPlanDbServiceImpl extends ServiceImpl<FotaPlanMapper, FotaPlanPo> implements IFotaPlanDbService {
    
	@Autowired
    private IFotaPlanFirmwareListDbService fotaPlanFirmwareListService;

    @Override
    public IPage<FotaPlanPo> queryPage(FotaPlanQuery query) {
        Page page = new Page(query.getCurrent(), query.getPageSize());
        QueryWrapper<FotaPlanPo> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(query.getName())) {
            queryWrapper.like("plan_name", query.getName());
        }
        if (Objects.nonNull(query.getIsEnable())) {
            queryWrapper.eq("is_enable", query.getIsEnable());
        }
        if (Objects.nonNull(query.getTaskStatus())) {
            //历史原因，使用plan_status
            queryWrapper.eq("plan_status", query.getTaskStatus());
        }
        
        if (Objects.nonNull(query.getRebuildFlag())) {
            queryWrapper.eq("rebuild_flag", query.getRebuildFlag());
        }
        queryWrapper.orderByDesc("create_time");

        IPage<FotaPlanPo> fotaPlanPos = getBaseMapper().selectPage(page, queryWrapper);
        return fotaPlanPos;
    }

    @Override
    public boolean existPlanWithFirmware(Long firmwareId, Long firmwareVersionId) {
        if (Objects.isNull(firmwareId) || Objects.isNull(firmwareVersionId)) {
            return false;
        }
        QueryWrapper<FotaPlanFirmwareListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("firmware_id", firmwareId);
        queryWrapper.eq("firmware_version_id", firmwareVersionId);
        List<FotaPlanFirmwareListPo> fotaPlanFirmwareListPos = fotaPlanFirmwareListService.getBaseMapper().selectList(queryWrapper);
        return MyCollectionUtil.isNotEmpty(fotaPlanFirmwareListPos) && Objects.nonNull(fotaPlanFirmwareListPos.get(0));
    }

    @Override
    public IPage<FotaPlanPo> getExpiredFotaPlan(int offset, int pageSize, long id) {
        QueryWrapper<FotaPlanPo> queryWrapper = new QueryWrapper<>();
        if(id>0) {
            queryWrapper.ge("id", id);
        }
        //TODO 此处需要添加索引
        queryWrapper.lt("plan_end_time", MyDateUtil.formateDateTime(new Date()));
        queryWrapper.ne("publish_state", PublishStateEnum.INVALID.getState());

        //ID升序排列
        queryWrapper.orderByAsc("id");

        Page page = new Page(offset, pageSize);

        IPage<FotaPlanPo> fotaPlanPos = getBaseMapper().selectPage(page, queryWrapper);
        return fotaPlanPos;
    }

    @Override
    public List<FotaPlanPo> getFotaPlans() {
        QueryWrapper<FotaPlanPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    public IPage<FotaPlanPo> queryPage(Integer current, Integer pageSize, Long treeNodeId, Integer rebuildFlag) {
        Page page = new Page(current, pageSize);
        QueryWrapper<FotaPlanPo> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(treeNodeId)) {
        	queryWrapper.eq("tree_node_id", treeNodeId);
        }
        
        if (Objects.nonNull(rebuildFlag)) {
        	queryWrapper.eq("rebuild_flag", rebuildFlag);
        }
        queryWrapper.orderByDesc("create_time");
        
        IPage<FotaPlanPo> fotaPlanPos = getBaseMapper().selectPage(page, queryWrapper);
        return fotaPlanPos;
    }

    @Override
    public IPage<FotaPlanPo> listNeedUpgradeNotifyFotaPlans(Integer offset, Integer pageSize, Long id, String beforeNowTime) {
        Page page = new Page(offset, pageSize);
        String nowStr = MyDateUtil.formateDate(new Date());
        LambdaQueryWrapper<FotaPlanPo> queryWrapper = new LambdaQueryWrapper<FotaPlanPo>()
                .apply(Objects.nonNull(id) && id.longValue() > 0L,
                        "id > "+id.longValue())
                /*.apply(!StringUtils.isEmpty(beforeNowTime),
                        //"date_format (now(),'%Y-%m-%d') >= date_format('" + beforeNowTime + "','%Y-%m-%d')")
                        "date_format (create_time,'%Y-%m-%d') >= '" + beforeNowTime + "'")*/
                .apply(
                        //"date_format (plan_start_time,'%Y-%m-%d') <= date_format(now(),'%Y-%m-%d')")
                        "date_format (plan_start_time,'%Y-%m-%d') <= '" + nowStr + "'")
                .apply(
                        //"date_format (plan_end_time,'%Y-%m-%d') >= date_format(now(),'%Y-%m-%d')")
                        "date_format (plan_end_time,'%Y-%m-%d') >= '" + nowStr + "'")
                .apply("publish_state = 2")
                .apply("is_enable = 1")
                .orderByAsc(FotaPlanPo::getId);
                //.orderByDesc(FotaPlanPo::getCreateTime);
        IPage<FotaPlanPo> fotaPlanPos = getBaseMapper().selectPage(page, queryWrapper);
        return fotaPlanPos;
    }

    @Override
    public List<FotaPlanPo> listWithOtaStrategyId(Long otaStrategyId) {
        QueryWrapper<FotaPlanPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fota_strategy_id", otaStrategyId);
        return getBaseMapper().selectList(queryWrapper);
    }

	@Override
	public IPage<FotaPlanPo> queryDelayPlanPage(FotaPlanDelayQuery fotaPlanDelayQuery) {
		Page page = new Page(fotaPlanDelayQuery.getCurrent(), fotaPlanDelayQuery.getPageSize());
		Date datetime = fotaPlanDelayQuery.getDatetime();
		QueryWrapper<FotaPlanPo> queryWrapper = new QueryWrapper<>();
		queryWrapper.lt("plan_start_time", datetime);
		queryWrapper.gt("plan_end_time", datetime);
		if (CollectionUtils.isNotEmpty(fotaPlanDelayQuery.getApprovedStatus())) {
			queryWrapper.in("approve_state", fotaPlanDelayQuery.getApprovedStatus());
		}
		if (CollectionUtils.isNotEmpty(fotaPlanDelayQuery.getPublishStatus())) {
			queryWrapper.in("publish_state", fotaPlanDelayQuery.getApprovedStatus());
		}
		
		if (CollectionUtils.isNotEmpty(fotaPlanDelayQuery.getPublishStatus())) {
			queryWrapper.in("is_enable", fotaPlanDelayQuery.getEnableState());
		}
		IPage<FotaPlanPo> fotaPlanPos = getBaseMapper().selectPage(page, queryWrapper);
		return fotaPlanPos;
	}
}
