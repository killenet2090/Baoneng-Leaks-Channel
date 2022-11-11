package com.bnmotor.icv.tsp.commons.oss.service.impl;

import com.bnmotor.icv.tsp.commons.oss.dao.FileUploadHisMapper;
import com.bnmotor.icv.tsp.commons.oss.entity.FileUploadHisDto;
import com.bnmotor.icv.tsp.commons.oss.entity.FileUploadHisPo;
import com.bnmotor.icv.tsp.commons.oss.service.IFileUploadHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author :luoyang
 * @date_time :2020/9/28 17:17
 * @desc:文件上传历史记录
 */
@Slf4j
@Service
public class FileUploadHistoryService implements IFileUploadHistoryService {

    private final FileUploadHisMapper fileUploadHisMapper;

    public FileUploadHistoryService(FileUploadHisMapper fileUploadHisMapper) {
        this.fileUploadHisMapper = fileUploadHisMapper;
    }


    @Override
    public List<FileUploadHisPo> getFileuploadHisList(FileUploadHisDto dto, Integer pageNum, Integer pageSize) {
        if (pageNum <1){
            pageNum = 1;
        }
        return fileUploadHisMapper.getFileUploadHisListBy(dto, (pageNum-1)*pageSize, pageSize);
    }
}
