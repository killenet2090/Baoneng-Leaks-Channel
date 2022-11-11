package com.bnmotor.icv.tsp.ota.schedule;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.AppEnums;
import com.bnmotor.icv.tsp.ota.common.enums.BusinessTypeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.PublishStateEnum;
import com.bnmotor.icv.tsp.ota.handler.tbox.TBoxDownHandler;
import com.bnmotor.icv.tsp.ota.model.entity.CustBasePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanObjListPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.service.IFotaDeviceSyncService;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanObjListDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanService;
import com.bnmotor.icv.tsp.ota.util.CommonUtil;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.bnmotor.icv.tsp.ota.util.MyDateUtil;
import com.bnmotor.icv.tsp.ota.util.TBoxUtil;
import com.google.common.collect.Maps;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.swagger.annotations.Api;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName: scheduleTasks
 * @Description: 定时任务
 * @author: handong1
 * @date: 2020/7/20 10:28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Api(value="定时任务",tags={"定时任务"})
@Slf4j
public class ScheduleTasks {

    @Autowired
    private IFotaPlanService fotaPlanService;

    @Autowired
    private IFotaPlanDbService fotaPlanDbService;

    @Autowired
    private IFotaPlanObjListDbService fotaPlanObjListDbService;

    @Autowired
    @Qualifier("fotaDeviceSyncServiceV2")
    private IFotaDeviceSyncService fotaDeviceSyncService;

    @Autowired
    private TBoxDownHandler tBoxDownHandler;

    private final Integer upgradeNotifyTryNum = 3;

    public Map<String, Runnable> map = Maps.newHashMap();


    @PostConstruct
    public void init(){
        map.put("invalidFotaPlan", () -> updateFotaPlan2Invalid());
        map.put("upgradeNotify", () -> upgradeNotify2TBox());
    }

    /**
     * 内部测试使用方法
     * @param taskName
     */
    public void scheduleTask(String taskName) {
        if(null != map.get(taskName)){
            map.get(taskName).run();
        }
    }

    @XxlJob("otaFotaPlanInvalidJobHandler")
    public ReturnT<String> handle4OtaFotaPlanInvalid(String s) throws Exception {
        updateFotaPlan2Invalid();
        return ReturnT.SUCCESS;
    }

    @XxlJob("otaFotaPlanNotifyJobHandler")
    public ReturnT<String> handle4OtaFotaPlanNotify(String s) throws Exception {
        upgradeNotify2TBox();
        return ReturnT.SUCCESS;
    }

    @XxlJob("otaBuildDeviceTreeJobHandler")
    public ReturnT<String> handle4BuildDeviceTree(String s) throws Exception {
        fotaDeviceSyncService.buildTreeNodeFromTspDevice();
        return ReturnT.SUCCESS;
    }

    /**
     * 更新计划任务状态
     */
    public void updateFotaPlan2Invalid() {

        String taskDesc = "更新到期任务升级状态为失效的任务";

        Function<PageParam, IPage<FotaPlanPo>> function = pageParam -> fotaPlanDbService.getExpiredFotaPlan(pageParam.getOffset(), pageParam.getPageSize(), pageParam.getId());
        final LocalDateTime dateTime = LocalDateTime.now();
        Function<IPage<FotaPlanPo>, Boolean> functionN = iPage -> {
            List<FotaPlanPo> fotaPlanPos = iPage.getRecords().stream().map(item -> {
                FotaPlanPo newItem = new FotaPlanPo();
                newItem.setId(item.getId());
                newItem.setPublishState(PublishStateEnum.INVALID.getState());
                newItem.setUpdateTime(dateTime);
                newItem.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
                return newItem;
            }).collect(Collectors.toList());

            boolean flag = fotaPlanDbService.saveOrUpdateBatch(fotaPlanPos);
            return flag;
        };
        commonDo(taskDesc, function, functionN);
    }

