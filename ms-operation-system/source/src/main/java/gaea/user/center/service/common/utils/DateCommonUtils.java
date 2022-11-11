package gaea.user.center.service.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName: DateUtils
 * @Description: 日期操作工具类
 * @author: jiangchangyuan1
 * @date: 2020/11/3
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class DateCommonUtils {
    /**
     * @Description: 当前时间增加分钟数
     */
    public static Date afterMinutes(Integer minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, (calendar.get(Calendar.MINUTE) + minutes));
        return calendar.getTime();
    }
}
