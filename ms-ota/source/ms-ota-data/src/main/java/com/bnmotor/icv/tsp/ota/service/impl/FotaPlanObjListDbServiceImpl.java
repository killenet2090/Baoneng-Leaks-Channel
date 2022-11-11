package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.mapper.FotaPlanObjListMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanObjListPo;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanObjListDbService;
import com.bnmotor.icv.tsp.ota.util.CommonUtil;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author xxc
 * @ClassName: FotaPlanObjListServiceImpl
 * @Description: OTA升级计划对象清单定义一次升级中包含哪些需要升级的车辆 服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-17
 */

@Service
@Slf4j
public class FotaPlanObjListDbServiceImpl extends ServiceImpl<FotaPlanObjListMapper, FotaPlanObjListPo> implements IFotaPlanObjListDbService {
    @Override
    public FotaPlanObjListPo findOneByObjectId(Long objectId) {
        if (Objects.isNull(objectId)) {
            log.warn("参数异常.objectId={}", objectId);
            return null;
        }
        //按照创建时间降序排列，获取最新的升级任务对象
        QueryWrapper<FotaPlanObjListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_object_id", objectId);
        queryWrapper.orderByDesc("create_time");
        List<FotaPlanObjListPo> list = list(queryWrapper);
        return (MyCollectionUtil.isNotEmpty(list)) ? list.get(0) : null;
    }

    @Override
    public List<FotaPlanObjListPo> listByOtaPlanId(Long planId) {
        QueryWrapper<FotaPlanObjListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_plan_id", planId);
        List<FotaPlanObjListPo> list = list(queryWrapper);
        log.info("list.size={}", MyCollectionUtil.size(list));
        return MyCollectionUtil.safeList(list);
    }

    @Override
    public List<FotaPlanObjListPo> listByOtaObjectId(Long otaObjectId) {
        QueryWrapper<FotaPlanObjListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_object_id", otaObjectId);
        List<FotaPlanObjListPo> list = list(queryWrapper);
        return MyCollectionUtil.safeList(list);
    }

    @Override
    public IPage<FotaPlanObjListPo> queryPage(Long otaPlanId, int current, int pageSize) {
        Page page = new Page(current, pageSize);
        QueryWrapper<FotaPlanObjListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_plan_id", otaPlanId);
        queryWrapper.orderByDesc("create_time");
        IPage<FotaPlanObjListPo> fotaPlanPos = getBaseMapper().selectPage(page, queryWrapper);
        return fotaPlanPos;
    }

	@Override
	public <T, R> List<R> queryVehicleConunt(List<T> fotaObjList, Function function) {
		QueryWrapper<FotaPlanObjListPo> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("count(ota_object_id) as batch_size, ota_plan_id");
        queryWrapper.in("ota_plan_id", fotaObjList);
        queryWrapper.groupBy("ota_plan_id");
        return listObjs(queryWrapper, function);
	}
	
	@Override
	public List<FotaPlanObjListPo> queryPlanObjectListByTime(Long otaObjectId, Date targetTime) {
		return getBaseMapper().queryPlanObjectListByTime(otaObjectId, targetTime);
	}

    @Override
    public void updateInvalidStatus(Long otaPlanId, Integer currentStatus, Integer status) {
        FotaPlanObjListPo fotaPlanObjListPo = new FotaPlanObjListPo();
        fotaPlanObjListPo.setStatus(status);
        CommonUtil.wrapBasePo4Update(fotaPlanObjListPo);

        UpdateWrapper<FotaPlanObjListPo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("ota_plan_id", otaPlanId);
        //如果需要制定当前某个状态的数据才变更状态：如启用，将状态为不可用（40）转换成（0）
        if(Objects.nonNull(currentStatus)){
            updateWrapper.eq("status", currentStatus);
        }
        update(fotaPlanObjListPo, updateWrapper);
    }
}
