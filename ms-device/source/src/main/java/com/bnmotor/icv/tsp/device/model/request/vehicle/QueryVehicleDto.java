package com.bnmotor.icv.tsp.device.model.request.vehicle;

import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: QueryVehicleDto
 * @Description: 根据各种条件查询车辆信息
 * @author: zhangwei2
 * @date: 2020/7/4
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ApiModel(value = "queryVehicleDto", description = "查询车辆信息")
public class QueryVehicleDto extends PageRequest {

    @ApiModelProperty(value = "车架号", name = "vin")
    private String vin;

    @ApiModelProperty(value = "车牌号", name = "plate")
    private String plate;

    @ApiModelProperty(value = "内置SIM卡号", name = "sim")
    private String sim;

    @ApiModelProperty(value = "零部件SN", name = "sn")
    private String sn;

    @ApiModelProperty(value = "姓名", name = "userName")
    private String userName;

    @ApiModelProperty(value = "手机号", name = "phone")
    private String phone;

    @ApiModelProperty(value = "身份证号", name = "idCardNum")
    private String idCardNum;

    @ApiModelProperty(value = "动力类型: 0-燃油；1-纯电动；2-混动车", name = "vehType")
    private List<Integer> vehType;

    @ApiModelProperty(value = "绑定状态：0-未绑定；1-已绑定", name = "status")
    private List<Integer> status;

    @ApiModelProperty(value = "车辆状态：1-已创建， 2-已销售，3-已报废", name = "vehStatus")
    private List<Integer> vehStatus;

    @ApiModelProperty(value = "车辆认证状态：0-未认证，1-已认证", name = "certificationStatus")
    private List<Integer> certificationStatus;

    @ApiModelProperty(value = "品牌ID列表", name = "brandIds")
    private List<String> brandIds;

    @ApiModelProperty(value = "车系ID列表", name = "seriesIds")
    private List<String> seriesIds;

    @ApiModelProperty(value = "型号ID列表", name = "vehModelIds")
    private List<String> vehModelIds;

    @ApiModelProperty(value = "车辆属性ID", name = "categoryId")
    private String categoryId = "1";

    @ApiModelProperty(value = "标签ID列表", name = "tagIds")
    private List<String> tagIds;

    //车架号编码
    private List<String> vins;

    private String userID;
}
