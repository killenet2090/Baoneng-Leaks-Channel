package com.bnmotor.icv.tsp.ota.model.compose;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@ApiModel(description = "标签参数")
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class GroupInfo {

	Long groupId;
	String groupName;
	List<LabelInfo> labels;
}