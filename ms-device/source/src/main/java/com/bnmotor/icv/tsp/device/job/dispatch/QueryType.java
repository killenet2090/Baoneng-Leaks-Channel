package com.bnmotor.icv.tsp.device.job.dispatch;

/**
 * @ClassName: QueryType
 * @Description: 查询类型
 * @author: zhangwei2
 * @date: 2020/6/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum QueryType {
    ALL(0, "全量"),
    CONFIG_ID(1, "配置id"),
    TAG_ID(2, "标签id"),
    MODEL_ID(3, "车型id");

    private final Integer type;
    private final String desp;

    QueryType(Integer type, String desp) {
        this.type = type;
        this.desp = desp;
    }

    public static QueryType valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        for (QueryType queryType : QueryType.values()) {
            if (queryType.type.equals(type)) {
                return queryType;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getDesp() {
        return desp;
    }
}
