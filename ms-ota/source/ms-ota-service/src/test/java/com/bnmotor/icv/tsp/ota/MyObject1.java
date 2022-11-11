package com.bnmotor.icv.tsp.ota;

import lombok.Data;

/**
 * @ClassName: MyObject1
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/8/13 9:34
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class MyObject1 {
    private Long pkgId;
    private Integer spendTime;
    private Integer status;
    private Long progressedSize;
    private Integer accumulateNum;
}
