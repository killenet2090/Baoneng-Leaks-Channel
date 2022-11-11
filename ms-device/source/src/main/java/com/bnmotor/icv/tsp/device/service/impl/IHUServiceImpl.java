package com.bnmotor.icv.tsp.device.service.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.uid.IUIDGenerator;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.core.utils.UUIDUtil;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.BusinessExceptionEnums;
import com.bnmotor.icv.tsp.device.common.Constant;
import com.bnmotor.icv.tsp.device.common.Enviroment;
import com.bnmotor.icv.tsp.device.common.ReqContext;
import com.bnmotor.icv.tsp.device.common.enums.QrCodeStateStatus;
import com.bnmotor.icv.tsp.device.common.enums.feign.OpTypeEnum;
import com.bnmotor.icv.tsp.device.common.enums.sms.MsgFromTypeEnum;
import com.bnmotor.icv.tsp.device.common.enums.sms.SendTypeEnum;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.ActivationStatus;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.BindStatus;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.EnrollStatus;
import com.bnmotor.icv.tsp.device.config.QRProperties;
import com.bnmotor.icv.tsp.device.mapper.DeviceMapper;
import com.bnmotor.icv.tsp.device.mapper.VehicleMapper;
import com.bnmotor.icv.tsp.device.model.entity.DevicePo;
import com.bnmotor.icv.tsp.device.model.entity.VehiclePo;
import com.bnmotor.icv.tsp.device.model.request.feign.JpushComDetailDto;
import com.bnmotor.icv.tsp.device.model.request.feign.JpushMsgDto;
import com.bnmotor.icv.tsp.device.model.request.feign.VehLoginDto;
import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleActivateDto;
import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleActiveCallbackDto;
import com.bnmotor.icv.tsp.device.model.response.feign.GetICCIDStateVo;
import com.bnmotor.icv.tsp.device.model.response.sim.JwtTokenVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.QRCodeScanVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.QRCodeVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.VehActivateStateVo;
import com.bnmotor.icv.tsp.device.service.IHUService;
import com.bnmotor.icv.tsp.device.service.feign.UserAuthFeighService;
import com.bnmotor.icv.tsp.device.service.feign.UserFeignService;
import com.bnmotor.icv.tsp.device.service.feign.impl.CmpFeignImpl;
import com.bnmotor.icv.tsp.device.service.feign.impl.DeviceOnlineFeignImpl;
import com.bnmotor.icv.tsp.device.service.feign.impl.PushFeignImpl;
import com.bnmotor.icv.tsp.device.service.feign.impl.UserFeignImpl;
import com.bnmotor.icv.tsp.device.util.QRCodeUtils;
import com.bnmotor.icv.tsp.device.util.RedisHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: DeviceServiceImpl
 * @Description: 车辆设备服务实现
 * @author: zhangwei2
 * @date: 2020/7/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Service
@RefreshScope
public class IHUServiceImpl implements IHUService {
    @Resource
    private VehicleMapper vehicleMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private Enviroment env;
    @Resource
    private UserFeignService userFeignService;
    @Resource
    private DeviceOnlineFeignImpl deviceOnlineFeign;
    @Resource
    private UserAuthFeighService userAuthFeighService;
    @Resource
    private DeviceMapper deviceMapper;
    @Autowired
    private IUIDGenerator iuidGenerator;
    @Autowired
    private PushFeignImpl pushFeign;
    @Autowired
    private UserFeignImpl userFeign;
    @Autowired
    private CmpFeignImpl cmpFeign;
    @Autowired
    private QRProperties qrProperties;


    /**
     * 获取令牌
     * @param vin
     * @param deviceId
     * @return
     */
    @Override
    public String getActivateToken(String vin, String deviceId) {
        String vinStr = String.join(Constant.EQUAL_CHAR, Constant.VIN, vin);
        String deviceIdStr = String.join(Constant.EQUAL_CHAR, Constant.DEVICE_ID, deviceId);
        String uuidStr = String.join(Constant.EQUAL_CHAR, Constant.UID, UUIDUtil.getUUID32());
        String token = String.join(Constant.AND_CHAR, vinStr, deviceIdStr, uuidStr);
        return Base64Utils.encodeToString((token).getBytes(CharsetUtil.UTF_8));
    }

