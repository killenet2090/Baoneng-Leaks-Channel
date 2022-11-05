package com.bnmotor.icv.tsp.ble.common.constant;

import java.util.regex.Pattern;

/**
 * @ClassName: Constant
 * @Description: 通用基础类
 * @author: shuqi1
 * @date: 2020/4/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class Constant {
    private Constant(){

    }
    /**
     * 手机号正则
     */
    public static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{5,15}$");
    public static final String CREATE_BLEKEY_ERROR = "crate blekey error";
    public static final String BLEKEY_EXPIRED_OP = "admin";
    public static final String BLEKEY_SYSTEM_OP_ID = "1111";
    public static final String BLEKEY_TBOX_OP = "tbox";
    public static final Integer INIT_VERSION = 0;
    public static final Integer INIT_CONFIRME = 0;
    public static final Integer WAS_CONFIRMED = 1;
    public static final Integer INIT_STATUS = 0;
    public static final Integer ACTIVE_STATUS = 1;
    public static final Integer TBOX_DEL = 3;
    public static final Integer CONFIRME_STATUS = 0;
    public static final Integer CANCEL_STATUS = 2;
    public static final Integer EXPIRE_STATUS = 3;
    public static final Integer INIT_KEY_MODEL = 2;
    public static final Integer INIT_DEL_FLAG = 0;
    public static final Integer CANCEL_DEL_FLAG = 1;
    public static final Integer USER_MSTER_TYPE = 1;
    public static final Integer USER_SLAVE_TYPE = 2;
    public static final Integer AUTH_EXPIRE_SPAN = 24;
    public static final Integer AUTH_VOUCHER_EXPIRE_TIME=60;
    public static final Integer CA_PIN_LIMIT=11;
    public static final Integer BLE_NUM_LIMIT=10;
    public static final String UID = "uid";
    public static final Integer OP_BASE_DEFAULT=0;
    public static final Integer OP_BASE_ADD=1;
    public static final Integer OP_BASE_DEL=2;
    public static final Integer OP_EXPIRE_DEL=3;
    public static final Integer OP_OFF_DEL=4;
    public static final Integer OP_BASE_UPDATE=5;
    public static final Integer OP_UNLOAD_UPDATE=6;
    public static final Integer OP_LIMIT_UPDATE=7;
    public static final Integer OP_UNBIND_UPDATE=8;

    public static final Integer DEL_TYPE=3;
    public static final Integer DEL_CMD=1;
    public static final Integer TBOX_DEL_SUCCESS=1;
    public static final Integer UPDATE_DEVICE_PIN_TYPE=7;
    public static final Integer UPDATE_DEVICE_PIN_CMD=1;
    public static final Long DEFAULT_FLAG=0L;
    public static final Integer OPERATION_SUCCESS=0;
    public static final Integer OPERATION_FAILURE=1;
    public static final Integer RECORD_EXIST=1;
    public static final Integer BEFORE_THREE_MONTH=-3;
    public static final Integer BEFORE_ONE_DAY=-1;
    public static final int SUCCESS_STATUS=1;
    public static final Integer BEFORE_FT_HOUR=-24;
    public static final Long COMPARE_ZERO_VALUE=0L;
    public static final Integer COMPARE_EQUAL_VALUE=0;
    public static final String ALGORITHM = "RSA";
    public static final String ALGORITHM_RSA = "SHA256WithRSA";
    public static final String ALGORITHM_APPLICATION_NAME = "ms-bluetooth-key";
       /**
     * 验证码过期时间
     */
    public static final int AUTH_CODE_EXPIRED_TIME = 60;
}
