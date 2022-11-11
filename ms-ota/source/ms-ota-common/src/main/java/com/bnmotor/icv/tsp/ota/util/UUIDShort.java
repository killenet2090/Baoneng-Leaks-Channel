package com.bnmotor.icv.tsp.ota.util;

import java.util.Base64;
import java.util.UUID;

/**
 * @ClassName: UUIDShort.java UUIDShort
 * @Description: 生成日志跟踪标识
 * @author E.YanLonG
 * @since 2020-11-20 11:12:23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public final class UUIDShort {

	public static String generate() {
		UUID uuid = UUID.randomUUID();
		return compressedUUID(uuid);
	}

	static String compressedUUID(UUID uuid) {
		byte[] byUuid = new byte[16];
		long least = uuid.getLeastSignificantBits();
		long most = uuid.getMostSignificantBits();
		long2bytes(most, byUuid, 0);
		long2bytes(least, byUuid, 8);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(byUuid);
	}

	static void long2bytes(long value, byte[] bytes, int offset) {
		for (int i = 7; i > -1; i--) {
			bytes[offset++] = (byte) ((value >> 8 * i) & 0xFF);
		}
	}

	static String compress(String uuidString) {
		UUID uuid = UUID.fromString(uuidString);
		return compressedUUID(uuid);
	}

}