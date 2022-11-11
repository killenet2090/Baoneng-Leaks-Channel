package com.bnmotor.icv.tsp.ota.util;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author eucId + 8
 *
 */
public class EcuHexUtil {

	public static String increase(String origin, Integer deta) {
		Integer changed = NumberUtils.toInt(origin) + deta;
		return changed.toString();
	}
	
}