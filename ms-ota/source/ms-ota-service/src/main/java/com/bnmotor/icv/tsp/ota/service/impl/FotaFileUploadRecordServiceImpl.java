package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bnmotor.icv.adam.data.oss.IOSSProvider;
import com.bnmotor.icv.adam.data.oss.model.OSSObject;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct.Dto2PoMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFileUploadRecordPo;
import com.bnmotor.icv.tsp.ota.model.req.FileUploadReq;
import com.bnmotor.icv.tsp.ota.model.req.upload.UploadCachedInfo;
import com.bnmotor.icv.tsp.ota.model.req.upload.UploadDto;
import com.bnmotor.icv.tsp.ota.model.resp.FileUploadVo;
import com.bnmotor.icv.tsp.ota.model.resp.upload.UploadVo;
import com.bnmotor.icv.tsp.ota.service.IFotaFileUploadRecordDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaFileUploadRecordInnerService;
import com.bnmotor.icv.tsp.ota.service.IFotaFileUploadRecordService;
import com.bnmotor.icv.tsp.ota.util.*;
import io.minio.ObjectStat;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @ClassName: FotaFileUploadRecordServiceImpl
 * @Description: 文件上传记录表 服务实现类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class FotaFileUploadRecordServiceImpl implements IFotaFileUploadRecordService {
    @Autowired
    private IFotaFileUploadRecordDbService fotaFileUploadRecordDbService;

    @Autowired
    private IOSSProvider ossProvider;

    @Autowired
    private IFotaFileUploadRecordInnerService fotaFileUploadRecordInnerService;

    @Override
    public FileUploadVo upload(MultipartFile multipartFile, FileUploadReq fileUploadReq) {
        long fileSize = multipartFile.getSize();
        MyAssertUtil.notNull(fileUploadReq, OTARespCodeEnum.FILE_UPLOAD_PARAM_ERROR);
        InputStream inputStream;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            log.error("获取文件流异常.", e);
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.SYSTEM_ERROR);
        }

        //如果需要验证文件合法性
        String fileSha256 = FileVerifyUtil.sha256(inputStream).toLowerCase();
        if(1 == fileUploadReq.getNeedVerify()) {
            boolean verify = fileSha256.equalsIgnoreCase(fileUploadReq.getVerfiyCode());

            if (!verify) {
                log.warn("文件哈希码验证失败.fileSha256={}, fileUploadReq.getVerfiyCode()={}", fileSha256, fileUploadReq.getVerfiyCode());
                throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FILE_VERIFY_ERROR);
            }
        }

        if(StringUtils.isEmpty(fileUploadReq.getFileName())){
            log.info("未设置文件名称，补全文件名.multipartFile.getOriginalFilename()={}", multipartFile.getOriginalFilename());
            fileUploadReq.setFileName(multipartFile.getOriginalFilename());
        }
        
        String fileName = FilenameUtils.getName(fileUploadReq.getFileName());
        String fileKey = buildFileKeyWithFileName(fileUploadReq.getFileType(), fileSha256, fileName);
        Integer fileType = fileUploadReq.getFileType();
        String fileAddress = null;
        
        // 上传到外部存储 避免fileKey目录过长，所以采取下级目录存储文件名的方式，如果上级目录已上传文件，则采用'_'连接的位置存储文件
        // 目前采用当前捕捉异常的方式判断(推荐文件上传前先检查目标state是否存在)
        try {
        	fileAddress = executeUpload(multipartFile, fileKey, fileSha256, fileName, fileType);
        } catch (Exception e) {
        	  if (e.getCause().getCause().toString().contains("XMinioParentIsObject")) {
              	fileKey = buildFileKeyWithXMinioParentIsObject(fileType, fileSha256, fileName);
              	fileAddress = executeUpload(multipartFile, fileKey, fileSha256, fileName, fileType);
              } else {
            	  throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FILE_UPLOAD_ERROR);
              }
		}
        
        	Date uploadBeginDt = new Date();
        	Date uploadEndDt = new Date();
            FotaFileUploadRecordPo fileUploadRecordDo = new FotaFileUploadRecordPo();
            fileUploadRecordDo.setFileSize(fileSize);
            fileUploadRecordDo.setFileName(fileUploadReq.getFileName());
            fileUploadRecordDo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
            fileUploadRecordDo.setUploadBeginDt(uploadBeginDt);
            fileUploadRecordDo.setUploadEndDt(uploadEndDt);
            fileUploadRecordDo.setFileAddress(fileAddress);
            fileUploadRecordDo.setFileKey(fileKey);
            fileUploadRecordDo.setFileType(fileUploadReq.getFileType());

            //计算sha值
            fileUploadRecordDo.setFileSha(fileSha256);
            CommonUtil.wrapBasePo(fileUploadRecordDo, fileUploadReq.getCreateBy(), true);
            fileUploadRecordDo.setStatus("0");
            fileUploadRecordDo.setVersion(0);
            log.info("file.info={}", fileUploadRecordDo);

            boolean save = fotaFileUploadRecordDbService.save(fileUploadRecordDo);
            //文件存储成功
            if(save){
                log.info("save file upload record successfully.fileName={}, fileSha256={}", multipartFile.getOriginalFilename(), fileSha256);
                return new FileUploadVo(fileUploadRecordDo.getFileAddress(), fileUploadRecordDo.getId());
            } else{
                log.warn("save file upload record failed.fileName={}, fileSha256={}", multipartFile.getOriginalFilename(), fileSha256);
                throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FILE_UPLOAD_ERROR);
            }
    }
    
    public String executeUpload(MultipartFile multipartFile, String fileKey, String fileSha256, String  fileName, Integer fileType) {
    	try{
            // String fileKey = buildFileKey(fileUploadReq.getFileType(), fileSha256);
            //long fileSize = (long)bytes.length;
            //处理文件上传
            ossProvider.putObject(CommonConstant.BUCKET_OTA_NAME, fileKey, multipartFile.getInputStream());
            return ossProvider.getObjectUrl(CommonConstant.BUCKET_OTA_NAME, fileKey);
        } catch (Exception e){
            log.error("handle file info error.fileSha256={}, e={}", fileSha256, e.getMessage(), e);
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FILE_UPLOAD_ERROR, e);
        }
    }

    /**
     * 补全fileKey
     * @param fileType
     * @param fileKey
     * @return
     */
    private String buildFileKey(Integer fileType, String fileKey){
        Enums.UploadFileTypeEnum uploadFileTypeEnum = Enums.UploadFileTypeEnum.getByType(fileType);
        if(Objects.isNull(uploadFileTypeEnum)){
            log.warn("不支持的文件类型.fileType={}", fileType);
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FILE_TYPE_NOT_SUPPORTED);
        }
        return uploadFileTypeEnum.getBucketName() + "/" + fileKey;
    }
    
    /**
     * 文件上传时，保存文件key为 sha + 文件名
     * http://zentao.bnicvc.com/bug-view-2451.html
     * @param fileType
     * @param fileKey
     * @param fileName
     * @return
     */
    public String buildFileKeyWithFileName(Integer fileType, String fileKey, String fileName){
        Enums.UploadFileTypeEnum uploadFileTypeEnum = Enums.UploadFileTypeEnum.getByType(fileType);
        if(Objects.isNull(uploadFileTypeEnum)){
            log.warn("不支持的文件类型.fileType={}", fileType);
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FILE_TYPE_NOT_SUPPORTED);
        }
        
        fileKey += (StringUtils.isEmpty(fileName) ? "" : "/" + fileName);
        return uploadFileTypeEnum.getBucketName() + "/" + fileKey;
    }
    
    /**
     * XMinioParentIsObject
     * 上级目录存在文件的情况
     * @param fileType
     * @param fileKey
     * @param fileName
     * @return
     */
    public String buildFileKeyWithXMinioParentIsObject(Integer fileType, String fileKey, String fileName){
        Enums.UploadFileTypeEnum uploadFileTypeEnum = Enums.UploadFileTypeEnum.getByType(fileType);
        if(Objects.isNull(uploadFileTypeEnum)){
            log.warn("不支持的文件类型.fileType={}", fileType);
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FILE_TYPE_NOT_SUPPORTED);
        }
        
        fileKey += (StringUtils.isEmpty(fileName) ? "" : "_" + fileName);
        return uploadFileTypeEnum.getBucketName() + "/" + fileKey;
    }

    @Override
    public UploadVo uploadSliceFile(MultipartFile multipartFile, UploadDto uploadDto){
        //如果是未分片逻辑，走原来接口
        if(uploadDto.getChunkCount().intValue() == 1 && uploadDto.getPartNumber().intValue() == 1){
            log.info("文件分块数量为1，走单个文件上传逻辑.uploadDto={}", uploadDto);
            FileUploadReq fileUploadReq = Dto2PoMapper.INSTANCE.uploadDto2FileUploadReq(uploadDto);
            fileUploadReq.setNeedVerify(1);
            fileUploadReq.setVerfiyCode(uploadDto.getFileSha256());
            fileUploadReq.setVerifyType(FileVerifyUtil.FileVerifyEnum.SHA256.getType());
            fileUploadReq.setFileName(uploadDto.getFileName());
            FileUploadVo fileUploadVo = upload(multipartFile, fileUploadReq);
            return buildUploadVo(CommonConstant.OTA_SLICE_UPLOAD_SUCCESS, null, fileUploadVo.getUrl(), fileUploadVo.getFileUploadRecordId());
        }
        try {
            return uploadSliceFile(multipartFile.getInputStream(), uploadDto);
        } catch (IOException e) {
            log.error("文件上传异常.", e);
        }
        throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FILE_UPLOAD_ERROR);
    }

    /**
     *
     * @param inputStream
     * @param uploadDto
     * @return
     */
    @Override
    public UploadVo uploadSliceFile(InputStream inputStream, UploadDto uploadDto){
        MyAssertUtil.isTrue(Objects.nonNull(uploadDto.getChunkCount()) && uploadDto.getChunkCount() > 0, "分片数量不能小于0");
        //续传
        //定义上传当前分片的逻辑
        Runnable uploadChunkRunnable = () -> {
            uploadChunkInner(inputStream, uploadDto);
        };

        UploadVo uploadVo = runUploadInner(uploadDto, uploadChunkRunnable, true);
        return uploadVo;
    }

    /**
     *
     * @param uploadDto
     * @return
     */
    @Override
    public UploadVo fileMerge(UploadDto uploadDto){
        return composeFileSupplier(uploadDto).get();
    }

    /**
     * 获取可能已经上传的文件
     * @param fileSha256
     * @return
     */
    private UploadVo getSuccessUpload(String fileSha256){
        //校验SHA码值
        try {
            ObjectStat objectStat = MinIOUtil.statObject(CommonConstant.BUCKET_OTA_NAME, buildFileKey(Enums.UploadFileTypeEnum.PKG.getType(), fileSha256));
            if(Objects.nonNull(objectStat)){
                log.info("文件已经上传完成，无需再次上传.fileSha256={}", fileSha256);
                //可能文件记录还没有写入数据库
                FotaFileUploadRecordPo fotaFileUploadRecordPo = findFotaFileUploadRecordPo(fileSha256);
                if(Objects.nonNull(fotaFileUploadRecordPo)){
                    return buildUploadVo(CommonConstant.OTA_SLICE_UPLOAD_SUCCESS, null, fotaFileUploadRecordPo.getFileAddress(), fotaFileUploadRecordPo.getId());
                }
            }else{
                log.info("当前文件未上传。fileSha256={}", fileSha256);
            }
        }catch (Exception e){
            log.error("获取对象异常.e.getMessage={}", e.getMessage());
        }
        return null;
    }

    @Override
    public UploadVo fileCheck(String fileSha256) {
        UploadVo uploadVo = getSuccessUpload(fileSha256);
        //待分片
        return Objects.nonNull(uploadVo) ? uploadVo : buildUploadVo(CommonConstant.OTA_SLICE_UPLOAD_WAIT_SLICE, null, null, null);
    }

    @Override
    public void delByFileIds(Collection<Long> fileIds) {
        List<FotaFileUploadRecordPo> fotaFileUploadRecordPos = fotaFileUploadRecordDbService.listByIds(fileIds);
        if(MyCollectionUtil.isNotEmpty(fotaFileUploadRecordPos)){
            /*List<String> fileAddresses = Lists.newArrayList();*/
            fotaFileUploadRecordPos.forEach(item -> {
                try {
                    //删除对应的Minio文件
                    log.info("待删除的文件fileKey={}, sha256={}", item.getFileKey(), item.getFileSha());
                    ossProvider.deleteObject(CommonConstant.BUCKET_OTA_NAME, item.getFileKey());

                    //如果为分块上传
                    if (Objects.nonNull(item.getUploadSlice()) && item.getUploadSlice().intValue() == 1) {
                        //根据sha256获取所有分片文件名称(minio的文件名称 = 文件path)
                        List<String> chunks = MinIOUtil.listObjectNames(CommonConstant.BUCKET_OTA_NAME, buildChunkPrefix(item.getFileSha()));
                        if (MyCollectionUtil.isNotEmpty(chunks)) {
                            chunks.forEach(item1 -> {
                                log.info("待删除的分块文件名称={}, sha256={}", item1, item.getFileSha());
                                ossProvider.deleteObject(CommonConstant.BUCKET_OTA_NAME, item1);
                            });
                        }
                    }
                }catch(Exception e){
                    log.error("删除文件异常.item={}", item, e.getMessage());
                }
            });
        }
    }

    @Override
    public void delByFileIdsTest(List<Long> fileIds) {
        List<FotaFileUploadRecordPo> fotaFileUploadRecordPos = fotaFileUploadRecordDbService.listByIds(fileIds);
        if(MyCollectionUtil.isNotEmpty(fotaFileUploadRecordPos)){
            /*List<String> fileAddresses = Lists.newArrayList();*/
            fotaFileUploadRecordPos.forEach(item -> {
                item.getFileAddress();
                //删除对应的Minio文件
                log.info("待删除的文件fileKey={}, sha256={}", item.getFileKey(), item.getFileSha());
                //ossProvider.deleteObject(CommonConstant.BUCKET_OTA_NAME, item.getFileKey());

                //如果为分块上传
                if(Objects.nonNull(item.getUploadSlice()) && item.getUploadSlice().intValue() == 1){
                    //根据sha256获取所有分片文件名称(minio的文件名称 = 文件path)
                    List<String> chunks = MinIOUtil.listObjectNames(CommonConstant.BUCKET_OTA_NAME, buildChunkPrefix(item.getFileSha()));
                    if(MyCollectionUtil.isNotEmpty(chunks)){
                        chunks.forEach(item1 -> {
                            log.info("待删除的分块文件名称={}, sha256={}", item1, item.getFileSha());
                            //ossProvider.deleteObject(CommonConstant.BUCKET_OTA_NAME, item1);
                        });
                    }
                }
            });

            /*String testKey = "pkg/2332CCCCCC23333";
            List<String> chunks = MinIOUtil.listObjectNames(CommonConstant.BUCKET_OTA_NAME, testKey);
            log.info(chunks.toString());*/
        }
    }

    /**
     * 查找文件上传记录
     * @param fileSha256
     * @return
     */
    private FotaFileUploadRecordPo findFotaFileUploadRecordPo(String fileSha256) {
        QueryWrapper<FotaFileUploadRecordPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_sha", fileSha256);
        queryWrapper.orderByDesc("create_time");
        List<FotaFileUploadRecordPo> fileUploadRecordDos = fotaFileUploadRecordDbService.list(queryWrapper);
        if(MyCollectionUtil.isEmpty(fileUploadRecordDos)){
            log.info("当前文件对应上传记录还未保存，请确认。fileSha256={}", fileSha256);
            return null;
        }
        return fileUploadRecordDos.get(0);
    }

    /**
     * 构建缓存Key
     * @param fileSha256
     * @return
     */
    private String buildCacheKey(String fileSha256){
        return "OTA_UPLOAD_SLICE_" + fileSha256;
    }

    /*private String buildCacheLockedKey(String fileSha256){
        return buildCacheKey(fileSha256) + "_LOCKED";
    }*/

    /**
     *
     * @param uploadDto
     * @param r
     * @param execute
     * @return
     */
    private UploadVo runUploadInner(final UploadDto uploadDto, Runnable r, boolean execute){
        Map<Integer,String> okChunkMap = MinIOUtil.mapChunkObjectNames(CommonConstant.BUCKET_OTA_NAME, buildChunkPrefix(uploadDto.getFileSha256()));
        List<Integer> chunkUploadUrls = new ArrayList<>();
        //如果minio中是否存在对应的上传文件和记录
        if (Objects.nonNull(okChunkMap) && okChunkMap.size() > 0){
            for (int i = 1; i <= uploadDto.getChunkCount(); i++) {
                //判断当前分片是否已经上传过了
                if(!okChunkMap.containsKey(i)){
                    chunkUploadUrls.add(i);
                }
            }
            //全部上传成功
            if (chunkUploadUrls.size() == 0){
                log.info("所有分片已经上传完成，仅需要合并文件");
                //如果锁已经存在,直接返回提示正在合并提示

                UploadVo uploadVo = buildUploadVo(CommonConstant.OTA_SLICE_UPLOAD_SUCCESS, null, null, null);
                uploadVo.setNeedMerge(1);
                uploadVo.setFileName(uploadDto.getFileName());
                uploadVo.setFileSize(uploadDto.getFileSize());
                return uploadVo;
                //return composeFile(buildCacheLockedKey(uploadDto.getFileSha256()), uploadDto);
            }
            log.info("execute={}, !chunkUploadUrls.contains(uploadDto.getPartNumber())={}", execute, !chunkUploadUrls.contains(uploadDto.getPartNumber()));
            //上传当前文件:不包括当前分片
            if(execute && chunkUploadUrls.contains(uploadDto.getPartNumber())){
                r.run();
                return runUploadInner(uploadDto, r,false);
            }else{
                log.warn("execute={}, !chunkUploadUrls.contains(uploadDto.getPartNumber())={}", execute, !chunkUploadUrls.contains(uploadDto.getPartNumber()));
            }
        }else{
            if(execute){
                r.run();
                return runUploadInner(uploadDto, r,false);
            }else{
                chunkUploadUrls = IntStream.rangeClosed(1, uploadDto.getChunkCount()).boxed().collect(Collectors.toList());
            }
        }
        return buildUploadVo(CommonConstant.OTA_SLICE_UPLOAD_PART, chunkUploadUrls);
    }

    /**
     * 构建UploadVo对象
     * @param result
     * @param partNums
     * @return
     */
    private UploadVo buildUploadVo(int result, List<Integer> partNums){
        UploadVo uploadVo = new UploadVo();
        uploadVo.setResult(result);
        uploadVo.setPartNums(partNums);
        return uploadVo;
    }

    /**
     * 构建 UploadVo对象
     * @param result
     * @param partNums
     * @param url
     * @param fileUploadRecordId
     * @return
     */
    private UploadVo buildUploadVo(int result, List<Integer> partNums, String url, Long fileUploadRecordId){
        UploadVo uploadVo = new UploadVo();
        uploadVo.setResult(result);
        uploadVo.setPartNums(partNums);
        uploadVo.setUrl(url);
        uploadVo.setFileUploadRecordId(fileUploadRecordId);
        return uploadVo;
    }

    /**
     *
     * @param redisLockName
     * @param uploadDto
     * @return
     */
    private UploadVo composeFile(String redisLockName, final UploadDto uploadDto) {
        return fotaFileUploadRecordInnerService.composeFile(redisLockName, composeFileSupplier(uploadDto));
    }

    /**
     *
     * @param uploadDto
     * @return
     */
    private Supplier<UploadVo> composeFileSupplier(final UploadDto uploadDto){
        Supplier<UploadVo> supplier = () -> {

            String fileKey = buildFileKey(uploadDto.getFileType(), uploadDto.getFileSha256());
            UploadCachedInfo uploadCachedInfo = Dto2PoMapper.INSTANCE.uploadDto2UploadCachedInfo(uploadDto);
            uploadCachedInfo.setFileKey(fileKey);
            OSSObject ossObject = null;
            ObjectStat objectStat = null;
            try{
                objectStat = MinIOUtil.statObject(CommonConstant.BUCKET_OTA_NAME, fileKey);
            }catch (Exception e){
                log.error("获取对象失败.e.getMessage={}", e.getMessage());
            }
            boolean compose;
            UploadVo uploadVo;
            if(Objects.nonNull(objectStat)){
                compose = true;
            }else {
                //根据sha256获取所有分片文件名称(minio的文件名称 = 文件path)
                List<String> chunks = MinIOUtil.listObjectNames(CommonConstant.BUCKET_OTA_NAME, buildChunkPrefix(uploadDto.getFileSha256()));

                //排序
                chunks = chunks.stream().sorted((item1, item2) -> {
                    int partNum1 = Integer.parseInt(item1.substring(item1.lastIndexOf("/") + 1, item1.lastIndexOf(".")));
                    int partNum2 = Integer.parseInt(item2.substring(item2.lastIndexOf("/") + 1, item2.lastIndexOf(".")));
                    return partNum1 - partNum2;
                }).collect(Collectors.toList());

                try {
                    compose = MinIOUtil.composeObject(CommonConstant.BUCKET_OTA_NAME, CommonConstant.BUCKET_OTA_NAME, chunks, fileKey);
                } catch (Exception e) {
                    log.error("合并文件异常", e);
                    return buildUploadVo(CommonConstant.OTA_SLICE_UPLOAD_COMBINNATING, null);
                }
            }

            //合并文件
            if (compose) {
                boolean verify = verify(ossObject, uploadCachedInfo.getFileSha256(), uploadCachedInfo.getFileKey());
                if(!verify){
                    log.warn("计算sha256码值异常。uploadDto.getFileSha256()={}", uploadDto.getFileSha256());
                    //删除可能存在的脏文件
                    //MinIOUtil.removeObject(CommonConstant.BUCKET_OTA_NAME, uploadCachedInfo.getFileKey());
                    throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FILE_VERIFY_ERROR);
                }

                FotaFileUploadRecordPo existFotaFileUploadRecordPo = findFotaFileUploadRecordPo(uploadCachedInfo.getFileSha256());
                //保存上传记录到数据库
                if(Objects.isNull(existFotaFileUploadRecordPo)) {
                    FileUploadVo fileUploadVo = saveFileUploadRecordDo(uploadCachedInfo);
                    uploadVo = buildUploadVo(CommonConstant.OTA_SLICE_UPLOAD_SUCCESS, null, fileUploadVo.getUrl(), fileUploadVo.getFileUploadRecordId());
                }else{
                    uploadVo = buildUploadVo(CommonConstant.OTA_SLICE_UPLOAD_SUCCESS, null, existFotaFileUploadRecordPo.getFileAddress(), existFotaFileUploadRecordPo.getId());
                }
            } else {
                //合并文件记录失败
                log.warn("合并文件记录失败.fileKey={}, uploadDto={}", fileKey, uploadCachedInfo);
                uploadVo = new UploadVo();
                uploadVo.setResult(CommonConstant.OTA_SLICE_UPLOAD_FAIL);
            }
            return uploadVo;
        };
        return supplier;
    }

    /**
     * 校验验证码是否一致
     * @param fileSha256
     * @param fileKey
     * @return
     */
    private boolean verify(OSSObject ossObject, String fileSha256, String fileKey){
        //校验SHA码值
        ossObject = Objects.nonNull(ossObject) ? ossObject: ossProvider.getObject(CommonConstant.BUCKET_OTA_NAME, fileKey);
        String calFileSha256 = FileVerifyUtil.sha256(ossObject.getContent());
        log.info("calFileSha256={}, fileSha256={}", calFileSha256, fileSha256);
        return fileSha256.equalsIgnoreCase(calFileSha256);
    }

    /**
     * 保存上传记录
     * @param uploadCachedInfo
     * @return
     */
    private FileUploadVo saveFileUploadRecordDo(UploadCachedInfo uploadCachedInfo) {
        FotaFileUploadRecordPo fileUploadRecordDo = new FotaFileUploadRecordPo();
        try{
            Date uploadBeginDt = new Date();
            Long fileSize = uploadCachedInfo.getFileSize();
            Date uploadEndDt = new Date();
            String fileAddress = ossProvider.getObjectUrl(CommonConstant.BUCKET_OTA_NAME, uploadCachedInfo.getFileKey());

            fileUploadRecordDo.setFileSize(fileSize);
            fileUploadRecordDo.setFileName(uploadCachedInfo.getFileName());
            fileUploadRecordDo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
            fileUploadRecordDo.setUploadBeginDt(uploadBeginDt);
            fileUploadRecordDo.setUploadEndDt(uploadEndDt);
            fileUploadRecordDo.setFileAddress(fileAddress);
            fileUploadRecordDo.setFileKey(uploadCachedInfo.getFileKey());
            fileUploadRecordDo.setFileType(uploadCachedInfo.getFileType());
            fileUploadRecordDo.setUploadSlice(1);

            //计算sha值
            //TODO 此处计算SHA256值可能又缺陷
            fileUploadRecordDo.setFileSha(uploadCachedInfo.getFileSha256());
            fileUploadRecordDo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
            /*LocalDateTime date = LocalDateTime.now();*/
            CommonUtil.wrapBasePo(fileUploadRecordDo, uploadCachedInfo.getCreateBy(), true);
            fileUploadRecordDo.setStatus("0");
            fileUploadRecordDo.setVersion(0);
            log.info("file.info={}", fileUploadRecordDo.toString());

            boolean save = fotaFileUploadRecordDbService.save(fileUploadRecordDo);
            //文件存储成功
            if(save){
                log.info("save file upload record successfully.save={}", save);
                return new FileUploadVo(fileUploadRecordDo.getFileAddress(), fileUploadRecordDo.getId());
            }else{
                log.warn("save file upload record failed.save={}", save);
            }
        }catch (Exception e){
            log.error("handle file info error.e={}", e.getMessage(), e);
        }
        throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FILE_UPLOAD_ERROR);
    }

    /**
     * 内部文件上传
     * @param uploadDto
     * @return
     */
    private void uploadChunkInner(InputStream inputStream, UploadDto uploadDto){
        ossProvider.putObject(CommonConstant.BUCKET_OTA_NAME, buildChunkPrefix(uploadDto.getFileSha256()) +"/" +uploadDto.getPartNumber() + ".chunk", inputStream);
    }

    /**
     * 构建chunk前缀
     * @param fileSha256
     * @return
     */
    private String buildChunkPrefix(String fileSha256){
        return "chunk/" + fileSha256;
    }
}
