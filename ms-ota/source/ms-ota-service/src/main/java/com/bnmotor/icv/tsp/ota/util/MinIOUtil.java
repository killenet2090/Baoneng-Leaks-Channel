package com.bnmotor.icv.tsp.ota.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.bnmotor.icv.adam.data.oss.exception.OSSException;
import com.bnmotor.icv.adam.data.oss.model.GetObjectRequest;
import com.bnmotor.icv.adam.data.oss.model.OSSObject;

import io.minio.ComposeSource;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName:  MinIOUtil
 * @Description:   专门处理文件上传内部MiniIO工具类
 * @author: xuxiaochang1
 * @date: 2020/10/30 9:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class MinIOUtil implements InitializingBean {

    @Value("${adam.oss.endpoint}")
    private String url;

    @Value("${adam.oss.access_key}")
    private String accessKey;

    @Value("${adam.oss.secret_key}")
    private String secretKey;

    /*public static String chunkBucKet;
    public static String fileBucKet;
*/
    private static MinioClient minioClient;
    
    /**
     * 排序
     */
    public final static boolean SORT = true;
    /**
     * 不排序
     */
    public final static boolean NOT_SORT = false;
    /**
     * 默认过期时间(分钟)
     */
    private final static Integer DEFAULT_EXPIRY = 60;

    private final static Integer DEFAULT_MAX_EXPIPY_SECONDS = 604800;

    @Override
    public void afterPropertiesSet() throws Exception {

        minioClient = new MinioClient(url, accessKey, secretKey);
        //minioClient.builder().endpoint(url).credentials(accessKey,secretKey).build();
		//方便管理分片文件，则单独创建一个分片文件的存储桶
        /*if (!isBucketExist(chunkBucKet)){
            createBucket(chunkBucKet);
        }*/
    }

    /**
     * 初始化MinIo对象
     * 非此工具类请勿使用该方法
     */
    @PostConstruct
    public void init(){
        /*fileBucKet = CommonConstant.BUCKET_OTA_NAME;
        chunkBucKet = fileBucKet;*/
    }

    /**
     * 存储桶是否存在
     * @param bucketName 存储桶名称
     * @return true/false
     */
    @SneakyThrows
    public static boolean isBucketExist(String bucketName){
        return minioClient.bucketExists(bucketName);
    }

    /**
     * 创建存储桶
     * @param bucketName 存储桶名称
     * @return true/false
     */
    @SneakyThrows
    public static boolean createBucket(String bucketName){
        minioClient.makeBucket(bucketName);
        return true;
    }

    /**
     * 获取访问对象的外链地址
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param expiry 过期时间(分钟) 最大为7天 超过7天则默认最大值
     * @return viewUrl
     */
    @SneakyThrows
    public static String getObjectUrl(String bucketName,String objectName,Integer expiry){
        /*if(Objects.isNull(bucketName)){
            bucketName = fileBucKet;
        }*/
        expiry = expiryHandle(expiry);
        return minioClient.getPresignedObjectUrl(Method.GET, bucketName, objectName, expiry, null);
    }
    
    public static String getObjectUrl(String bucketName, String objectName) {
    	try
        {
            return minioClient.getObjectUrl(bucketName, objectName);
        }
        catch (Exception ex)
        {
            log.error("获取对象链接失败，bucketName:{},objectName:{}", bucketName, objectName, ex);
            throw new OSSException("获取对象链接失败", ex);
        }
    }

    /**
     * 创建上传文件对象的外链
     * @param bucketName 存储桶名称
     * @param objectName 欲上传文件对象的名称
     * @param expiry 过期时间(分钟) 最大为7天 超过7天则默认最大值
     * @return uploadUrl
     */
    @SneakyThrows
    public static String createUploadUrl(String bucketName,String objectName,Integer expiry){
        expiry = expiryHandle(expiry);
        return minioClient.getPresignedObjectUrl(Method.PUT, bucketName, objectName, expiry, null);
    }

    /**
     * 创建上传文件对象的外链
     * @param bucketName 存储桶名称
     * @param objectName 欲上传文件对象的名称
     * @return uploadUrl
     */
    public static String createUploadUrl(String bucketName,String objectName){
        return createUploadUrl(bucketName,objectName,DEFAULT_EXPIRY);
    }

    /**
     * 批量创建分片上传外链
     * @param bucketName 存储桶名称
     * @param objectMd5 欲上传分片文件主文件的MD5
     * @param chunkCount 分片数量
     * @return uploadChunkUrls
     */
    public static List<String> createUploadChunkUrlList(String bucketName,String objectMd5,Integer chunkCount){
        /*if (null == bucketName){
            bucketName = chunkBucKet;
        }*/
        if (null == objectMd5){
            return null;
        }
        objectMd5 += "/";
        if(null == chunkCount || 0 == chunkCount){
            return null;
        }
        List<String> urlList = new ArrayList<>(chunkCount);
        for (int i = 1; i <= chunkCount; i++){
            String objectName = objectMd5 + i + ".chunk";
            urlList.add(createUploadUrl(bucketName,objectName,DEFAULT_EXPIRY));
        }
        return urlList;
    }

    /**
     * 创建指定序号的分片文件上传外链
     * @param bucketName 存储桶名称
     * @param objectMd5 欲上传分片文件主文件的MD5
     * @param partNumber 分片序号
     * @return uploadChunkUrl
     */
    public static String createUploadChunkUrl(String bucketName,String objectMd5,Integer partNumber){
       /* if (null == bucketName){
            bucketName = chunkBucKet;
        }*/
        if (null == objectMd5){
            return null;
        }
        objectMd5 += "/" + partNumber + ".chunk";
        return createUploadUrl(bucketName,objectMd5,DEFAULT_EXPIRY);
    }
    
    @Deprecated
    public static String createUploadChunkUrl(String bucketName, String objectName, String partNumber){
         if (null == objectName){
             return null;
         }
         objectName += "/" + partNumber + ".chunk";
         return createUploadUrl(bucketName, objectName, DEFAULT_EXPIRY);
     }

    /**
     * 获取对象文件名称列表
     * @param bucketName 存储桶名称
     * @param prefix 对象名称前缀
     * @param sort 是否排序(升序)
     * @return objectNames
     */
    @SneakyThrows
    public static List<String> listObjectNames(String bucketName,String prefix,Boolean sort){
        Iterable<Result<Item>> chunks = minioClient.listObjects(bucketName, prefix);
        List<String> chunkPaths = new ArrayList<>();
        for (Result<Item> item : chunks){
            chunkPaths.add(item.get().objectName());
        }
        if (sort){
            return chunkPaths.stream().distinct().collect(Collectors.toList());
        }
        return chunkPaths;
    }
    
    public static List<String> chunkSort(List<String> chunks) {
    	// chunk like this 'pkg/d41d8cd98f00b204e9800998ecf8427e/123.chunk
    	Comparator<String> comparator = (item1, item2) -> {
	   		String chunk1 = FilenameUtils.getBaseName(item1); // 123
	   		String chunk2 = FilenameUtils.getBaseName(item2);
	        int partNum1 = NumberUtils.toInt(chunk1);
	        int partNum2 = NumberUtils.toInt(chunk2);
	        return partNum1 - partNum2;
        };
//    	 chunks = chunks.stream().sorted(comparator).collect(Collectors.toList());
    	 Collections.sort(chunks, comparator);
    	 return chunks;
    }
    
    /**
     * 获取对象文件名称列表
     * @param bucketName 存储桶名称
     * @param prefix 对象名称前缀
     * @return objectNames
     */
    public static List<String> listObjectNames(String bucketName,String prefix){
        return listObjectNames(bucketName, prefix, NOT_SORT);
    }

    /**
     * 获取分片文件名称列表
     * @param bucketName 存储桶名称
     * @param objectMd5 对象Md5
     * @return objectChunkNames
     */
    public static List<String> listChunkObjectNames(String bucketName,String objectMd5){
        /*if (null == bucketName){
            bucketName = chunkBucKet;
        }*/
        if (null == objectMd5){
            return null;
        }
        return listObjectNames(bucketName,objectMd5,SORT);
    }

    /**
     * 获取分片名称地址HashMap key=分片序号 value=分片文件地址
     * @param objectMd5 对象Md5
     * @return objectChunkNameMap
     */
    public static Map<Integer,String> mapChunkObjectNames(String chunkBucKet, String objectName){
        if (null == objectName){
            return null;
        }
        List<String> chunkPaths = listObjectNames(chunkBucKet, objectName);
        if (null == chunkPaths || chunkPaths.size() == 0){
            return null;
        }
        Map<Integer,String> chunkMap = new HashMap<>(chunkPaths.size());
        for (String chunkName : chunkPaths) {
            log.info("chunkName={}", chunkName);
            Integer partNumber = NumberUtils.toInt(chunkName.substring(chunkName.lastIndexOf("/") + 1,chunkName.lastIndexOf(".")));
            chunkMap.put(partNumber,chunkName);
        }
        return chunkMap;
    }

    /**
     * 合并分片文件成对象文件
     * @param chunkBucKetName 分片文件所在存储桶名称
     * @param composeBucketName 合并后的对象文件存储的存储桶名称
     * @param chunkNames 分片文件名称集合
     * @param objectName 合并后的对象文件名称
     * @return true/false
     */
    @SneakyThrows
    public static boolean composeObject(String chunkBucKetName, String composeBucketName,List<String> chunkNames,String objectName){
        /*if (null == chunkBucKetName){
            chunkBucKetName = chunkBucKet;
        }*/
        List<ComposeSource> sourceObjectList = new ArrayList<>(chunkNames.size());
        for (String chunk : chunkNames){
            sourceObjectList.add(new ComposeSource(chunkBucKetName, chunk));
        }
        minioClient.composeObject(composeBucketName, objectName, sourceObjectList, null, null);
        return true;
    }

    /*public static boolean composeObject(String chunkBucKet, String fileBucKet, List<String> chunkNames, String objectName){
        return composeObject(chunkBucKet, fileBucKet, chunkNames, objectName);
    }*/

    /**
     * 将分钟数转换为秒数
     * @param expiry 过期时间(分钟数)
     * @return expiry
     */
    private static int expiryHandle(Integer expiry){
        expiry = expiry * 60;
        if (expiry > DEFAULT_MAX_EXPIPY_SECONDS){
            return DEFAULT_MAX_EXPIPY_SECONDS;
        }
        return expiry;
    }


    /**
     * 获取对象
     * @param bucketName
     * @param objectName
     * @return
     */
    public static OSSObject getObject(String bucketName, String objectName) {
        Assert.hasText(bucketName, "bucketName不能为空");
        Assert.hasText(objectName, "objectName不能为空");
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().objectName(objectName).bucketName(bucketName).build();
        return getObject(getObjectRequest);
    }

    /**
     * 获取对象
     * @param getObjectRequest
     * @return
     */
    public static OSSObject getObject(GetObjectRequest getObjectRequest) {
        Assert.notNull(getObjectRequest, "getObjectRequest不能为空");
        Assert.notNull(getObjectRequest.getBucketName(), "bucketName不能为空");
        Assert.notNull(getObjectRequest.getObjectName(), "objectName");
        OSSObject ossObject = null;

        try {
            InputStream inputStream = minioClient.getObject(getObjectRequest.getBucketName(), getObjectRequest.getObjectName());
            if (inputStream != null) {
                ossObject = OSSObject.builder().bucketName(getObjectRequest.getBucketName()).objectName(getObjectRequest.getObjectName()).content(inputStream).build();
            }
            return ossObject;
        } catch (Exception var4) {
            log.error("获取对象失败，BucketName:{},ObjectName:{}", new Object[]{getObjectRequest.getBucketName(), getObjectRequest.getObjectName(), var4});
            //throw new OSSException("获取对象失败", var4);
        }
        return null;
    }

    /**
     * 获取元数据
     * @param bucketName
     * @param objectName
     * @return
     */
    public static ObjectStat statObject(String bucketName, String objectName)  {
        try {
            return minioClient.statObject(bucketName, objectName);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidBucketNameException | InvalidKeyException| InvalidResponseException| IOException| NoSuchAlgorithmException| XmlParserException e) {
            log.error("获取对象元数据异常", e);
        }
        return null;
    }

    /**
     * 移除数据
     * @param bucketName
     * @param objectName
     */
    public static void removeObject(String bucketName, String objectName)  {
        try {
            minioClient.removeObject(bucketName, objectName);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidBucketNameException | InvalidKeyException| InvalidResponseException| IOException| NoSuchAlgorithmException| XmlParserException e) {
            log.error("移除对象数据异常", e);
        }
    }
}
