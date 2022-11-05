package com.bnmotor.icv.tsp.ble.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.adam.core.utils.HexUtil;
import com.bnmotor.icv.adam.core.utils.StringUtil;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.mapper.BleAuthCompeMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyGeneratorDto;
import com.bnmotor.icv.tsp.ble.model.request.pki.BleEncryptDto;
import com.bnmotor.icv.tsp.ble.model.request.pki.BleSignDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleDeviceSn;
import com.bnmotor.icv.tsp.ble.service.BlePkiService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import com.bnmotor.icv.tsp.ble.util.BleKeyUtil;
import com.bnmotor.icv.tsp.ble.util.ByteUtil;
import com.bnmotor.icv.tsp.ble.util.MD5Builder;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class BlePkiServiceImpl implements BlePkiService {

    @Value("${ble.appliction-name}")
    public String  bleAppName;

    LocalDateTime dt = LocalDateTime.now();
    Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
    Integer timestamp = (int)(now.getTime()/1000);
    @Resource
    private BleCommonFeignService bleCommonFeignService;
    @Resource
    private BleAuthCompeMapper bleAuthCompeMapper;

    @Override
    public  BleKeyGeneratorDto createBleKey(UserBleKeyPo tspBleKeyPo, String authCode, Boolean offLine) {
        BleKeyGeneratorDto build = BleKeyGeneratorDto.builder().build();
        Map<String, String> encrypBleKeyMap = createEncrypBleKey(tspBleKeyPo);
        String encryAppBleKey=encrypBleKeyMap.get("encryAppBleKey");
        String encryTboxBleKey =encrypBleKeyMap.get("encryTboxBleKey");
        String bleKey =encrypBleKeyMap.get("bleKey");
        String bleKeyByte =encrypBleKeyMap.get("bleKey");
        List<Long> longs = bleAuthCompeMapper.queryBleCompeServerId();
        Long bleRight = BleKeyUtil.generateRight(longs);

        ByteBuffer allocate = ByteBuffer.allocate(304);
        allocate.put(ByteUtil.getBytes(Long.valueOf(tspBleKeyPo.getUsedUserId())));
        allocate.put(ByteUtil.getBytes(tspBleKeyPo.getUsedUserMobileDeviceId()));
        Long effectiveTime = (tspBleKeyPo.getBleKeyEffectiveTime().getTime()/1000);
        allocate.put(ByteUtil.getBytes(effectiveTime.intValue()));
        byte[] expireByte;
        if (tspBleKeyPo.getBleKeyExpireTime().compareTo(Long.MAX_VALUE)== Constant.COMPARE_EQUAL_VALUE||
                tspBleKeyPo.getBleKeyExpireTime().compareTo(9999999999000L)== Constant.COMPARE_EQUAL_VALUE){
            expireByte = new byte[4];
            Arrays.fill(expireByte, (byte) 15);
        }else {
            Long expireTime = tspBleKeyPo.getBleKeyExpireTime()/1000;
            expireByte = ByteUtil.getBytes(expireTime.intValue());
        }
        allocate.put(expireByte);
        allocate.put(HexUtil.hexStr2Byte(encryAppBleKey));
        byte[] result = allocate.array();
        allocate.flip();
        //reuslt 为已经的app的签名原始数据
        byte[] appSignSrc = result;

        build.setByteSign(Hex.toHexString(appSignSrc));
        build.setByteAppBleKey(HexUtil.hexStr2Byte(encryAppBleKey));
        allocate.clear();

        allocate= ByteBuffer.allocate(294);
        allocate.put(ByteUtil.getBytes(timestamp));
        allocate.put(ByteUtil.getBytes(Long.valueOf(tspBleKeyPo.getUsedUserId())));
        allocate.put(ByteUtil.getBytes(Long.valueOf(tspBleKeyPo.getBleKeyId())));
        Integer userType = tspBleKeyPo.getUserType().intValue();
        allocate.put(userType.byteValue());
        allocate.put(HexUtil.hexStr2Byte(encryTboxBleKey));
        allocate.put(ByteUtil.getBytes(effectiveTime.intValue()));
        allocate.put(expireByte);
        allocate.put(ByteUtil.getBytes(bleRight));
        allocate.put(Constant.ACTIVE_STATUS.byteValue());
        result = allocate.array();
        allocate.flip();
        byte[] tboxSignSrc =HexUtil.hexStr2Byte(Hex.toHexString(result));
        log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        log.info(HexUtil.byte2HexStr(result));
        BleSignDto bleSignDto = BleSignDto.builder()
                .algorithm(Constant.ALGORITHM_RSA)
                .applicationName("rsa2048")
                .inData(appSignSrc)
                .build();
        log.info("********************************************************");
        log.info(bleSignDto.toString());
        byte[] appSign = bleCommonFeignService.pkiSign(bleSignDto);
        bleSignDto.setInData(tboxSignSrc);
        log.info("tbox签名原始数据为={}",bleSignDto);
        byte[] tboxSign = bleCommonFeignService.pkiSign(bleSignDto);
        log.info("tbox签名后数据为={}",tboxSign);

        build.setBleSrc(bleKey);
        build.setEncrypAppBleKey(encryAppBleKey);
        build.setAppSign(appSign);
        build.setEncrypTboxBleKey(encryTboxBleKey);
        build.setTboxSign(tboxSign);
        build.setBleRight(bleRight);
        build.setTboxByteSrc(tboxSignSrc);
        build.setBlekeyByte(bleKeyByte);
        if ((!offLine) && (!StringUtil.EMPTY_STRING.equals(authCode))){
            Map<String, byte[]> bleVoucher = createBleVoucher(tspBleKeyPo, authCode, bleKey, bleRight);
            build.setAuthVoucher(bleVoucher.get("voucher"));
            build.setAuthVoucherSign(bleVoucher.get("voucherSign"));
        }
        return build;
    }

    @Override
    public Map<String,String> createEncrypBleKey(UserBleKeyPo tspBleKeyPo){
        String userId = tspBleKeyPo.getOwnerUserId();
        List<BleDeviceSn> bleDeviceSns = bleCommonFeignService.queryBleDeviceSn(2,tspBleKeyPo.getDeviceId());
        //log.info("sn码为={}",bleDeviceSnVo.toString());
        if (!Optional.ofNullable(tspBleKeyPo.getOwnerUserId()).isPresent()) {
            userId = tspBleKeyPo.getUsedUserId();
        }
        String keySrc = userId.concat("#").concat(tspBleKeyPo.getDeviceId()).concat("#").concat(tspBleKeyPo.getUsedUserMobileDeviceId());
        //MD5摘要生成原始蓝牙钥匙
        String bleKey = createBleKeySrc(keySrc);
        String byteKey = Hex.toHexString(ByteUtil.getBytes(bleKey));
        BleEncryptDto bleEncryptDto =  BleEncryptDto.builder()
                .algorithm(Constant.ALGORITHM)
                .content(ByteUtil.getBytes(bleKey))
                .deviceId(tspBleKeyPo.getUsedUserMobileDeviceId())
                .build();
        //app端蓝牙钥匙用app ID
        byte[] byteAppBleKey = bleCommonFeignService.pkiAsymmetricEncrypt(bleEncryptDto);
        String encryAppBleKey = Hex.toHexString(byteAppBleKey);
        bleEncryptDto.setDeviceId(bleDeviceSns.get(0).getProductSn());
        //tbox端蓝牙钥匙用tbox端ID
        byte[] byteTboxBleKey = bleCommonFeignService.pkiAsymmetricEncrypt(bleEncryptDto);
        String encryTboxBleKey = Hex.toHexString(byteTboxBleKey);
        Map<String,String> map = new ConcurrentHashMap<>();
        map.put("encryAppBleKey",encryAppBleKey);
        map.put("encryTboxBleKey",encryTboxBleKey);
        map.put("bleKey",bleKey);
        map.put("bleKeyByte",byteKey);
        return  map;
    }
    public Map<String,byte[]> createBleVoucher(UserBleKeyPo tspBleKeyPo, String authCode, String bleSrc, Long right) {
        ByteBuffer allocate = ByteBuffer.allocate(70);
        allocate.put(ByteUtil.getBytes(tspBleKeyPo.getDeviceId(),"UTF-8"));
        allocate.put(ByteUtil.getBytes(Integer.valueOf(authCode)));
        allocate.put(ByteUtil.getBytes(Long.valueOf(tspBleKeyPo.getUsedUserId())));
        allocate.put(ByteUtil.getBytes(Long.valueOf(tspBleKeyPo.getBleKeyId())));
        allocate.put(tspBleKeyPo.getUserType().byteValue());
        allocate.put(ByteUtil.getBytes(bleSrc));
        Long effectiveTime = (tspBleKeyPo.getBleKeyEffectiveTime().getTime()/1000);
        allocate.put(ByteUtil.getBytes(effectiveTime.intValue()));
        byte[] expireByte;
        if (tspBleKeyPo.getBleKeyExpireTime().compareTo(Long.MAX_VALUE)== Constant.COMPARE_EQUAL_VALUE||
                tspBleKeyPo.getBleKeyExpireTime().compareTo(9999999999000L)== Constant.COMPARE_EQUAL_VALUE){
            expireByte = new byte[4];
            Arrays.fill(expireByte, (byte) 15);
        }else {
            Long expireTime = tspBleKeyPo.getBleKeyEffectiveTime().getTime()/1000;
            expireByte = ByteUtil.getBytes(expireTime.intValue());
        }
        allocate.put(expireByte);
        allocate.put(ByteUtil.getBytes(right));
        byte[] result = allocate.array();
        allocate.flip();
        //reuslt 为已经的app的签名原始数据
        String voucher = result.toString();
        List<BleDeviceSn> bleDeviceSns = bleCommonFeignService.queryBleDeviceSn(2,tspBleKeyPo.getDeviceId());
        BleEncryptDto bleEncryptDto =  BleEncryptDto.builder()
                .algorithm(Constant.ALGORITHM)
                .content(voucher.getBytes())
                .deviceId(bleDeviceSns.get(0).getProductSn())
                .build();
        //app端蓝牙钥匙用app ID
        byte[] byteVoucher = bleCommonFeignService.pkiAsymmetricEncrypt(bleEncryptDto);
        String encryVoucher =  HexUtil.byte2HexStr(byteVoucher);
        BleSignDto bleSignDto = BleSignDto.builder()
                .algorithm(Constant.ALGORITHM_RSA)
                .applicationName("rsa2048")
                .inData(byteVoucher)
                .build();
        byte[] encryVoucherSign = bleCommonFeignService.pkiSign(bleSignDto);
        Map<String,byte[]> mapVoucher = new ConcurrentHashMap<>();
        mapVoucher.put("voucher",byteVoucher);
        mapVoucher.put("voucherSign",encryVoucherSign);
        return mapVoucher;
    }


    public Map<String,byte[]> createSignFunc(byte[] appContent,byte[] tboxContent){
        Map<String,byte[]> map = new ConcurrentHashMap<>();
        BleSignDto bleSignDto = BleSignDto.builder()
                .algorithm(Constant.ALGORITHM_RSA)
                .applicationName(bleAppName)
                .inData(appContent)
                .build();
        log.info("*****************************************");
        log.info(bleSignDto.toString());
        if (Optional.ofNullable(appContent).isPresent()) {
            byte[] appSign = bleCommonFeignService.pkiSign(bleSignDto);
            map.put("appSign", appSign);
        }
        if (Optional.ofNullable(tboxContent).isPresent()) {
            bleSignDto.setInData(tboxContent);
            byte[] tboxSign = bleCommonFeignService.pkiSign(bleSignDto);
            map.put("tboxSign", tboxSign);
        }
        return map;
    }

    @Override
    public BleKeyGeneratorDto createBleReplaceSign(Date now,UserBleKeyPo bleKeyPo) {
        BleKeyGeneratorDto build = BleKeyGeneratorDto.builder().build();
        Map<String, String> encrypBleKeyMap = createEncrypBleKey(bleKeyPo);
        String encryAppBleKey = encrypBleKeyMap.get("encryAppBleKey");
        String encryTboxBleKey = encrypBleKeyMap.get("encryTboxBleKey");
        String bleKey = encrypBleKeyMap.get("bleKey");
        ByteBuffer allocate = ByteBuffer.allocate(268);
        Long timestamps = Instant.now().atZone(ZoneId.of("+8")).toEpochSecond() / 1000;
        byte[] timeBytes = ByteUtil.getBytes(timestamps.intValue());
        allocate.put(timeBytes);//4
        byte[] bleBytes = ByteUtil.getBytes(Long.valueOf(bleKeyPo.getBleKeyId()));
        allocate.put(bleBytes);//8
        byte[] bleEncrypByte = HexUtil.hexStr2Byte(encryTboxBleKey);
        allocate.put(bleEncrypByte);//256
        byte[] tboxSignSrc = allocate.array();
        allocate.clear();
        build.setTboxByteSrc(tboxSignSrc);
        Map<String, byte[]> signFuncMap = createSignFunc(HexUtil.hexStr2Byte(encryAppBleKey), tboxSignSrc);
        byte[] appSign = signFuncMap.get("appSign");
        byte[] tboxSign = signFuncMap.get("tboxSign");
        build.setBleSrc(bleKey);
        build.setEncrypAppBleKey(encryAppBleKey);
        build.setAppSign(appSign);
        build.setEncrypTboxBleKey(encryTboxBleKey);
        build.setTboxSign(tboxSign);
        return build;
    }

    @Override
    public BleKeyGeneratorDto createTboxSignBase(byte[]  tboxContent) {
        BleKeyGeneratorDto build = BleKeyGeneratorDto.builder().build();
        Map<String, byte[]> signFuncMap = createSignFunc(null, tboxContent);
        byte[] tboxSign = signFuncMap.get("tboxSign");
        build.setTboxSign(tboxSign);
        return build;
    }

    @Override
    public BleKeyGeneratorDto createBleUpdateExpireSign(byte[]  tboxContent) {
        BleKeyGeneratorDto build = BleKeyGeneratorDto.builder().build();
        Map<String, byte[]> signFuncMap = createSignFunc(null, tboxContent);
        byte[] tboxSign = signFuncMap.get("tboxSign");
        build.setTboxSign(tboxSign);
        return build;
    }

    /**
     * @param src 相助生成蓝牙钥匙原始16位字符串
     * @return 利用MD5对src生成16位蓝牙钥匙
     */
    public static String createBleKeySrc(String src) {
        String dest = src.concat("#").concat(IdWorker.getIdStr());
        String blekey = MD5Builder.bit16(dest);
        return blekey;
    }
}
