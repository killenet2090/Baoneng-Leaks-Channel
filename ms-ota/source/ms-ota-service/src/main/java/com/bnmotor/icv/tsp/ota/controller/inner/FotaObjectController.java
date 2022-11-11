package com.bnmotor.icv.tsp.ota.controller.inner;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.page.PageResult;
import com.bnmotor.icv.tsp.ota.model.query.FotaObjectQuery;
import com.bnmotor.icv.tsp.ota.model.resp.FotaObjectVo;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName: FotaObjectController
 * @Description: OTA升级计划表 controller层
 * @author xxc
 * @since 2020-07-07
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Api(value="升级对象管理",tags={"升级对象管理"})
@RestController
@RequestMapping(value = "/v1/fotaobject")
public class FotaObjectController extends AbstractController {

	@Autowired
    private IFotaObjectService fotaObjectService;

	@Autowired
    private IFotaObjectDbService fotaObjectDbService;

    @ApiOperation(value = "分页查询升级对象", response = FotaObjectVo.class)
    @GetMapping("listPageByTreeNodeId")
    public ResponseEntity listPageByTreeNodeId(FotaObjectQuery query) {
        IPage<FotaObjectPo> iPage = fotaObjectDbService.listPageByTreeNodeId(query.getTreeNodeId(), query.getCurrent(), query.getPageSize());
        List<FotaObjectPo> list = iPage.getRecords();

        if(MyCollectionUtil.isNotEmpty(list)){
            AtomicBoolean flag = new AtomicBoolean(false);
            List<FotaObjectVo> fotaObjectVos = MyCollectionUtil.map2NewList(list, item ->{
                FotaObjectVo fotaObjectVO = new FotaObjectVo();
                /*fotaObjectVO.setCurrentArea(item.getc);*/
                fotaObjectVO.setVin(item.getObjectId());
                fotaObjectVO.setCurrentArea(item.getCurrentArea());
                fotaObjectVO.setSaleArea(item.getSaleArea());
                fotaObjectVO.setId(Long.toString(item.getId()));
                fotaObjectVO.setDisabled(item.getDisabled());
                flag.set(true);
                return fotaObjectVO;
            });

            return RestResponse.ok(new PageResult<>(query, fotaObjectVos));
//            return RestResponse.ok(list);
        }
        return RestResponse.ok(new PageResult<>(query, Collections.emptyList()));
//        return RestResponse.ok(Collections.emptyList());
    }
}