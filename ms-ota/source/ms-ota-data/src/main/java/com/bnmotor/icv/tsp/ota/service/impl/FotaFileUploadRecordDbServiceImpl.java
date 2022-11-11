package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.data.oss.IOSSProvider;
import com.bnmotor.icv.tsp.ota.mapper.FotaFileUploadRecordMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFileUploadRecordPo;
import com.bnmotor.icv.tsp.ota.service.IFotaFileUploadRecordDbService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: FotaFileUploadRecordServiceImpl
 * @Description: 文件上传记录表 服务实现类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class FotaFileUploadRecordDbServiceImpl extends ServiceImpl<FotaFileUploadRecordMapper, FotaFileUploadRecordPo> implements IFotaFileUploadRecordDbService {

    @Autowired
    private IOSSProvider ossProvider;


    @Override
    public FotaFileUploadRecordPo findFotaFileUploadRecordPo(String fileSha256) {
        QueryWrapper<FotaFileUploadRecordPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_sha", fileSha256);
        queryWrapper.orderByDesc("create_time");
        List<FotaFileUploadRecordPo> fileUploadRecordDos = list(queryWrapper);
        if(MyCollectionUtil.isEmpty(fileUploadRecordDos)){
            log.info("当前文件对应上传记录还未保存，请确认。fileSha256={}", fileSha256);
            return null;
        }
        return fileUploadRecordDos.get(0);
    }
}
