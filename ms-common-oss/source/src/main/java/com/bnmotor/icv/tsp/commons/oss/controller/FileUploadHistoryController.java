package com.bnmotor.icv.tsp.commons.oss.controller;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.commons.oss.entity.FileUploadHisDto;
import com.bnmotor.icv.tsp.commons.oss.entity.FileUploadHisPo;
import com.bnmotor.icv.tsp.commons.oss.service.IFileUploadHistoryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author :luoyang
 * @date_time :2020/9/28 17:15
 * @desc:文件上传历史记录
 */
@Slf4j
@RestController
@RequestMapping("v1/oss/his")
public class FileUploadHistoryController {

    private final IFileUploadHistoryService iFileUploadHistoryService;

    public FileUploadHistoryController(IFileUploadHistoryService iFileUploadHistoryService) {
        this.iFileUploadHistoryService = iFileUploadHistoryService;
    }

    @PostMapping("list")
    @ApiOperation(value="文件上传历史列表", notes="文件上传历史列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码 从1开始， 默认1", required = false),
            @ApiImplicitParam(name = "pageSize", value = "每页条数 默认 10", required = false)
    })
    public ResponseEntity getFileuploadHisList(FileUploadHisDto dto ,
                                               @RequestParam(defaultValue = "1", required = false) Integer pageNum,
                                               @RequestParam(defaultValue = "10",required = false)Integer pageSize){

        List<FileUploadHisPo> list = iFileUploadHistoryService.getFileuploadHisList(dto,pageNum,pageSize);

        return RestResponse.ok(list);
    }


}
