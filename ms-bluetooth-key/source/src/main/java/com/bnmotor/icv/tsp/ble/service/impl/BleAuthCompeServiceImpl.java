package com.bnmotor.icv.tsp.ble.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthCompePo;
import com.bnmotor.icv.tsp.ble.mapper.BleAuthCompeMapper;
import com.bnmotor.icv.tsp.ble.service.BleAuthCompeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author shuqi1
 * @ClassName: TspBleAuthCompe
 * @Description: 查询所有权限
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-20
 */

@Service
public class BleAuthCompeServiceImpl extends ServiceImpl<BleAuthCompeMapper, BleAuthCompePo> implements BleAuthCompeService {

    @Resource
    BleAuthCompeMapper bleAuthCompeMapper;

    @Override
    public Map<Long, String> queryAllCompe() {
        Map<Long, String> map = new HashMap<>();
        List<BleAuthCompePo> bleAuthCompePoList = bleAuthCompeMapper.queryBleCompe();
        Iterator<BleAuthCompePo> iterator = bleAuthCompePoList.iterator();
        while (iterator.hasNext()) {
            BleAuthCompePo bleAuthCompePo = (BleAuthCompePo) iterator.next();
            map.put(bleAuthCompePo.getId(), bleAuthCompePo.getServiceName());
        }
        return map;
    }

    @Override
    public Map<Long, String> queryLimitCompe(List<Long> compeList) {
        Map<Long, String> map = new HashMap<>();
        List<BleAuthCompePo> bleAuthCompePoList = bleAuthCompeMapper.queryBleCompe();
        Iterator<BleAuthCompePo> iterator = bleAuthCompePoList.iterator();
        while (iterator.hasNext()) {
            BleAuthCompePo bleAuthCompePo = (BleAuthCompePo) iterator.next();
            for (long compe : compeList) {
                if (compe == bleAuthCompePo.getId()) {
                    map.put(bleAuthCompePo.getId(), bleAuthCompePo.getServiceName());
                    continue;
                }
            }


        }
        return map;
    }

    @Override
    public List<Long> queryBleCompeId() {
        return null;
    }

    @Override
    public int addCompe(BleAuthCompePo bleAuthCompePo) {
        return 0;
    }

    @Override
    public int updateCompe(BleAuthCompePo bleAuthCompePo) {
        return 0;
    }

    @Override
    public int delCompe(List<BleAuthCompePo> bleAuthCompePoList) {
        return 0;
    }
}
