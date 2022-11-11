package com.bnmotor.icv.tsp.ota.util;

import java.io.IOException;
import java.time.LocalDateTime;

import com.bnmotor.icv.tsp.ota.common.convert.LocalDateTimeDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @ClassName: JsonUtil
 * @Description: Json工具类
 * @author: hao.xinyue
 * @date: 2020/3/10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class OtaJsonUtil
{
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        //将null值不序列化
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //允许单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //允许不带引号的字段名称
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //将null值转换为空串
        /*
        mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException, JsonProcessingException
            {
                gen.writeString("");
            }
        });
         */
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        mapper.registerModule(timeModule);
    }

    /**
     * 对象转换成JSON
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public static String toJson(Object obj) throws JsonProcessingException
    {
        return mapper.writeValueAsString(obj);
    }

    /**
     * 将 JSON 字符串转化为 Java 对象
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public static <T> T toObject(String json, Class<T> clazz) throws IOException
    {
        return mapper.readValue(json, clazz);
    }
}
