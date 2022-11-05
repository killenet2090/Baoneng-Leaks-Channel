package com.bnmotor.icv.tsp.ble.model.request.pki;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: BleEncryptDto
 * @Description: 对称加密
 * @author shuqi1
 * @since 2020-11-1
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
public class BleEncrptyDto {
      /**
       *
       */
      private  String  algorithm;
      /**
       * 加密内容
       */
      private  byte[]  content;
      /**
       * 组织ID
       */
      private  String  groupId;
      /**
       *
       */
      private  Integer keyIndex;
}
