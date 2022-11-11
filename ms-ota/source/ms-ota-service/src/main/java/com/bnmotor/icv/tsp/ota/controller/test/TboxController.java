package com.bnmotor.icv.tsp.ota.controller.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: TboxController
 * @Description: 查询指定车型下所有可升级的ECU固件清单，详见《OTA概要设计》6.7节 获取服务端配置
 * 
 * @author: xuxiaochang1
 * @date: 2020/7/8 9:35
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
/*@RestController
@RequestMapping("/v1/tbox")
@Api(value = "tbox => ota(http通信)", tags = { "tbox => ota(http通信)" })*/
@Slf4j
public class TboxController {

	/*@Autowired
	ITboxHttpSevice tboxHttpSevice;

    @ApiOperation("查询车辆配置（固件升级清单）")
	@PostMapping("/{vin}/get/server/config")
	public ResponseEntity queryVehicleEcuConfig(@PathVariable("vin") String vin) {
		log.info("查询服务器配置请求参数|vin={}", vin);
		EcuFirmwareConfigListVo ecuFirmwareConfigListVo = tboxHttpSevice.queryEcuConfigFirmwareDos(vin);
		log.info("查询到的固件清单数量|ecuFirmwareConfigListVo={}", MyCollectionUtil.size(Objects.nonNull(ecuFirmwareConfigListVo) ? ecuFirmwareConfigListVo.getEcus() : null));
		return RestResponse.ok(ecuFirmwareConfigListVo);
	}

	@RequestMapping(value = "/{vin}/file/upload", method = RequestMethod.POST)
	@ApiOperation(value="日志和升级过程文件上传", notes="日志和升级过程文件上传")
	@ResponseBody
	@NoPrintParam
	public ResponseEntity upload(@RequestPart("file") MultipartFile file, @PathVariable("vin") String vin, LogFileUploadDto logFileUploadDto){
		log.info("查询服务器配置请求参数|vin={}, logFileUploadDto={}", vin, Objects.toString(logFileUploadDto));
		tboxHttpSevice.uploadFile(file, vin, logFileUploadDto.getTransId(), logFileUploadDto.getTaskId());
		return RestResponse.ok(null);
	}*/
}