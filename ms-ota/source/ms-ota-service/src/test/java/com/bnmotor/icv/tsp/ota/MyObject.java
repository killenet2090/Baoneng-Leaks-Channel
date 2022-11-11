package com.bnmotor.icv.tsp.ota;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: MyObject
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/8/13 9:33
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class MyObject {
    private Long transId;
    private Long transId1;
    private Long transId2;
    private Integer status;
    private List<MyObject1> abc;
}
