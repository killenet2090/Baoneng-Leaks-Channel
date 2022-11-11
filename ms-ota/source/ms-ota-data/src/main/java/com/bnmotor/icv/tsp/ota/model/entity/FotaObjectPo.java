package com.bnmotor.icv.tsp.ota.model.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName: FotaObjectDo
 * @Description: OTA升级对象指需要升级的一个完整对象，
在车联网中指一辆车
通常拿车的vin作为升级的ID
                                    实体类
 * @author xxc
 * @since 2020-07-06
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_object")
@ApiModel(value="FotaObjectDo对象", description="OTA升级对象指需要升级的一个完整对象， 在车联网中指一辆车 通常拿车的vin作为升级的ID ")
public class FotaObjectPo extends BasePo{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增序列  即ID")
    private Long id;

    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @ApiModelProperty(value = "设备树节点ID")
    private Long treeNodeId;

    @ApiModelProperty(value = "设备树节点代码路径")
    private String treeNodeCodePath;

    @ApiModelProperty(value = "对象ID升级对象的唯一识别ID")
    private String objectId;

    @ApiModelProperty(value = "车牌号")
    private String licenceNo;

    @ApiModelProperty(value = "生产日期")
    private Date productionDate;

    @ApiModelProperty(value = "生产批次")
    private String prodBatchNo;

    @ApiModelProperty(value = "当前区域名称")
    private String currentArea;
    
    @ApiModelProperty(value = "当前区域编号")
    private String currentCode;

    @ApiModelProperty(value = "销售区域")
    private String saleArea;

    @ApiModelProperty(value = "原始版本")
    private String initVersion;

    @ApiModelProperty(value = "上一个版本")
    private String lastVersion;

    @ApiModelProperty(value = "当前版本")
    private String currentVersion;

    @ApiModelProperty(value = "数据库并发控制版本")
    private Integer version;

    /*@ApiModelProperty(value = "上次运行大版本")
    private String lastVersion;*/

    /**
     * 是否可选（在有效任务内）
     */
    @TableField(exist = false)
    private Integer disabled;

    @ApiModelProperty(value = "固件清单配置版本号")
    public String confVersion;
}
