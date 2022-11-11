package com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo;

import com.bnmotor.icv.tsp.cpsp.engine.rules.engine.pojo.channel.InstChannelApiDomain;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: CPSPInput
 * @Description:
 * @author liuyiwei1
 * @date 2020-08-18 15:49
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CPSPInput<T extends CPSPOutput> implements Serializable {

    private static final long serialVersionUID = 505396134583633771L;

    /**
     * 项目ID
     */
    @Id
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 内容服务提供商
     */
    private String supplier;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 业务接口类别
     */
    private String serviceCode;

    /**
     * 策略类KEY
     */
    private String key;

    /**
     * 当前业务类型
     */
    private String currentAPI;

    /**
     * 缓存或持久化的KEY
     */
    private String cacheKey;

    /**
     * 缓存的对象类
     */
    private Class cacheClass;

    /**
     * 服务渠道列表
     */
    private List<InstChannelApiDomain> channels;
}
