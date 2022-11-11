package com.bnmotor.icv.tsp.ota.controller.inner;


import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.entity.DictionaryPo;
import com.bnmotor.icv.tsp.ota.service.IDictionaryDbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: DictionaryController
 * @Description: 字典表 controller层
 * @author xxc
 * @since 2020-06-17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@RestController
@RequestMapping(value="/v1/dict", method = RequestMethod.POST)
@Api(value="字典操作",tags={"字典操作"})
@AllArgsConstructor
public class DictionaryController extends AbstractController{
    private final IDictionaryDbService dictionaryDbService;

    @RequestMapping(value = "listAll")
    @ApiOperation(value="获取所有字典值列表", notes="获取所有字典值列表", response = DictionaryPo.class)
    @ResponseBody
    public ResponseEntity listAll(){
        return RestResponse.ok(dictionaryDbService.listAll());
    }
}
