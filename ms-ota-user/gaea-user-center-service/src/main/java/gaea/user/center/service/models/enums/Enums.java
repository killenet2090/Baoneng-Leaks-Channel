package gaea.user.center.service.models.enums;

/**
 * <pre>
 *  通用枚举类，防止枚举类过多.
 * </pre>
 *
 * @author jiankang.xia
 * @version 1.0.0
 */
public class Enums {
    /**
     * <pre>
     * 系统返回状态码
     * </pre>
     */
    public enum Status {
        /**
         * 操作成功
         */
        Success(200),
        /**
         * 未授权
         */
        Unauthorized(401),
        /**
         * 系统异常
         */
        Error(500),
        /**
         * 业务失败
         */
        BizFailure(700),
        /**
         * 参数校验不通过
         */
        ValidFailure(800),
        ;
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
        private Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
