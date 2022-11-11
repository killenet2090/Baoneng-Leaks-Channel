package com.bnmotor.icv.tsp.device.service.impl;

import com.alibaba.excel.util.DateUtils;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.uid.IUIDGenerator;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.BusinessExceptionEnums;
import com.bnmotor.icv.tsp.device.common.Constant;
import com.bnmotor.icv.tsp.device.common.enums.feign.FromTypeEnum;
import com.bnmotor.icv.tsp.device.common.enums.sms.ChannelEnum;
import com.bnmotor.icv.tsp.device.common.enums.sms.MappingTemplateIdEnum;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.BindStatus;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.VehicleStatus;
import com.bnmotor.icv.tsp.device.mapper.VehicleBindInvoiceMapper;
import com.bnmotor.icv.tsp.device.mapper.VehicleMapper;
import com.bnmotor.icv.tsp.device.mapper.VehicleSaleInfoMapper;
import com.bnmotor.icv.tsp.device.mapstuct.VehicleInvoiceVoMapper;
import com.bnmotor.icv.tsp.device.mapstuct.VehicleLicenseVoMapper;
import com.bnmotor.icv.tsp.device.mapstuct.VehicleSaleInfoDtoMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehicleBindInvoicePo;
import com.bnmotor.icv.tsp.device.model.entity.VehicleLicensePo;
import com.bnmotor.icv.tsp.device.model.entity.VehiclePo;
import com.bnmotor.icv.tsp.device.model.entity.VehicleSaleInfoPo;
import com.bnmotor.icv.tsp.device.model.request.excel.FileDto;
import com.bnmotor.icv.tsp.device.model.request.feign.TemplateMsgDto;
import com.bnmotor.icv.tsp.device.model.request.feign.UserVehicleBindDto;
import com.bnmotor.icv.tsp.device.model.request.feign.UserVehicleRelationDto;
import com.bnmotor.icv.tsp.device.model.request.vehBind.*;
import com.bnmotor.icv.tsp.device.model.response.vehBind.VehicleUserVo;
import com.bnmotor.icv.tsp.device.model.response.feign.ExistVo;
import com.bnmotor.icv.tsp.device.model.response.vehBind.VehicleBindVo;
import com.bnmotor.icv.tsp.device.model.response.vehBind.VehicleInvoiceVo;
import com.bnmotor.icv.tsp.device.model.response.vehBind.VehicleLicenseVo;
import com.bnmotor.icv.tsp.device.service.IVehicleBindService;
import com.bnmotor.icv.tsp.device.service.feign.OssService;
import com.bnmotor.icv.tsp.device.service.feign.SmsFeignService;
import com.bnmotor.icv.tsp.device.service.feign.UserFeignService;
import com.bnmotor.icv.tsp.device.service.thirdparty.IOcrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;

