package com.bnmotor.icv.tsp.ota;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.tsp.ota.common.enums.DeviceTreeNodeLevelEnum;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.mapper.DeviceTreeNodeMapper;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareVersionPo;
import com.bnmotor.icv.tsp.ota.service.IDeviceTreeNodeDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwareService;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwareVersionDbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DeviceNodeDBTest
{
    @Resource
    private DeviceTreeNodeMapper deviceTreeNodeMapper;

    @Autowired
    private IDeviceTreeNodeDbService deviceTreeNodeDbService;

    @Autowired
    private IFotaFirmwareService fotaFirmwareService;

    @Autowired
    private IFotaFirmwareVersionDbService fotaFirmwareVersionDbService;

    /*@Autowired
    private IFotaFirmwareVersionDbService fotaFirmwareVersionDbService;*/

    /*@Test
    public void query(){
        List<DeviceTreeNodePo> deviceTreeNodePoList = deviceTreeNodeService.listChildren(null);
        if(!CollectionUtils.isEmpty(deviceTreeNodePoList)){
            deviceTreeNodePoList.forEach(System.out::println);
        }
    }*/

    @Test
    public void insert(){
        DeviceTreeNodePo node = new DeviceTreeNodePo();
        node.setProjectId("baoqi001");
        node.setNodeCode("baoqi");
        node.setNodeCodePath("/"+node.getNodeCode());
        node.setNodeName("宝汽");
        node.setNodeNamePath("/"+node.getNodeName());
        long id = IdWorker.getId();

        node.setId(id);
        node.setNodeIdPath("/"+id);
        node.setOrderNum(0);
        node.setRootNodeId(null);
        node.setTreeLevel(0);
        node.setCreateBy("xxc");
        LocalDateTime date = LocalDateTime.now();
        node.setCreateTime(date);
        node.setUpdateBy("xxc");
        node.setUpdateTime(date);

        deviceTreeNodeMapper.insert(node);
    }

    @Test
    public void addFotaFirmwareVersion(){
        String projectId = "guanzhi001";
        long rootNodeId = 100001;
        long treeNodeId = 1272736983981015042L;
        List<FotaFirmwarePo> fotaFirmwarePos = fotaFirmwareService.listFirmwarePos(/*projectId, rootNodeId, */treeNodeId);

        if(!CollectionUtils.isEmpty(fotaFirmwarePos)){
            FotaFirmwarePo fotaFirmwarePo = fotaFirmwarePos.get(0);
            FotaFirmwareVersionPo fotaFirmwareVersion = new FotaFirmwareVersionPo();
            fotaFirmwareVersion.setProjectId(fotaFirmwarePo.getProjectId());
            fotaFirmwareVersion.setAppliedFirmwareVersion("1273463744150708226;1273486656228364289");
            fotaFirmwareVersion.setAppliedHardwareVersion("100;101");
            fotaFirmwareVersion.setFirmwareVersionNo("V1.0.6");
            fotaFirmwareVersion.setReleaseNotes("发布说明");
            fotaFirmwareVersion.setReleaseUser("sunwukong");
            fotaFirmwareVersion.setCreateBy("sunwukong");
            fotaFirmwareVersion.setUpdateBy("sunwukong");
            fotaFirmwareVersion.setFirmwareId(fotaFirmwarePo.getId());
            fotaFirmwareVersion.setIsForceFullUpdate(Enums.ZeroOrOneEnum.ONE.getValue());
            fotaFirmwareService.addFotaFirmwareVersion(fotaFirmwareVersion);
        }
    }

    @Test
    public void test(){
        DeviceTreeNodePo rootDeviceTreeNodePo = null;
        List<DeviceTreeNodePo> deviceTreeNodePoList = deviceTreeNodeDbService.listRoot(/*"baoqi001"*/);
        if(!CollectionUtils.isEmpty(deviceTreeNodePoList)){
            rootDeviceTreeNodePo = deviceTreeNodePoList.get(0);
        }
        final DeviceTreeNodePo root = rootDeviceTreeNodePo;
        if(Objects.isNull(root)){
            return;
        }

        batchDo(root.getId(), root, DeviceTreeNodeLevelEnum.SERIES.getLevel());

        /*String[] brands = {"观致"};

        /*AtomicInteger counter = new AtomicInteger(0);
        Stream.of(series).forEach(series1 ->{
            String[] items = series1.split(":");
            DeviceTreeNodePo seriesDeviceTreeNodePo = buildDeviceTreeNodePo(root, items[0], items[1], counter.incrementAndGet(), DeviceTreeNodeLevelEnum.SERIES);
            deviceTreeNodeMapper.insert(seriesDeviceTreeNodePo);
        });


        DeviceTreeNodePo node = new DeviceTreeNodePo();
        node.setNodeCode("guanzhi");
        node.setNodeCodePath("/"+node.getNodeCode());
        node.setNodeName("观致");
        node.setNodeNamePath("/"+node.getNodeName());
        long id = IdWorker.getId();

        node.setId(id);
        node.setNodeIdPath("/"+id);
        node.setOrderNum(0);
        node.setRootNodeId(null);
        node.setTreeLevel(0);
        node.setCreateBy("xxc");
        Date date = new Date();
        node.setCreateTime(date);
        node.setUpdateBy("xxc");
        node.setUpdateTime(date);

        deviceTreeNodeMapper.insert(node);*/
    }

    public String[] getArrays(DeviceTreeNodeLevelEnum deviceTreeNodeLevelEnum){
        String[] series = {"车系1:series1", "车系2:series1", "车系3:series1" };

        String[] models = {"车型1:model1", "车型2:model2", "车型3:model3" };
        String[] years = {"2020:2020"};
        String[] confs = {"高配:high" };
        String[] ecus = {"MTU:mtu", "TUP:tup", "SEC:sec" };

        /*String[] series = {"车系1:series1", "车系2:series1", "车系3:series1" };

        String[] models = {"车型1:model1", "车型2:model2", "车型3:model3" };
        String[] years = {"2019:2019", "2020:2020", "2021:2021" };
        String[] confs = {"低配:low", "中配:mid", "高配:high" };
        String[] ecus = {"MTU:mtu", "TUP:tup", "SEC:sec" };*/
        //DeviceTreeNodeLevelEnum deviceTreeNodeLevelEnum = DeviceTreeNodeLevelEnum.getByLevel(level);
        if(deviceTreeNodeLevelEnum.equals(DeviceTreeNodeLevelEnum.SERIES)){
            return series;
        }else if(deviceTreeNodeLevelEnum.equals(DeviceTreeNodeLevelEnum.MODEL)){
            return models;
        }else if(deviceTreeNodeLevelEnum.equals(DeviceTreeNodeLevelEnum.YEAR)){
            return years;
        }else if(deviceTreeNodeLevelEnum.equals(DeviceTreeNodeLevelEnum.CONF)){
            return confs;
        }/*else if(deviceTreeNodeLevelEnum.equals(DeviceTreeNodeLevelEnum.ECU)){
            return ecus;
        }*/
        return null;
    }

    private void batchDo(long rootId, DeviceTreeNodePo parent, int level){
        DeviceTreeNodeLevelEnum deviceTreeNodeLevelEnum = DeviceTreeNodeLevelEnum.getByLevel(level);
        if(null == deviceTreeNodeLevelEnum){
            return;
        }
        String[] arrays = getArrays(deviceTreeNodeLevelEnum);
        AtomicInteger counter = new AtomicInteger(0);
        Stream.of(arrays).forEach(series1 ->{
            String[] items = series1.split(":");
            DeviceTreeNodePo deviceTreeNodePo = buildDeviceTreeNodePo(rootId, parent, items[0], items[1], counter.incrementAndGet(), deviceTreeNodeLevelEnum);
            deviceTreeNodeMapper.insert(deviceTreeNodePo);

            batchDo(rootId, deviceTreeNodePo, level +1);
        });
    }

    private DeviceTreeNodePo buildDeviceTreeNodePo(long rootId, DeviceTreeNodePo parent, String nodeName, String nodeCode, int orderNum, DeviceTreeNodeLevelEnum deviceTreeNodeLevelEnum){
        DeviceTreeNodePo node = new DeviceTreeNodePo();
        long id = IdWorker.getId();
        node.setId(id);
        node.setNodeIdPath(parent.getNodeIdPath() +"/"+id);
        //Date date = new Date();
        LocalDateTime date = LocalDateTime.now();
        node.setParentId(parent.getId());
        node.setProjectId(parent.getProjectId());

        node.setNodeCode(nodeCode);
        node.setNodeCodePath(parent.getNodeCodePath() +"/" + node.getNodeCode());

        node.setNodeName(nodeName);
        node.setNodeNamePath(parent.getNodeNamePath() +"/" + node.getNodeName());

        node.setOrderNum(orderNum);
        node.setRootNodeId(rootId);
        node.setTreeLevel(deviceTreeNodeLevelEnum.getLevel());
        node.setCreateBy("xxc");

        node.setCreateTime(date);
        node.setUpdateBy("xxc");
        node.setUpdateTime(date);
        node.setDelFlag(0);
        node.setVersion(0);

        return node;
    }

    @Test
    public void updateByIdWithVersion(){
        Serializable id = 1277487111820849154L;
        FotaFirmwareVersionPo exist = fotaFirmwareVersionDbService.getById(id);
        FotaFirmwareVersionPo fotaFirmwareVersionPo = new FotaFirmwareVersionPo();
        Integer version = exist.getVersion();
        fotaFirmwareVersionPo.setId(exist.getId());
        fotaFirmwareVersionPo.setVersion(version + 1);
        LocalDateTime date = LocalDateTime.now();
        fotaFirmwareVersionPo.setUpdateTime(date);
        fotaFirmwareVersionPo.setUpdateBy("xxc_1");
        fotaFirmwareVersionPo.setFirmwareVersionName(exist.getFirmwareVersionName() + "_test");
        boolean update = fotaFirmwareVersionDbService.updateByIdWithVersion(fotaFirmwareVersionPo, version);
    }

    @Test
    public void testMapper(){
        //System.out.println(fotaPlanTaskDetailMapper.queryById(35L));
    }
}


