package com.bnmotor.icv.tsp.ota.service.pki.kit;

/**
 * @ClassName: AlgorithmTypeEnum.java AlgorithmTypeEnum
 * @Description: 加密方式
 * @author E.YanLonG
 * @since 2020-11-16 9:32:35
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum AlgorithmTypeEnum {
  RSA("rsa", Integer.valueOf(1)), //
  AES("aes", Integer.valueOf(3)), //
  SHA256WithRSA("sha256WithRsa", Integer.valueOf(4)); //
  
  private Integer value;
  
  private String name;
  
  AlgorithmTypeEnum(String name, Integer value) {
    this.name = name;
    this.value = value;
  }
  
  public Integer getValue() {
    return this.value;
  }
  
  public void setValue(Integer value) {
    this.value = value;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public static AlgorithmTypeEnum getEnumByAlgCode(int alg) {
    for (AlgorithmTypeEnum algEnum : values()) {
      if (alg == algEnum.getValue().intValue()) {
        return algEnum;
      }
    } 
    return null;
  }
}
