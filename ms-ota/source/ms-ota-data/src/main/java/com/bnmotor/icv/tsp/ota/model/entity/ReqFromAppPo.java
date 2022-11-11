package com.bnmotor.icv.tsp.ota.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: ReqFromAppPo
 * @Description: 存储来自APP的请求 实体类
 * @author xuxiaochang1
 * @since 2020-12-21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_req_from_app")
public class ReqFromAppPo extends BasePo{

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
     * 设备id
     */
    private String deviceId;

    /**
     * 设备类型 APP客户端设备类型:1=安卓,2=ios
     */
    private Integer deviceType;

    /**
     * vin码
     */
    private String vin;

    /**
     * 请求类型：1=版本检查，2=升级确认，3=安装确认
     */
    private Integer reqType;

    /**
     * 升级事务Id
     */
    private Long transId;

    /**
     * 升级任务Id
     */
    private Long taskId;

    /**
     * 附加属性
     */
    private String extra;

    /**
     * OTA云端推送会APP状态：0=新建，1=TBOX已响应，2=已推送成功，3=推送异常
     */
    private Integer ackStatus;

    /**
     * 数据记录版本，用于控制并发
     */
    private Integer version;

}
