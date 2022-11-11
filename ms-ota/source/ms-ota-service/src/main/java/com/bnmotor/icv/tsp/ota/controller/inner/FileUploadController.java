package com.bnmotor.icv.tsp.ota.controller.inner;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.aop.aspect.WrapBasePo;
import com.bnmotor.icv.tsp.ota.model.req.FileUploadReq;
import com.bnmotor.icv.tsp.ota.model.req.upload.FileSha256Dto;
import com.bnmotor.icv.tsp.ota.model.req.upload.UploadDto;
import com.bnmotor.icv.tsp.ota.model.resp.FileUploadVo;
import com.bnmotor.icv.tsp.ota.model.resp.upload.UploadVo;
import com.bnmotor.icv.tsp.ota.service.IFotaFileUploadRecordService;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName: FileUploadController
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/6/5 13:13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@RestController
@Api(value="文件上传",tags={"文件上传"})
@RequestMapping(value = "/v1/upload")
@Slf4j
public class FileUploadController extends AbstractController {
    @Autowired
    private IFotaFileUploadRecordService fotaFileUploadRecordService;

    @PostMapping(value = "upload")
    @ApiOperation(value="文件上传", notes="文件上传", response = FileUploadVo.class)
    @ResponseBody
    @WrapBasePo
    public ResponseEntity upload(@RequestPart("file") MultipartFile data, FileUploadReq fileUploadReq){
        return RestResponse.ok(fotaFileUploadRecordService.upload(data, fileUploadReq));
    }

    @PostMapping("uploadSliceFile")
    @ApiOperation(value="文件分片上传", notes="文件分片上传", response = UploadVo.class)
    @WrapBasePo
    public ResponseEntity uploadSliceFile(@Nullable @RequestPart("file") MultipartFile data, UploadDto uploadDto){
        log.info("fileName={}", data.getOriginalFilename());
        return RestResponse.ok(fotaFileUploadRecordService.uploadSliceFile(data, uploadDto));
    }

    @PostMapping("fileMerge")
    @ApiOperation(value="文件合并", notes="文件合并", response = UploadVo.class)
    public ResponseEntity fileMerge(@RequestBody UploadDto uploadDto){
        return RestResponse.ok(fotaFileUploadRecordService.fileMerge(uploadDto));
    }

    @PostMapping("fileCheck")
    @ApiOperation(value="检查文件是否上传完成", notes="检查文件是否上传完成", response = UploadVo.class)
    public ResponseEntity fileCheck(@RequestBody FileSha256Dto fileSha256Dto){
        return RestResponse.ok(fotaFileUploadRecordService.fileCheck(fileSha256Dto.getFileSha256()));
    }

    @GetMapping("testUploadSliceFile")
    @ApiOperation(value="测试内部分片上传", notes="测试内部分片上传", response = UploadVo.class)
    public ResponseEntity testUploadSliceFile(@RequestParam String path) throws IOException, InterruptedException {
        //String path = "D:\\software\\Navicat.Premium.15.rar";
        log.info("path={}", path);
        File file = new File(path);
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        Files.list(Paths.get("")).filter(item -> item.getFileName().startsWith("testLargeFile")).forEach(item -> {
            try {
                Files.delete(Paths.get(item.toUri()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        long fileSize = 1024 * 1024 * 10;
        int fileNum = (int) (bytes.length / fileSize);
        fileNum += bytes.length % fileSize > 0 ? 1 :0;
        System.out.println("fileNum=" + fileNum);

        String[] partNames = new String[fileNum];
        for(int i = 0;i<fileNum;i++){
            int begin = (int)fileSize * i;
            int end = (int)fileSize * (i+1);
            end = end > bytes.length ? bytes.length : end;
            byte[] bytes1 = Arrays.copyOfRange(bytes, begin, end);
            partNames[i] = (i + 1) + ".chunk";
            Files.write(Paths.get(partNames[i]), bytes1);
        }
        //return partNames;

        UploadDto uploadDto = new UploadDto();
        uploadDto.setFileSize((long)bytes.length);
        uploadDto.setChunkCount(partNames.length);
        String fileSha256 = Hashing.sha256().hashBytes(bytes).toString();
        uploadDto.setFileSha256(fileSha256);
        uploadDto.setPartNumber(1);
        uploadDto.setFileName(file.getName());
        //uploadDto.setCreateBy(CommonConstant.USER_ID_SYSTEM);
        /*uploadDto.setPhase(1);*/
        //uploadDto.set

        final ExecutorService executorService = Executors.newCachedThreadPool();
        ResponseEntity<RestResponse<String>> returnResponseEntity = null;
        for(int index = 0;index<10;index++) {

            Runnable r1 = () -> {

                File firstPartFile = new File(partNames[0]);
                ResponseEntity<RestResponse<UploadVo>> tempResponseEntity = null;
                try {
                    tempResponseEntity = uploadSliceFileInner(new FileInputStream(firstPartFile), uploadDto);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("tempResponseEntity=" + tempResponseEntity.toString());
                ResponseEntity<RestResponse<UploadVo>> responseEntity = tempResponseEntity;
                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    UploadVo uploadVo = responseEntity.getBody().getRespData();
                    //成功
                    if (uploadVo.getResult() == 0) {
                        System.out.println("大文件上传已完成");
                    } else {

                        List<Future<Boolean>> futures = Lists.newArrayList();
                        for (Integer partNum : uploadVo.getPartNums()) {
                            Callable<Boolean> r = () -> {
                                log.info("-------------ThreadName={}", Thread.currentThread().getName());
                                UploadDto newUploadDto = new UploadDto();
                                newUploadDto.setChunkCount(partNames.length);
                                newUploadDto.setPartNumber(partNum);
                                /*newUploadDto.setPhase(2);*/
                                newUploadDto.setFileSize(uploadDto.getFileSize());
                                newUploadDto.setFileSha256(fileSha256);
                                newUploadDto.setFileName(file.getName());
                                File itemFile = new File(partNames[partNum - 1]);
                                try {
                                    ResponseEntity<RestResponse<UploadVo>> nweResponseEntity = uploadSliceFileInner(new FileInputStream(itemFile), newUploadDto);
                                    if (responseEntity.getStatusCode() == HttpStatus.OK) {
                                        UploadVo newUploadVo = responseEntity.getBody().getRespData();
                                        if (newUploadVo.getResult() == 0) {
                                            System.out.println("大文件上传已完成");
                                            //return RestResponse.ok("success");
                                            return Boolean.TRUE;
                                        }
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                return Boolean.FALSE;
                            };

                            Future<Boolean> future = executorService.submit(r);
                            futures.add(future);
                        }

                        boolean notContinue = false;
                        while (true && !notContinue) {
                            for (Future<Boolean> future : futures) {
                                try {
                                    Boolean result = future.get(1, TimeUnit.SECONDS);
                                    if (result) {
                                        //returnResponseEntity = RestResponse.ok("success");
                                        notContinue = true;
                                        break;
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (TimeoutException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            };

            executorService.submit(r1);

        }
        return RestResponse.ok("success");
    }

    /**
     * 内部测试方法
     * @param data
     * @param uploadDto
     * @return
     */
    private ResponseEntity uploadSliceFileInner(InputStream data, UploadDto uploadDto){
        return RestResponse.ok(fotaFileUploadRecordService.uploadSliceFile(data, uploadDto));
    }
}
