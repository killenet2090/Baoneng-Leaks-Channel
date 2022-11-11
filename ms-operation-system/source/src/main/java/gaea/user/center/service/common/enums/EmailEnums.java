package gaea.user.center.service.common.enums;

/**
 * <pre>
 *  邮件发送类型枚举
 * </pre>
 *
 * @author jiankang.xia
 * @version 1.0.0
 */
public class EmailEnums
{
    /**
     * <pre>
     * 系统返回状态码
     * </pre>
     */
    public enum Status
    {
        /**
         * 注册类型
         */
        REGISTER(0),
        /**
         * 密码重置类型
         */
        PASSWORD_RESET(1),
        /**
         * 账户激活类型
         */
        ACTIVIATION(2);
        /**
         * 枚举值
         */
        private final int value;

        /**
         * <pre>
         *  私有构造函数
         * </pre>
         *
         * @param value 值
         */
        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
