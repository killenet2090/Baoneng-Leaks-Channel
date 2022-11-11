package com.bnmotor.icv.tsp.ota.service.pki.kit;

import java.security.SecureRandom;

/**
 * @ClassName: SecretKeygenerator.java SecretKeygenerator
 * @Description: 生成AES加密密码
 * @author E.YanLonG
 * @since 2020-11-16 9:30:12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class SecretKeygenerator {

	static final String CHARACTER = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM`~!@#$%^&*()_+[]{};':,./<>?";

	public static String keygen(int length) {
		StringBuilder builder = new StringBuilder();
		
		SecureRandom random = new SecureRandom(String.valueOf(System.nanoTime()).getBytes());
		for (int i = 0; i < length; i++) {
			int p = random.nextInt(CHARACTER.length());
			builder.append(CHARACTER.charAt(p));
		}
		return builder.toString();
	}
	
	public static String keygen() {
		return keygen(20);
	}
	
}