package com.bnmotor.icv.tsp.demo.demo;

import com.bnmotor.icv.tsp.ble.util.MD5Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: TestMD5Builder
 * @Description: 描述类的作用
 * @author: liuyiwei
 * @date: 2020/7/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
public class MD5BuilderTest {

    public static void main(String[] args) {
        log.info(MD5Builder.getMD5("admin"));;
        log.info(MD5Builder.bit16("adminfskafjaisjoiwruewrjnjfkdasjfjdfjkdsfksdjgjsij_123456jfshfisjfijusifjuisdji_8899958i9irfoiwjf9iasdfuashjfidashfj_80099783"));
    }
}
