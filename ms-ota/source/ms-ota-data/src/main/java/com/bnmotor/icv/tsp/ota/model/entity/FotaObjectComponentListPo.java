package com.bnmotor.icv.tsp.ota.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaObjectComponentListPo
 * @Description: OTA升级对象零件表,用于接收车辆零件信息同步数据 实体类
 * @author xuxiaochang1
 * @since 2020-12-07
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_object_component_list")
public class FotaObjectComponentListPo extends BasePo{

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 项目Id
     */
    private String projectId;

    /**
     * 所属升级对象ID
     */
    private Long otaObjectId;

    /**
     * 零件类型,同设备库元数据component_code定义
     */
    private String componentType;

    /**
     * 零件类型名称
     */
    private String componentTypeName;

    /**
     * 零件型号
     */
    private String componentModel;

    /**
     * 零件名称
     */
    private String componentName;

    /**
     * TSP设备数据老Id
     */
    private String deviceId;

    /**
     * 唯一序列码
     */
    private String sn;

    /**
     * 固件代码固件代码由OTA平台同终端提前约定
     */
    private String firmwareCode;

    /**
     * 软件初始版本
     */
    private String softwareVersion;

    /**
     * 硬件版本
     */
    private String hardwareVersion;

    /**
     *
     */
    /*private Long otaFirmwareListId;*/

    /**
     * 数据并发控制字段
     */
    private Integer version;

}
