package com.bnmotor.icv.tsp.ota;

import com.bnmotor.icv.tsp.ota.controller.inner.FileUploadController;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaCarDeviceItemInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaCarInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaDeviceComponentInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaDeviceTreeNodeDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaFirmwareDto;
import com.bnmotor.icv.tsp.ota.service.IDeviceTreeNodeDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaDeviceSyncService;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwareService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class FotaDeviceSyncTest
{
    @Autowired
    private IFotaDeviceSyncService fotaDeviceSyncService;

    @Autowired
    private FileUploadController fileUploadController;

    @Autowired
    private IFotaFirmwareService fotaFirmwareService;

    @Autowired
    private IDeviceTreeNodeDbService deviceTreeNodeDbService;

    @Test
    public void test(){
        /*FotaDeviceTreeNodeDto fotaDeviceTreeNodeDto = new FotaDeviceTreeNodeDto();
        fotaDeviceTreeNodeDto.setTreeLevel(0);
        fotaDeviceTreeNodeDto.setBrand("xxc");
        fotaDeviceTreeNodeDto.setBrandCode("xxc");
        //fotaDeviceSyncService.addFotaDeviceTreeNode(fotaDeviceTreeNodeDto);

        FotaDeviceTreeNodeDto fotaDeviceTreeNodeDto1 = new FotaDeviceTreeNodeDto();
        BeanUtils.copyProperties(fotaDeviceTreeNodeDto, fotaDeviceTreeNodeDto1);
        fotaDeviceTreeNodeDto1.setTreeLevel(1);
        fotaDeviceTreeNodeDto1.setSeries("xxc-series-01");
        fotaDeviceTreeNodeDto1.setSeriesCode("xxc-series-01");
        fotaDeviceSyncService.addFotaDeviceTreeNode(fotaDeviceTreeNodeDto1);


        FotaDeviceTreeNodeDto fotaDeviceTreeNodeDto2 = new FotaDeviceTreeNodeDto();
        BeanUtils.copyProperties(fotaDeviceTreeNodeDto1, fotaDeviceTreeNodeDto2);
        fotaDeviceTreeNodeDto2.setTreeLevel(2);
        fotaDeviceTreeNodeDto2.setModel("xxc-model-01");
        fotaDeviceTreeNodeDto2.setModelCode("xxc-model-01");
        fotaDeviceSyncService.addFotaDeviceTreeNode(fotaDeviceTreeNodeDto2);

        FotaDeviceTreeNodeDto fotaDeviceTreeNodeDto3 = new FotaDeviceTreeNodeDto();
        BeanUtils.copyProperties(fotaDeviceTreeNodeDto2, fotaDeviceTreeNodeDto3);
        fotaDeviceTreeNodeDto3.setTreeLevel(3);
        fotaDeviceTreeNodeDto3.setYear("xxc-year-01");
        fotaDeviceTreeNodeDto3.setYearCode("xxc-year-01");
        fotaDeviceSyncService.addFotaDeviceTreeNode(fotaDeviceTreeNodeDto3);

        FotaDeviceTreeNodeDto fotaDeviceTreeNodeDto4 = new FotaDeviceTreeNodeDto();
        BeanUtils.copyProperties(fotaDeviceTreeNodeDto3, fotaDeviceTreeNodeDto4);
        fotaDeviceTreeNodeDto4.setTreeLevel(4);
        fotaDeviceTreeNodeDto4.setConf("xxc-conf-01");
        fotaDeviceTreeNodeDto4.setConfCode("xxc-conf-01");
        fotaDeviceSyncService.addFotaDeviceTreeNode(fotaDeviceTreeNodeDto4);*/
    }


    @Test
    public void testAddFotaCar(){
        FotaDeviceTreeNodeDto fotaDeviceTreeNodeDto = new FotaDeviceTreeNodeDto();
        fotaDeviceTreeNodeDto.setTreeLevel(0);
        fotaDeviceTreeNodeDto.setBrand("xxc");
        fotaDeviceTreeNodeDto.setBrandCode("xxc");
        //fotaDeviceSyncService.addFotaDeviceTreeNode(fotaDeviceTreeNodeDto);

        FotaCarInfoDto fotaCarInfoDto = new FotaCarInfoDto();
        fotaCarInfoDto.setBrand("xxc");
        fotaCarInfoDto.setBrandCode("xxc");
        fotaCarInfoDto.setSeries("xxc-series-01");
        fotaCarInfoDto.setSeriesCode("xxc-series-01");
        fotaCarInfoDto.setModel("xxc-model-01");
        fotaCarInfoDto.setModelCode("xxc-model-01");
        fotaCarInfoDto.setYear("xxc-year-01");
        fotaCarInfoDto.setYearCode("xxc-year-01");
        fotaCarInfoDto.setConf("xxc-conf-01");
        fotaCarInfoDto.setConfCode("xxc-conf-01");
        fotaCarInfoDto.setVin("xxc-vin-00001");
        /*fotaDeviceSyncService.syncFotaCar(fotaCarInfoDto, null);*/
    }

    @Test
    public void testSyncFotaDeviceComponentInfo(){
        List<FotaDeviceComponentInfoDto> fotaDeviceComponentInfoDtoList = IntStream.rangeClosed(1, 10).mapToObj(item -> {
            FotaDeviceComponentInfoDto fotaDeviceComponentInfoDto = new FotaDeviceComponentInfoDto();
            fotaDeviceComponentInfoDto.setBrand("xxc");
            fotaDeviceComponentInfoDto.setBrandCode("xxc");
            fotaDeviceComponentInfoDto.setSeries("xxc-series-01");
            fotaDeviceComponentInfoDto.setSeriesCode("xxc-series-01");
            fotaDeviceComponentInfoDto.setModel("xxc-model-01");
            fotaDeviceComponentInfoDto.setModelCode("xxc-model-01");
            fotaDeviceComponentInfoDto.setYear("xxc-year-01");
            fotaDeviceComponentInfoDto.setYearCode("xxc-year-01");
            fotaDeviceComponentInfoDto.setConf("xxc-conf-01");
            fotaDeviceComponentInfoDto.setConfCode("xxc-conf-01");

            fotaDeviceComponentInfoDto.setComponentName("常爷的测试零件" + item);
            fotaDeviceComponentInfoDto.setComponentCode("xxc-component-code-" + item);
            fotaDeviceComponentInfoDto.setComponentModel("xxc-component-model-" + item);
            return fotaDeviceComponentInfoDto;
        }).collect(Collectors.toList());
        try {
            for (FotaDeviceComponentInfoDto fotaDeviceComponentInfoDto : fotaDeviceComponentInfoDtoList) {
                fotaDeviceSyncService.syncFotaDeviceComponentInfo(fotaDeviceComponentInfoDto, null, null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testSyncFotaCar(){
        FotaCarInfoDto fotaCarInfoDto = new FotaCarInfoDto();
        fotaCarInfoDto.setBrand("xxc");
        fotaCarInfoDto.setBrandCode("xxc");
        fotaCarInfoDto.setSeries("xxc-series-01");
        fotaCarInfoDto.setSeriesCode("xxc-series-01");
        fotaCarInfoDto.setModel("xxc-model-01");
        fotaCarInfoDto.setModelCode("xxc-model-01");
        fotaCarInfoDto.setYear("xxc-year-01");
        fotaCarInfoDto.setYearCode("xxc-year-01");
        fotaCarInfoDto.setConf("xxc-conf-01");
        fotaCarInfoDto.setConfCode("xxc-conf-01");
        fotaCarInfoDto.setVin("xxc-vin-test-0001");

        List<FotaCarDeviceItemInfoDto> fotaCarDeviceItemInfoDtoList = IntStream.rangeClosed(1, 10).mapToObj(item -> {
            FotaCarDeviceItemInfoDto fotaCarDeviceItemInfoDto = new FotaCarDeviceItemInfoDto();
            fotaCarDeviceItemInfoDto.setComponentName("常爷的测试零件" + item);
            fotaCarDeviceItemInfoDto.setComponentCode("xxc-component-code-" + item);
            fotaCarDeviceItemInfoDto.setComponentModel("xxc-component-model-" + item);
            return fotaCarDeviceItemInfoDto;
        }).collect(Collectors.toList());
        fotaCarInfoDto.setVehDevices(fotaCarDeviceItemInfoDtoList);
        try {
            fotaDeviceSyncService.syncFotaCar(fotaCarInfoDto, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testAddFotaFirmware(){
        FotaCarInfoDto fotaCarInfoDto = new FotaCarInfoDto();
        fotaCarInfoDto.setBrand("xxc");
        fotaCarInfoDto.setBrandCode("xxc");
        fotaCarInfoDto.setSeries("xxc-series-01");
        fotaCarInfoDto.setSeriesCode("xxc-series-01");
        fotaCarInfoDto.setModel("xxc-model-01");
        fotaCarInfoDto.setModelCode("xxc-model-01");
        fotaCarInfoDto.setYear("xxc-year-01");
        fotaCarInfoDto.setYearCode("xxc-year-01");
        fotaCarInfoDto.setConf("xxc-conf-01");
        fotaCarInfoDto.setConfCode("xxc-conf-01");
        fotaCarInfoDto.setVin("xxc-vin-test-0001");

        String nodeCodePath = "/" + fotaCarInfoDto.getBrandCode() +  "/" + fotaCarInfoDto.getSeriesCode() + "/" + fotaCarInfoDto.getModelCode() + "/" + fotaCarInfoDto.getYearCode() + "/" + fotaCarInfoDto.getConfCode();
        DeviceTreeNodePo deviceTreeNodePo = deviceTreeNodeDbService.getByNodeCodePath(nodeCodePath);
        IntStream.rangeClosed(1, 10).forEach(item -> {
            FotaFirmwareDto fotaFirmwarePo = new FotaFirmwareDto();
            fotaFirmwarePo.setTreeNodeId(deviceTreeNodePo.getId());
            fotaFirmwarePo.setComponentName("常爷的测试零件" + item);
            fotaFirmwarePo.setComponentCode("xxc-component-code-" + item);
            fotaFirmwarePo.setComponentModel("xxc-component-model-" + item);
            fotaFirmwarePo.setFirmwareCode("xxc-firmware-code-" + item);
            fotaFirmwarePo.setFirmwareName("常爷的测试固件"+item);
            /*CommonUtil.wrapBasePo(fotaFirmwarePo, true);*/
            fotaFirmwareService.addFotaFirmware(fotaFirmwarePo);
        });

    }

    /*@Test
    public void testUploadLargeFile() throws IOException {
        String path = "D:\\software\\Navicat.Premium.15.rar";
        File file = new File(path);
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        Files.list(Paths.get("")).filter(item -> item.getFileName().startsWith("testLargeFile")).forEach(item -> {
            try {
                Files.delete(Paths.get(item.toUri()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        long fileSize = 1024 * 1024 * 5;
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
        uploadDto.setChunkCount(partNames.length);
        String fileSha256 = Hashing.sha256().hashBytes(bytes).toString();
        uploadDto.setFileSha256(fileSha256);
        uploadDto.setPartNumber(1);
        uploadDto.setFileName(file.getName());
        uploadDto.setPhase(1);
        //uploadDto.set

        File firstPartFile = new File(partNames[0]);
        ResponseEntity<UploadVo> responseEntity = fileUploadController.uploadSliceFileInner(new FileInputStream(firstPartFile), uploadDto);
        System.out.println("responseEntity="+responseEntity.toString());
        if(responseEntity.getStatusCode() == HttpStatus.OK){
            UploadVo uploadVo = responseEntity.getBody();
            //成功
            if(uploadVo.getResult()==0){
                System.out.println("大文件上传已完成");
            }else{
                for (Integer partNum : uploadVo.getPartNums()) {
                    UploadDto newUploadDto = new UploadDto();
                    newUploadDto.setChunkCount(partNames.length);
                    newUploadDto.setPhase(2);
                    newUploadDto.setFileSha256(fileSha256);
                    newUploadDto.setFileName(file.getName());
                    File itemFile = new File(partNames[partNum-1]);
                    ResponseEntity<UploadVo> nweResponseEntity = fileUploadController.uploadSliceFileInner(new FileInputStream(itemFile), newUploadDto);
                    if(responseEntity.getStatusCode() == HttpStatus.OK) {
                        UploadVo newUploadVo = responseEntity.getBody();
                        if(newUploadVo.getResult()==0) {
                            System.out.println("大文件上传已完成");
                            break;
                        }
                    }
                }
            }
        }
    }*/
}


