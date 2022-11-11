package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.mapper.FotaFirmwareMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwareDbService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author xuxiaochang1
 * @ClassName: FotaFirmwarePo
 * @Description: OTA固件信息，定义各个零部件需要支持的OTA升级软件服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-06-11
 */

@Service
@Slf4j
public class FotaFirmwareDbServiceImpl extends ServiceImpl<FotaFirmwareMapper, FotaFirmwarePo> implements IFotaFirmwareDbService {

    /**
     * 根据固件Code获取固件
     *
     * @param firmwareCode
     * @return
     */
    @Override
    public List<FotaFirmwarePo> getByFrimwareCodes(String firmwareCode) {
        QueryWrapper<FotaFirmwarePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("firmware_code", firmwareCode);
        return baseMapper.selectList(queryWrapper);
    }

    /*private List<FotaFirmwarePo> checkExistFotaFirmware(String projectId, String firmwareCode) {
        List<FotaFirmwarePo> existList = getByFrimwareCodes(firmwareCode);
        if (MyCollectionUtil.isNotEmpty(existList)) {
            log.warn("exist firmware,quit.projectId={}, firmwareCode={}", projectId, firmwareCode);
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FIRMWARE_NEW_EXIST_ERROR);
        }
        return existList;
    }*/

    @Override
    public FotaFirmwarePo getByFrimwareCode(String firmwareCode) {
        QueryWrapper<FotaFirmwarePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("firmware_code", firmwareCode);

        List<FotaFirmwarePo> frimwareCodes = getByFrimwareCodes(/*projectId, */firmwareCode);
        return MyCollectionUtil.isNotEmpty(frimwareCodes) ? frimwareCodes.get(0) : null;
    }

    /**
     * 并发更新
     *
     * @param fotaFirmwarePo
     * @param version
     * @return
     */
    private boolean updateByIdWithVersion(FotaFirmwarePo fotaFirmwarePo, Integer version) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", fotaFirmwarePo.getId());
        updateWrapper.eq("version", version);
        return this.update(fotaFirmwarePo, updateWrapper);
    }

    @Override
    public List<FotaFirmwarePo> listFirmwareDos(Long treeNodeId) {
        QueryWrapper<FotaFirmwarePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tree_node_id", treeNodeId);
        List<FotaFirmwarePo> fotaFirmwarePos = list(queryWrapper);
        return MyCollectionUtil.newSortedList(fotaFirmwarePos, (o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()), FotaFirmwareDbServiceImpl::wrapFotaFirmwarePoStr);
    }

    @Override
    public List<FotaFirmwarePo> getByComponentCodeAndModel(String componentCode, String componentModel) {
        QueryWrapper<FotaFirmwarePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("firmware_code", componentCode);
        queryWrapper.eq("firmware_mode", componentModel);
        List<FotaFirmwarePo> frimwareCodes = list(queryWrapper);
        return MyCollectionUtil.safeList(frimwareCodes);
    }

    @Override
    public List<FotaFirmwarePo> listByTreeNodeId(Long treeNodeId) {
        QueryWrapper<FotaFirmwarePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tree_node_id", treeNodeId);
        List<FotaFirmwarePo> fotaFirmwarePos = list(queryWrapper);
        return fotaFirmwarePos;
    }

    /**
     * 添加前端需要的附加属性--包装FotaFirmwarePo
     *
     * @param item
     */
    private static void wrapFotaFirmwarePoStr(final FotaFirmwarePo item) {
        if (Objects.nonNull(item)) {
            item.setIdStr(MyCollectionUtil.long2Str(item.getId()));
            item.setTreeNodeIdStr(MyCollectionUtil.long2Str(item.getTreeNodeId()));
        }
    }
}
