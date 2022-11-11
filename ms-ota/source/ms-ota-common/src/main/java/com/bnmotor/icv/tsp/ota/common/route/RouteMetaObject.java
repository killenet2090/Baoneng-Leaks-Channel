package com.bnmotor.icv.tsp.ota.common.route;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.common.collect.Maps;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: RouteMetaObject.java 
 * @Description: RouteMetaObject.java
 * @author E.YanLonG
 * @since 2020-12-21 8:41:50
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class RouteMetaObject {

	final static Map<Class<MetaPair>, MetaPair> FieldMetaMap = Maps.newConcurrentMap();

	public void register(Class clazz, Class annotation) {
		List<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, annotation );
		fields.stream().forEach(field -> {
			MetaPair metaPair = MetaPair.of().setClazz(clazz).setField(field);
			FieldMetaMap.put(clazz, metaPair);
		});
	}

	public Object reverse(Object object) throws Exception {
		Class<?> clazz = object.getClass();
		MetaPair metaPair = FieldMetaMap.get(clazz);
		return Objects.isNull(metaPair) ? null: FieldUtils.readField(metaPair.getField(), object, true);
	}
	
	@NoArgsConstructor(staticName = "of")
	@Accessors(chain = true)
	@Data
	static class MetaPair {
		Class<?> clazz;
		Field field;
	}
}
