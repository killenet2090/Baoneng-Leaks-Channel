package com.bnmotor.icv.tsp.commons.oss.service;

import com.bnmotor.icv.tsp.commons.oss.entity.FileUploadHisDto;
import com.bnmotor.icv.tsp.commons.oss.entity.FileUploadHisPo;

import java.util.List;

public interface IFileUploadHistoryService {

    List<FileUploadHisPo> getFileuploadHisList(FileUploadHisDto dto, Integer pageNum, Integer pageSize);

}
