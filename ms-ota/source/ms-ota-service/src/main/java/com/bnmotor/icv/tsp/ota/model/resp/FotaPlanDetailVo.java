package com.bnmotor.icv.tsp.ota.model.resp;


import com.bnmotor.icv.tsp.ota.model.entity.*;
import lombok.Data;

import java.util.List;

/**
 * 任务详情实体
 */
@Data
public class FotaPlanDetailVo {
    /**
     *
     */
    private FotaPlanPo fotaPlanPo;

    /**
     *
     */
    private FotaPlanPo fotaPlanTaskDetail;


    /**
     *
     */
    private TaskTerminatePo taskTerminate;

    /**
     *
     */
    private List<UpgradeTaskConditionPo> fotaUpgradeConditionList;

    /**
     *
     */
    private List<FotaPlanObjListVo> fotaPlanObjLists;

    /**
     *
     */
    private List<FotaPlanFirmwareListVo> fotaPlanFirmwareLists;
}
