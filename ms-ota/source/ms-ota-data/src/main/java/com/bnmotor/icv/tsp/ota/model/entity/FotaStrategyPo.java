package com.bnmotor.icv.tsp.ota.model.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaStrategyPo
 * @Description: OTA升级策略-新表 实体类
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_strategy")
public class FotaStrategyPo extends BasePo{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 设备树节点ID
     */
    private Long treeNodeId;

    /**
     * 设备树节点业务Code路径
     */
    private String treeNodeCodePath;

    /**
     * 品牌名称
     */
    private String brand;

    /**
     * 品牌代码
     */
    private String brandCode;

    /**
     * 系列名称
     */
    private String series;

    /**
     * 系列代码
     */
    private String seriesCode;

    /**
     * 车型名称
     */
    private String model;

    /**
     * 车型代码
     */
    private String modelCode;

    /**
     * 年款名称
     */
    private String year;

    /**
     * 年款代码
     */
    private String yearCode;

    /**
     * 配置名称
     */
    private String conf;

    /**
     * 配置代码
     */
    private String confCode;

    /**
     * 策略名称
     */
    private String name;

    /**
     * 整车版本号
     */
    private String entireVersionNo;

    /**
     * 回滚模式； 1 - 激进策略， 0 - 保守策略
     */
    private Integer rollbackMode;

    /**
     * 预估升级时长
     */
    private Integer estimateUpgradeTime;

    /**
     * 状态:0=新建,1=审批中,2=审核通过，3=审批拒绝，4=失效
     */
    private Integer status;

    /**
     * 状态:0=不开启,1=开启
     */
    private Integer isEnable;

    /**
     * 数据并发控制版本
     */
    private Integer version;
    
    /**
     * 对应上传的文件id(策略验收报告)
     */
    private Long fileRecordId;
    
    /**
     * 审批备注
     */
    private String remark;

}