    /**
     * 生成二维码
     * @param vin
     * @param token
     * @return
     */
    @Override
    public byte[] generateActivateQRCode(String vin, String token) {
        byte[] qrCodeImage;
        try {
            //放入缓存
            String cacheKey = RedisHelper.generateKey(env.getAppName(), Constant.CACHE_KEY_ACTIVATE_QRCODE_NAME, vin);
            QRCodeVo QRCodeVo = new QRCodeVo();
            QRCodeVo.setQrCodeState(QrCodeStateStatus.NORMAL.getStatus());
            QRCodeVo.setQrcodeKey(token);
            stringRedisTemplate.opsForValue().set(cacheKey, JsonUtil.toJson(QRCodeVo), qrProperties.getValidTimeSeconds(), TimeUnit.SECONDS);
            StringBuffer stringBuffer = new StringBuffer();
            //todo 后面优化 使用加密token
            String content = stringBuffer.append(Constant.QR_CODE_CONTENT_PREFIX)
                    .append(qrProperties.getCheckQrcodeUrl())
                    .append(Constant.ASK_CHAR)
                    .append(Constant.QR_CODE_TOKEN)
                    .append(Constant.EQUAL_CHAR)
                    .append(token).toString();
            if (StringUtils.isEmpty(qrProperties.getLogoUrl())) {
                qrCodeImage = QRCodeUtils.getQRCodeImage(content, qrProperties.getWidth(), qrProperties.getWidth());
            } else {
                qrCodeImage = QRCodeUtils.getQRCodeImage(content, qrProperties.getWidth(), qrProperties.getWidth(), qrProperties.getLogoUrl());
            }
            return qrCodeImage;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 扫描二维码
     * @param vehicleActivateDto
     * @return
     */
    @Override
    public QRCodeScanVo scanQRCode(VehicleActivateDto vehicleActivateDto) {
        Map<String, String> paramMap = decodeToken(vehicleActivateDto.getQrcodeKey());
        //判断qrCode
        QRCodeVo QRCodeVo = validQrCodeToken(paramMap);
        preCheckActive(paramMap, QRCodeVo.getQrCodeState());
        QRCodeVo.setQrCodeState(QrCodeStateStatus.SCANNED.getStatus());
        QRCodeVo.setUserId(String.valueOf(ReqContext.getUid()));
        String cacheKey = RedisHelper.generateKey(env.getAppName(), Constant.CACHE_KEY_ACTIVATE_QRCODE_NAME, paramMap.get(Constant.VIN));
        try {
            Long expireTime = stringRedisTemplate.opsForValue().getOperations().getExpire(cacheKey,  TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().set(cacheKey, JsonUtil.toJson(QRCodeVo), expireTime, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("扫描车机激活二维码发生异常[{}]", e.getMessage());
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }

        QRCodeScanVo QRCodeScanVo = new QRCodeScanVo();
        QRCodeScanVo.setScanCancel(Constant.QR_CODE_CONTENT_PREFIX + qrProperties.getQrcodeCancelUrl());
        QRCodeScanVo.setScanConfirm(Constant.QR_CODE_CONTENT_PREFIX + qrProperties.getQrcodeConfirmUrl());
        return QRCodeScanVo;
    }

    /**
     * 取消激活
     * @param vehicleActivateDto
     */
    @Override
    public void cancelActivateHu(VehicleActivateDto vehicleActivateDto) {
        try {
            Map<String, String> paramMap = decodeToken(vehicleActivateDto.getQrcodeKey());
            QRCodeVo QRCodeVo = validQrCodeToken(paramMap);
            if (!QrCodeStateStatus.SCANNED.getStatus().equals(QRCodeVo.getQrCodeState())) {
                throw new AdamException(BusinessExceptionEnums.QR_CODE_INVALID);
            }
            QRCodeVo.setQrCodeState(QrCodeStateStatus.CANCEL.getStatus());
            QRCodeVo.setUserId(String.valueOf(ReqContext.getUid()));
            String cacheKey = RedisHelper.generateKey(env.getAppName(), Constant.CACHE_KEY_ACTIVATE_QRCODE_NAME, paramMap.get(Constant.VIN));
            stringRedisTemplate.opsForValue().set(cacheKey, JsonUtil.toJson(QRCodeVo), qrProperties.getValidTimeSeconds(), TimeUnit.SECONDS);
        } catch (AdamException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void activeCallback(String vin, VehicleActiveCallbackDto vehicleActiveCallbackDto) {
        DevicePo devicePo = deviceMapper.getByDeviceId(vehicleActiveCallbackDto.getDeviceId());
        if (devicePo == null) {
            throw new AdamException(BusinessExceptionEnums.DEVICE_NOT_EXIST);
        }
        LocalDateTime curTime = LocalDateTime.now();
        //判断车辆是否已绑定
        VehiclePo vehiclePo = vehicleMapper.selectByVin(vin);
        if (vehiclePo == null) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_NOT_EXIST);
        }
        //判断车辆激活状态
        if (!ActivationStatus.ACTIVING.getStatus().equals(vehiclePo.getActivationStatus())) {
            return;
        }
        //成功
        if (JpushComDetailDto.SUCCESS_FLAG.equals(vehicleActiveCallbackDto.getResult())) {
            devicePo.setEnrollTime(Date.from(curTime.atZone(ZoneId.systemDefault()).toInstant()));
            devicePo.setEnrollStatus(EnrollStatus.SUCCESS.getStatus());
            vehiclePo.setActivationStatus(ActivationStatus.ACTIVED.getStatus());
            vehiclePo.setActivationDate(curTime);
        } else {
            //失败 回滚车辆车机激活状态
            devicePo.setEnrollStatus(EnrollStatus.FAIL.getStatus());
            vehiclePo.setActivationStatus(ActivationStatus.FAILED_ACTIVE.getStatus());
        }
        devicePo.setUpdateBy(String.valueOf(ReqContext.getUid()));
        devicePo.setUpdateTime(curTime);
        deviceMapper.updateById(devicePo);
        vehiclePo.setUpdateBy(String.valueOf(ReqContext.getUid()));
        vehiclePo.setUpdateTime(curTime);
        vehicleMapper.updateById(vehiclePo);
        //需要极光推送通知到app
        JpushComDetailDto jpushComDetailDto = new JpushComDetailDto();
        jpushComDetailDto.setStatus(vehicleActiveCallbackDto.getResult());
        jpushComDetailDto.setOpType(OpTypeEnum.HU_ACTIVATE.getType());
        jpushComDetailDto.setVin(vin);
        JpushMsgDto jpushMsgDto = appendJpushData(jpushComDetailDto);
        pushFeign.sendMessage(jpushMsgDto);
    }

    /**
     * 查询车机激活状态
     * @param vin
     * @param deviceId
     * @return
     */
    @Override
    public VehActivateStateVo queryActiveState(String vin, String deviceId) {
        DevicePo devicePo = deviceMapper.getByDeviceId(deviceId);
        VehActivateStateVo vehActivateStateVo = new VehActivateStateVo();
        if (Objects.isNull(devicePo)) {
            throw new AdamException(RespCode.USER_INVALID_INPUT);
        }
        vehActivateStateVo.setState(devicePo.getEnrollStatus() != null ? devicePo.getEnrollStatus() : 0);
        return vehActivateStateVo;
    }

    /**
     * 拼装极光推送数据
     * @param jpushComDetailDto
     * @return
     */
    private JpushMsgDto appendJpushData(JpushComDetailDto jpushComDetailDto) {
        JpushMsgDto jpushMsgDto = new JpushMsgDto();
        jpushMsgDto.setBusinessId(String.valueOf(iuidGenerator.nextId()));
        jpushMsgDto.setSendAllFlag(false);
        jpushMsgDto.setFromType(MsgFromTypeEnum.VEHICLE_SETTING.getValue());
        jpushMsgDto.setSendType(SendTypeEnum.APP.getValue());
        GetICCIDStateVo getICCIDStateVo = cmpFeign.getICCIDState(jpushComDetailDto.getVin());
        jpushComDetailDto.setCertificationStatus(getICCIDStateVo != null ? getICCIDStateVo.getCertificationStatus() : null);
        try {
            jpushMsgDto.setSendContent(JsonUtil.toJson(jpushComDetailDto));
        } catch (JsonProcessingException e) {
            log.error("极光请求参数转json发生异常[{}]", e.getMessage());
            throw new AdamException(RespCode.SERVER_DATA_ERROR);
        }
        String rid = userFeign.getUserRidByVin(jpushComDetailDto.getVin());
        List<String> registrationIdList = new ArrayList<>();
        if (StringUtil.isNotBlank(rid)) {
            registrationIdList.add(rid);
        }
        jpushMsgDto.setSendRegistrationIdList(registrationIdList);
        return jpushMsgDto;
    }

    /**
     * 激活前校验
     * @param paramMap
     * @param qrCodeState
     * @return
     */
    private VehiclePo preCheckActive(Map<String, String> paramMap, Integer qrCodeState) {
        if (QrCodeStateStatus.ACTIVATION.getStatus().equals(qrCodeState) || QrCodeStateStatus.INVALID.getStatus().equals(qrCodeState)) {
            throw new AdamException(BusinessExceptionEnums.QR_CODE_INVALID);
        }
        //判断车辆是否已绑定
        VehiclePo vehiclePo = vehicleMapper.selectByVin(paramMap.get(Constant.VIN));

        if (vehiclePo == null) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_NOT_EXIST);
        }
        if (!BindStatus.BIND.getStatus().equals(vehiclePo.getBindStatus())) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_HAS_NO_BIND);
        }
        //判断当前用户是否为车主
        RestResponse<Boolean> result = userFeignService.checkVehicleHasBind(ReqContext.getUid(), paramMap.get(Constant.VIN));
        if (!RespCode.SUCCESS.getValue().equals(result.getRespCode())) {
            throw new AdamException(result.getRespCode(), result.getRespMsg());
        }
        if (!result.getRespData()) {
            throw new AdamException(BusinessExceptionEnums.NO_VEHICLE_OWNER);
        }
        //判断车辆是否在线
        boolean isOnline = deviceOnlineFeign.checkOnline(paramMap.get(Constant.VIN));
        if (!isOnline) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_NOT_ONLINE);
        }
        //todo 车辆激活逻辑判断--b.	驻车状态（P档且车速为0）
        return vehiclePo;
    }

    /**
     * 激活确认
     * @param vehicleActivateDto
     */
    @Override
    public void vehicleActivate(VehicleActivateDto vehicleActivateDto) {
        try {
            Map<String, String> paramMap = decodeToken(vehicleActivateDto.getQrcodeKey());
            QRCodeVo QRCodeVo = validQrCodeToken(paramMap);
            //重复确认 直接返回成功
            if (QrCodeStateStatus.ACTIVATION.getStatus().equals(QRCodeVo.getQrCodeState())) {
                throw new AdamException(BusinessExceptionEnums.VEHICLE_HAS_ACTIVATE);
            }
            VehiclePo vehiclePo = preCheckActive(paramMap, QRCodeVo.getQrCodeState());
            //更新车辆为激活状态
            DevicePo devicePo = deviceMapper.getByDeviceId(paramMap.get(Constant.DEVICE_ID));
            if (!(ActivationStatus.ACTIVED.getStatus().equals(vehiclePo.getActivationStatus()) &&
                    devicePo != null && ActivationStatus.ACTIVED.getStatus().equals(devicePo.getEnrollStatus()))) {
                //判断车辆状态【未激活】
                if (ActivationStatus.ACTIVED.getStatus().equals(vehiclePo.getActivationStatus()) ||
                        ActivationStatus.ACTIVING.getStatus().equals(vehiclePo.getActivationStatus())) {
                    throw new AdamException(BusinessExceptionEnums.VEHICLE_STATUS_NOT_SUPPORT);
                }
                if (devicePo == null || ActivationStatus.ACTIVED.getStatus().equals(devicePo.getEnrollStatus()) ||
                        ActivationStatus.ACTIVING.getStatus().equals(devicePo.getEnrollStatus())) {
                    throw new AdamException(BusinessExceptionEnums.HU_STATUS_NOT_SUPPORT);
                }
                LocalDateTime curTime = LocalDateTime.now();
                devicePo.setEnrollStatus(EnrollStatus.ENROLLING.getStatus());
                devicePo.setUpdateBy(String.valueOf(ReqContext.getUid()));
                devicePo.setUpdateTime(curTime);
                deviceMapper.updateById(devicePo);
                vehiclePo.setActivationStatus(ActivationStatus.ACTIVING.getStatus());
                vehiclePo.setUpdateBy(String.valueOf(ReqContext.getUid()));
                vehiclePo.setUpdateTime(curTime);
                vehicleMapper.updateById(vehiclePo);
            } else {
                throw new AdamException(BusinessExceptionEnums.VEHICLE_HAS_ACTIVATE);
            }
            QRCodeVo.setQrCodeState(QrCodeStateStatus.ACTIVATION.getStatus());
            QRCodeVo.setUserId(String.valueOf(ReqContext.getUid()));
            String cacheKey = RedisHelper.generateKey(env.getAppName(), Constant.CACHE_KEY_ACTIVATE_QRCODE_NAME, paramMap.get(Constant.VIN));
            stringRedisTemplate.opsForValue().set(cacheKey, JsonUtil.toJson(QRCodeVo), qrProperties.getValidTimeSeconds(), TimeUnit.SECONDS);
        } catch (AdamException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 对qrCode进行解码
     * @param qrCode
     * @return
     */
    private Map<String, String> decodeToken(String qrCode) {
        String token = new String(Base64Utils.decode(qrCode.getBytes()), CharsetUtil.UTF_8);
        String[] params = token.split(Constant.AND_CHAR);
        Map<String, String> paramMap = new HashMap<>(3);
        Arrays.stream(params).forEach(param -> {
            String[] data = param.split(Constant.EQUAL_CHAR);
            paramMap.put(data[0], data[1]);
        });
        return paramMap;
    }

    /**
     * 轮询查询二维码状态
     * @param vin
     * @param deviceId
     * @param token
     * @return
     */
    @Override
    public QRCodeVo roundCheckActivateState(String vin, String deviceId, String token) {
        Map<String, String> paramMap = decodeToken(token);
        QRCodeVo QRCodeVo = validQrCodeToken(paramMap);
        if (!vin.equals(paramMap.get(Constant.VIN)) || !deviceId.equals(paramMap.get(Constant.DEVICE_ID))) {
            throw new AdamException(RespCode.USER_INVALID_INPUT);
        }
        if (QrCodeStateStatus.ACTIVATION.getStatus().equals(QRCodeVo.getQrCodeState())) {
            VehLoginDto vehLoginDto = new VehLoginDto();
            vehLoginDto.setUid(QRCodeVo.getUserId());
            vehLoginDto.setVin(vin);
            vehLoginDto.setModeType(3);
            RestResponse<JwtTokenVo> getJwtTokenResp = userAuthFeighService.vehLogin(vehLoginDto);
            if (!RespCode.SUCCESS.getValue().equals(getJwtTokenResp.getRespCode())) {
                throw new AdamException(getJwtTokenResp.getRespCode(), getJwtTokenResp.getRespMsg());
            }
            JwtTokenVo jwtTokenVo = getJwtTokenResp.getRespData();
            QRCodeVo.setUserId(null);
            QRCodeVo.setAccessToken(jwtTokenVo.getAccessToken());
            QRCodeVo.setRefreshToken(jwtTokenVo.getRefreshToken());
        }
        return QRCodeVo;
    }

    /**
     *校验token
     * @param paramMap
     * @return
     */
    private QRCodeVo validQrCodeToken(Map<String, String> paramMap) {
        try {
            QRCodeVo QRCodeVo = new QRCodeVo();
            //判断是否失效
            String cacheKey = RedisHelper.generateKey(env.getAppName(), Constant.CACHE_KEY_ACTIVATE_QRCODE_NAME, paramMap.get(Constant.VIN));
            if (!stringRedisTemplate.hasKey(cacheKey)) {
                QRCodeVo.setQrCodeState(QrCodeStateStatus.INVALID.getStatus());
                return QRCodeVo;
            }
            String data = stringRedisTemplate.opsForValue().get(cacheKey);
            QRCodeVo = JsonUtil.toObject(data, QRCodeVo.class);
            //校验随机数
            Map<String, String> redisParamMap = decodeToken(QRCodeVo.getQrcodeKey());
            if (!redisParamMap.get(Constant.UID).equals(paramMap.get(Constant.UID))) {
                throw new AdamException(RespCode.USER_INVALID_INPUT);
            }
            return QRCodeVo;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }
}
