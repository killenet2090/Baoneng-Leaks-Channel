package com.bnmotor.icv.tsp.ota.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: DeviceComponentPo
 * @Description: OTA升级硬件设备信息库 实体类
 * @author xuxiaochang1
 * @since 2020-11-05
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_device_component")
public class DeviceComponentPo extends BasePo{

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目ID多租户，不同的租户具有不同的项目ID
     */
    private String projectId;

    /**
     * 零件类型
     */
    private String componentType;

    /**
     * 零件类型名称
     */
    private String componentTypeName;

    /**
     * 零件代码区分不同零件的代号
     */
    private String componentCode;

    /**
     * 零件型号
     */
    private String componentModel;

    /**
     * 零件名称
     */
    private String componentName;

    /**
     * 数据版本d，用于更新并发控制使用
     */
    private Integer version;

}
