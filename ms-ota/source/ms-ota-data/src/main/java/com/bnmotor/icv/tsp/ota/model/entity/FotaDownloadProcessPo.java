package com.bnmotor.icv.tsp.ota.model.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName: FotaDownloadProcessPo
 * @Description: OTA升级下载进度
记录下载的当前进度信息。一个升级对象的一个软件的升级记录对应一条下载进度信息
                                             -&#& 实体类
 * @author xxc
 * @since 2020-07-23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_download_process")
@ApiModel(value="FotaDownloadProcessPo对象", description="OTA升级下载进度 记录下载的当前进度信息。一个升级对象的一个软件的升级记录对应一条下载进度信息 ")
public class FotaDownloadProcessPo extends BasePo{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "升级记录ID")
    private Long upgradeRecordId;

    @ApiModelProperty(value = "已下载字节数, byte")
    private Long downloadedSize;

    @ApiModelProperty(value = "总字节数")
    private Long totalSize;

    @ApiModelProperty(value = "截至时间")
    private Date uptoTime;

    private Integer version;

}
