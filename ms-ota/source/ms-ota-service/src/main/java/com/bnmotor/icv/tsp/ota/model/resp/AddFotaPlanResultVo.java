package com.bnmotor.icv.tsp.ota.model.resp;

import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: AddFotaPlanResultVo
 * @Description:    新增加任务结果类
 * @author: xuxiaochang1
 * @date: 2020/7/29 17:01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
public class AddFotaPlanResultVo {
    /**
     * 新增成功=1，新增失败=0
     */
    private int result;
    private String msg;
    List<ExistValidPlanObjVo> existValidPlanObjVos;

    private String otaPlanId;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(MyCollectionUtil.isNotEmpty(existValidPlanObjVos)) {
            sb.append(msg +"\n\n");
            int index = 0;
            for(ExistValidPlanObjVo existValidPlanObjVo : existValidPlanObjVos){
                sb.append((++index) + ": 车辆Vin码:"+existValidPlanObjVo.getVin()+"\r\n");
            }
        }
        return sb.toString();
    }
}
