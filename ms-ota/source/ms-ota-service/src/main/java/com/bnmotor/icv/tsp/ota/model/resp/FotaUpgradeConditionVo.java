package com.bnmotor.icv.tsp.ota.model.resp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(chain = true)
@Data
public class FotaUpgradeConditionVo {

	Long id;
	String projectId;
	String condName;
	String condCode;
	String value;
	Integer valueSource;
	Integer valueType;
	
	/*public static FotaUpgradeConditionVo translate(FotaUpgradeCondition fotaUpgradeCondition) {
		FotaUpgradeConditionVo fotaUpgradeConditionVo = new FotaUpgradeConditionVo();
		fotaUpgradeConditionVo.setId(fotaUpgradeCondition.getId());
		fotaUpgradeConditionVo.setProjectId(fotaUpgradeCondition.getProjectId());
		fotaUpgradeConditionVo.setCondCode(fotaUpgradeCondition.getCondCode());
		fotaUpgradeConditionVo.setCondName(fotaUpgradeCondition.getCondName());
		fotaUpgradeConditionVo.setValue(fotaUpgradeCondition.getValue());
		fotaUpgradeConditionVo.setValueSource(fotaUpgradeCondition.getValueSource());
		fotaUpgradeConditionVo.setValueType(fotaUpgradeCondition.getValueType());
		return fotaUpgradeConditionVo;
	}*/
	
	/*public static List<FotaUpgradeConditionVo> translate(List<FotaUpgradeCondition> fotaUpgradeConditionPoList) {
		
		return fotaUpgradeConditionPoList.stream().map(FotaUpgradeConditionVo::translate).collect(Collectors.toList());
	}*/

}