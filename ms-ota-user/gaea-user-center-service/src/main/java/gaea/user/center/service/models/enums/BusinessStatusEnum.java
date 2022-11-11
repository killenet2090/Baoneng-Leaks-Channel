package gaea.user.center.service.models.enums;

/**
 * @ClassName: BussinessStatusEnum
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: jiankang
 * @date: 2020/4/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum BusinessStatusEnum {

    // 系统级别
    SYSTEM_ERROR("系统繁忙，请联系管理员！", "5000"),

    //数据级别
    NO_PERMISSION("无数据访问权限！", "4003"),
    DATA_NOT_FOUND("数据未找到！", "4004"),
    DATA_NOT_UPDATED("数据更新失败！", "4005");

    //外部


    private String description;
    private String code;

    private BusinessStatusEnum(String description, String code) {
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
