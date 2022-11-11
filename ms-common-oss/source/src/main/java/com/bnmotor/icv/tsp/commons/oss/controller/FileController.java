package com.bnmotor.icv.tsp.commons.oss.controller;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.ValidationUtils;
import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.commons.oss.config.CommonOssProperties;
import com.bnmotor.icv.tsp.commons.oss.constant.OssConstant;
import com.bnmotor.icv.tsp.commons.oss.entity.FileDto;
import com.bnmotor.icv.tsp.commons.oss.entity.FileVo;
import com.bnmotor.icv.tsp.commons.oss.entity.NfsFileDto;
import com.bnmotor.icv.tsp.commons.oss.service.IFileService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 文件服务器接口
 * @author zhangjianghua1
 */
@Slf4j
@RestController
@RequestMapping("v1/oss")
public class FileController extends BaseController {
    private final IFileService fileService;
    private final CommonOssProperties commonOssProperties;

    public FileController(IFileService fileService, CommonOssProperties commonOssProperties) {
        this.fileService = fileService;
        this.commonOssProperties = commonOssProperties;
    }

    @ApiOperation(value="本地文件上传", notes="本地文件上传")
    @PostMapping("/upload")
    public ResponseEntity uploadFiles(@RequestHeader(value = "uid",required = false ,defaultValue = "0") Long uid,FileDto fileDto){
        //检验uid（用户id'）
//        if (Objects.isNull(uid)){
//            throw new AdamException(RespCode.REQUEST_PARAMETER_MISSING,"缺失请求头uid");
//        }
        //信息校验
        ValidationUtils.validate(fileDto);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < fileDto.getFile().length; i++) {
            MultipartFile file = fileDto.getFile()[i];
            String originalName = file.getOriginalFilename();
            int index = originalName.lastIndexOf(".");
            if (index > 0) {
                String suffix = originalName.substring(index + 1);
                list.add(suffix.toLowerCase());
            }else {
                throw new AdamException(RespCode.USER_FILE_TYPE_NOT_MATCH,"第"+(i+1)+"个文件未知文件类型，不支持该文件上传");
            }
        }
        checkFileStuffix(list);
        List<FileVo> files = fileService.uploadFile(fileDto,uid);
        return RestResponse.ok(files);
    }



    @ApiOperation(value="网络文件上传", notes="网络文件上传")
    @PostMapping("/nsf/upload")
    public ResponseEntity uploadNfsFiles(@RequestHeader(value = "uid" , required = false ,defaultValue = "0") Long uid, @RequestBody @Valid NfsFileDto fileDto){
        //检验uid（用户id'）
//        if (Objects.isNull(uid)){
//            throw new AdamException(RespCode.REQUEST_PARAMETER_MISSING,"缺失请求头uid");
//        }

        //校验files url合法性
        for(String url:fileDto.getFiles()){
            if(!Pattern.matches(OssConstant.URL_PATTERN, url)){
                throw new AdamException(RespCode.USER_PARAM_TYPE_ERROR);
            }
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < fileDto.getFiles().size(); i++) {
            String file = fileDto.getFiles().get(i);
            int index = file.lastIndexOf(".");
            if (index > 0){
                String stuffix = file.substring(index + 1);
                list.add(stuffix.toLowerCase());
            }else {
                throw new AdamException(RespCode.USER_FILE_TYPE_NOT_MATCH,"第"+(i+1)+"个文件未知文件类型，不支持该文件上传");
            }
        }
        checkFileStuffix(list);
        List<FileVo> files = fileService.uploadFile(fileDto,uid);
        return RestResponse.ok(files);
    }

    @ApiOperation(value="获取文件地址", notes="文件上传")
    @GetMapping("/ephemeral/url")
    public ResponseEntity getObjectUrl(String bucket, String fileName){
        String ephemeralUrl = fileService.obtainFileEphemeralUrl(bucket, fileName);
        return RestResponse.ok(ephemeralUrl);
    }


    private void checkFileStuffix(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (!commonOssProperties.getAcceptFileSuffix().contains(list.get(i))){
                throw new AdamException(RespCode.USER_FILE_TYPE_NOT_MATCH,"第"+(i+1)+"个文件的类型为"+list.get(i)+"，系统未开放该类型文件上传");
            }
        }
    }
}
