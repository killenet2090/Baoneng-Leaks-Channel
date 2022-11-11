package com.bnmotor.icv.tsp.ota.common.convert;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @ClassName: LocalDateTimeDeserializer.java 
 * @Description: 时间序列化时变为时间戳
 * @author E.YanLonG
 * @since 2020-11-15 9:04:28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
	@Override
	public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator,
			SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeNumber(localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
	}
}