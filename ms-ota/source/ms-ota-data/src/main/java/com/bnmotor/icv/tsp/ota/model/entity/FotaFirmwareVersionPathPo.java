package com.bnmotor.icv.tsp.ota.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaFirmwareVersionPathPo
 * @Description: 软件版本升级路径记录全量包、补丁包和差分包的升级条件信息记录从适应的版本到当前版本的升级路径 实体类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_firmware_version_path")
@ApiModel(value="FotaFirmwareVersionPathPo对象", description="软件版本升级路径记录全量包、补丁包和差分包的升级条件信息记录从适应的版本到当前版本的升级路径")
public class FotaFirmwareVersionPathPo extends BasePo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @ApiModelProperty(value = "起始固件版本ID")
    private Long startFirmwareVerId;

    @ApiModelProperty(value = "目标固件版本ID")
    private Long targetFirmwareVerId;

    @ApiModelProperty(value = "升级路径类型:0-全量升级路径，1-补丁升级路径，2-差分升级路径")
    private Integer upgradePathType;

    @ApiModelProperty(value = "固件包ID:如果是差分升级路径，则存放差分包id如果是补丁升级路径，则存放补丁包id如果是全量包升级，则该字段为空")
    private Long firmwarePkgId;

    @ApiModelProperty(value = "以版本的ID组成的路径,便于后续的路径检索以/分格的完整的id路径, 到本路径指向的版本id为止对于根节点，path为/。比如当前路径指向的版本为10004,其id path如下：/100001/100002/10003/10004")
    private String firmwareVersionIdPath;

    @ApiModelProperty(value = "是否已经上传了升级包：0=未上传，1=已上传")
    private int pkgUpload;

    @ApiModelProperty(value = "数据版本，后台使用")
    private Integer version;

}
