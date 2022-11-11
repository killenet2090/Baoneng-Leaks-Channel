package gaea.vehicle.basic.service.config;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

/**
 * @ClassName: ServiceNamePatternConverter
 * @Description: TODO(自定义日志字段输出类，后期完善)
 * @author: jiankang
 * @date: 2020/4/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Plugin(name = "ServiceNamePatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"docker_id", "service_Name","pid"})
public class ServiceNamePatternConverter extends LogEventPatternConverter {


    protected ServiceNamePatternConverter(String name, String style) {
        super(name, style);
    }

    @Override
    public void format(LogEvent logEvent, StringBuilder stringBuilder) {

    }
}
