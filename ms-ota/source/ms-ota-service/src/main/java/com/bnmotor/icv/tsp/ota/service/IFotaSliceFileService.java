package com.bnmotor.icv.tsp.ota.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFileUploadRecordPo;
import com.bnmotor.icv.tsp.ota.model.req.upload.UploadDto;
import com.bnmotor.icv.tsp.ota.util.CommonUtil;
import com.bnmotor.icv.tsp.ota.util.ExceptionUtil;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
/**
 * @ClassName: RedisSliceFileService.java 
 * @Description: V2版本分片上传
 * @author E.YanLonG
 * @since 2021-3-15 15:57:28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class IFotaSliceFileService {

	@Autowired
	IFotaFileUploadRecordDbService fotaFileUploadRecordDbService;
	
	/**
	 * 
	 * @param fileKey sha256
	 * @return
	 */
	public boolean containsKey(String fileKey) {
		FotaFileUploadRecordPo fotaFileUploadRecordPo = findFotaFileUploadRecordPo(fileKey);
		return Objects.nonNull(fotaFileUploadRecordPo);
	}
	
	public void put(String fileKey, UploadDto uploadDto) {
		saveFileUploadRecordDo(uploadDto);
	}
	
	public UploadDto get(String fileKey) {
		FotaFileUploadRecordPo fotaFileUploadRecordPo = findFotaFileUploadRecordPo(fileKey);
		
		UploadDto dto = null;
		if (Objects.nonNull(fotaFileUploadRecordPo)) {
			dto = buildUploadDto(fotaFileUploadRecordPo);
		}
		
		return dto;
	}
	
	public UploadDto buildUploadDto(FotaFileUploadRecordPo fotaFileUploadRecordPo) {
		UploadDto dto = new UploadDto();
		dto.setFileName(fotaFileUploadRecordPo.getFileName());
		dto.setFileKey(fotaFileUploadRecordPo.getFileKey());
		dto.setUploadStatus(NumberUtils.toInt(fotaFileUploadRecordPo.getStatus()));
		dto.setChunkCount(fotaFileUploadRecordPo.getChunkSum());
		dto.setFilePath(fotaFileUploadRecordPo.getFilePath());
		dto.setFileSha256(fotaFileUploadRecordPo.getFileSha());
		dto.setFileSize(fotaFileUploadRecordPo.getFileSize());
		dto.setFileType(fotaFileUploadRecordPo.getFileType());
		dto.setFileUploadRecordId(fotaFileUploadRecordPo.getId());
		// dto.setPartNumber(fotaFileUploadRecordPo.getUploadSlice());
		String suffix = FilenameUtils.getExtension(fotaFileUploadRecordPo.getFileName());
		dto.setSuffix(suffix);
		
		// uploadStatus需要做转换 前端值 与后端值有区别 前端：1部分上传 0上传成功 后端：0未上传 1已上传
		dto.setUploadStatus(NumberUtils.toInt(fotaFileUploadRecordPo.getStatus()));
//		dto.setUploadUrl(fotaFileUploadRecordPo.getFileAddress());
		return dto;
	}

	public Map<Integer, String> sliceCompleted(String fileKey) throws IOException {
		return Maps.newHashMap();
	}

	@Deprecated
	public List<Integer> toInteger(List<Object> objects) {
		Function<Object, Integer> mapper = it -> NumberUtils.toInt(String.valueOf(it));
		return objects.stream().map(mapper).collect(Collectors.toList());
	}

	public FotaFileUploadRecordPo saveFileUploadRecordDo(UploadDto uploadDto) {
        FotaFileUploadRecordPo fileUploadRecordDo = new FotaFileUploadRecordPo();
        try{
            Date uploadBeginDt = new Date();
            Date uploadEndDt = new Date();
            String fileAddress = uploadDto.getFilePath();

            fileUploadRecordDo.setFileSize(uploadDto.getFileSize());
            fileUploadRecordDo.setFileName(uploadDto.getFileName());
            fileUploadRecordDo.setFileAddress(fileAddress);
            fileUploadRecordDo.setFileKey(uploadDto.getFileKey());
            fileUploadRecordDo.setFileType(uploadDto.getFileType());
            fileUploadRecordDo.setFileSha(uploadDto.getFileSha256());
            fileUploadRecordDo.setUploadSlice(1);
            fileUploadRecordDo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
            fileUploadRecordDo.setUploadBeginDt(uploadBeginDt);
            fileUploadRecordDo.setUploadEndDt(uploadEndDt);
            fileUploadRecordDo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
            fileUploadRecordDo.setChunkSum(uploadDto.getChunkCount());
            
            CommonUtil.wrapBasePo(fileUploadRecordDo, "sys", true);
            fileUploadRecordDo.setStatus("0");
            fileUploadRecordDo.setVersion(0);
            log.info("file.info={}", fileUploadRecordDo.toString());

            boolean save = fotaFileUploadRecordDbService.save(fileUploadRecordDo);
            //文件存储成功
            if(save){
                log.info("save file upload record successfully.save={}", save);
//                return new FileUploadVo(fileUploadRecordDo.getFileAddress(), fileUploadRecordDo.getId());
                return fileUploadRecordDo;
            }else{
                log.warn("save file upload record failed.save={}", save);
            }
        }catch (Exception e){
            log.error("handle file info error.e={}", e.getMessage(), e);
        }
        throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FILE_UPLOAD_ERROR);
    }
	
	public FotaFileUploadRecordPo updateFileUploadRecordDo(FotaFileUploadRecordPo fileUploadRecordDo, UploadDto uploadDto) {
        try{
            String fileAddress = uploadDto.getFilePath();

            fileUploadRecordDo.setFileSize(uploadDto.getFileSize());
            fileUploadRecordDo.setFileName(uploadDto.getFileName());
            fileUploadRecordDo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
            // fileUploadRecordDo.setUploadBeginDt(uploadBeginDt);
            fileUploadRecordDo.setUploadEndDt(new Date());
            fileUploadRecordDo.setFileAddress(fileAddress);
            fileUploadRecordDo.setFileKey(uploadDto.getFileKey());
            fileUploadRecordDo.setFileType(uploadDto.getFileType());
            fileUploadRecordDo.setUploadSlice(1);
            fileUploadRecordDo.setFileSha(uploadDto.getFileSha256());
            fileUploadRecordDo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
            fileUploadRecordDo.setChunkSum(uploadDto.getChunkCount());
            fileUploadRecordDo.setFilePath(fileAddress);
            
            CommonUtil.wrapBasePo(fileUploadRecordDo, "sys", true);
            fileUploadRecordDo.setStatus("0");
            fileUploadRecordDo.setVersion(0);
            log.info("file.info={}", fileUploadRecordDo.toString());

            boolean save = fotaFileUploadRecordDbService.updateById(fileUploadRecordDo);
            //文件存储成功
            if(save){
                log.info("save file upload record successfully.save={}", save);
//                return new FileUploadVo(fileUploadRecordDo.getFileAddress(), fileUploadRecordDo.getId());
                return fileUploadRecordDo;
            }else{
                log.warn("save file upload record failed.save={}", save);
            }
        }catch (Exception e){
            log.error("handle file info error.e={}", e.getMessage(), e);
        }
        throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FILE_UPLOAD_ERROR);
    }
	
	 public FotaFileUploadRecordPo findFotaFileUploadRecordPo(String fileSha256) {
	        FotaFileUploadRecordPo fileUploadRecordDo = fotaFileUploadRecordDbService.findFotaFileUploadRecordPo(fileSha256);
	        if(Objects.isNull(fileUploadRecordDo)){
	            log.info("当前文件对应上传记录还未保存，请确认。fileSha256={}", fileSha256);
	            return null;
	        }
	        
//	        Optional<FotaFileUploadRecordPo> opt = fileUploadRecordDos.stream().max((x, y) -> x.getCreateTime().compareTo(y.getCreateTime()));
	        return fileUploadRecordDo;
	    }

}