package com.bnmotor.icv.tsp.device.service.thirdparty.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.DateUtil;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.BusinessExceptionEnums;
import com.bnmotor.icv.tsp.device.model.entity.VehicleBindInvoicePo;
import com.bnmotor.icv.tsp.device.model.entity.VehicleLicensePo;
import com.bnmotor.icv.tsp.device.service.thirdparty.IOcrService;
import com.bnmotor.icv.tsp.device.util.CustomAipOcr;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

/**
 * @ClassName: OcrUtil
 * @Description: ocr工具类
 * @author: huangyun1
 * @date 2020/09/24 *
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class OcrServiceImpl implements IOcrService {

	@Autowired
	private CustomAipOcr customAipOcr;
	/**
	 * words_result字符串
	 */
	private static final String WORDS_RESULT_STR = "words_result";
	/**
	 * 解析行驶证
	 * @param file
	 * @return
	 */
	@Override
	public RestResponse<VehicleLicensePo> analysisVehicleLicense(MultipartFile file) {
		RestResponse.RestResponseBuilder builder = RestResponse.builder();
		try {
			// 传入可选参数调用接口
			HashMap<String, String> options = new HashMap<String, String>(3);
			org.json.JSONObject resultObj = new JSONObject();
			options.put("detect_direction", "true");
			//options.put("accuracy", "normal");
			// 参数为本地图片二进制数组
			resultObj = customAipOcr.vehicleLicense(FileCopyUtils.copyToByteArray(file.getInputStream()), options);
			log.info("解析行驶证ocr返回:[{}]", resultObj);
			if (!resultObj.isNull(WORDS_RESULT_STR)) {
				builder.respCode(RespCode.SUCCESS.getValue());
				builder.respMsg(RespCode.SUCCESS.getDescription());
				//驾驶证
				builder.respData(appendVehicleLicenseData(resultObj));
			} else {
				builder.respCode(BusinessExceptionEnums.OCR_RECOGNIZED_VEH_LICENSE_ERROR.getValue());
				builder.respMsg(BusinessExceptionEnums.OCR_RECOGNIZED_VEH_LICENSE_ERROR.getDescription());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			builder.respCode(BusinessExceptionEnums.OCR_RECOGNIZED_VEH_LICENSE_ERROR.getValue());
			builder.respMsg(BusinessExceptionEnums.OCR_RECOGNIZED_VEH_LICENSE_ERROR.getDescription());
		}
		return builder.build();
	}

	/**
	 * 解析机动车销售发票信息
	 * @param file
	 * @return
	 */
	@Override
	public RestResponse<VehicleBindInvoicePo> analysisVehicleInvoice(MultipartFile file) {
		RestResponse.RestResponseBuilder builder = RestResponse.builder();
		try {
			org.json.JSONObject resultObj = new JSONObject();
			// 参数为本地图片二进制数组
			resultObj = customAipOcr.vehicleInvoice(FileCopyUtils.copyToByteArray(file.getInputStream()), null);
			log.info("解析机动车销售发票ocr返回:[{}]", resultObj);
			if (!resultObj.isNull(WORDS_RESULT_STR)) {
				builder.respCode(RespCode.SUCCESS.getValue());
				builder.respMsg(RespCode.SUCCESS.getDescription());
				//驾驶证
				builder.respData(appendVehicleInvoiceData(resultObj));
			} else {
				builder.respCode(BusinessExceptionEnums.OCR_RECOGNIZED_VEH_INVOICE_ERROR.getValue());
				builder.respMsg(BusinessExceptionEnums.OCR_RECOGNIZED_VEH_INVOICE_ERROR.getDescription());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			builder.respCode(BusinessExceptionEnums.OCR_RECOGNIZED_VEH_INVOICE_ERROR.getValue());
			builder.respMsg(BusinessExceptionEnums.OCR_RECOGNIZED_VEH_INVOICE_ERROR.getDescription());
		}
		return builder.build();
	}

	/**
	 * 拼装车辆行驶证信息
	 * @param resultObj
	 * @return
	 * @throws Exception
	 */
	private static VehicleLicensePo appendVehicleLicenseData(org.json.JSONObject resultObj) throws Exception {
		org.json.JSONObject result = resultObj.getJSONObject(WORDS_RESULT_STR);
		java.util.Iterator<String> ite = result.keys();
		VehicleLicensePo vo = new VehicleLicensePo();
		
		while (ite.hasNext()) {
			String key = ite.next();
			String value = result.getJSONObject(key).get("words").toString();
			switch (key) {
			case "所有人":
				vo.setUserName(value);
				break;
			case "车辆识别代号":
				vo.setVin(value);
				break;
			case "发动机号码":
				vo.setEngineNo(value);
				break;
			case "注册日期":
				if (StringUtil.isNotBlank(value)) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.TIME_MASK_4);
					value = DateUtil.formatDateToString(simpleDateFormat.parse(value), DateUtil.TIME_MASK_3);
				}
				vo.setRegisterDate(value);
				break;
			case "发证日期":
				if (StringUtil.isNotBlank(value)) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.TIME_MASK_4);
					value = DateUtil.formatDateToString(simpleDateFormat.parse(value), DateUtil.TIME_MASK_3);
				}
				vo.setIssueDate(value);
				break;
			default:
				break;
			}
			
		}
		return vo;
	}

	/**
	 * 拼装机动车销售发票信息
	 * @param resultObj
	 * @return
	 * @throws Exception
	 */
	private static VehicleBindInvoicePo appendVehicleInvoiceData(org.json.JSONObject resultObj) throws Exception {
		org.json.JSONObject result = resultObj.getJSONObject(WORDS_RESULT_STR);
		java.util.Iterator<String> ite = result.keys();
		VehicleBindInvoicePo vo = new VehicleBindInvoicePo();

		while (ite.hasNext()) {
			String key = ite.next();
			String value = result.getString(key);
			switch (key) {
				case "InvoiceNum":
					vo.setInvoiceNum(value);
					break;
				case "InvoiceCode":
					vo.setInvoiceCode(value);
					break;
				case "VinNum":
					vo.setVin(value);
					break;
				case "EngineNum":
					vo.setEngineNo(value);
					break;
				case "InvoiceDate":
					Date invoiceDate = DateUtil.parseStringToDate(value, DateUtil.TIME_MASK_3);
					Instant instant = invoiceDate.toInstant();
					vo.setInvoiceDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
					break;
				case "PurchaserCode":
					vo.setPurchaserCode(value);
					break;
				case "Purchaser":
					vo.setPurchaser(value);
					break;
				default:
					break;
			}

		}
		return vo;
	}

}
