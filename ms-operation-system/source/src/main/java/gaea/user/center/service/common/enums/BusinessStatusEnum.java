package gaea.user.center.service.common.enums;

/**
 * @ClassName: BussinessStatusEnum
 * @Description: TODO(这里用一句话描述这个类的作用)
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
    USER_CANT_DELETE("管理员账户无法删除！", "6001"),
    USER_NAME_ALREADY_EXISTS("用户登陆名称已存在！", "6002"),
    USER_EMAIL_ALREADY_EXISTS("用户邮箱已存在！", "6005"),
    USER_EMAIL_PATTERN_INCORRECT("用户邮箱格式不正确！", "6006"),
    USER_PHONE_ALREADY_EXISTS("用户手机号已存在！", "6008"),
    USER_NOT_FOUND("用户名和密码未匹配到相关用户!", "6010"),
    PASSWORD_NOT_MATCH("密码不正确!", "6015"),
    USER_IS_DISABLED("账户已禁用", "6020"),
    USER_CAR_ASSEMBLY_ERROR("用户车辆集数据错误", "6030"),
    USER_IS_EXPIRE_TIME_OVER("该账户已过期，请编辑过期时间", "6040"),
    USER_PHONE_CODE_NOT_MATCH("验证码不匹配或已失效！", "6050"),
    USER_EMAIL_PHONE_NOT_MATCH("手机号邮箱不匹配！", "6060"),
    USER_IS_NOT_ENABLED("该账户未禁用，请核实后重试", "6070"),
    USER_PHONE_NOT_EXIST("手机号不存在", "6080"),
    USER_EMAIL_PASSWORD_NOT_MATCH("邮箱密码不匹配", "6090"),
    //前端要求错误码
    USER_IS_NOT_LOGIN("用户未登录或登录已失效！", "A0303"),
    //第三方服务调用出错
    OTHER_SERVICE_INVOKE_ERROR("调用第三方服务出错", "C0001"),
    ROLE_IS_USING("该角色正在被使用，操作失败!", "6000"),
    ROLE_ID_IS_NULL("角色ID不能为空，操作失败!", "6001"),
	ROLE_DUPLICATE_NAME("系统中存在相同名称的角色，操作失败!", "6006"),
    RESOURCE_NOT_FOUND("未选择资源，操作失败!", "6007"),
    PROJECT_CODE_REPEAT("项目编码重复，请调整后重试", "10000"),

    PRIVILEGE_INSERT_REPEAT("资源类型相同且“资源名称”或“资源路径”相同,不能新增", "7001"),
    PRIVILEGE_UPDATE_REPEAT("资源类型相同且“资源名称”或“资源路径”相同,不能更新", "7002"),
    PRIVILEGE_UPDATE_PARENT("父节点中存在“资源类型“相同且“资源名称”或“资源路径”相同的资源", "7003");

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
