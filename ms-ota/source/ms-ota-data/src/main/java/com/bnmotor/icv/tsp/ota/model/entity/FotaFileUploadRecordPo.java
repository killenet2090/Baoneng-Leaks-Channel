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
 * @ClassName: FotaFileUploadRecordPo
 * @Description: 文件上传记录表 实体类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_file_upload_record")
@ApiModel(value="FotaFileUploadRecordPo对象", description="文件上传记录表")
public class FotaFileUploadRecordPo extends BasePo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "文件原名称")
    private String fileName;

    @ApiModelProperty(value = "文件大小，单位：字节")
    private Long fileSize;

    @ApiModelProperty(value = "文件md5值")
    private String fileMd5;

    @ApiModelProperty(value = "文件sha值")
    private String fileSha;

    @ApiModelProperty(value = "总分片数")
    private Integer chunkSum;

    @ApiModelProperty(value = "上传开始时间")
    private Date uploadBeginDt;

    @ApiModelProperty(value = "上传结束时间")
    private Date uploadEndDt;

    @ApiModelProperty(value = "文件上传路径")
    private String filePath;

    @ApiModelProperty(value = "服务器磁盘位置")
    private String fileAddress;

    @ApiModelProperty(value = "oss存储中的key值")
    private String fileKey;

    @ApiModelProperty(value = "文件用途类型:0=安装包文件，1=测试报告文件，2=脚本类型")
    private int fileType;

    @ApiModelProperty(value = "是否秒传")
    private Boolean fast;

    @ApiModelProperty(value = "是否为分块上床：0=不分块，1=分块")
    private Integer uploadSlice;

    @ApiModelProperty(value = "状态")
    private String status;

    private Integer version;

    /*@ApiModelProperty(value = "删除标志:0=正常,1=删除")
    private Integer delFlag;*/

}
