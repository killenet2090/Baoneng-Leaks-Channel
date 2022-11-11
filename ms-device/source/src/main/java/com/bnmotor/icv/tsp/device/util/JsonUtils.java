package com.bnmotor.icv.tsp.device.util;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.device.common.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

/**
 * @ClassName: JsonUtil
 * @Description: json序列化工具
 * @author: zhangwei2
 * @date: 2020/12/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class JsonUtils {
    /**
     * 将JSON字符串转换成对象
     *
     * @param json  json字符串
     * @param clazz 指定的类
     *              <p>
     *              如果实体类中包含时间类别的字段，clazz不允许出现集合类型。
     *              如果哟啊获取结合，请使用getList()方法
     *              </p>
     * @return 指定的类型
     */
    public static <T> T jsonStringToObject(String json, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JavaTimeModule timeModule = new JavaTimeModule();
            timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(Constant.DATATIME_FORMATTER));
            mapper.registerModule(timeModule);
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 将对象转换成JSON字符串
     *
     * @param obj 对象
     * @return json字符串
     */
    public static String objectToJsonString(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JavaTimeModule timeModule = new JavaTimeModule();
            timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(Constant.DATATIME_FORMATTER));
            mapper.registerModule(timeModule);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }
}
