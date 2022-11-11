package com.bnmotor.icv.tsp.device.service.impl;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.BusinessExceptionEnums;
import com.bnmotor.icv.tsp.device.common.ReqContext;
import com.bnmotor.icv.tsp.device.common.VehLocalCache;
import com.bnmotor.icv.tsp.device.common.enums.dataaysn.MessageType;
import com.bnmotor.icv.tsp.device.mapper.VehicleTagMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehicleTagPo;
import com.bnmotor.icv.tsp.device.model.request.tag.EditVehTagDto;
import com.bnmotor.icv.tsp.device.model.request.tag.TagUsedCountDto;
import com.bnmotor.icv.tsp.device.model.request.feign.MqMessageDto;
import com.bnmotor.icv.tsp.device.model.response.tag.TagVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.OrgLocalCacheVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.VehicleVo;
import com.bnmotor.icv.tsp.device.service.IVehTagService;
import com.bnmotor.icv.tsp.device.service.IVehicleService;
import com.bnmotor.icv.tsp.device.service.feign.TagFeighService;
import com.bnmotor.icv.tsp.device.service.mq.producer.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: VehLabelServiceImpl
 * @Description: 车辆标签实现类
 * @author: zhangwei2
 * @date: 2020/7/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class VehTagServiceImpl implements IVehTagService {
    @Resource
    private VehicleTagMapper vehTagMapper;
    @Resource
    private TagFeighService tagFeighService;
    @Resource
    private IVehicleService vehicleService;
    @Resource
    private KafkaSender mqSender;
    @Resource
    private VehLocalCache vehLocalCache;

    @Override
    @Transactional
    public void addVehTag(EditVehTagDto editVehTagDto) {
        //查询车辆信息
        VehicleVo vehicleVo = vehicleService.getVehicleVo(editVehTagDto.getVin());
        if (vehicleVo == null) {
            throw new AdamException(BusinessExceptionEnums.VEHICLE_NOT_EXIST);
        }

        RestResponse<List<TagVo>> restResponse = tagFeighService.listTags(2l);
        List<TagVo> tags = restResponse.getRespData();
        if (!restResponse.isSuccess() || CollectionUtils.isEmpty(tags)) {
            return;
        }

        // 校验使用
        Map<Long, String> categoryIdToName = new HashMap<>();
        Map<Long, String> tagIdToName = new HashMap<>();
        Map<Long, List<String>> categoryIdToTagIds = new HashMap<>();
        for (TagVo tagVo : tags) {
            categoryIdToName.put(Long.parseLong(tagVo.getCategoryId()), tagVo.getCategoryName());
            tagIdToName.put(Long.parseLong(tagVo.getTagId()), tagVo.getTagName());
            List<String> tagIds = categoryIdToTagIds.get(Long.parseLong(tagVo.getCategoryId()));
            if (tagIds == null) {
                tagIds = new ArrayList<>();
                categoryIdToTagIds.put(Long.parseLong(tagVo.getCategoryId()), tagIds);
            }
            tagIds.add(String.valueOf(tagVo.getTagId()));
        }

        List<VehicleTagPo> relationPos = new ArrayList<>();
        //组装数据
        editVehTagDto.getCats().stream().forEach(categoryDto -> {
            if (!categoryIdToName.containsKey(categoryDto.getCategoryId())) {
                return;
            } else {
                categoryDto.setCategoryName(categoryIdToName.get(categoryDto.getCategoryId()));
            }

            categoryDto.getTags().stream().forEach(tagDto -> {
                        // 校验分类和标签是否在标签系统中存在,不存在直接过滤掉
                        List<String> tagIds = categoryIdToTagIds.get(categoryDto.getCategoryId());
                        if (!tagIds.contains(String.valueOf(tagDto.getTagId()))) {
                            return;
                        }

                        if (!tagIdToName.containsKey(tagDto.getTagId())) {
                            return;
                        } else {
                            tagDto.setTagName(tagIdToName.get(tagDto.getTagId()));
                        }

                        VehicleTagPo relationPo = new VehicleTagPo();
                        relationPo.setVin(editVehTagDto.getVin());
                        relationPo.setCategoryId(categoryDto.getCategoryId());
                        relationPo.setCategoryName(categoryDto.getCategoryName());
                        relationPo.setTagId(tagDto.getTagId());
                        relationPo.setTagName(tagDto.getTagName());
                        relationPo.setVersion(1);
                        relationPo.setDelFlag(0);
                        relationPo.setCreateBy(getCurrentUser());
                        relationPos.add(relationPo);
                    }
            );
        });

        // 更新标签系统同标签使用数统计
        List<String> vins = Arrays.asList(editVehTagDto.getVin());
        List<VehicleTagPo> vehTags = vehTagMapper.listByVinsAndCategoryId(vins, null);
        List<Long> tagIds = vehTags.stream().map(o -> o.getTagId()).collect(Collectors.toList());
        List<Long> newTagIds = relationPos.stream().map(o -> o.getTagId()).collect(Collectors.toList());
        List<Long> addTagsIds = (List<Long>) CollectionUtils.subtract(newTagIds, tagIds);
        List<Long> deleteTagIds = (List<Long>) CollectionUtils.subtract(tagIds, newTagIds);

        //直接物理删除
        vehTagMapper.deleteByVin(editVehTagDto.getVin());
        //车辆打标
        if (CollectionUtils.isNotEmpty(relationPos)) {
            vehTagMapper.saveAllInBatch(relationPos);
        }

        // 发送kafka消息
        MqMessageDto messageDto = new MqMessageDto();
        messageDto.setVin(editVehTagDto.getVin());
        messageDto.setType(MessageType.INCREMENT.getType());
        List<Long> labelIds = relationPos.stream().map(o -> o.getTagId()).collect(Collectors.toList());
        OrgLocalCacheVo cacheVo = vehLocalCache.getOrgById(Long.parseLong(vehicleVo.getOrgId()));
        if (cacheVo != null) {
            messageDto.setConfigId(cacheVo.getConfigurationId());
        }
        messageDto.setTagIds(labelIds);
        mqSender.sendMqMsg(messageDto);

        // 统计数据
        if (CollectionUtils.isEmpty(addTagsIds) && CollectionUtils.isEmpty(deleteTagIds)) {
            return;
        }

        TagUsedCountDto countDto = new TagUsedCountDto(addTagsIds, deleteTagIds);
        tagFeighService.countTag(countDto);
    }

    /**
     * 获取当前操作用户
     *
     * @return 获取获取不到，则默认System
     */
    private String getCurrentUser() {
        return ReqContext.getUid() != null ? String.valueOf(ReqContext.getUid()) : "System";
    }

    @Override
    public List<VehicleTagPo> listByVin(String vin) {
        return vehTagMapper.listByVin(vin);
    }
}

