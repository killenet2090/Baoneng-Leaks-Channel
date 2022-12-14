package com.bnmotor.icv.tsp.commons.oss.service.impl;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.data.oss.IOSSProvider;
import com.bnmotor.icv.adam.data.oss.exception.OSSException;
import com.bnmotor.icv.adam.starter.data.oss.OSSProperties;
import com.bnmotor.icv.tsp.commons.oss.config.CommonOssProperties;
import com.bnmotor.icv.tsp.commons.oss.constant.AccessPrivilegeEnum;
import com.bnmotor.icv.tsp.commons.oss.constant.OssConstant;
import com.bnmotor.icv.tsp.commons.oss.dao.FileUploadHisMapper;
import com.bnmotor.icv.tsp.commons.oss.entity.FileDto;
import com.bnmotor.icv.tsp.commons.oss.entity.FileUploadHisPo;
import com.bnmotor.icv.tsp.commons.oss.entity.FileVo;
import com.bnmotor.icv.tsp.commons.oss.entity.NfsFileDto;
import com.bnmotor.icv.tsp.commons.oss.service.IFileService;
import com.bnmotor.icv.tsp.commons.oss.util.PicUtil;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.librealsense.frame;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


/**
 * @author zhangjianghua1
 */
@Slf4j
@Service
public class FileService implements IFileService {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final OSSProperties ossProperties;
    private final CommonOssProperties commonOSSProperties;
    private final IOSSProvider provider;
    private final FileUploadHisMapper fileUploadHisMapper;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public FileService(IOSSProvider provider, CommonOssProperties commonOSSProperties,
                       OSSProperties ossProperties, FileUploadHisMapper fileUploadHisMapper,
                       ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.provider = provider;
        this.commonOSSProperties = commonOSSProperties;
        this.ossProperties = ossProperties;
        this.fileUploadHisMapper = fileUploadHisMapper;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public String generateFilePath(String bucket, String group, String fileName){
        if(null == commonOSSProperties.getBucketPolicy()){
            return fileName;
        }
        StringBuffer filePath = new StringBuffer();
        List<CommonOssProperties.BucketPolicy> bucketPolicies = commonOSSProperties.getBucketPolicy().get(bucket);
        if(!CollectionUtils.isEmpty(bucketPolicies)) {
            for(CommonOssProperties.BucketPolicy bucketPolicy : bucketPolicies){
                String access = bucketPolicy.getAccess().toUpperCase();
                String prefix = bucketPolicy.getPrefix();
                if(StringUtils.isEmpty(access) || StringUtils.isEmpty(prefix)){
                    continue;
                }
                if (AccessPrivilegeEnum.READ_WRITE.getValue().equals(access)) {
                    if (OssConstant.PREFIX_ALL.equals(prefix) || prefix.equals(group)) {
                        filePath.append(provider.getObjectUrl(bucket, fileName));
                        break;
                    }
                }
            }
        }

        if(filePath.length()==0){
            filePath.append(fileName);
        }
        return filePath.toString();
    }


    @Override
    public List<FileVo> uploadFile(FileDto fileDto, Long uid) {
        List<FileVo> files = new ArrayList<>();
        List<FileUploadHisPo> list = new ArrayList<>();
        try {
            for(int i=0; i<fileDto.getFile().length; i++){
                MultipartFile file = fileDto.getFile()[i];
                String originalFilename = file.getOriginalFilename();
                //????????????????????????????????????
                String suffixName = originalFilename.substring(originalFilename.lastIndexOf("." ) + 1).toLowerCase();
                //??????????????????
                String fileName = fileDto.getGroup() + "/" + UUID.randomUUID() + "." + suffixName;
                FileVo fileVo = new FileVo();
                if(commonOSSProperties.getMediaSuffix().contains(suffixName)){
                    //????????????????????????????????????????????????
                    List<CompletableFuture<String>> futures = new ArrayList<>();
                    InputStream thumbnailInputStream = file.getInputStream();
                    CompletableFuture<String> updateThumbnailCompletableFuture = CompletableFuture.supplyAsync(() -> uploadThumbnail(thumbnailInputStream, fileDto.getBucket(), fileDto.getGroup(), uid, list, originalFilename,fileName), threadPoolTaskExecutor.getThreadPoolExecutor());
                    InputStream fileInputStream = file.getInputStream();
                    CompletableFuture<String> updateFileCompletableFuture = CompletableFuture.supplyAsync(() -> updateFile(fileInputStream, fileDto.getBucket(), fileDto.getGroup(), fileName), threadPoolTaskExecutor.getThreadPoolExecutor());
                    futures.add(updateThumbnailCompletableFuture);
                    futures.add(updateFileCompletableFuture);

                    CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
                    CompletableFuture<List<String>> listCompletableFuture = allDoneFuture.thenApply(v -> futures.stream().map(CompletableFuture::join)
                            .collect(Collectors.toList()));
                    List<String> values = listCompletableFuture.get();
                    for (String filePath : values) {
                        if(filePath.lastIndexOf(OssConstant.THUMBNAIL_SUFFIX)==-1){
                            fileVo.setRelativePath(filePath);
                        }else{
                            fileVo.setThumbnailPath(filePath);
                        }
                    }
                } else{
                    String fileName1 = fileDto.getGroup() + "/" + UUID.randomUUID() + "." + suffixName;
                    String filePath = updateFile(file.getInputStream(), fileDto.getBucket(), fileDto.getGroup(), fileName1);
                    fileVo.setRelativePath(filePath);
                }
                files.add(fileVo);

                // ??????????????????????????????????????????
                getFileUploadInfo(fileDto.getBucket(),fileDto.getGroup(), list, file.getSize(),
                        (short)1,originalFilename, suffixName, fileName, fileVo.getRelativePath(),uid);
            }
        } catch (OSSException e) {
            log.error(e.getMessage(), e);
            throw new AdamException(RespCode.USER_UPLOAD_ERROR, e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new AdamException(RespCode.USER_UPLOAD_ERROR);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new AdamException(RespCode.USER_UPLOAD_ERROR);
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
            throw new AdamException(RespCode.USER_UPLOAD_ERROR);
        }

        // ??????????????????????????????
        saveFileUploadHistory(list);

        return files;
    }

    /**
     * ??????????????????????????????????????????
     */
    private void getFileUploadInfo(String bucket,String group, List<FileUploadHisPo> list, Long size, short sourceType,
                                   String originalFilename, String suffixName, String fileName, String filePath,Long uid) {
        FileUploadHisPo po = FileUploadHisPo.builder()
                .bucket(bucket)
                .fileName(fileName)
                .fileSize(size)
                .fileSource(sourceType)
                .fileSuffix(suffixName)
                .fileGroup(group)
                .originName(originalFilename)
                .relativePath(filePath)
                .uploadTime(formatter.format(LocalDateTime.now()))
                .userId(uid)
                .build();
        list.add(po);
    }

    @Override
    public List<FileVo> uploadFile(NfsFileDto fileDto, Long uid) {
        List<FileVo> files = new ArrayList<>();
        List<String> fileUrls = fileDto.getFiles();
        List<FileUploadHisPo> list = new ArrayList<>();
        try {
            for(int i=0; i < fileUrls.size(); i++){
                //??????????????????
                String url1 = fileUrls.get(i);
                String suffixName = getNfsFileSuffix(url1);
                //??????????????????
                String uuid = UUID.randomUUID().toString();
                String fileName = fileDto.getGroup() + "/" + uuid + suffixName;
                URL url = new URL(url1);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
                String localFile= writeNfsToLocalFile(bufferedInputStream);
                File file = new File(localFile);
                try {
                    if(file.exists()) {
                        provider.putObject(fileDto.getBucket(), fileName, localFile);
                        String filePath = generateFilePath(fileDto.getBucket(), fileDto.getGroup(), fileName);
                        FileVo fileVo = new FileVo();
                        fileVo.setRelativePath(filePath);
                        files.add(fileVo);

                        // ??????????????????????????????????????????
                        int index = url1.lastIndexOf("/");
                        String originalFilename = url1;
                        if (index > 0){
                            originalFilename = url1.substring(index +1);
                        }
                        getFileUploadInfo(fileDto.getBucket(),fileDto.getGroup(), list,file.length() ,
                                (short)2,originalFilename, suffixName, fileName, filePath,uid);

                    } else{
                        throw new AdamException(RespCode.SERVER_CONNECT_TIMEOOUT, "????????????????????????");
                    }
                }finally {
                    if(file.exists()) {
                        if (file.isFile()) {
                            file.delete();
                        }
                    }
                }
            }
        } catch (OSSException e) {
            log.error(e.getMessage(), e);
            throw new AdamException(RespCode.USER_UPLOAD_ERROR, e.getMessage());
        } catch (IOException e) {
            String errorMessage = "????????????????????????????????????"+e.getMessage();
            log.error(errorMessage, e);
            throw new AdamException(RespCode.USER_UPLOAD_ERROR, errorMessage);
        } catch (AdamException e) {
            log.error(e.getMessage(), e);
            throw new AdamException(e.getCode(), e.getMessage());
        }

        // ??????????????????????????????
        saveFileUploadHistory(list);

        return files;
    }

    /**
     * ??????????????????????????????
     * @param bufferedInputStream
     */
    private String writeNfsToLocalFile(BufferedInputStream bufferedInputStream) throws IOException {
        String tmpFile = commonOSSProperties.getTmpFilePath() + "/" + UUID.randomUUID();
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(commonOSSProperties.getTmpFilePath());
            if (!file.exists()){
                file.mkdirs();
            }
            fileOutputStream = new FileOutputStream(tmpFile);
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = bufferedInputStream.read(buf)) != -1) {
                fileOutputStream.write(buf, 0, len);
            }
        } finally {
            if(null != fileOutputStream){
                fileOutputStream.close();
            }
            bufferedInputStream.close();
        }
        return tmpFile;
    }

    /**
     * ???????????????????????????????????????
     * ?????? .png, .xlsx
     * @param url
     * @return
     */
    private String getNfsFileSuffix(String url){
        String[] split = url.split("/");
        if(split.length>3){
            String s = split[split.length - 1];
            if(s.lastIndexOf(".")==-1) {
                return s;
            } else{
                return s.substring(s.lastIndexOf("."));
            }
        }
        return "";
    }
    @Override
    public String obtainFileEphemeralUrl(String bucket, String fileName) {
        return this.obtainFileEphemeralUrl(bucket, fileName, null);
    }

    @Override
    public String obtainFileEphemeralUrl(String bucket, String fileName, Integer expires) {
        if(expires == null){
            return provider.presignedGetObject(bucket, fileName, 60);
        }
        return provider.presignedGetObject(bucket, fileName, expires);
    }

    /**
     * ????????????
     * @param fileInputStream
     * @param bucket
     * @param group
     * @param fileName
     * @return
     */
    private String updateFile(InputStream fileInputStream, String bucket, String group, String fileName){
        //??????????????????
       // String fileName = group + "/" + UUID.randomUUID() + "." + suffix;
        provider.putObject(bucket, fileName, fileInputStream);
        return generateFilePath(bucket, group, fileName);
    }

    /**
     * ??????????????????????????????
     * @param list
     */
    private void saveFileUploadHistory(List<FileUploadHisPo> list) {
        if (!list.isEmpty()) {
            if (list.size() == 1) {
                log.info("?????????????????????????????? -- {}", list.get(0));
                fileUploadHisMapper.save(list.get(0));
            } else {
                fileUploadHisMapper.saveBatch(list);
                log.info("????????????{}??????????????????????????? -- {}", list.size(), list.toString());
            }
        }
    }

    /**
     * ???????????????????????????
     * @param fileInputStream
     * @param group
     * @param bucket
     * @return
     */
    private String uploadThumbnail(InputStream fileInputStream, String bucket, String group,Long uid, List<FileUploadHisPo> list, String originalFilename,String videoName){
        FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(fileInputStream);
        //????????????
        InputStream thumbnailInputStream = null ;
        ByteArrayInputStream bos = null;
        try{
            fFmpegFrameGrabber.start();
            //?????????????????????
            int ftp = fFmpegFrameGrabber.getLengthInFrames();
            //???????????????
            int frameNum = ftp > PicUtil.start_frame ? PicUtil.start_frame : ftp/2;
            fFmpegFrameGrabber.setFrameNumber(frameNum);
            //??????????????????????????????
            Frame frame = fFmpegFrameGrabber.grabImage();
            thumbnailInputStream = frameToInputStream(frame, OssConstant.THUMBNAIL_SUFFIX);

            //??????????????????
            //String fileName = group + "/" + UUID.randomUUID() + "." + OssConstant.THUMBNAIL_SUFFIX;
            //ByteArrayInputStream bos = PicUtil.dealPictureBlackBoundary(thumbnailInputStream);

           // BufferedImage bufferedImage = PicUtil.checkBlackPicture(thumbnailInputStream,true);

            //?????????
             bos = PicUtil.dealPictureBlackBoundary(thumbnailInputStream,true);
            //????????????????????????
            while (Objects.isNull(bos)){
                frameNum += PicUtil.frame_size;
                if (frameNum > ftp){
                    fFmpegFrameGrabber.setFrameNumber(frameNum-PicUtil.frame_size);
                    //??????????????????????????????
                    frame = fFmpegFrameGrabber.grabImage();
                    thumbnailInputStream = frameToInputStream(frame, OssConstant.THUMBNAIL_SUFFIX);
                    //???????????????????????????????????????????????????
                    bos = PicUtil.dealPictureBlackBoundary(thumbnailInputStream,false);
                    break;
                }
                 fFmpegFrameGrabber.setFrameNumber(frameNum);
                //??????????????????????????????
                 frame = fFmpegFrameGrabber.grabImage();
                 thumbnailInputStream = frameToInputStream(frame, OssConstant.THUMBNAIL_SUFFIX);
                //?????????
                bos = PicUtil.dealPictureBlackBoundary(thumbnailInputStream,true);
            }
            log.info("???????????????{}?????????", frameNum);
            long available = bos.available();
            //??????????????????
            String fileName = videoName.substring(0,videoName.lastIndexOf("."))+"-s"+"."+OssConstant.THUMBNAIL_SUFFIX;
            provider.putObject(bucket, fileName, bos);
            String generateFilePath = generateFilePath(bucket, group, fileName);

            // ??????????????????????????????????????????
            getFileUploadInfo(bucket, group, list, available,
                    (short) 1, originalFilename, OssConstant.THUMBNAIL_SUFFIX, fileName, generateFilePath, uid);

            return generateFilePath;
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
           PicUtil.close(bos);
           PicUtil.close(thumbnailInputStream);
        }
        return "";
    }

    /**
     * ?????????????????????????????????
     * @param frame
     * @param suffixName
     * @return
     * @throws IOException
     */
    private static InputStream frameToInputStream(Frame frame, String suffixName) throws IOException {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, suffixName, os);
        return new ByteArrayInputStream(os.toByteArray());
    }


    private static BufferedImage frameToBufferedImage(Frame frame) {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }
}
