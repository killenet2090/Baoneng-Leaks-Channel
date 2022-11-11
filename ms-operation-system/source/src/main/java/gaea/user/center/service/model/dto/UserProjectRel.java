package gaea.user.center.service.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: UserProjectRel
 * @Description: TODO(用户与项目关联)
 * @author: yuhb1
 * @date: 2020/8/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */

@Data
public class UserProjectRel {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty(value = "项目列表，1,2,3", example = "1", dataType = "List")
    private List<Integer> projects;
}
