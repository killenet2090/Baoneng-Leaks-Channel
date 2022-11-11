package com.bnmotor.icv.tsp.vehstatus.config;

import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.config.convert.VehStatusConvertForMap;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: VehStatusMappingConfig
 * @Description: 读取车况映射配置文件信息
 * @author: huangyun1
 * @data: 2020-06-15
 * @Copyrigght: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Setter
@Configuration
public class VehStatusMappingConfig {

    private VehStatusConvertForMap<String> statusKeyForStatusConvertMap = new VehStatusConvertForMap<>();
    private VehStatusConvertForMap<String> statusNameForStatusConvertMap = new VehStatusConvertForMap<>();
    private Map<String, List<String>> vehStatusGroupMap = new HashMap();

    /**
     * 根据statusKey解析成物理值的map
     * @return
     */
    @Bean
    public VehStatusConvertForMap<String> statusKeyForStatusConvertMap() {
        return statusKeyForStatusConvertMap;
    }

    /**
     * 分组map
     * @return
     */
    @Bean
    public Map<String, List<String>> vehStatusGroupMap() {
        return vehStatusGroupMap;
    }

    /**
     * 根据statusName解析成物理值的map
     * @return
     */
    @Bean
    public VehStatusConvertForMap<String> statusNameForStatusConvertMap() {
        return statusNameForStatusConvertMap;
    }

    @PostConstruct
    public void initVehStatusMap() {
        for(VehStatusEnum vehStatusEnum : VehStatusEnum.values()) {
            statusKeyForStatusConvertMap.put(vehStatusEnum.getCode(), vehStatusEnum);
            statusNameForStatusConvertMap.put(vehStatusEnum.getColumnName(), vehStatusEnum);
            String groupName = vehStatusEnum.getGroupName();
            List<String> groupMapList = vehStatusGroupMap.get(groupName);
            if(groupMapList != null && !groupMapList.isEmpty()) {
                groupMapList.add(vehStatusEnum.getColumnName());
                vehStatusGroupMap.put(groupName, groupMapList);
            } else {
                groupMapList = new ArrayList<>();
                groupMapList.add(vehStatusEnum.getColumnName());
                vehStatusGroupMap.put(groupName, groupMapList);
            }
        }
    }

}
