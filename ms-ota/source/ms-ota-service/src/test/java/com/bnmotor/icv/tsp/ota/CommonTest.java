package com.bnmotor.icv.tsp.ota;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.adam.sdk.ota.proto.OtaMessageProtoToJava;
import com.bnmotor.icv.adam.sdk.ota.up.OtaUpDownloadProcess;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.*;
import com.bnmotor.icv.tsp.ota.event.OtaUpgradeMessageBase;
import com.bnmotor.icv.tsp.ota.event.OtaUpgradeOtherMessage;
import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.model.req.FileUploadReq;
import com.bnmotor.icv.tsp.ota.model.req.device.DeviceSyncInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaCarDeviceItemInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaCarInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaDeviceTreeNodeDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyFirmwareListDto;
import com.bnmotor.icv.tsp.ota.model.resp.app.InstalledProcessBodyVo;
import com.bnmotor.icv.tsp.ota.model.resp.app.InstalledResultBodyVo;
import com.bnmotor.icv.tsp.ota.model.resp.app.InstalledResultVo;
import com.bnmotor.icv.tsp.ota.service.impl.FotaVersionCheckVerifyServiceImpl;
import com.bnmotor.icv.tsp.ota.util.FileVerifyUtil;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @ClassName:
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/6/12 17:16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
public class CommonTest {

    @Test
    public void test1(){
        String str1 = "{\"body\":{\"immediateDownload\":1,\"taskId\":1304009252303482881,\"transId\":1308232728675667970,\"verifyResult\":1,\"verifySource\":3},\"businessId\":1,\"businessType\":3}";
        List<String> lists = Lists.newArrayList("aabc", "bbccd", "ddde", null);
        String find = lists.stream().filter(item -> Objects.isNull(item)).findFirst().orElse(null);
        System.out.println(find);
    }

    @Test
    public void verifyTest() throws IOException {
        String filePath = "C:\\Users\\xxc\\Desktop\\ms-ota-sync.yaml";
        filePath = "C:\\Users\\xxc\\Downloads\\winrar-x64-590scp.exe";
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        /*System.out.println(FileVerifyUtil.caculate(bytes, FileVerifyUtil.FileVerifyEnum.MD5));
        System.out.println(FileVerifyUtil.caculate(bytes, FileVerifyUtil.FileVerifyEnum.SHA1));*/
    }

    @Test
    public void classMatchTest() throws IOException {
        FileUploadReq parameter = new FileUploadReq();
        System.out.println(parameter.getClass());
        System.out.println(BasePo.class.isAssignableFrom(parameter.getClass()));
    }

    @AllArgsConstructor
    @Data
    private static class Student implements Comparable{
        private int id;
        private String name;
        private int age;

        @Override
        public int compareTo(@NotNull Object o) {
            return this.id  >= ((Student) o).getId() ? 1 : -1;
        }
    }

