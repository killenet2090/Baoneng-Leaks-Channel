package gaea.user.center.service.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @ClassName: PhoneSmsDTO
 * @Description: 手机短信发送实体
 * @author: jiangchangyuan1
 * @date: 2020/11/3
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ApiModel(description = "手机短信发送实体")
public class TemplateSmsDto {
    private String businessId;
    private int fromType;
    private String sendPhone;
    private int sendChannel;
    private int mappingTemplateId;
    private Map extraData;
}
