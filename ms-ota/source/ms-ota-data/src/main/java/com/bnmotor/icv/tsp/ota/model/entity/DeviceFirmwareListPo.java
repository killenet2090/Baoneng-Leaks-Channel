package com.bnmotor.icv.tsp.ota.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: DeviceFirmwareListPo
 * @Description: OTA升级硬件与固件关联清单 实体类
 * @author xuxiaochang1
 * @since 2020-11-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_device_firmware_list")
public class DeviceFirmwareListPo extends BasePo{

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目Id
     */
    private String projectId;

    /**
     * 关联的树节点ID
     */
    private Long treeNodeId;

    /**
     * 关联的设备零件列表ID
     */
    private Long deviceListId;

    /**
     * 软件ID
     */
    private Long firmwareId;

    /**
     * 数据并发控制字段
     */
    private Integer version;

}
