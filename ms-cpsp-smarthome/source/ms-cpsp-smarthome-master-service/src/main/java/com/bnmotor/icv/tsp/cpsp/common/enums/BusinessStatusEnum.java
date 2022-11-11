package com.bnmotor.icv.tsp.cpsp.common.enums;

/**
 * @ClassName: BussinessStatusEnum
 * @Description: 业务异常枚举
 * @author: jiankang
 * @date: 2020/4/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum BusinessStatusEnum
{
    // 系统级别
    SYSTEM_ERROR("系统繁忙，请联系管理员！", "5000"),

    //数据级别
    NO_PERMISSION("无数据访问权限！", "4003"),
    TOKEN_EXPIRED("登陆已过期或者token验证失败,请重新登陆！", "4001"),

    DATA_NOT_FOUND("数据未找到！", "4004"),
    DATA_NOT_UPDATED("数据更新失败！", "4007"),
    DATA_NOT_DELETED("数据删除失败","4010"),
    DATA_NOT_INSERT("数据插入失败", "4013"),
    DATA_IS_ALLOCATED("数据已被分配占用，不允许删除", "4016"),

    //业务类型
    SCENE_GEOFENCE_IS_EXIST("场景已存在地理围栏", "10000");
    //外部
    private String description;
    private String code;

    BusinessStatusEnum(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }


}