    /**
     * 升级通知推送到车机任务
     */
    public void upgradeNotify2TBox() {

        String taskDesc = "升级通知推送到TBOX任务";
        LocalDateTime beforeNowLocalDate = LocalDateTime.now().minusMonths(1);
        String beforeNowTime = MyDateUtil.localDateTimeStr(beforeNowLocalDate);

        //获取待推送的任务列表
        Function<PageParam, IPage<FotaPlanPo>> function = pageParam -> fotaPlanDbService.listNeedUpgradeNotifyFotaPlans(pageParam.getOffset(), pageParam.getPageSize(), pageParam.getId(), beforeNowTime);
        Function<IPage<FotaPlanPo>, Boolean> functionN = iPage -> {
            iPage.getRecords().stream().forEach(item -> {
                Long otaPlanId = item.getId();
                log.info("执行任务推送对应的升级任务信息={}", item);
                List<FotaPlanObjListPo> fotaPlanObjListPos = fotaPlanObjListDbService.listByOtaPlanId(otaPlanId);
                if(MyCollectionUtil.isEmpty(fotaPlanObjListPos)){
                    log.info("当前任务不存在升级车辆列表，请检查.otaPlanId={}", otaPlanId);
                    return;
                }

                fotaPlanObjListPos.forEach(fotaPlanObjListPo -> {
                    try {
                        if (Objects.nonNull(fotaPlanObjListPo.getNotifyStatus()) && fotaPlanObjListPo.getNotifyStatus().intValue() == 2) {
                            log.info("升级推送已完成，且车端已经完成版本检测，无需重复推送.任务id={},vin={}", otaPlanId, fotaPlanObjListPo.getVin());
                            return;
                        }

                        //如果为空，补充推送
                        if(Objects.isNull(fotaPlanObjListPo.getNotifyTryNum())){
                            fotaPlanObjListPo.setNotifyTryNum(0);
                        }
                        if (fotaPlanObjListPo.getNotifyTryNum() < upgradeNotifyTryNum) {
                            log.info("任务id={},车辆={}, 推送次数<{}", otaPlanId, fotaPlanObjListPo.getVin(), upgradeNotifyTryNum);
                            //执行推送
                            OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(BusinessTypeEnum.OTA_DOWN_VERSION_CHECK_FROM_OTA, fotaPlanObjListPo.getVin(), null, IdWorker.getId());
                            otaProtocol.getBody().getOtaCommonPayload().setSourceType(AppEnums.ClientSourceTypeEnum.FROM_OTA.getType());
                            boolean send2Tbox = tBoxDownHandler.send(otaProtocol);
                            if (!send2Tbox) {
                                log.warn("推送升级通知到APP失败.vin={}", fotaPlanObjListPo.getVin());
                            }else{
                                log.info("推送升级通知到APP成功.vin={}", fotaPlanObjListPo.getVin());
                            }

                            FotaPlanObjListPo updateFotaPlanObjListPo = new FotaPlanObjListPo();
                            updateFotaPlanObjListPo.setId(fotaPlanObjListPo.getId());
                            updateFotaPlanObjListPo.setNotifyTryNum(fotaPlanObjListPo.getNotifyTryNum() + 1);
                            updateFotaPlanObjListPo.setNofityTime(LocalDateTime.now());
                            updateFotaPlanObjListPo.setNotifyStatus(1);
                            CommonUtil.wrapBasePo4Update(updateFotaPlanObjListPo);
                            fotaPlanObjListDbService.updateById(updateFotaPlanObjListPo);
                        }
                    }catch (Exception e){
                        log.error("推送升级通知到APP失败异常.vin={}", fotaPlanObjListPo.getVin(), e);
                    }
                });
            });
            return null;
        };
        commonDo(taskDesc, function, functionN);
    }

    @Builder
    @Data
    static class PageParam{
        private int offset = 0;
        private int pageSize = 10;
        private long id = 0L;
    }

    /**
     * 内部函数，用于执行通用的全表扫描处理任务
     * @param taskDesc
     * @param function
     * @param functionN
     * @param <T>
     * @param <N>
     */
    private <T extends CustBasePo, N> void commonDo(String taskDesc, Function<PageParam, IPage<T>> function, Function<IPage<T>, N> functionN) {
        log.info("========" + taskDesc + "开始=========");
        int offset = 0;
        int pageSize = 10;
        long id = 0L;

        int all = 0;
        int batchSize = 0;
        while(true) {
            batchSize += 1;
            log.warn("批次={}," + taskDesc + "开始。=================== 偏移={}, 页码={}, 当前数据记录Id={}=================", batchSize, offset, pageSize, id);
            IPage<T> ipage = function.apply(PageParam.builder().offset(offset).pageSize(pageSize).id(id).build());
            if (Objects.isNull(ipage) || MyCollectionUtil.isEmpty(ipage.getRecords())) {
                log.warn("批次={}未获取到" + taskDesc + "集合,退出任务。偏移={}, 页码={}, 当前数据记录Id={}", batchSize, offset, pageSize, id);
                break;
            }
            log.info("批次={}" + taskDesc + "批次数量：{}", batchSize, ipage.getRecords().size());
            N n = functionN.apply(ipage);
            log.info("批次={}" + taskDesc + "结果{}", batchSize, n);
            log.warn("批次={}" + taskDesc + "结束。================偏移={}, 页码={}, 当前数据记录Id={}=================", batchSize, offset, pageSize, id);
            all += ipage.getRecords().size();
            id = ipage.getRecords().get(ipage.getRecords().size() - 1).getId();
        }
        log.info("=========结束" + taskDesc + "，共有批次={}, 总条数={}========", batchSize, all);
    }
}
