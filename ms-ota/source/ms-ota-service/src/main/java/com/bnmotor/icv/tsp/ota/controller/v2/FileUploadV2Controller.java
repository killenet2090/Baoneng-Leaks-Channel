package com.bnmotor.icv.tsp.ota.controller.v2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.controller.inner.AbstractController;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFileUploadRecordPo;
import com.bnmotor.icv.tsp.ota.model.req.upload.UploadDto;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaSliceUploadResp;
import com.bnmotor.icv.tsp.ota.service.IFotaSliceFileService;
import com.bnmotor.icv.tsp.ota.util.ExceptionUtil;
import com.bnmotor.icv.tsp.ota.util.MinIOUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: FileUploadController
 * @Description:
 * @author: E.YanLonG
 * @date: 2021/3/15 15:17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@RestController
@Api(value = "V2版本文件上传", tags = { "V2版本文件上传" })
@RequestMapping(value = "/v2/upload")
@Slf4j
public class FileUploadV2Controller extends AbstractController {

	@Autowired
	private IFotaSliceFileService fotaSliceFileService;

	/**
	 * 上传成功
	 */
	private final Integer UPLOAD_SUCCESS = 0;

	/**
	 * 部分上传
	 */
	private final Integer UPLOAD_PART = 1;

	@ApiOperation(value = "文件预上传", notes = "文件预上传", response = FotaSliceUploadResp.class)
	@PostMapping("/init-chunk-upload")
	public FotaSliceUploadResp initChunkUpload(@RequestBody UploadDto uploadDto) throws IOException {
		// 校验文件sha，该文件是否上传过

		if (fotaSliceFileService.containsKey(uploadDto.getFileSha256())) {
			UploadDto mysqlFileData = fotaSliceFileService.get(uploadDto.getFileSha256());
			// 秒传
			if (UPLOAD_SUCCESS.equals(mysqlFileData.getUploadStatus())) {
				return FotaSliceUploadResp.ok(UPLOAD_SUCCESS, mysqlFileData);
			}

			// 获取到该文件已上传分片
//            Map<Integer,String> okChunkMap = MinIoUtils.mapChunkObjectNames(uploadDto.getBucketName(),uploadDto.getFileMd5());
			Map<Integer, String> okChunkMap = MinIOUtil.mapChunkObjectNames(CommonConstant.BUCKET_OTA_NAME, buildChunkPrefix(uploadDto.getFileSha256()));

			List<UploadDto> chunkUploadUrls = new ArrayList<>();
			if (okChunkMap.size() > 0) {
				for (int i = 1; i <= uploadDto.getChunkCount(); i++) {
					// 判断当前分片是否已经上传过了
					if (!okChunkMap.containsKey(i)) {
						// 生成分片上传url
						UploadDto url = new UploadDto();
						url.setPartNumber(i);
//                        url.setUploadUrl(MinIOUtil.createUploadChunkUrl(uploadDto.getBucketName(),uploadDto.getFileMd5(),chunk));
						url.setUploadUrl(MinIOUtil.createUploadChunkUrl(CommonConstant.BUCKET_OTA_NAME, buildChunkPrefix(uploadDto.getFileSha256()), i));
						chunkUploadUrls.add(url);
					}
				}
				if (chunkUploadUrls.size() == 0) {
					return FotaSliceUploadResp.ok("所有分片已经上传完成，仅需要合并文件");
				}
				return FotaSliceUploadResp.ok(mysqlFileData.getUploadStatus(), chunkUploadUrls);
			}
		}
		// 初次上传和已有文件信息但未上传任何分片的情况下则直接生成所有上传url
//        List<String> uploadUrls = MinIOUtil.createUploadChunkUrlList(uploadDto.getBucketName(),uploadDto.getFileSha256(),uploadDto.getChunkCount());
		List<String> uploadUrls = MinIOUtil.createUploadChunkUrlList(CommonConstant.BUCKET_OTA_NAME, buildChunkPrefix(uploadDto.getFileSha256()), uploadDto.getChunkCount());
		List<UploadDto> chunkUploadUrls = new ArrayList<>();
		for (int i = 1; i <= uploadUrls.size(); i++) {
			UploadDto url = new UploadDto();
			url.setPartNumber(i);
//			String chunk = padding(i, uploadDto.getChunkCount());
			url.setUploadUrl(MinIOUtil.createUploadChunkUrl(CommonConstant.BUCKET_OTA_NAME, buildChunkPrefix(uploadDto.getFileSha256()), i));
			chunkUploadUrls.add(url);
		}
		// 向数据库中记录该文件的上传信息
		uploadDto.setUploadStatus(UPLOAD_PART);
		fotaSliceFileService.put(uploadDto.getFileSha256(), uploadDto);

		return FotaSliceUploadResp.ok(UPLOAD_PART, chunkUploadUrls);
	}

//	@Deprecated
//	public String padding(Integer i, Integer chunkCount) {
//		String chunk = StringUtils.leftPad(String.valueOf(i), String.valueOf(chunkCount).length(), "0");
//		return chunk;
//	}

