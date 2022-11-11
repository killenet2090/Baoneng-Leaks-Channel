package com.bnmotor.icv.tsp.device.controller.open;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.enums.img.ImgCategory;
import com.bnmotor.icv.tsp.device.common.enums.img.ImgType;
import com.bnmotor.icv.tsp.device.model.request.vehModel.ConfigImgDto;
import com.bnmotor.icv.tsp.device.service.IVehModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @ClassName: VehicleModelController
 * @Description: 2c车型管理相关接口
 * @author: zhangwei2
 * @date: 2020/11/16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/v1/vehModel")
@Api(value = "2c车型管理相关接口", tags = {"2c车型管理接口"})
@Slf4j
public class VehicleModelController {
    @Resource
    private IVehModelService modelService;

    /**
     * 查询车型下不同配置对应的图片信息
     */
    @GetMapping("/configPic/listAll")
    @ApiOperation(value = "查询车型下所有配置对应的部分图片信息")
    public ResponseEntity listConfigPics(@Valid ConfigImgDto picDto) {
        if (picDto.getModelId() == null && picDto.getYearId() == null && picDto.getConfigId() == null) {
            log.warn("查询车型所有配置图片请求参数不能同时为空");
            return RestResponse.error(RespCode.REQUEST_PARAMETER_MISSING);
        }

        ImgType imgType = ImgType.valueOf(picDto.getImgType());
        if (imgType == null) {
            return RestResponse.error(RespCode.USER_INVALID_INPUT);
        }

        ImgCategory imgCategory = ImgCategory.valueOf(picDto.getImgCategory());
        if (imgCategory == null) {
            return RestResponse.error(RespCode.USER_INVALID_INPUT);
        }

        return RestResponse.ok(modelService.listAllConfigPic(picDto));
    }

    /**
     * 查询车型下不同配置对应的图片信息
     */
    @GetMapping("/configPic/list")
    @ApiOperation(value = "查询车型下单个配置对应的所有图片信息")
    public ResponseEntity listConfigPicDetail(@RequestParam Long configId,
                                              @RequestParam(required = false, defaultValue = "1") Integer imgCategory,
                                              @RequestParam(required = false, defaultValue = "1") Integer imgType) {
        ImgType tempImgType = ImgType.valueOf(imgType);
        if (tempImgType == null) {
            return RestResponse.error(RespCode.USER_INVALID_INPUT);
        }

        ImgCategory tempImgCategory = ImgCategory.valueOf(imgCategory);
        if (tempImgCategory == null) {
            return RestResponse.error(RespCode.USER_INVALID_INPUT);
        }
        return RestResponse.ok(modelService.listConfigPic(configId, imgCategory, imgType));
    }

    /**
     * 查询所有车型年款信息
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有车型信息")
    public ResponseEntity listAllModel() {
        return RestResponse.ok(modelService.listAllModel());
    }

    /**
     * 查询所有年款配置
     */
    @GetMapping("/listYearConfig")
    @ApiOperation(value = "查询所有年款配置")
    public ResponseEntity listYearConfig(@RequestParam Long modelId) {
        return RestResponse.ok(modelService.listYearConfig(modelId));
    }

    /**
     * 查询所有年款配置
     */
    @GetMapping("/getRelationImg")
    @ApiOperation(value = "查看关联图片")
    public ResponseEntity getPic(@RequestParam Long imgId) {
        return RestResponse.ok(modelService.getRelationImg(imgId));
    }
}
