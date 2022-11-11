package com.bnmotor.icv.tsp.ota.common.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @ClassName: LocalDateTimeDeserializer.java 
 * @Description: JS不支持LONG精度Long转String
 * @author E.YanLonG
 * @since 2020-11-15 9:04:28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class LongJsonSerializer extends JsonSerializer<Long> {

	@Override
	public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		String text = (value == null ? null : String.valueOf(value));
		if (text != null) {
			jsonGenerator.writeString(text);
		}
	}
}