    @Test
    public void compareTest(){
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "Mahesh", 12));
        list.add(new Student(2, "Suresh", 15));
        list.add(new Student(3, "Nilesh", 10));

        System.out.println("---Natural Sorting by Name---");
        List<Student> slist = list.stream().sorted().collect(Collectors.toList());
        slist.forEach(e -> System.out.println("Id:" + e.getId() + ", Name: " + e.getName() + ", Age:" + e.getAge()));

        System.out.println("---Natural Sorting by Name in reverse order---");
        //slist = list.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        slist.forEach(e -> System.out.println("Id:" + e.getId() + ", Name: " + e.getName() + ", Age:" + e.getAge()));

        System.out.println("---Sorting using Comparator by Age---");
        slist = list.stream().sorted(Comparator.comparing(Student::getAge)).collect(Collectors.toList());
        slist.forEach(e -> System.out.println("Id:" + e.getId() + ", Name: " + e.getName() + ", Age:" + e.getAge()));

        System.out.println("---Sorting using Comparator by Age with reverse order---");
        slist = list.stream().sorted(Comparator.comparing(Student::getAge).reversed()).collect(Collectors.toList());
        slist.forEach(e -> System.out.println("Id:" + e.getId() + ", Name: " + e.getName() + ", Age:" + e.getAge()));
    }

    @Test
    public void testCollection(){
        List<FotaFirmwareVersionPo> fotaFirmwareVersionPos = Lists.newArrayList();
        //fotaFirmwareVersionDos.add(new FotaFirmwareVersionDo());
        //
        MyAssertUtil.isTrue(MyCollectionUtil.isEmpty(fotaFirmwareVersionPos), OTARespCodeEnum.FIRMWARE_WITHVERSION_NOTALLOWED_DEL);
        System.out.println("addsd");
    }

    @Test
    public void testP(){
        int update = 1;
        int insert = 1;
        System.out.println(update < 1 & insert < 1);
    }


    @Test
    public void testM(){
        List<FotaFirmwareVersionPo> fotaFirmwareVersionPos = Lists.newArrayList();
        /*FotaFirmwareVersionDo item1 = new FotaFirmwareVersionDo();
        item1.setId(100L);
        item1.setCreateTime(new Date(item1.getId()));
        FotaFirmwareVersionDo item2 = new FotaFirmwareVersionDo();
        item2.setId(1000L);
        item2.setCreateTime(new Date(item2.getId()));
        FotaFirmwareVersionDo item3 = new FotaFirmwareVersionDo();
        item3.setId(10000L);
        item3.setCreateTime(new Date(item3.getId()));
        FotaFirmwareVersionDo item4 = new FotaFirmwareVersionDo();
        item4.setId(10000000L);
        item4.setCreateTime(new Date(item4.getId()));*/
        /*fotaFirmwareVersionDos.add(item1);
        fotaFirmwareVersionDos.add(item2);
        fotaFirmwareVersionDos.add(item3);
        fotaFirmwareVersionDos.add(item4);*/
        if(MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPos)){

            FotaFirmwareVersionPo itemP1 = fotaFirmwareVersionPos.stream().max((o1, o2) -> o1.getCreateTime().compareTo(o2.getCreateTime())).get();
            System.out.println(itemP1.toString());

            fotaFirmwareVersionPos = fotaFirmwareVersionPos.stream().sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())).collect(Collectors.toList());
            FotaFirmwareVersionPo itemP = fotaFirmwareVersionPos.get(0);
            System.out.println(itemP.toString());
        }
    }

    @Test
    public void testM1(){
        Set<Long> set1 = Sets.newHashSet(100L, 1001L, 102L, 201L, 202L);
        Set<Long> set2 = Sets.newHashSet(100L, 1001L, 102L, 103L, 104L);
        Sets.SetView<Long> diff1 = Sets.difference(set1, set2);
        diff1.forEach(item -> log.info(item+""));
        Sets.SetView<Long> diff2 = Sets.difference(set2, set1);
        diff2.forEach(item -> log.info(item+""));
    }

    /*@Test
    public void testM2(){
        VersionCheck req = new VersionCheck();
        req.setBusinessId(10086L);
        req.setVin("1000*");
        req.setBusinessType(1);
        req.setTimestamp(100086L);
        req.setBody(null);
        MyValidationUtil.validate(req);
    }*/

    /*@Test
    public void testM3(){
        OtaMessage otaMessage = new OtaMessage();
        otaMessage.setBusinessType(1);
        otaMessage.setVersion(10);
        otaMessage.setTimestamp(System.currentTimeMillis());
        otaMessage.setOtaDownDownloadVerifyResult(null);
        otaMessage.setOtaUpVersionCheck(new OtaUpVersionCheck());
        otaMessage.getOtaUpVersionCheck().setEcuModules(Lists.newArrayList());

        EcuModule ecuModule = new EcuModule();
        ecuModule.setBackupFirmwareVersion("ddddddddddddw");
        otaMessage.getOtaUpVersionCheck().getEcuModules().add(ecuModule);
        VersionCheck rersionCheckReq = OtaMessageMapper.INSTANCE.otaMessage2VersionCheckReq(otaMessage);
        log.info("rersionCheckReq={}", rersionCheckReq.toString());
        System.out.println(rersionCheckReq.toString());
    }*/

    /*@Test
    public void testM4(){
        OtaMessage otaMessage = new OtaMessage();
        otaMessage.setOtaUpUpgradeVerifyFromHu(new OtaUpUpgradeVerifyFromHu());
        otaMessage.getOtaUpUpgradeVerifyFromHu().setTransId(1000086L);
        BaseUp req = OtaMessageMapper.INSTANCE.otaMessage2DownloadVerifyReq(otaMessage);
        req.setVin("abc");
        DownloadVerifyReq newReq = (DownloadVerifyReq)req;
        System.out.println(newReq.toString());
    }*/

    /*@Test
    public void testM5(){
        String jsonStr = "{\"otaMessageHeader\":{\"platformTime\":1596089671084,\"terminalTime\":1596089671000,\"vin\":\"LLNC6ADB5JA047666\",\"version\":1,\"businessSerialNum\":123,\"protocolSerialNum\":378732544,\"encryType\":0,\"encodeType\":0,\"msgCompressType\":0,\"checkNumType\":0,\"isReSend\":0,\"deviceType\":0,\"ackSerialNum\":0,\"msgTime\":0,\"msgBusinessType\":0,\"msgLength\":0,\"checkNumLength\":0,\"headOffset\":0}," +
                "\"body\":{\"version\":1,\"timestamp\":1596089671000,\"business_id\":123,\"business_type\":1," +
                "\"ota_up_version_check\":{\"ecuModules\":[{\"firmware_code\":\"xxc_btc_002\",\"firmware_version\":\"v1.1\",\"backup_firmware_version\":\"v1.2\",\"boot_version\":\"4\",\"hardware_version\":\"5\",\"ecu_id\":\"6\",\"ecu_seq\":\"7\",\"ecu_system_supplier\":\"8\",\"sys_name\":\"9\"}]}}}";
        OtaProtocol otaProtocol = null;
        try {
            otaProtocol = JsonUtil.toObject(jsonStr, OtaProtocol.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        OtaMessage otaMessage = new OtaMessage();
        otaMessage.setOtaUpUpgradeVerifyFromHu(new OtaUpUpgradeVerifyFromHu());
        otaMessage.getOtaUpUpgradeVerifyFromHu().setTransId(1000086L);
        BaseUp req = OtaMessageMapper.INSTANCE.otaMessage2DownloadVerifyReq(otaMessage);
        req.setVin("abc");
        DownloadVerifyReq newReq = (DownloadVerifyReq)req;
        System.out.println(null != otaProtocol?otaProtocol.toString():"");
    }*/


    @Test
    public void testM6(){

        /*OtaProtocol upOtaProtocol = new OtaProtocol();
        OtaMessageHeader otaMessageHeader = new OtaMessageHeader();
        otaMessageHeader.setMsgBusinessType(4);
        otaMessageHeader.setDeviceID("100239399d9");
        otaMessageHeader.setVersion(2);
        upOtaProtocol.setOtaMessageHeader(otaMessageHeader);

        VersionCheck req = new VersionCheck();
        req.setBusinessId(10000086L);
        req.setVin("100239399d9");

        VersionCheckResp resp = new VersionCheckResp();
        resp.setBusinessId(req.getBusinessId());
        resp.setBusinessType(BusinessTypeEnum.VERSION_CHECK_RESP.getType());
        resp.setVin(req.getVin());
        resp.setTimestamp(System.currentTimeMillis());


        VersionCheckRespBody body = new VersionCheckRespBody();
        body.setDisclaimer("ddddddddddd");
        body.setDownloadTips("eeeeeeeeee");
        body.setTaskTips("eeeeeeeeeeeeds");
        body.setUpgradeMode(1);
        body.setEstimatedDownloadPackageSize(3000);
        //TODO
        body.setEstimatedDownloadTime(300);

        List<TaskPreCondition> taskPreConditions = Lists.newArrayList();
        IntStream.rangeClosed(0,10).forEach(item -> {
            TaskPreCondition taskPreCondition = new TaskPreCondition();
            taskPreCondition.setCondCode("ddddd");
            taskPreCondition.setCondValue("dddddd");
            taskPreConditions.add(taskPreCondition);
        });
        body.setTaskPreConditions(taskPreConditions);

        List<EcuFirmwareVersionInfo> ecuFirmwareVersionInfos = Lists.newArrayList();
        IntStream.rangeClosed(0,10).forEach(item -> {
            EcuFirmwareVersionInfo ecuFirmwareVersionInfo = new EcuFirmwareVersionInfo();
            ecuFirmwareVersionInfo.setChipInfo("dddddddddd");
            ecuFirmwareVersionInfo.setFirmwareCode("10000d00");
            ecuFirmwareVersionInfos.add(ecuFirmwareVersionInfo);
        });

        body.setEcuFirmwareVersionInfos(ecuFirmwareVersionInfos);
        body.setCheckResult(Enums.ZeroOrOneEnum.ONE.getValue());
        body.setTransId(10000086L);
        *//*body.set(ecuUpgradeInfo);*//*
        resp.setBody(body);

        OtaMessage respOtaMessage = OtaMessageMapper.INSTANCE.versionCheckResp2OtaMessage(resp);
        OtaProtocol downOtaProtocol = new OtaProtocol();
        downOtaProtocol.setOtaMessageHeader(OtaMessageMapper.INSTANCE.otaMessageHeader2OtaMessageHeader(upOtaProtocol.getOtaMessageHeader()));
        downOtaProtocol.setBody(respOtaMessage);
        System.out.println(Objects.toString(resp));
        System.out.println(ToStringBuilder.reflectionToString(resp, SHORT_PREFIX_STYLE.SIMPLE_STYLE));
        System.out.println(ToStringBuilder.reflectionToString(resp, MULTI_LINE_STYLE));*/
    }

    @Test
    public void testM7(){
        Long id = IdWorker.getId();
        System.out.println(id);
        System.out.println(Integer.parseInt("1234444444"));

        System.out.println(Integer.MAX_VALUE);
    }

    @Test
    public void testM8(){
        Long s1 = null;
        System.out.println(new Date(s1));
    }

    @Test
    public void testM9(){
        String s0 = "{\n" +
                "       \"trans_id\": 1293442656758341633,\n" +
                "\"download_percent_rate\":30,\n" +
                "\t  \"status\": 2,\n" +
                "\t  \"progressed_size\": 46538,\n" +
                "      \"download_process_details\": [\n" +
                "        {\n" +
                "          \"pkg_id\": 66,\n" +
                "\t  \"status\": 2,\n" +
                "\t  \"progressed_size\": 46538\n" +
                "\t  \n" +
                "        }\n" +
                "      ]\n" +
                "    }";

        String s1 = "{\n" +
                "       \"transId\": 1293442656758341633,\n" +
                "       \"transId1\": 1293442656758341633,\n" +
                "       \"transId2\": 1293442656758341633,\n" +
                "\t  \"status\": 2,\n" +
                "      \"abc\": [\n" +
                "        {\n" +
                "          \"pkgId\": 66,\n" +
                "\t  \"status\": 2,\n" +
                "\t  \"progressedSize\": 46538\n" +
                "\t  \n" +
                "        }\n" +
                "      ]\n" +
                "    }";

        String s2 = "{\n" +
               /* "       \"transId\": 1293442656758341633,\n" +
                "       \"transId1\": 1293442656758341633,\n" +
                "       \"transId2\": 1293442656758341633,\n" +
                "\t  \"status\": 2,\n" +*/
                "      \"ecuModules\": [\n" +
                "        {\n" +
                "          \"firmware_code\": \"xxc_btc_001\",\n" +
                "          \"firmware_version\": \"v1.1\",\n" +
                "          \"backup_firmware_version\": \"v1.1\",\n" +
                "          \"boot_version\": \"4\",\n" +
                "          \"hardware_version\": \"5\",\n" +
                "          \"ecu_id\": 6,\n" +
                "          \"ecu_seq\": \"7\",\n" +
                "          \"ecu_system_supplier\": \"8\",\n" +
                "          \"sys_name\": \"9\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }";

        try {
            OtaUpDownloadProcess o0 = (OtaUpDownloadProcess)JsonUtil.toObject(s0, OtaUpDownloadProcess.class);
            MyObject o1 = (MyObject)JsonUtil.toObject(s1, MyObject.class);
            /*MyObject2 o3 = (MyObject2)JsonUtil.toObject(s1, MyObject2.class);
            MyObject3 o4 = (MyObject3)JsonUtil.toObject(s1, MyObject3.class);
            OtaUpVersionCheck o2 = (OtaUpVersionCheck)JsonUtil.toObject(s2, OtaUpVersionCheck.class);*/
            System.out.println(o0.toString());
            System.out.println(o1.toString());
            /*System.out.println(o3.toString());
            System.out.println(o4.toString());
            System.out.println(o2.toString());*/

            /*OtaUpVersionCheck o2 = (OtaUpVersionCheck)JsonUtil.toObject(s2, OtaUpVersionCheck.class);
            System.out.println(o2.toString());*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*@Data
    static class MyObject implements Serializable{
        private static final long serialVersionUID = 674513643491237894L;
        *//*private Long transId;
        private Long transId1;
        private Long transId2;
        private Integer status;*//*
        *//*@JsonProperty("download_process_details")*//*
        private List<MyObject1> downloadProcessDetails;
    }

    @Data
    static class MyObject1 implements Serializable {
        private static final long serialVersionUID = -7509222357584568583L;
        private Long pkgId;
        private Integer spendTime;
        private Integer status;
        private Long progressedSize;
        private Integer accumulateNum;
    }*/

    @Data
    static class MyObject2 extends OtaUpDownloadProcess {
        private static final long serialVersionUID = -7509222357584568583L;
        /*@JsonProperty("trans_id")
        private Long transId;*/
        private Long transId1;
        private Long transId2;
        private Integer status;
    }

    @Data
    static class MyObject3 extends OtaUpDownloadProcess {
        private static final long serialVersionUID = -7509222357584568583L;
    }

    @Test
    public void testM10(){
        Integer s1 = null;
        System.out.println(1 == s1);
    }

    @Test
    public void testM11() throws JsonProcessingException {
        InstalledResultVo installedResultVo = new InstalledResultVo();
        installedResultVo.setBusinessId(10L);
        installedResultVo.setBody(new InstalledResultBodyVo());
        installedResultVo.getBody().setMsg("100000");
        System.out.println("info=" + JsonUtil.toJson(installedResultVo));
    }


    @Test
    public void testM12() throws IOException {
        byte[] readAllBytes = Files.readAllBytes(new File("C:\\Users\\xuxiaochang1.BAONENGMOTOR\\Desktop\\PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png").toPath());
        System.out.println(readAllBytes.length);
    }

    @Test
    public void testM16() throws IOException {
        Runnable r = ()->{
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("====【特殊逻辑】===此处逻辑应该执行1次=========");
        };

        Supplier<ResponseEntity> responseEntitySupplier = () -> {
            System.out.println("====【最终逻辑】===此处逻辑应该执行1次=========");
            return ResponseEntity.ok("eeeee");
        };

        runUploadInner(r, responseEntitySupplier, true);
    }

    private ResponseEntity runUploadInner(Runnable r, Supplier<ResponseEntity> responseEntitySupplier, boolean execute){
        //一些代码
        System.out.println("====【通用逻辑】===此处逻辑应该执行2次=========");
        if(execute){
            r.run();
            return runUploadInner(r,  responseEntitySupplier,false);
        }
        return responseEntitySupplier.get();
    }

    @Test
    public void testM14() throws IOException {
        List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathPos = Lists.newArrayList();
        FotaFirmwareVersionPathPo f1 = new FotaFirmwareVersionPathPo();
        f1.setStartFirmwareVerId(100L);
        f1.setTargetFirmwareVerId(100L);
        fotaFirmwareVersionPathPos.add(f1);
        FotaFirmwareVersionPathPo f2 = new FotaFirmwareVersionPathPo();
        f2.setStartFirmwareVerId(58L);
        f2.setTargetFirmwareVerId(58L);
        fotaFirmwareVersionPathPos.add(f2);

        FotaFirmwareVersionPathPo f3 = new FotaFirmwareVersionPathPo();
        f3.setStartFirmwareVerId(59L);
        f3.setTargetFirmwareVerId(59L);
        fotaFirmwareVersionPathPos.add(f3);

        if(MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPathPos)) {
            fotaFirmwareVersionPathPos = fotaFirmwareVersionPathPos.stream().filter(item -> !item.getStartFirmwareVerId().equals(item.getTargetFirmwareVerId())).collect(Collectors.toList());
        }
        if(MyCollectionUtil.isEmpty(fotaFirmwareVersionPathPos)){
            System.out.println("abcdef");
        }else{
            System.out.println("ok");
        }
    }

    @Test
    public void testM15() throws IOException {
        List<DeviceTreeNodePo> deviceTreeNodePos = Lists.newArrayList();
        IntStream.range(0, 10).forEach(item -> {
            DeviceTreeNodePo d1 = new DeviceTreeNodePo();
            d1.setOrderNum(item +1);
            deviceTreeNodePos.add(d1);
        });

        //List<DeviceTreeNodePo> deviceTreeNodePos = deviceTreeNodeService.getByLevel(DeviceTreeNodeLevelEnum.BRAND.getLevel());
        int orderNum = 0;
        if(MyCollectionUtil.isNotEmpty(deviceTreeNodePos)){
            orderNum = deviceTreeNodePos.stream().max((item1, item2) -> item1.getOrderNum() - item2.getOrderNum()).get().getOrderNum();
        }
        System.out.println("orderNum="+orderNum);

        FotaDeviceTreeNodeDto fotaDeviceTreeNodeDto = new FotaDeviceTreeNodeDto();
        fotaDeviceTreeNodeDto.setBrand("abc");
        fotaDeviceTreeNodeDto.setBrandCode("cba");
        test(fotaDeviceTreeNodeDto, DeviceTreeNodeLevelEnum.CONF);

    }

    private void test(final FotaDeviceTreeNodeDto fotaDeviceTreeNodeDto, DeviceTreeNodeLevelEnum deviceTreeNodeLevelEnum){
        List<DeviceTreeNodeLevelEnum> deviceTreeNodeLevelEnums = EnumSet.allOf(DeviceTreeNodeLevelEnum.class).stream().filter(item -> item.getLevel() <= deviceTreeNodeLevelEnum.getLevel()).sorted((item1, item2) -> item1.getLevel() - item2.getLevel()).collect(Collectors.toList());
        for (DeviceTreeNodeLevelEnum item : deviceTreeNodeLevelEnums) {
            String methodName = "get" + (item.getCode().substring(0, 1).toUpperCase()) + item.getCode().substring(1, item.getCode().length());
            log.info("methodName={}", methodName);
            String value = (String) ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(FotaDeviceTreeNodeDto.class, methodName), fotaDeviceTreeNodeDto);
            System.out.println("value=" + value);
        }
    }

    @Test
    public void testM13() throws IOException {
        String msg = "{\"otaMessageHeader\":{\"platformTime\":1602642339275,\"terminalTime\":1602671138952,\"vin\":\"BCM20200928000001\",\"version\":0,\"businessSerialNum\":2,\"protocolSerialNum\":2,\"encryType\":0,\"encodeType\":0,\"msgCompressType\":0,\"checkNumType\":0,\"isReSend\":0,\"deviceType\":0,\"ackSerialNum\":0,\"msgTime\":0,\"msgBusinessType\":0,\"msgLength\":0,\"checkNumLength\":0,\"headOffset\":0},\"body\":{\"timestamp\":0,\"business_id\":2,\"business_type\":106,\"ota_down_config_check_response\":{\"ecu_configs\":[{\"ecu_id\":\"F517RRM000000001\",\"ecu_name\":\"车身控制模块\",\"ecu_did\":\"1101\",\"ecu_swid\":\"bcm-test-full-90\"},{\"ecu_id\":\"F517RRM000000001\",\"ecu_name\":\"21321\",\"ecu_did\":\"214\",\"ecu_swid\":\"tbox-test-2322\"}]}}}";
        OtaProtocol protocol = JsonUtil.toObject(msg, OtaProtocol.class);

        OtaMessageProtoToJava.OtaMessage otaMessage = parseObjectToProtoByte(protocol);
        System.out.println(JsonUtil.toJson(protocol));
    }

    private OtaMessageProtoToJava.OtaMessage parseObjectToProtoByte(OtaProtocol otaProtocol) throws JsonProcessingException, InvalidProtocolBufferException {
        OtaMessageProtoToJava.OtaMessage.Builder otaMessageProto = OtaMessageProtoToJava.OtaMessage.newBuilder();
        System.out.println(JsonUtil.toJson(otaProtocol));
        try {
            JsonFormat.parser().merge(JsonUtil.toJson(otaProtocol.getBody()),otaMessageProto);
        }catch (Exception e){
            e.printStackTrace();
        }
        return otaMessageProto.build();
    }

    @Test
    public void testM202() throws IOException {
        String path = "C:\\Users\\xuxiaochang1.BAONENGMOTOR\\Downloads\\CentOS-7-x86_64-Minimal-2003.iso";
        path = "C:\\Users\\xuxiaochang1.BAONENGMOTOR\\Documents\\WXWork\\1688853172914725\\Cache\\File\\2020-12\\GX16_VB.00.03_OTA.bin";
        //8a28b6556b4b2b2838960e8f02ec3f5c7edc91adea7077d6e284987de94cd68b
        //8A28B6556B4B2B2838960E8F02EC3F5C7EDC91ADEA7077D6E284987DE94CD68B
        //byte[] bytes = Files.readAllBytes(Paths.get(path));

        System.out.println(FileVerifyUtil.sha256(Files.newInputStream(Paths.get(path))));

        String hashCode1 = "8A28B6556B4B2B2838960E8F02EC3F5C7EDC91ADEA7077D6E284987DE94CD68B";
        String hashCode2 = "8a28b6556b4b2b2838960e8f02ec3f5c7edc91adea7077d6e284987de94cd68b";

        System.out.println(hashCode1.equals(hashCode2));
    }


    @Test
    public void testM20() throws IOException {
        String path = "D:\\software\\Navicat.Premium.15.rar";
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

        for(int i = 0;i<fileNum;i++){
            byte[] bytes1 = Arrays.copyOfRange(bytes, (int)fileSize * i, (int)fileSize * (i+1));
            Files.write(Paths.get("testLargeFilePart"  + (i + 1)), bytes1);
        }
    }

    @Test
    public void testM21(){
        //校验actionParams参数格式
        try {
            JsonUtil.toObject(null, HashMap.class);
        } catch (IOException e) {
            log.error("{}:{}", RespCode.USER_PARAM_TYPE_ERROR.getDescription(), e.getMessage());
            throw new AdamException(RespCode.USER_PARAM_TYPE_ERROR);
        }
    }

    @Test
    public void testM22() {
        List<FotaFirmwareVersionPo> fotaFirmwareVersionPos = null;
        System.out.println(fotaFirmwareVersionPos.stream().max(Comparator.comparing(BasePo::getCreateTime)).get());
    }

    @SneakyThrows
    @Test
    public void testM23(){
        String licenseNo = "dseweewe";
        System.out.println("{\"licensePlateNumber\":\""+licenseNo+"\"}");

        FotaCarInfoDto fotaCarInfoDto = new FotaCarInfoDto();
        fotaCarInfoDto.setBrand("xxc-brand");
        fotaCarInfoDto.setBrandCode("xxc-brand-code");
        fotaCarInfoDto.setSeries("xxc-series");
        fotaCarInfoDto.setSeriesCode("xxc-series-code");
        fotaCarInfoDto.setModel("xxc");
        fotaCarInfoDto.setModelCode("xxc-code");
        fotaCarInfoDto.setYear("xxc");
        fotaCarInfoDto.setYearCode("xxc-code");
        fotaCarInfoDto.setConf("xxc");
        fotaCarInfoDto.setConfCode("xxc-code");

        //
        fotaCarInfoDto.setVehDevices(IntStream.rangeClosed(1, 3).mapToObj(item -> {
            FotaCarDeviceItemInfoDto fotaCarDeviceItemInfoDto = new FotaCarDeviceItemInfoDto();
            fotaCarDeviceItemInfoDto.setComponentCode("xxc-component-code" + item);
            fotaCarDeviceItemInfoDto.setComponentModel("xxc-model-code" + item);
            fotaCarDeviceItemInfoDto.setComponentName("xxc-component-name" + item);
            return fotaCarDeviceItemInfoDto;
        }).collect(Collectors.toList()));

        DeviceSyncInfoDto deviceSyncInfoDto = new DeviceSyncInfoDto();
        deviceSyncInfoDto.setBusinessType(1);
        deviceSyncInfoDto.setType(1);
        deviceSyncInfoDto.setData(fotaCarInfoDto);

        try {
            String jsonStr = JsonUtil.toJson(deviceSyncInfoDto);
            System.out.println(jsonStr);


            DeviceSyncInfoDto newDeviceSyncInfoDto = JsonUtil.toObject(jsonStr, DeviceSyncInfoDto.class);
            FotaCarInfoDto newFotaCarInfoDto = JsonUtil.toObject(JsonUtil.toJson(newDeviceSyncInfoDto.getData()), FotaCarInfoDto.class);
            System.out.println(newFotaCarInfoDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testM25(){
        /*EnumSet.allOf(DeviceTreeNodeLevelEnum.class).stream().filter(item1 -> !item1.equals(DeviceTreeNodeLevelEnum.ECU)).sorted(Comparator.comparingInt(DeviceTreeNodeLevelEnum::getLevel)).forEach(enumItem -> {
            System.out.println(enumItem.getLevel() + enumItem.getName());
        });*/
    }

    @Test
    public void testM26(){

        Optional<Integer> testOptional = Optional.ofNullable(null);
        testOptional.ifPresentOrElse(System.out::println, ()-> {System.out.println("do nothing");});

        /*System.out.println(enumItem.getLevel() + enumItem.getName());
        });*/
    }


    @Test
    public void testM27(){
        List<Long> ids = Lists.newArrayList(1L,3L,4L);
        AtomicInteger result = new AtomicInteger(0);
        Optional.ofNullable(ids).ifPresent(item -> result.set(item.size()));
        System.out.println(result.get());

        String nodeIdPath = "/eiweiwe/eweiie/3999323";
        if(nodeIdPath.startsWith("/")){
            nodeIdPath = nodeIdPath.substring(1);
        }
        System.out.println(nodeIdPath);

        FotaStrategyDto fotaStrategyDto = new FotaStrategyDto();
        FotaStrategyFirmwareListDto fotaStrategyFirmwareListDto1 = new FotaStrategyFirmwareListDto();
        fotaStrategyFirmwareListDto1.setGroupSeq(null);
        FotaStrategyFirmwareListDto fotaStrategyFirmwareListDto2 = new FotaStrategyFirmwareListDto();
        fotaStrategyFirmwareListDto2.setGroupSeq(100);
        FotaStrategyFirmwareListDto fotaStrategyFirmwareListDto3 = new FotaStrategyFirmwareListDto();
        fotaStrategyFirmwareListDto3.setGroupSeq(null);
        FotaStrategyFirmwareListDto fotaStrategyFirmwareListDto4 = new FotaStrategyFirmwareListDto();
        fotaStrategyFirmwareListDto4.setGroupSeq(3);
        fotaStrategyDto.setFotaStrategyFirmwareListDtos(Lists.newArrayList());
        fotaStrategyDto.getFotaStrategyFirmwareListDtos().add(fotaStrategyFirmwareListDto1);
        //fotaStrategyDto.getFotaStrategyFirmwareListDtos().add(fotaStrategyFirmwareListDto2);
        fotaStrategyDto.getFotaStrategyFirmwareListDtos().add(fotaStrategyFirmwareListDto3);
        //fotaStrategyDto.getFotaStrategyFirmwareListDtos().add(fotaStrategyFirmwareListDto4);

        FotaStrategyFirmwareListDto maxFotaStrategyFirmwareListDto = fotaStrategyDto.getFotaStrategyFirmwareListDtos().stream().max(Comparator.comparingInt((FotaStrategyFirmwareListDto item) -> {
            if (Objects.isNull(item.getGroupSeq())) {
                return 0;
            } else {
                return item.getGroupSeq();
            }
        })).get();
        /*Integer maxGroupSeq = Objects.isNull(maxFotaStrategyFirmwareListDto)?0:maxFotaStrategyFirmwareListDto.getGroupSeq();
        System.out.println(maxGroupSeq);*/

        int estimatedUpgradeTime = 30;
        int total = 21;
        int secondsPerEcu = (int)Math.ceil(estimatedUpgradeTime * 60 /total);
        int installedCurrentIndex = 3;
        int installPercentRate = 35;
        double percentRate = (double)installPercentRate / 100;
        int installedRemainTime = estimatedUpgradeTime * 60 - (installedCurrentIndex - 1) * secondsPerEcu - (int)Math.floor(secondsPerEcu * percentRate);
        System.out.println(installedRemainTime);
    }

    @Test
    public void testM28(){
        DeviceTreeNodePo deviceTreeNodePo = new DeviceTreeNodePo();
        deviceTreeNodePo.setNodeName("abc_name");
        FotaStrategyPo fotaStrategyPo = new FotaStrategyPo();
        DeviceTreeNodeLevelEnum deviceTreeNodeLevelEnum = DeviceTreeNodeLevelEnum.CONF;
        String getNameMethodName = "set" + (deviceTreeNodeLevelEnum.getCode().substring(0, 1).toUpperCase()) + deviceTreeNodeLevelEnum.getCode().substring(1, deviceTreeNodeLevelEnum.getCode().length());
        //设置对应属性
        Class<?>[] params = {String.class};
        ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(FotaStrategyPo.class, getNameMethodName, params), fotaStrategyPo, deviceTreeNodePo.getNodeName());
        System.out.println(fotaStrategyPo);


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.insert(0, "a10/");
        stringBuilder.insert(0, "a9/");
        stringBuilder.insert(0, "a8/");
        stringBuilder.insert(0, "a7/");
        stringBuilder.insert(0, "a6/");

        System.out.println(stringBuilder.toString());
    }

    @Test
    public void testM29(){
        Map<String, Object> map = Maps.newHashMap();
        /**
         * @ApiModelProperty(value = "唯一标识")
         *     private String id;
         *
         *     @ApiModelProperty(value = "任务名字")
         *     private String taskName;
         */
        map.put("id", "adbc");
        map.put("taskName", "taskName");

/*        PlanObjectListDetailVo planObjectListDetailVo = new PlanObjectListDetailVo();
                MyMapUtil.mapToBean(map, planObjectListDetailVo);
        System.out.println(planObjectListDetailVo);*/

        InstalledResultVo installedResultVo = new InstalledResultVo();

        InstalledResultBodyVo installedResultBodyVo = new InstalledResultBodyVo();
        installedResultBodyVo.setInstalledProcessType(AppEnums.InstalledProcessType4AppEnum.INSTALLED_RESULT.getType());

        installedResultVo.setBusinessType(AppEnums.AppResponseTypeEnum.INSTALLED_VERIFIED_RESPONSE.getType());
        installedResultVo.setBody(installedResultBodyVo);

        OtaUpgradeOtherMessage otaUpgradeOtherMessage = OtaUpgradeOtherMessage.builder().t(installedResultVo).reqId(1000L).build();
        OtaUpgradeMessageBase otaUpgradeMessageBase = OtaUpgradeMessageBase.builder().data(otaUpgradeOtherMessage).vin("100086L").otaUpgradeMessageType(Enums.OtaUpgradeMessageTypeEnum.OTHER.getType()).messageCenterMsgTypeEnum(null).build();
        log.info("otaUpgradeMessageBase|{}", otaUpgradeMessageBase.toString());
    }

    @Test
    public void M201(){
        //补充升级季度中间进度信息
        FotaVersionCheckVerifyServiceImpl.Temp4InstalledProcess t = FotaVersionCheckVerifyServiceImpl.Temp4InstalledProcess.builder().build();

        InstalledProcessBodyVo installedProcessBodyVo = new InstalledProcessBodyVo();
        t.setT(installedProcessBodyVo);
        System.out.println(((InstalledProcessBodyVo)t.getT()));
    }

    @Test
    public void M202(){
        System.out.println(Instant.now().toEpochMilli() +  86400000L);
        System.out.println(System.currentTimeMillis());
        System.out.println(Instant.now().toEpochMilli());
        System.out.println(System.currentTimeMillis());

        BigDecimal a1 = new BigDecimal(100083233);
        BigDecimal a2 = new BigDecimal(1000832332);

        for (RoundingMode roundingMode : EnumSet.allOf(RoundingMode.class)) {
            try {
                int f1 = (int)(a1.divide(a2, 2, roundingMode).floatValue()*100);
                System.out.println(f1);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Test
    public void M203() throws InvalidProtocolBufferException, DecoderException {
        //String hexstring = "00010004004C48583537323134323637303030303134000000000015C70000000000000177D6BFB643000400400010F0DAFEB5FD2E18C6F3DB8106200442171001208CA95428960738B8C3FEB5FD2E40F0DAFEB5FD2E82F10414088180AD9AED92A3F8121081C09CEAB8DBEAF712";
        //String hexstring = "10F0DAFEB5FD2E18C6F3DB8106200442171001208CA95428960738B8C3FEB5FD2E40F0DAFEB5FD2E82F10414088180AD9AED92A3F8121081C09CEAB8DBEAF712";
        String hexstring = "080110B7E3C4C5FD2E18C6C0E0DDADC5D7B4022001";
        OtaMessageProtoToJava.OtaMessage otaMessage = OtaMessageProtoToJava.OtaMessage.parseFrom(Hex.decodeHex(hexstring));
        //OtaMessageProtoToJava.OtaMessage otaMessage = OtaMessageProtoToJava.OtaMessage.parseFrom(destByte);
        System.out.println(otaMessage);

        FotaFirmwarePkgPo fotaFirmwarePkgPo = new FotaFirmwarePkgPo();
        fotaFirmwarePkgPo.setBuildPkgStatus(2);
        boolean buildFail = Objects.isNull(fotaFirmwarePkgPo) || Objects.isNull(fotaFirmwarePkgPo.getBuildPkgStatus()) || Enums.BuildStatusEnum.TYPE_FINISH.getType() != fotaFirmwarePkgPo.getBuildPkgStatus() || StringUtils.isEmpty(fotaFirmwarePkgPo.getReleasePkgFileDownloadUrl());
    }

    @Test
    public void M204(){
        AtomicLong estimateUpgradeTime = new AtomicLong(1093823L);
        System.out.println(estimateUpgradeTime.get() * CommonConstant.FOTA_FIRMWARE_VERSION_PKG_ESTIMATE_UPGRADE_TIME_SCALE);
        int intEstimateUpgradeTime = Double.valueOf(Math.ceil(estimateUpgradeTime.get() * CommonConstant.FOTA_FIRMWARE_VERSION_PKG_ESTIMATE_UPGRADE_TIME_SCALE)).intValue();
        System.out.println(intEstimateUpgradeTime);

        boolean checkFail = TBoxRespCodeEnum.downloadFail(104);
        System.out.println(checkFail);
        System.out.println(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_CHECK_FAIL_4DOWNLOAD.getCode());
    }
}