/**
 * @ClassName: TspDrivingLicensePo
 * @Description: 行驶证信息 服务实现类
 * @author huangyun1
 * @since 2020-09-24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
@RefreshScope
public class VehicleBindServiceImpl implements IVehicleBindService {

    @Autowired
    private OssService ossService;
    @Autowired
    private IOcrService ocrService;
    @Resource
    private VehicleMapper vehicleMapper;
    @Resource
    private UserFeignService userFeignService;
    @Resource
    private VehicleSaleInfoMapper vehicleSaleInfoMapper;
    @Autowired
    private IUIDGenerator generator;
    @Resource
    private SmsFeignService smsFeignService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Resource
    private VehicleBindInvoiceMapper vehicleBindInvoiceMapper;

    @Value("${device.sms.app-name}")
    private String appName;

//    @Value("${device.sms.app-download-url}")
//    private String appDownloadUrl;

    /**
     * 上传行驶证
     * @param file
     * @param uid
     * @return
     */
    @Override
    public VehicleLicenseVo uploadLicense(MultipartFile file, Long uid) {
        //解析行驶证
        RestResponse<VehicleLicensePo> analysisResult = ocrService.analysisVehicleLicense(file);
        if(RespCode.SUCCESS.getValue().equals(analysisResult.getRespCode())) {
            VehicleLicensePo vehicleLicensePo = analysisResult.getRespData();
            //保存到minio
            FileDto fileDto = new FileDto();
            fileDto.setBucket(Constant.OSS_BUCKET);
            fileDto.setGroup(Constant.OSS_GROUP_DRIVING_LICENSE);
            fileDto.setFile(file);
            RestResponse<List<LinkedHashMap>> responseEntity = ossService.uploadFiles(uid, fileDto);
            if(RespCode.SUCCESS.getValue().equals(responseEntity.getRespCode())) {
                List<LinkedHashMap> files = responseEntity.getRespData();
                vehicleLicensePo.setImgUrl((String) files.get(0).get("relativePath"));
                return VehicleLicenseVoMapper.INSTANCE.map(vehicleLicensePo);
            } else {
                throw new AdamException(responseEntity.getRespCode(), responseEntity.getRespMsg());
            }
        } else {
            throw new AdamException(analysisResult.getRespCode(), analysisResult.getRespMsg());
        }
    }

    /**
     * 上传机动车销售发票
     * @param multipartFile
     * @param uid
     * @return
     */
    @Override
    public VehicleInvoiceVo uploadVehicleInvoice(MultipartFile multipartFile, Long uid) {
        //解析行驶证
        RestResponse<VehicleBindInvoicePo> analysisResult = ocrService.analysisVehicleInvoice(multipartFile);
        if(RespCode.SUCCESS.getValue().equals(analysisResult.getRespCode())) {
            VehicleBindInvoicePo vehicleBindInvoicePo = analysisResult.getRespData();
            VehicleInvoiceVo vehicleInvoiceVo = VehicleInvoiceVoMapper.INSTANCE.map(vehicleBindInvoicePo);
            return vehicleInvoiceVo;
        } else {
            throw new AdamException(analysisResult.getRespCode(), analysisResult.getRespMsg());
        }
    }

    /**
     * 识别机动车销售发票信息
     * @param invoiceFile
     * @return
     */
    @Override
    public VehicleInvoiceVo recognitionVehInvoice(MultipartFile invoiceFile) {
        //解析行驶证
        RestResponse<VehicleBindInvoicePo> analysisResult = ocrService.analysisVehicleInvoice(invoiceFile);
        if(RespCode.SUCCESS.getValue().equals(analysisResult.getRespCode())) {
            VehicleBindInvoicePo vehicleBindInvoicePo = analysisResult.getRespData();
            return VehicleInvoiceVoMapper.INSTANCE.map(vehicleBindInvoicePo);
        } else {
            throw new AdamException(analysisResult.getRespCode(), analysisResult.getRespMsg());
        }
    }

    private List<VehicleUserVo> verifySubmitBind(SubmitBindDto submitBindDto, VehiclePo vehiclePo) {
        List<VehicleUserVo> vehicleUserVos = new ArrayList<>();
        //车辆销售状态必须为已销售状态
        if (!VehicleStatus.SOLED.getStatus().equals(vehiclePo.getVehStatus())) {
            String errorMsg = MessageFormat.format(BusinessExceptionEnums.STATUS_NOT_SUPPORT_BIND.getDescription(),
                    VehicleStatus.valueOf((vehiclePo.getVehStatus() != 0 ? vehiclePo.getVehStatus() : VehicleStatus.CREATED.getStatus())).getDesp());
            throw new AdamException(BusinessExceptionEnums.STATUS_NOT_SUPPORT_BIND, errorMsg);
        }
        //结果为【已绑定】，当前账户已经绑定该车，返回不支持绑定，“您当前已绑定该车辆”
        if (BindStatus.BIND.getStatus().equals(vehiclePo.getBindStatus())) {
            RestResponse<List<VehicleUserVo>> listVehicleUserResp = userFeignService.listVehicleUser(submitBindDto.getVin());
            if (listVehicleUserResp.isSuccess()) {
                vehicleUserVos = listVehicleUserResp.getRespData();
                if (vehicleUserVos != null && !vehicleUserVos.isEmpty()) {
                    vehicleUserVos.stream().forEach(vehicleUserVo -> {
                        if(String.valueOf(submitBindDto.getUserId()).equals(vehicleUserVo.getUid())) {
                            throw new AdamException(BusinessExceptionEnums.CUR_USER_HAS_BIND);
                        }
                    });
                }
            } else {
                throw new AdamException(listVehicleUserResp.getRespCode(), listVehicleUserResp.getRespMsg());
            }
        }
        //结果为【解绑中】，返回不支持绑定
        if (BindStatus.UN_BINDING.getStatus().equals(vehiclePo.getBindStatus())) {
            String errorMsg = MessageFormat.format(BusinessExceptionEnums.STATUS_NOT_SUPPORT_BIND.getDescription(),
                    BindStatus.valueOf(vehiclePo.getBindStatus()).getDesp());
            throw new AdamException(BusinessExceptionEnums.STATUS_NOT_SUPPORT_BIND.getValue(), errorMsg);
        }
        return vehicleUserVos;
    }

    /**
     * 个人车主提交绑定操作
     * @param submitBindDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public VehicleBindVo submitBind(SubmitBindDto submitBindDto) {
        VehicleSaleInfoPo vehicleSaleInfoPo = vehicleSaleInfoMapper.selectByVin(submitBindDto.getVin());
        if (vehicleSaleInfoPo == null) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_SALE_INFO_NOT_EXIST);
        }
        VehiclePo vehiclePo = vehicleMapper.selectByVin(submitBindDto.getVin());
        if (vehiclePo == null || !submitBindDto.getEngineNo().equals(vehiclePo.getEngineNo())) {
            throw new AdamException(RespCode.USER_INVALID_INPUT);
        }
        VehicleInvoiceVo vehicleInvoiceVo = new VehicleInvoiceVo();
        vehicleInvoiceVo.setEngineNo(submitBindDto.getEngineNo());
        vehicleInvoiceVo.setInvoiceCode(submitBindDto.getInvoiceCode());
        vehicleInvoiceVo.setInvoiceNum(submitBindDto.getInvoiceNum());
        vehicleInvoiceVo.setVin(submitBindDto.getVin());
        //查询车辆绑定状态
        List<VehicleUserVo> vehicleUserVos = verifySubmitBind(submitBindDto, vehiclePo);
        //保存到minio
        FileDto fileDto = new FileDto();
        fileDto.setBucket(Constant.OSS_BUCKET);
        fileDto.setGroup(Constant.OSS_GROUP_VEHICLE_INVOICE);
        fileDto.setFile(submitBindDto.getInvoiceFile());
        RestResponse<List<LinkedHashMap>> responseEntity = ossService.uploadFiles(submitBindDto.getUserId(), fileDto);
        if(RespCode.SUCCESS.getValue().equals(responseEntity.getRespCode())) {
            List<LinkedHashMap> files = responseEntity.getRespData();
            vehicleInvoiceVo.setImgUrl((String) files.get(0).get("relativePath"));
        } else {
            throw new AdamException(responseEntity.getRespCode(), responseEntity.getRespMsg());
        }
        //todo 发票查验
        //对发票信息校验
        verifyVehicleInvoice(submitBindDto, vehicleSaleInfoPo, vehicleUserVos);
        saveLicenseAndInvoice(submitBindDto, vehicleInvoiceVo);
        VehicleBindVo vehicleBindVo = new VehicleBindVo();
        vehicleBindVo.setPhone(vehicleSaleInfoPo.getPurchaserPhone());
        return vehicleBindVo;
    }


    /**
     * 保存行驶证信息和发票信息
     * @param submitBindDto
     * @param vehicleInvoiceVo
     */
    private void saveLicenseAndInvoice(SubmitBindDto submitBindDto, VehicleInvoiceVo vehicleInvoiceVo) {
        //先删除旧的发票信息
        LocalDateTime curTime = LocalDateTime.now();
        VehicleBindInvoicePo delVehBindInvoicePo = new VehicleBindInvoicePo();
        delVehBindInvoicePo.setVin(vehicleInvoiceVo.getVin());
        delVehBindInvoicePo.setDelFlag(1);
        delVehBindInvoicePo.setUpdateBy(String.valueOf(submitBindDto.getUserId()));
        delVehBindInvoicePo.setUpdateTime(curTime);
        vehicleBindInvoiceMapper.deleteVehBindInvoiceByVin(delVehBindInvoicePo);
        VehicleBindInvoicePo vehicleBindInvoicePo = VehicleInvoiceVoMapper.INSTANCE.revertMap(vehicleInvoiceVo);
        vehicleBindInvoicePo.setImgUrl(vehicleInvoiceVo.getImgUrl());
        vehicleBindInvoicePo.setCreateBy(String.valueOf(submitBindDto.getUserId()));
        vehicleBindInvoicePo.setCreateTime(curTime);
        vehicleBindInvoiceMapper.insert(vehicleBindInvoicePo);
    }

    /**
     * 同步车辆销售信息
     * @param vehicleSaleInfoDto
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void synchronizationSaleInfo(VehicleSaleInfoDto vehicleSaleInfoDto) {
        VehiclePo vehiclePo = vehicleMapper.selectByVin(vehicleSaleInfoDto.getVin());
        VehicleSaleInfoPo saleInfoPo = vehicleSaleInfoMapper.selectByVin(vehicleSaleInfoDto.getVin());
        if(saleInfoPo != null) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_SALE_INFO_HAS_SYNCHRONIZATION);
        }
        boolean isExistUser = transactionTemplate.execute(transactionStatus -> {
            try {
                //保存销售信息并更新车辆销售状态
                saveSaleInfoAndUpdateVehStatus(vehicleSaleInfoDto, vehiclePo);
                //判断购车人手机号有没有注册宝能智联账户，有注册则B3-1，未注册则B3-2
                RestResponse<ExistVo> checkPhoneResp = userFeignService.checkAccount(vehicleSaleInfoDto.getPurchaserPhone());
                if (!checkPhoneResp.isSuccess()) {
                    throw new AdamException(checkPhoneResp.getRespCode(), checkPhoneResp.getRespMsg());
                }
                if(checkPhoneResp.getRespData().isExist()) {
                    //将该车辆与购车人账户关联，该账户在登录客户端时可查看该车
                    UserVehicleRelationDto userVehicleRelationDto = new UserVehicleRelationDto();
                    userVehicleRelationDto.setDrivingCicPlate(vehiclePo.getDrivingLicPlate());
                    userVehicleRelationDto.setPurchaserPhone(vehicleSaleInfoDto.getPurchaserPhone());
                    userVehicleRelationDto.setDealer(vehicleSaleInfoDto.getDealer());
                    userVehicleRelationDto.setVehName(vehiclePo.getVehModelName());
                    userVehicleRelationDto.setVin(vehiclePo.getVin());
                    RestResponse relationResp = userFeignService.relationVehicle(userVehicleRelationDto);
                    if (!relationResp.isSuccess()) {
                        throw new AdamException(relationResp.getRespCode(), relationResp.getRespMsg());
                    }
                    return true;
                }
                return false;
            } catch (AdamException e) {
                transactionStatus.setRollbackOnly();
                log.error(e.getMessage(), e);
                throw e;
            } catch (Exception e) {
                transactionStatus.setRollbackOnly();
                log.error(e.getMessage(), e);
                throw new AdamException(RespCode.UNKNOWN_ERROR);
            }
        });
        //短信推送购车人手机号，短信内容为：“【宝能汽车】感谢您选择宝能汽车，我们将竭诚为您服务，您可下载【APP名称】进行车辆的绑定和激活服务（下载链接：【下载链接】），我们期待给您带来全方位贴心的服务体验！”
        this.sendDownloadAppAndBindSms(vehicleSaleInfoDto.getPurchaserPhone());

        if(isExistUser) {
            //todo App推送消息，消息内容为：“您新购置了一辆【车辆型号】，可前往通过App进行绑定激活，开启一段新的旅程！”
        }
    }

    /**
     * 提交解绑
     * @param vehicleUnbindDto
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void submitUnbind(VehicleUnbindDto vehicleUnbindDto) {
        //记录本次提交，生成解绑业务单，标记为解绑中，车辆绑定状态标记为解绑中
        try {
            //车主和车辆绑定关系解除，解绑业务单关闭完成，车辆变为未绑定
            LocalDateTime curTime = LocalDateTime.now();
            VehiclePo vehPo = vehicleMapper.selectByVin(vehicleUnbindDto.getVin());
            vehPo.setOldBindStatus(BindStatus.BIND.getStatus());
            vehPo.setBindStatus(BindStatus.NOT_BIND.getStatus());
            vehPo.setUpdateBy(vehicleUnbindDto.getUserId().toString());
            vehPo.setUpdateTime(curTime);
            int unbindCount = vehicleMapper.compareBindStatusAndUpdate(vehPo);
            if(unbindCount < 1) {
                throw new AdamException(BusinessExceptionEnums.VEHICLE_BIND_STATUS_CHANGED);
            }
        } catch (AdamException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        //向关联运营卡平台通知车主解绑成功
    }


    /**
     * 完成车辆绑定
     * @param finishBindDto
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void finishBind(FinishBindDto finishBindDto) {
        //车辆绑定状态变为已绑定，并将车辆绑定在申请人账户下
        LocalDateTime curTime = LocalDateTime.now();
        VehiclePo vehiclePo = vehicleMapper.selectByVin(finishBindDto.getVin());
        vehiclePo.setOldBindStatus(BindStatus.NOT_BIND.getStatus());
        vehiclePo.setBindStatus(BindStatus.BIND.getStatus());
        vehiclePo.setBindDate(curTime);
        vehiclePo.setUpdateBy(finishBindDto.getUserId().toString());
        vehiclePo.setUpdateTime(curTime);
        int count = vehicleMapper.compareBindStatusAndUpdate(vehiclePo);
        if(count < 1) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_BIND_STATUS_CHANGED);
        }
        UserVehicleBindDto userVehicleBindDto = new UserVehicleBindDto();
        userVehicleBindDto.setVin(finishBindDto.getVin());
        userVehicleBindDto.setDrivingCicPlate(vehiclePo.getDrivingLicPlate());
        //userVehicleBindDto.setVehName(vehiclePo.getVehModelName());
    }

    @Override
    public void resetBindStatus(ResetVehicleBindStatusDto resetVehicleBindStatusDto) {
        VehiclePo vehiclePo = vehicleMapper.selectByVin(resetVehicleBindStatusDto.getVin());
        VehiclePo updateBean = new VehiclePo();
        updateBean.setId(vehiclePo.getId());
        updateBean.setBindStatus(resetVehicleBindStatusDto.getBindStatus());
        updateBean.setOldBindStatus(resetVehicleBindStatusDto.getOldBindStatus());
        updateBean.setUpdateBy(String.valueOf(resetVehicleBindStatusDto.getUserId()));
        updateBean.setUpdateTime(LocalDateTime.now());
        vehicleMapper.compareBindStatusAndUpdate(updateBean);
    }

    /**
     * 保存销售信息并更新车辆销售状态
     * @param vehicleSaleInfoDto
     * @param vehiclePo
     */
    private void saveSaleInfoAndUpdateVehStatus(VehicleSaleInfoDto vehicleSaleInfoDto, VehiclePo vehiclePo) {
        LocalDateTime curTime = LocalDateTime.now();
        //存入销售记录
        VehicleSaleInfoPo vehicleSaleInfoPo = VehicleSaleInfoDtoMapper.INSTANCE.revertMap(vehicleSaleInfoDto);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.DATE_FORMAT_10);
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().append(formatter)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
        vehicleSaleInfoPo.setInvoiceDate(LocalDateTime.parse(vehicleSaleInfoDto.getInvoiceDateStr(), dateTimeFormatter));
        vehicleSaleInfoPo.setSaleDate(LocalDateTime.parse(vehicleSaleInfoDto.getSaleDateStr(), dateTimeFormatter));
        vehicleSaleInfoPo.setVersion(0);
        vehicleSaleInfoPo.setCreateBy(vehicleSaleInfoDto.getDealer());
        vehicleSaleInfoPo.setCreateTime(curTime);
        vehicleSaleInfoMapper.insert(vehicleSaleInfoPo);
        //修改车辆状态[已销售]
        if(vehiclePo == null) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_NOT_EXIST);
        }
        vehiclePo.setVehStatus(VehicleStatus.SOLED.getStatus());
        vehiclePo.setUpdateBy(vehicleSaleInfoDto.getDealer());
        vehiclePo.setUpdateTime(curTime);
        vehicleMapper.updateById(vehiclePo);
    }

    /**
     * 发送通知用户下载app进行绑定激活提醒短信
     * @param purchaserPhone
     */
    private void sendDownloadAppAndBindSms(String purchaserPhone) {
        RestResponse sendResult;
        try {
            sendResult = smsFeignService.sendByTemplate(appendDownloadAppSms(purchaserPhone));
        } catch (AdamException e) {
            log.error(e.getMessage());
            throw e;
        }catch (Exception e) {
            log.error(e.getMessage());
            throw new AdamException(RespCode.SERVER_EXECUTE_ERROR);
        }

        if(!RespCode.SUCCESS.getValue().equals(sendResult.getRespCode())) {
            throw new AdamException(sendResult.getRespCode(), sendResult.getRespMsg());
        }
    }

    /**
     * 拼接通知下载app短信参数
     * @param phone
     * @return
     */
    private TemplateMsgDto appendDownloadAppSms(String phone) {
        TemplateMsgDto templateMsgDto = new TemplateMsgDto();
        templateMsgDto.setBusinessId(String.valueOf(generator.nextId()));
        templateMsgDto.setFromType(FromTypeEnum.USER_MODEL.getValue());
        templateMsgDto.setMappingTemplateId(MappingTemplateIdEnum.NOTIFY_DOWNLOAD_APP_MSG.getValue());
        Map<String, String> templateParam = new HashMap<>(3);
        templateParam.put("appName", appName);
//        templateParam.put("appDownloadUrl", appDownloadUrl);
        templateMsgDto.setExtraData(templateParam);
        templateMsgDto.setSendChannel(ChannelEnum.JSMS.getValue());
        templateMsgDto.setSendPhone(phone);
        return templateMsgDto;
    }

    /**
     * 对行驶证进行查验
     * @param submitBindDto
     * @param vehicleLicenseVo
     * @param vehicleSaleInfoPo
     */
    private void verifyVehicleLicense(SubmitBindDto submitBindDto, VehicleLicenseVo vehicleLicenseVo, VehicleSaleInfoPo vehicleSaleInfoPo,
                                      List<VehicleUserVo> vehicleUserVos) {
        //行驶证 -【车辆识别代号 VIN】，【发动机号码】与申请绑定的车辆信息不一致，则返回失败原因
        if (!submitBindDto.getVin().equals(vehicleLicenseVo.getVin())) {
            throw new AdamException(BusinessExceptionEnums.LICENSE_VIN_NOT_IDENTICAL);
        }

        if (!submitBindDto.getEngineNo().equals(vehicleLicenseVo.getEngineNo())) {
            throw new AdamException(BusinessExceptionEnums.LICENSE_ENGINE_NO_NOT_IDENTICAL);
        }

        //行驶证 -【车辆识别代号 VIN】，【发动机号码】与申请绑定的车辆信息一致，但【所有人】与购车记录中的发票信息中【购买方名称及身份证号码/组织机构代码】的购买方名称不一致，提示需要进入二手车绑定（流程待定）
        if (!vehicleLicenseVo.getUserName().equals(vehicleSaleInfoPo.getPurchaser())) {
            throw new AdamException(BusinessExceptionEnums.INVOICE_PURCHASER_NOT_IDENTICAL);
        }

        //【车辆识别代号 VIN】，【发动机号码】与申请绑定的车辆信息一致，且【所有人】与购车记录中的发票信息中【购买方名称及身份证号码/组织机构代码】的购买方名称一致，则【未绑定】状态触发个人绑定B6-1和C1，【已绑定】状态A4-2返回“该车辆已被绑定，请先解绑“
        if (submitBindDto.getVin().equals(vehicleLicenseVo.getVin()) && submitBindDto.getEngineNo().equals(vehicleLicenseVo.getEngineNo())
                && vehicleLicenseVo.getUserName().equals(vehicleSaleInfoPo.getPurchaser()) && vehicleUserVos != null && !vehicleUserVos.isEmpty()) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_HAS_BIND);
        }
    }

    /**
     * 对发票信息查验
     * @param submitBindDto
     * @param vehicleSaleInfoPo
     */
    private VehicleSaleInfoPo verifyVehicleInvoice(SubmitBindDto submitBindDto, VehicleSaleInfoPo vehicleSaleInfoPo, List<VehicleUserVo> vehicleUserVos) {
        if(vehicleSaleInfoPo == null) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_SALE_INFO_NOT_EXIST);
        }
        //判断销售发票 -【发票代码】，【发票号码】与申请绑定的车辆购车发票记录不一致
        if (!submitBindDto.getInvoiceCode().equals(vehicleSaleInfoPo.getInvoiceCode()) ||
                !submitBindDto.getInvoiceNum().equals(vehicleSaleInfoPo.getInvoiceNum())) {
            if(vehicleUserVos != null && !vehicleUserVos.isEmpty()) {
                throw new AdamException(BusinessExceptionEnums.INVOICE_PURCHASER_CODE_NOT_IDENTICAL_BIND);
            }
            throw new AdamException(BusinessExceptionEnums.INVOICE_PURCHASER_CODE_NOT_IDENTICAL);
            //但【车辆识别代号 车架号码】，【发动机号码】与申请绑定的车辆信息一致
        } else if(vehicleUserVos != null && !vehicleUserVos.isEmpty()) {
            //【已绑定】状态A4-2返回“该车辆已被绑定，请先解绑“
            throw new AdamException(BusinessExceptionEnums.VEHICLE_HAS_BIND);
        }
        return vehicleSaleInfoPo;
    }
}