	/**
	 * 合并文件并返回文件信息
	 * 
	 * @param uploadDto
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "文件合并", notes = "文件合并", response = FotaSliceUploadResp.class)
	@PostMapping("/compose-file")
	public FotaSliceUploadResp composeFile(@RequestBody UploadDto uploadDto) throws IOException {
		// 根据md5获取所有分片文件名称(minio的文件名称 = 文件path)
		List<String> chunks = MinIOUtil.listObjectNames(CommonConstant.BUCKET_OTA_NAME, buildChunkPrefix(uploadDto.getFileSha256()));

		// 自定义文件名称
		String fileName = uploadDto.getFileName();
//		String suffix = fileName.substring(fileName.lastIndexOf("."));
		String suffix = FilenameUtils.getExtension(fileName);
		// DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		// fileName = df.format(LocalDateTime.now()) + suffix;
		
		String fileSha256 = uploadDto.getFileSha256();

		MinIOUtil.chunkSort(chunks);

		String fileKey = buildFileKey(uploadDto.getFileType(), uploadDto.getFileSha256());
		// 合并文件
		if (MinIOUtil.composeObject(CommonConstant.BUCKET_OTA_NAME, CommonConstant.BUCKET_OTA_NAME, chunks, fileKey)) {
//          String url = MinIOUtil.getObjectUrl(uploadDto.getBucketName(), fileName, 60);	// 获取文件访问外链(1小时过期)
			String url = MinIOUtil.getObjectUrl(CommonConstant.BUCKET_OTA_NAME, fileKey);
			// 获取数据库里记录的文件信息，修改数据并返回文件信息
			UploadDto dbData = fotaSliceFileService.get(uploadDto.getFileSha256());
			dbData.setUploadStatus(UPLOAD_SUCCESS);
			dbData.setFileName(fileName);
			dbData.setFilePath(url);
			dbData.setSuffix(suffix);
			dbData.setFileKey(fileKey);
			
			// mysql.put(uploadDto.getFileSha256(), dbData);
			FotaFileUploadRecordPo fotaFileUploadRecordPo = fotaSliceFileService.findFotaFileUploadRecordPo(fileSha256);
			if(Objects.isNull(fotaFileUploadRecordPo)) {
//				fotaFileUploadRecordPo = mysql.saveFileUploadRecordDo(dbData);
				fotaFileUploadRecordPo = fotaSliceFileService.updateFileUploadRecordDo(fotaFileUploadRecordPo, dbData);
                Long fileUploadRecordId = fotaFileUploadRecordPo.getId();
                dbData.setFileUploadRecordId(fileUploadRecordId);
                FotaSliceUploadResp.ok(dbData);
            }else{
            	fotaSliceFileService.updateFileUploadRecordDo(fotaFileUploadRecordPo, dbData);
            	dbData.setFileUploadRecordId(fotaFileUploadRecordPo.getId());
            	FotaSliceUploadResp.ok(dbData);
            }
			return FotaSliceUploadResp.ok(dbData);
		}
		return FotaSliceUploadResp.failed("合并文件失败");
	}

	/**
	 * 构建chunk前缀
	 * 
	 * @param fileSha256
	 * @return
	 */
	private String buildChunkPrefix(String fileSha256) {
		return "chunk/" + fileSha256;
	}

	/**
	 * 补全fileKey
	 * 
	 * @param fileType
	 * @param fileKey
	 * @return
	 */
	private String buildFileKey(Integer fileType, String fileKey) {
		Enums.UploadFileTypeEnum uploadFileTypeEnum = Enums.UploadFileTypeEnum.getByType(fileType);
		if (Objects.isNull(uploadFileTypeEnum)) {
			log.warn("不支持的文件类型.fileType={}", fileType);
			throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FILE_TYPE_NOT_SUPPORTED);
		}
		return uploadFileTypeEnum.getBucketName() + "/" + fileKey;
	}

}