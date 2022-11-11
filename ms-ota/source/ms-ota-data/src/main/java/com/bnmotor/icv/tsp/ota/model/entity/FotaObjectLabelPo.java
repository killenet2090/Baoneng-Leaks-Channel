package com.bnmotor.icv.tsp.ota.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaObjectLabelPo
 * @Description: OTA升级对象指需要升级的一个完整对象，
在车联网中指一辆车
通常拿车的vin作为升级的ID
                                          实体类
 * @author xuxiaochang1
 * @since 2020-11-27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_object_label")
public class FotaObjectLabelPo extends BasePo{

    private static final long serialVersionUID = 1L;

    /**
     * 自增序列  即ID
     */
    private Long id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 对象ID
            升级对象的唯一识别ID
     */
    private Long objectId;
    
    /**
     * 车辆vin
     */
    private String vin;

    /**
     * 标签key
     */
    private Long labelGroupId;

    /**
     * 标签key
     */
    private String labelGroupName;

    /**
     * 标签key
     */
    private String labelKey;

    /**
     * 标签值
     */
    private String labelValue;

    /**
     * 数据记录版本，用于控制并发
     */
    private Integer version;

}
