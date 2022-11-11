package com.bnmotor.icv.tsp.ota.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.tsp.ota.model.query.FotaStrategyQuery;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyAuditDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyEnableDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyFirmwareListDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyReportDto;
import com.bnmotor.icv.tsp.ota.model.resp.web.FotaStrategySelectableFirmwareVo;

/**
 * @ClassName: FotaStrategyPo
 * @Description: OTA升级策略-新表 服务类
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaStrategyService{
    /**
     * 新增/更新策略
     * @param fotaStrategyDto
     * @return
     */
    Long saveOrUpdateFotaStrategy(FotaStrategyDto fotaStrategyDto);

    /**
     * 计算策略刷新时长
     * @param fotaStrategyFirmwareListDtos
     * @return
     */
    int calcEstimateUpgradeTime(List<FotaStrategyFirmwareListDto> fotaStrategyFirmwareListDtos);

    /**
     * 删除策略
     * @param id
     */
    void delFotaStrategy(Long id);

    /**
     * 分页查询列表
     * @param fotaStrategyQuery
     * @return
     */
    IPage<FotaStrategyDto> list(FotaStrategyQuery fotaStrategyQuery);

    /**
     * 列表
     * @param treeNodeId
     * @return
     */
    List<FotaStrategyDto> list(Long treeNodeId);

    /**
     * 查看策略详情
     * @param id
     * @return
     */
    FotaStrategyDto findOneFotaStrategy(Long id);

    /**
     * 测录审批相关
     * @param fotaStrategyAuditDto
     */
    void strategyAudit(FotaStrategyAuditDto fotaStrategyAuditDto);

    /**
     * 获取配置下可选的固件列表
     * @param treeNodeId
     * @return
     */
    List<FotaStrategySelectableFirmwareVo> listSelectableFirmwares(Long treeNodeId);

    /**
     * 获取最新的整车版本号
     * @param treeNodeId
     * @return
     */
    String latestEntireVersionNo(String treeNodeId);

    /**
     * 策略打开/关闭
     * @param fotaStrategyEnableDto
     */
    void strategyEnable(FotaStrategyEnableDto fotaStrategyEnableDto);
    
    /**
     * 关联审批报告
     * @param fotaStrategyReportDto
     */
    boolean accociateStrategyReport(FotaStrategyReportDto fotaStrategyReportDto);
}
