package com.bnmotor.icv.tsp.ota.common.enums;

/**
 * @ClassName: EncryptEnum.java EncryptEnum
 * @Description: 加密状态枚举类
 * @author E.YanLonG
 * @since 2020-11-12 18:07:39
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum EncryptStateEnum {

	 TYPE_FAIL(3, "加密失败"),
     TYPE_FINISH(2, "加密成功"),
     TYPE_BUILDING(1, "加密中"),
     TYPE_WAIT(0, "未加密"),
	;
	Integer state;
	String desc;
	
	EncryptStateEnum(Integer state, String desc) {
		this.state = state;
		this.desc = desc;
	}
	
	public Integer getState() {
		return this.state;
	}

}