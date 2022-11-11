package com.bnmotor.icv.tsp.ota.service.impl;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: FotaDeviceSyncServiceImpl
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/10/27 11:40
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Deprecated
//@Service("fotaDeviceSyncService")
@Slf4j
public class FotaDeviceSyncServiceImpl /*implements IFotaDeviceSyncService*/ {

    /*@Autowired
    private IDeviceTreeNodeDbService deviceTreeNodeDbService;

    @Autowired
    private IFotaObjectDbService fotaObjectDbService;

    @Autowired
    private IFotaObjectService fotaObjectService;

    @Autowired
    private IFotaFirmwareListDbService fotaFirmwareListDbService;

    @Autowired
    private IFotaFirmwareService fotaFirmwareService;

    @Autowired
    private IFotaFirmwareDbService fotaFirmwareDbService;

    @Autowired
    private IDeviceComponentDbService deviceComponentDbService;

    @Autowired
    private IFotaComponentListDbService fotaComponentListDbService;

    @Autowired
    private IFotaObjectLabelDbService fotaObjectLabelDbService;

    @Autowired
    private IFotaObjectComponentListDbService fotaObjectComponentListDbService;

    @Autowired
    private FotaDeviceSyncInner fotaDeviceSyncInner;

    @Autowired
    private MsDeviceFeignService msDeviceFeignService;

    @Override
    public void buildTreeNodeFromTspDevice(){
        RestResponse<VehAllLevelVo>  restResponse = msDeviceFeignService.getOrgRelations();
        MyAssertUtil.notNull(restResponse, "获取TSP设备车型配置数据异常");
        MyAssertUtil.notNull(restResponse.getRespData(), "获取TSP设备车型配置数据异常");

        VehAllLevelVo vehAllLevelVo = restResponse.getRespData();

        Map<DeviceTreeNodeLevelEnum, Map> deviceTreeNodeLevelEnumMapMap = Maps.newHashMap();
        Map<Long, VehBrandVo> vehBrandVoMap = vehAllLevelVo.getBrands().stream().collect(Collectors.toMap(VehBrandVo::getId, Function.identity(), (x, y) -> x));
        Map<Long, VehSeriesVo> vehSeriesVoMap = vehAllLevelVo.getSeries().stream().collect(Collectors.toMap(VehSeriesVo::getId, Function.identity(), (x, y) -> x));
        Map<Long, VehModelVo> vehModelVoMap = vehAllLevelVo.getModels().stream().collect(Collectors.toMap(VehModelVo::getId, Function.identity(), (x, y) -> x));
        Map<Long, VehYearStyleVo> vehYearStyleVoMap = vehAllLevelVo.getYearStyles().stream().collect(Collectors.toMap(VehYearStyleVo::getId, Function.identity(), (x, y) -> x));
        Map<Long, VehConfigurationVo> vehConfigurationVoMap = vehAllLevelVo.getConfigurations().stream().collect(Collectors.toMap(VehConfigurationVo::getId, Function.identity(), (x, y) -> x));
        deviceTreeNodeLevelEnumMapMap.put(DeviceTreeNodeLevelEnum.BRAND, vehBrandVoMap);
        deviceTreeNodeLevelEnumMapMap.put(DeviceTreeNodeLevelEnum.SERIES, vehSeriesVoMap);
        deviceTreeNodeLevelEnumMapMap.put(DeviceTreeNodeLevelEnum.MODEL, vehModelVoMap);
        deviceTreeNodeLevelEnumMapMap.put(DeviceTreeNodeLevelEnum.YEAR, vehYearStyleVoMap);
        deviceTreeNodeLevelEnumMapMap.put(DeviceTreeNodeLevelEnum.CONF, vehConfigurationVoMap);

        EnumSet.allOf(DeviceTreeNodeLevelEnum.class).stream().sorted(Comparator.comparingInt(DeviceTreeNodeLevelEnum::getLevel)).forEach(enumItem -> {
            Map<Long, ?> map = deviceTreeNodeLevelEnumMapMap.get(enumItem);
            //针对本级进行处理
            for (Map.Entry<Long, ?> longEntry : map.entrySet()) {
                try {
                    //筛选出本级节点类型及上级
                    AtomicLong tempId = new AtomicLong(0L);
                    //处理品牌
                    if(longEntry.getValue() instanceof VehBrandVo){
                        tempId.set(((VehBrandVo) longEntry.getValue()).getId());
                    }else if(longEntry.getValue() instanceof VehSeriesVo){
                        //处理车系
                        tempId.set(((VehSeriesVo) longEntry.getValue()).getId());
                    }else if(longEntry.getValue() instanceof VehModelVo){
                        //处理车型
                        tempId.set(((VehModelVo) longEntry.getValue()).getId());
                    }else if(longEntry.getValue() instanceof VehYearStyleVo){
                        //处理年款
                        tempId.set(((VehYearStyleVo) longEntry.getValue()).getId());
                    }else if(longEntry.getValue() instanceof VehConfigurationVo){
                        //处理配置
                        tempId.set(((VehConfigurationVo) longEntry.getValue()).getId());
                    }

                    AtomicReference<VehBrandVo> vehBrandVoAtomicReference = new AtomicReference<>();
                    AtomicReference<VehSeriesVo> vehSeriesVoAtomicReference = new AtomicReference<>();
                    AtomicReference<VehModelVo> vehModelVoAtomicReference = new AtomicReference<>();
                    AtomicReference<VehYearStyleVo> vehYearStyleVoAtomicReference = new AtomicReference<>();
                    AtomicReference<VehConfigurationVo> vehConfigurationVoAtomicReference = new AtomicReference<>();
                    //需要保证顺序：倒序排列
                    EnumSet.allOf(DeviceTreeNodeLevelEnum.class).stream().filter(enumItem1 -> enumItem1.getLevel() <= enumItem.getLevel()).sorted(Comparator.comparingInt(DeviceTreeNodeLevelEnum::getLevel).reversed()).forEach(enumItem1 ->{

                        Long tempIdValue = tempId.get();
                        Object tempObject = deviceTreeNodeLevelEnumMapMap.get(enumItem1).get(tempIdValue);
                        log.info("tempObject={}", tempObject);

                        //处理品牌
                        if(tempObject instanceof VehBrandVo){
                            vehBrandVoAtomicReference.set(((VehBrandVo) tempObject));
                        }else if(tempObject instanceof VehSeriesVo){
                            //处理车系
                            tempId.set(((VehSeriesVo) tempObject).getBrandId());
                            vehSeriesVoAtomicReference.set(((VehSeriesVo) tempObject));
                        }else if(tempObject instanceof VehModelVo){
                            //处理车型
                            tempId.set(((VehModelVo) tempObject).getSeriesId());
                            vehModelVoAtomicReference.set(((VehModelVo) tempObject));
                        }else if(tempObject instanceof VehYearStyleVo){
                            //处理年款
                            tempId.set(((VehYearStyleVo) tempObject).getModelId());
                            vehYearStyleVoAtomicReference.set(((VehYearStyleVo) tempObject));
                        }else if(tempObject instanceof VehConfigurationVo){
                            //处理配置
                            tempId.set(((VehConfigurationVo) tempObject).getYearStyleId());
                            vehConfigurationVoAtomicReference.set(((VehConfigurationVo) tempObject));
                        }
                    });

                    saveFotaDeviceTreeNodePo(vehBrandVoAtomicReference.get(), vehSeriesVoAtomicReference.get(), vehModelVoAtomicReference.get(), vehYearStyleVoAtomicReference.get(), vehConfigurationVoAtomicReference.get());
                }catch (Exception e){
                    log.error("生成设备树节点异常", e);
                }
            }
        });
    }

    *//**
     * 测试数据
     * @return
     *//*
    private VehAllLevelVo initVehAllLevelVo(){
        VehAllLevelVo vehAllLevelVo = new VehAllLevelVo();
        *//**
         * 车辆品牌
         *//*
        List<VehBrandVo> brands = Lists.newArrayList();
        List<VehSeriesVo> series = Lists.newArrayList();
        List<VehModelVo> models = Lists.newArrayList();
        List<VehYearStyleVo> yearStyles = Lists.newArrayList();
        List<VehConfigurationVo> configurations = Lists.newArrayList();

        AtomicLong id1 = new AtomicLong(0L);
        AtomicLong id2 = new AtomicLong(0);
        AtomicLong id3 = new AtomicLong(0);
        AtomicLong id4 = new AtomicLong(0);
        AtomicLong id5 = new AtomicLong(0);
        IntStream.rangeClosed(1, 2).forEach( item1 -> {
            id1.incrementAndGet();
            VehBrandVo vehBrandVo = new VehBrandVo();
            vehBrandVo.setId(id1.get());
            vehBrandVo.setCode("xxc_brand_" + item1);
            vehBrandVo.setName("牛逼特斯拉"+item1+"号");
            brands.add(vehBrandVo);

            IntStream.rangeClosed(1, 2).forEach( item2 -> {
                id2.incrementAndGet();
                VehSeriesVo vehSeriesVo = new VehSeriesVo();
                vehSeriesVo.setId(id2.get());
                vehSeriesVo.setCode("xxc_series_" + item2);
                vehSeriesVo.setName("X系列"+item2+"号");
                vehSeriesVo.setBrandId(vehBrandVo.getId());
                series.add(vehSeriesVo);


                IntStream.rangeClosed(1, 2).forEach( item3 -> {
                    id3.incrementAndGet();
                    VehModelVo vehModelVo = new VehModelVo();
                    vehModelVo.setId(id3.get());
                    vehModelVo.setCode("xxc_model_" + item3);
                    vehModelVo.setName("B车型"+item3+"号");
                    vehModelVo.setSeriesId(vehSeriesVo.getId());
                    models.add(vehModelVo);

                    IntStream.rangeClosed(1, 2).forEach( item4 -> {
                        id4.incrementAndGet();
                        VehYearStyleVo vehYearStyleVo = new VehYearStyleVo();
                        vehYearStyleVo.setId(id4.get());
                        vehYearStyleVo.setCode("xxc_year_" + item4);
                        vehYearStyleVo.setName("202"+item4);
                        vehYearStyleVo.setModelId(vehModelVo.getId());
                        yearStyles.add(vehYearStyleVo);

                        IntStream.rangeClosed(1, 2).forEach( item5 -> {
                            id5.incrementAndGet();
                            VehConfigurationVo vehConfigurationVo = new VehConfigurationVo();
                            vehConfigurationVo.setId(id5.get());
                            vehConfigurationVo.setCode("xxc_conf_" + item5);
                            vehConfigurationVo.setName("conf"+item5);
                            vehConfigurationVo.setYearStyleId(vehYearStyleVo.getId());
                            configurations.add(vehConfigurationVo);
                        });
                    });
                });
            });
        });

        vehAllLevelVo.setBrands(brands);
        vehAllLevelVo.setSeries(series);
        vehAllLevelVo.setModels(models);
        vehAllLevelVo.setYearStyles(yearStyles);
        vehAllLevelVo.setConfigurations(configurations);
        return vehAllLevelVo;
    }

    *//**
     * 补全路径
     * @param sb
     * @param addStr
     *//*
    private void add2Path(StringBuilder sb, String addStr){
        if(!StringUtils.isEmpty(addStr)){
            sb.append("/" + addStr);
        }
    }

    *//**
     * 获取实际的设备树节点层次
     * @param deviceTreeNodeLevelEnum
     * @param otherDeviceTreeNodeLevelEnum
     * @return
     *//*
    private DeviceTreeNodeLevelEnum selectDeviceTreeNodeLevelNum(DeviceTreeNodeLevelEnum deviceTreeNodeLevelEnum, DeviceTreeNodeLevelEnum otherDeviceTreeNodeLevelEnum){
        return Objects.nonNull(deviceTreeNodeLevelEnum) ? deviceTreeNodeLevelEnum : otherDeviceTreeNodeLevelEnum;
    }

    *//**
     * 构建FotaDeviceTreeNodeDto
     * @param vehBrandVo
     * @param vehSeriesVo
     * @param vehModelVo
     * @param vehYearStyleVo
     * @param vehConfigurationVo
     *//*
    private void saveFotaDeviceTreeNodePo(VehBrandVo vehBrandVo, VehSeriesVo vehSeriesVo, VehModelVo vehModelVo, VehYearStyleVo vehYearStyleVo, VehConfigurationVo vehConfigurationVo){
        StringBuilder sb = new StringBuilder();
        add2Path(sb, Objects.nonNull(vehBrandVo) ? vehBrandVo.getName() : null);
        add2Path(sb, Objects.nonNull(vehSeriesVo) ? vehSeriesVo.getName() : null);
        add2Path(sb, Objects.nonNull(vehModelVo) ? vehModelVo.getName() : null);
        add2Path(sb, Objects.nonNull(vehYearStyleVo) ? vehYearStyleVo.getName() : null);
        add2Path(sb, Objects.nonNull(vehConfigurationVo) ? vehConfigurationVo.getName() : null);
        String nodeNamePath = sb.toString();
        log.info("nodeNamePath={}", nodeNamePath);

        DeviceTreeNodePo deviceTreeNodePo = getDeviceTreeNodeByNodeNamePath(nodeNamePath);
        if(Objects.isNull(deviceTreeNodePo)){
            FotaDeviceTreeNodeDto fotaDeviceTreeNodeDto = new FotaDeviceTreeNodeDto();
            DeviceTreeNodeLevelEnum deviceTreeNodeLevelEnum = null;
            if(Objects.nonNull(vehConfigurationVo)){
                fotaDeviceTreeNodeDto.setConfCode(vehConfigurationVo.getCode());
                fotaDeviceTreeNodeDto.setConf(vehConfigurationVo.getName());
                deviceTreeNodeLevelEnum = selectDeviceTreeNodeLevelNum(deviceTreeNodeLevelEnum, DeviceTreeNodeLevelEnum.CONF);
            }

            if(Objects.nonNull(vehYearStyleVo)){
                fotaDeviceTreeNodeDto.setYearCode(vehYearStyleVo.getCode());
                fotaDeviceTreeNodeDto.setYear(vehYearStyleVo.getName());
                deviceTreeNodeLevelEnum = selectDeviceTreeNodeLevelNum(deviceTreeNodeLevelEnum, DeviceTreeNodeLevelEnum.YEAR);
            }

            if(Objects.nonNull(vehModelVo)){
                fotaDeviceTreeNodeDto.setModelCode(vehModelVo.getCode());
                fotaDeviceTreeNodeDto.setModel(vehModelVo.getName());
                deviceTreeNodeLevelEnum = selectDeviceTreeNodeLevelNum(deviceTreeNodeLevelEnum, DeviceTreeNodeLevelEnum.MODEL);
            }

            if(Objects.nonNull(vehSeriesVo)){
                fotaDeviceTreeNodeDto.setSeriesCode(vehSeriesVo.getCode());
                fotaDeviceTreeNodeDto.setSeries(vehSeriesVo.getName());
                deviceTreeNodeLevelEnum = selectDeviceTreeNodeLevelNum(deviceTreeNodeLevelEnum, DeviceTreeNodeLevelEnum.SERIES);
            }

            if(Objects.nonNull(vehBrandVo)){
                fotaDeviceTreeNodeDto.setBrandCode(vehBrandVo.getCode());
                fotaDeviceTreeNodeDto.setBrand(vehBrandVo.getName());
                deviceTreeNodeLevelEnum = selectDeviceTreeNodeLevelNum(deviceTreeNodeLevelEnum, DeviceTreeNodeLevelEnum.BRAND);
            }
            fotaDeviceTreeNodeDto.setTreeLevel(deviceTreeNodeLevelEnum.getLevel());
            addFotaDeviceTreeNodeInner(fotaDeviceTreeNodeDto, deviceTreeNodeLevelEnum);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long syncFotaCar(FotaCarInfoDto fotaCarInfoDto, DeviceTreeNodePo deviceTreeNodePo, FotaObjectPo existFotaObjectPo) {
        MyAssertUtil.notNull(fotaCarInfoDto.getVehDevices(), "车辆零件列表不能为空");

        if(Objects.isNull(existFotaObjectPo)) {
            existFotaObjectPo = fotaObjectDbService.findByVin(fotaCarInfoDto.getVin());
        }

        Long nodeId = deviceTreeNodePo.getId();

        FotaObjectPo fotaObjectPo = OtaMessageMapper.INSTANCE.fotaCarInfoDto2FotaObjectDo(fotaCarInfoDto);
        fotaObjectPo.setProjectId(deviceTreeNodePo.getProjectId());
        fotaObjectPo.setTreeNodeId(nodeId);
        fotaObjectPo.setTreeNodeCodePath(deviceTreeNodePo.getNodeCodePath());

        long objectId;
        *//*FotaObjectPo existFotaObjectPo = fotaObjectDbService.findByVin(fotaCarInfoDto.getVin());
        fotaObjectPo.setObjectId(fotaCarInfoDto.getVin());
        fotaObjectPo.setProjectId(deviceTreeNodePo.getProjectId());*//*

        boolean addDeviceComponent;
        if(Objects.nonNull(existFotaObjectPo)){
            log.info("车辆信息已存在，需要更新车辆属性");
            fotaObjectPo.setId(existFotaObjectPo.getId());
            objectId = existFotaObjectPo.getId();
            CommonUtil.wrapBasePo(fotaObjectPo, false);
            boolean update = fotaObjectDbService.updateById(fotaObjectPo);
            log.info("fotaObjectDo.getId={}, update={}", fotaObjectPo.getId(), update);
            addDeviceComponent = update;
        }else{
            fotaObjectPo.setId(IdWorker.getId());
            objectId = fotaObjectPo.getId();
            CommonUtil.wrapBasePo(fotaObjectPo, true);

            boolean save = fotaObjectDbService.save(fotaObjectPo);
            log.info("fotaObjectDo.getId={}, save={}", objectId, save);
            addDeviceComponent = save;
        }

        //TODO
        *//*
            需要考虑一些问题
            1、如果多次同步，之前的固件信息如何处理？原则上零件与固件的信息由OTA云端维护
         *//*
        //如果车辆更新没问题，则需要添加车辆固件列表
        if(addDeviceComponent){
            //保存车辆零件信息
            fotaObjectComponentListDbService.delByFotaObjectIdPysical(fotaObjectPo.getId());

            List<FotaObjectComponentListPo> fotaObjectComponentListPos = MyCollectionUtil.map2NewList(fotaCarInfoDto.getVehDevices(), item ->{
                FotaObjectComponentListPo fotaObjectComponentListPo = new FotaObjectComponentListPo();
                fotaObjectComponentListPo.setId(IdWorker.getId());
                fotaObjectComponentListPo.setOtaObjectId(fotaObjectPo.getId());
                fotaObjectComponentListPo.setProjectId(fotaObjectPo.getProjectId());
                fotaObjectComponentListPo.setComponentType(item.getComponentType());
                fotaObjectComponentListPo.setComponentTypeName(item.getComponentTypeName());
                fotaObjectComponentListPo.setComponentModel(item.getComponentModel());
                fotaObjectComponentListPo.setComponentName(item.getComponentName());
                CommonUtil.wrapBasePo(fotaObjectComponentListPo, true);
                return fotaObjectComponentListPo;
            });
            if(MyCollectionUtil.isNotEmpty(fotaObjectComponentListPos)){
                fotaObjectComponentListDbService.saveBatch(fotaObjectComponentListPos);
            }

            List<FotaFirmwarePo> fotaFirmwarePos = fotaFirmwareService.listFirmwarePos(deviceTreeNodePo.getId());
            MyAssertUtil.notNull(fotaFirmwarePos, "车型固件列表不能为空");
            final Map<String, List<FotaFirmwarePo>> fotaFirmwarePoMap = fotaFirmwarePos.stream().collect(Collectors.groupingBy(item -> item.getComponentCode() + "-" + item.getComponentModel(),
                    Collectors.mapping(Function.identity(), Collectors.toList())));

            //获取可能已经存在的车辆升级固件列表
            final List<FotaFirmwareListPo> existFotaFirmwareListPos = fotaFirmwareListDbService.listAllByObjectId(fotaObjectPo.getId());
            final Map<Long, FotaFirmwareListPo> fotaFirmwareListPoMap = MyCollectionUtil.isNotEmpty(existFotaFirmwareListPos) ? existFotaFirmwareListPos.stream().collect(Collectors.toMap(FotaFirmwareListPo::getFirmwareId, Function.identity())) : Maps.newHashMap();
            fotaCarInfoDto.getVehDevices().forEach(item -> {
                //TSP设备管理系统同步过来的数据种只定义了componentType，需要补全信息
                item.setComponentCode(item.getComponentType());
                String componentKey = item.getComponentCode() + "-" + item.getComponentModel();
                List<FotaFirmwarePo> itemFotaFirmwarePos = fotaFirmwarePoMap.get(componentKey);
                //如果不为空
                if(Objects.nonNull(itemFotaFirmwarePos)){
                    //List<fotaFirmwareListPo> fotaFirmwareListPos = Lists.newArrayListWithCapacity(itemFotaFirmwarePos.size());
                    itemFotaFirmwarePos.forEach(itemFotaFirmwarePo -> {
                        //如果已经存在
                        if(Objects.nonNull(fotaFirmwareListPoMap.get(itemFotaFirmwarePo.getId()))){
                            log.info("升级车辆中已经存在该固件信息，不需要重复添加.vin={}, item={}", fotaCarInfoDto.getVin(), item);
                            return;
                        }
                        FotaFirmwareListPo fotaFirmwareListPo = new FotaFirmwareListPo();
                        fotaFirmwareListPo.setId(IdWorker.getId());
                        fotaFirmwareListPo.setOtaObjectId(fotaObjectPo.getId());
                        fotaFirmwareListPo.setProjectId(fotaObjectPo.getProjectId());
                        fotaFirmwareListPo.setFirmwareId(itemFotaFirmwarePo.getId());
                        fotaFirmwareListPo.setComponentId(itemFotaFirmwarePo.getComponentCode());
                        CommonUtil.wrapBasePo(fotaFirmwareListPo, true);
                        log.info("添加车辆中该固件列表信息。vin={}, fotaFirmwareListPo={}", fotaCarInfoDto.getVin(), fotaFirmwareListPo);
                        boolean save = fotaFirmwareListDbService.save(fotaFirmwareListPo);
                        log.info("车辆该固件信息结果.vin={}, save={}", fotaCarInfoDto.getVin(), save);
                    });

                }else{
                    log.warn("OTA云端管理的固件列表不存在该零件对应的固件信息.vin={}, item={}", fotaCarInfoDto.getVin(), item);
                }
            });
        }
        return objectId;
    }

    @Override
    public FotaDeviceSyncResult process4Firmware(FotaDeviceSyncMessage fotaDeviceSyncMessage){
        FotaDeviceSyncResult fotaDeviceSyncResult = new FotaDeviceSyncResult();
        FotaFirmwarePo fotaFirmwareDo = fotaFirmwareDbService.getById(fotaDeviceSyncMessage.getFirmwareId());
        List<FotaObjectPo> fotaObjectDos = fotaObjectDbService.listAllByTreeNodeId(fotaDeviceSyncMessage.getTreeNodeId());
        if(MyCollectionUtil.isEmpty(fotaObjectDos)){
            return fotaDeviceSyncResult;
        }
        log.info("size={}", fotaObjectDos.size());
        fotaDeviceSyncResult.setTotal(fotaObjectDos.size());
        AtomicInteger succes = new AtomicInteger(0);
        AtomicInteger noNeed = new AtomicInteger(0);
        List<FotaDeviceSyncResult.FotaDeviceSyncDetail> fotaDeviceSyncDetailList = Lists.newArrayList();
        fotaObjectDos.forEach(item ->{
            try {
*//*
                //如果是新增固件
                if(fotaDeviceSyncMessage.isSave()) {
                    fotaObjectService.process4AddFotaFirmware(item, fotaFirmwareDo);
                }else{
                    //如果是删除固件
                    fotaObjectService.process4DelFotaFirmware(item.getId(), fotaFirmwareDo.getId());
                }*//*
                succes.incrementAndGet();
            }catch (Exception e){
                log.error("处理车辆固件列表更新异常.e.getMessage={}", e.getMessage(), e);
                FotaDeviceSyncResult.FotaDeviceSyncDetail deviceSyncDetail = buildFotaDeviceSyncDetail(item,"固件列表中数据已经设置，无需重复设置");
                fotaDeviceSyncDetailList.add(deviceSyncDetail);
            }
        });

        fotaDeviceSyncResult.setSuccess(succes.get());
        fotaDeviceSyncResult.setNoNeed(noNeed.get());
        return fotaDeviceSyncResult;
    }

    private FotaDeviceSyncResult.FotaDeviceSyncDetail buildFotaDeviceSyncDetail(FotaObjectPo fotaObjectDo, String errorMsg){
        FotaDeviceSyncResult.FotaDeviceSyncDetail deviceSyncDetail = new FotaDeviceSyncResult.FotaDeviceSyncDetail();
        deviceSyncDetail.setErrorMsg(errorMsg);
        deviceSyncDetail.setObjectId(fotaObjectDo.getId());
        deviceSyncDetail.setVin(fotaObjectDo.getObjectId());

        return deviceSyncDetail;
    }

    *//**
     * 获取配置级别设备树节点
     *
     * @param obj
     * @param clz
     * @param tobeSaveSupplier
     * @param <T>
     * @return link{#DeviceTreeNodePo}
     *//*
    @Override
    public  <T> DeviceTreeNodePo getConfLevelDeviceTreeNode(Object obj, Class<T> clz, Supplier<FotaDeviceTreeNodeDto> tobeSaveSupplier){
        DeviceTreeNodePo deviceTreeNodePo =  getDeviceTreeNodeByNodeNamePath(obj, clz, DeviceTreeNodeLevelEnum.CONF, false);
        if(Objects.isNull(deviceTreeNodePo)){
            log.info("当前设备树车型节点数据未建立，需要新建车型设备树节点。fotaCarInfoDto={}", obj);

            final FotaDeviceTreeNodeDto fotaDeviceTreeNodeDto = tobeSaveSupplier.get();
            //按照节点层次从根层到叶子层新建树形节点
            EnumSet.allOf(DeviceTreeNodeLevelEnum.class).stream().sorted(Comparator.comparingInt(DeviceTreeNodeLevelEnum::getLevel)).forEach(enumItem -> {
                log.info("enumItem={}", enumItem.toString());
                Runnable r = () -> {
                    log.info("--- addFotaDeviceTreeNodeInner start ---");
                    addFotaDeviceTreeNodeInner(fotaDeviceTreeNodeDto, enumItem);
                    log.info("--- addFotaDeviceTreeNodeInner end ---");
                };
                String lockName = "OTA_DEVICE_SYNC_" + buildNodeNamePath(obj, clz, enumItem, false);
                log.info("lockName={}", lockName);
                fotaDeviceSyncInner.handle(lockName, r);
            });
            deviceTreeNodePo = getDeviceTreeNodeByNodeNamePath(obj, clz, DeviceTreeNodeLevelEnum.CONF, false);
            MyAssertUtil.notNull(deviceTreeNodePo, "设备树节点未找到，请检查是否已经添加了节点数据");
        }
        return deviceTreeNodePo;
    }

    @Override
    public <T> DeviceTreeNodePo getConfLevelDeviceTreeNode(Object obj, Class<T> clz) {
        DeviceTreeNodePo deviceTreeNodePo =  getDeviceTreeNodeByNodeNamePath(obj, clz, DeviceTreeNodeLevelEnum.CONF, false);
        log.info("deviceTreeNodePo={}", deviceTreeNodePo);
        return deviceTreeNodePo;
    }

    *//**
     * 添加设备树节点内部函数
     *
     * @param fotaDeviceTreeNodeDto
     * @param deviceTreeNodeLevelEnum
     * @return
     *//*
    private long addFotaDeviceTreeNodeInner(FotaDeviceTreeNodeDto fotaDeviceTreeNodeDto, DeviceTreeNodeLevelEnum deviceTreeNodeLevelEnum) {
        MyAssertUtil.notNull(deviceTreeNodeLevelEnum, "设备树节点层次非法");
        log.info("fotaDeviceTreeNodeDto={}", fotaDeviceTreeNodeDto);
        MyValidationUtil.validate(fotaDeviceTreeNodeDto);

        DeviceTreeNodePo parent = null;
        Long parentId = null;
        Long rootId = null;
        //如果是非顶层
        if(!deviceTreeNodeLevelEnum.equals(DeviceTreeNodeLevelEnum.BRAND)){
            parent =  getDeviceTreeNodeByNodeNamePath(fotaDeviceTreeNodeDto, FotaDeviceTreeNodeDto.class, deviceTreeNodeLevelEnum, true);
            MyAssertUtil.notNull(parent, "父设备树节点未找到，请检查是否已经添加了节点数据");
            parentId = parent.getId();

            //获取rootId
            DeviceTreeNodePo root = getDeviceTreeNodeByTopName(fotaDeviceTreeNodeDto.getBrand());
            MyAssertUtil.notNull(deviceTreeNodeLevelEnum, "设备树目录节点未找到");
            rootId = root.getId();
        }

        log.info("parentId={}, level={}", parentId, fotaDeviceTreeNodeDto.getTreeLevel());
        List<DeviceTreeNodePo> deviceTreeNodePos = deviceTreeNodeDbService.listChildren(parentId);
        int orderNum = 0;
        String code = getValue(fotaDeviceTreeNodeDto, deviceTreeNodeLevelEnum, 1);
        String name = getValue(fotaDeviceTreeNodeDto, deviceTreeNodeLevelEnum, 2);
        if(MyCollectionUtil.isNotEmpty(deviceTreeNodePos)){
            DeviceTreeNodePo existDeviceTreeNodePo = deviceTreeNodePos.stream().filter(item -> item.getNodeName().equals(name)).findFirst().orElse(null);
            if(Objects.nonNull(existDeviceTreeNodePo)){
                log.info("该层级节点已存在，不需要重复添加.id={}", existDeviceTreeNodePo.getId());
                return existDeviceTreeNodePo.getId();
            }
            orderNum = deviceTreeNodePos.stream().max(Comparator.comparingInt(DeviceTreeNodePo::getOrderNum)).get().getOrderNum();
        }

        long id = saveDeviceTreeNodePo(parent, name, code, orderNum + 1, deviceTreeNodeLevelEnum, rootId);
        log.info("生成的设备树节点id={}", id);
        return id;
    }

    private DeviceComponentPo getDeviceComponentPo(String componentCode, String componentModel){
        //零件库中零件是否已经存在
        QueryWrapper<DeviceComponentPo> deviceComponentPoQueryWrapper = new QueryWrapper<>();
        deviceComponentPoQueryWrapper.eq("component_code", componentCode);
        deviceComponentPoQueryWrapper.eq("component_model", componentModel);
        DeviceComponentPo deviceCompnentPo = deviceComponentDbService.getOne(deviceComponentPoQueryWrapper);
        return deviceCompnentPo;
    }

    @Override
    public void syncFotaDeviceComponentInfo(FotaDeviceComponentInfoDto fotaDeviceComponentInfoDto, DeviceTreeNodePo deviceTreeNodePo, DeviceComponentPo deviceCompnentPo) {
        //循环检查是否需要新建树形节点
        *//*fotaDeviceComponentInfoDtoList.forEach(item -> {*//*
            //ComponentCode -->标识零件类型
        fotaDeviceComponentInfoDto.setComponentCode(fotaDeviceComponentInfoDto.getComponentType());
           *//* Supplier<FotaDeviceTreeNodeDto> supplier = () -> DeviceInfoSyncMapper.INSTANCE.fotaDeviceComponentInfoDto2FotaDeviceTreeNodeDto(deviceTreeNodePo);
            DeviceTreeNodePo deviceTreeNodePo =  getConfLevelDeviceTreeNode(deviceTreeNodePo, FotaDeviceComponentInfoDto.class, supplier);*//*

            //零件库中零件是否已经存在
            *//*QueryWrapper<DeviceComponentPo> deviceComponentPoQueryWrapper = new QueryWrapper<DeviceComponentPo>();
            deviceComponentPoQueryWrapper.eq("component_code", fotaDeviceComponentInfoDto.getComponentCode());
            deviceComponentPoQueryWrapper.eq("component_model", fotaDeviceComponentInfoDto.getComponentModel());
            DeviceComponentPo deviceCompnentPo = deviceComponentDbService.getOne(deviceComponentPoQueryWrapper);*//*

            if(Objects.isNull(deviceCompnentPo)) {
                deviceCompnentPo = getDeviceComponentPo(fotaDeviceComponentInfoDto.getComponentCode(), fotaDeviceComponentInfoDto.getComponentModel());
            }

            boolean saveDevcieComponent;
            if(Objects.isNull(deviceCompnentPo)){
                deviceCompnentPo = DeviceInfoSyncMapper.INSTANCE.fotaDeviceComponentInfoDto2DeviceComponentPo(fotaDeviceComponentInfoDto);
                deviceCompnentPo.setId(IdWorker.getId());
                deviceCompnentPo.setProjectId(deviceTreeNodePo.getProjectId());
                CommonUtil.wrapBasePo(deviceCompnentPo, true);
                saveDevcieComponent = deviceComponentDbService.save(deviceCompnentPo);
                log.info("添加零件信息 saveDevcieComponent={}, deviceCompnentPo={}", saveDevcieComponent, deviceCompnentPo);
            }else{
                saveDevcieComponent = true;
                log.info("零件信息已存在 saveDevcieComponent={}, deviceCompnentPo={}", saveDevcieComponent, deviceCompnentPo);
            }

            //建立设备树与零件节点关联关系
            if(saveDevcieComponent) {
                //查询树节点与零件是否已经绑定关系
                QueryWrapper<FotaComponentListPo> fotaComponentListPoQueryWrapper = new QueryWrapper<FotaComponentListPo>();
                fotaComponentListPoQueryWrapper.eq("tree_node_id", deviceTreeNodePo.getId());
                fotaComponentListPoQueryWrapper.eq("device_component_id", deviceCompnentPo.getId());
                FotaComponentListPo fotaComponentListPo = fotaComponentListDbService.getOne(fotaComponentListPoQueryWrapper);
                if(Objects.isNull(fotaComponentListPo)) {
                    fotaComponentListPo = new FotaComponentListPo();
                    fotaComponentListPo.setId(IdWorker.getId());
                    fotaComponentListPo.setProjectId(deviceTreeNodePo.getProjectId());
                    fotaComponentListPo.setDeviceComponentId(deviceCompnentPo.getId());
                    fotaComponentListPo.setTreeNodeId(deviceTreeNodePo.getId());
                    fotaComponentListPo.setTreeNodeCodePath(deviceTreeNodePo.getNodeCodePath());
                    CommonUtil.wrapBasePo(fotaComponentListPo, true);
                    boolean saveComponentList = fotaComponentListDbService.save(fotaComponentListPo);
                    log.info("添加零件关联信息 saveComponentList={}, fotaComponentListPo={}", saveComponentList, fotaComponentListPo);
                    log.info("零件信息入库成功 item={}", deviceTreeNodePo);
                }else{
                    log.info("零件关联信息已存在,不需要重复建立。 fotaComponentListPo={}", fotaComponentListPo);
                }
            }
        *//*});*//*
    }

    @Override
    public void syncVehTagInfo(VehTagDto vehTagDto) {
        QueryWrapper<FotaObjectLabelPo> queryWrapper = new QueryWrapper();
//        queryWrapper.eq("object_id", vehTagDto.getVin());
        queryWrapper.eq("vin", vehTagDto.getVin());
        queryWrapper.eq("label_key", Long.toString(vehTagDto.getTagId()));
        List<FotaObjectLabelPo> fotaObjectLabelPos = fotaObjectLabelDbService.list(queryWrapper);
        
        FotaObjectPo fotaObjectPo = fotaObjectDbService.findByVin(vehTagDto.getVin());
        if(MyCollectionUtil.isNotEmpty(fotaObjectLabelPos)){
            fotaObjectLabelPos.forEach(item ->{
                if(!vehTagDto.getTagName().equals(item.getLabelValue())){
                    FotaObjectLabelPo entity = new FotaObjectLabelPo();
                    entity.setId(item.getId());
                    entity.setLabelValue(vehTagDto.getTagName());
                    entity.setObjectId(Objects.nonNull(fotaObjectPo) ? fotaObjectPo.getId(): null);
                    CommonUtil.wrapBasePo(entity, false);
                    log.info("同步添加车辆标签.entity={}", entity);
                    fotaObjectLabelDbService.updateById(entity);
                }
            });
            return;
        }
        FotaObjectLabelPo entity = Dto2PoMapper.INSTANCE.vehTagDto2FotaObjectLabelPo(vehTagDto);
        entity.setLabelKey(Long.toString(vehTagDto.getTagId()));
        entity.setLabelValue(vehTagDto.getTagName());
        CommonUtil.wrapBasePo(entity, true);
        log.info("同步添加车辆标签.entity={}", entity);
        fotaObjectLabelDbService.save(entity);
    }

    private DeviceTreeNodePo getDeviceTreeNodeByNodeNamePath(String nodeNamePath){
        DeviceTreeNodePo deviceTreeNodePo = deviceTreeNodeDbService.getByNodeNamePath(nodeNamePath);
        return deviceTreeNodePo;
    }

    *//**
     * 通过节点名称路径获取节点
     * @param object
     * @param clz
     * @param deviceTreeNodeLevelEnum
     * @param parent
     * @return
     *//*
    private DeviceTreeNodePo getDeviceTreeNodeByNodeNamePath(Object object, Class<?> clz, DeviceTreeNodeLevelEnum deviceTreeNodeLevelEnum, boolean parent){
        String nodeNamePath = buildNodeNamePath(object, clz, deviceTreeNodeLevelEnum, parent);
        DeviceTreeNodePo deviceTreeNodePo = deviceTreeNodeDbService.getByNodeNamePath(nodeNamePath);
        return deviceTreeNodePo;
    }

    *//**
     * 获取顶级节点数据
     * @param topName
     * @return
     *//*
    private DeviceTreeNodePo getDeviceTreeNodeByTopName(String topName){
        DeviceTreeNodePo deviceTreeNodePo = deviceTreeNodeDbService.getByTopName(topName);
        MyAssertUtil.notNull(deviceTreeNodePo, "节点数据未找到，请检查");
        return deviceTreeNodePo;
    }

    *//**
     * 构建名称路径
     * @param obj
     * @param clz
     * @param deviceTreeNodeLevelEnum
     * @param onlyParent 是否只包括父节点级别以上目录
     * @return
     *//*
    private String buildNodeNamePath(final Object obj, final Class<?> clz, DeviceTreeNodeLevelEnum deviceTreeNodeLevelEnum, boolean onlyParent){
        List<DeviceTreeNodeLevelEnum> deviceTreeNodeLevelEnums;
        if(onlyParent){
            deviceTreeNodeLevelEnums = EnumSet.allOf(DeviceTreeNodeLevelEnum.class).stream().filter(item -> item.getLevel() < deviceTreeNodeLevelEnum.getLevel()).sorted(Comparator.comparingInt(DeviceTreeNodeLevelEnum::getLevel)).collect(Collectors.toList());
        }else {
            deviceTreeNodeLevelEnums = EnumSet.allOf(DeviceTreeNodeLevelEnum.class).stream().filter(item -> item.getLevel() <= deviceTreeNodeLevelEnum.getLevel()).sorted(Comparator.comparingInt(DeviceTreeNodeLevelEnum::getLevel)).collect(Collectors.toList());
        }
        if(MyCollectionUtil.isEmpty(deviceTreeNodeLevelEnums)){
            log.warn("获取树节点层次列表异常");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (DeviceTreeNodeLevelEnum item : deviceTreeNodeLevelEnums) {
            String getNameMethodName = "get" + (item.getCode().substring(0, 1).toUpperCase()) + item.getCode().substring(1, item.getCode().length());
            String name = (String) ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(clz, getNameMethodName), obj);
            sb.append("/" + name);
        }
        log.info("nodeNamePath={}", sb.toString());
        return sb.toString();
    }

    *//**
     *
     * @param fotaDeviceTreeNodeDto
     * @param deviceTreeNodeLevelEnum
     * @return
     *//*
    private String getValue(final FotaDeviceTreeNodeDto fotaDeviceTreeNodeDto, DeviceTreeNodeLevelEnum deviceTreeNodeLevelEnum, int methodType){
        String suffix = methodType == 1 ? "Code" : "";
        String methodName = "get" + (deviceTreeNodeLevelEnum.getCode().substring(0, 1).toUpperCase()) + deviceTreeNodeLevelEnum.getCode().substring(1) + suffix;
        Method method = ReflectionUtils.findMethod(FotaDeviceTreeNodeDto.class, methodName);
        if(Objects.isNull(method)){
            log.warn("方法不存在,请检查.mothodName={}", methodName);
            throw new IllegalArgumentException("方法不存在,请检查");
        }
        String value = (String) ReflectionUtils.invokeMethod(method, fotaDeviceTreeNodeDto);
        return value;
    }

    *//**
     * 创建节点
     *
     * @param parent
     * @param nodeName
     * @param nodeCode
     * @param orderNum
     * @param deviceTreeNodeLevelEnum
     * @param rootId
     * @return
     *//*
    private long saveDeviceTreeNodePo(DeviceTreeNodePo parent, String nodeName, String nodeCode, int orderNum, DeviceTreeNodeLevelEnum deviceTreeNodeLevelEnum, Long rootId){
        DeviceTreeNodePo node = new DeviceTreeNodePo();

        long id = IdWorker.getId();
        log.info("生成的节点Id={}", id);
        node.setRootNodeId(rootId);
        node.setId(id);
        if(StringUtils.isEmpty(nodeCode)){
            nodeCode = IdWorker.getIdStr();
        }
        node.setNodeCode(nodeCode);
        node.setNodeName(nodeName);

        //如果为非顶级节点
        if (Objects.nonNull(parent)) {
            node.setParentId(parent.getId());
            node.setNodeIdPath(parent.getNodeIdPath() +"/"+id);
            node.setNodeCodePath(parent.getNodeCodePath() +"/" + node.getNodeCode());
            node.setNodeNamePath(parent.getNodeNamePath() +"/" + node.getNodeName());
        }else {
            node.setParentId(null);
            node.setNodeIdPath("/"+id);
            node.setNodeCodePath("/"+node.getNodeCode());
            node.setNodeNamePath("/"+node.getNodeName());
        }

        node.setOrderNum(orderNum);
        node.setTreeLevel(deviceTreeNodeLevelEnum.getLevel());
        CommonUtil.wrapBasePo(node, true);
        node.setVersion(0);
        boolean save = deviceTreeNodeDbService.save(node);
        log.info("添加树节点。save={}", save);
        return node.getId();
    }*/
}
