package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhangwei2
 * @ClassName: VehDataAsynTaskPo
 * @Description: 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_veh_data_asyn_task")
public class VehDataAsynTaskPo extends BasePo {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id；0-系统用户
     */
    private Long uid;

    /**
     * 业务类型:1-uc;2-ota
     */
    private Integer businessType;

    /**
     * 任务类型:1-一次性全量任务;2-永久性增量任务
     */
    private Integer taskType;

    /**
     * 查询类型:0-全量;1-configId;2-标签id;3-modelId;
     */
    private Integer queryType;

    /**
     * 查询值
     */
    private Long queryValue;

    /**
     * 数据类型:1-查询车辆；2-查询零部件
     */
    private Integer dataType;

    /**
     * 游标，标识当前执行到的条目，用于任务重启后重新执行的起始位置
     */
    private Long queryCursor;

    /**
     * 消息队列主体
     */
    private String topic;

    /**
     * 状态值1-未执行；2-执行中;3-执行完成；4-执行失败
     */
    private Integer status;

    /**
     * 版本号
     */
    private Integer version;
}
