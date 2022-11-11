package com.bnmotor.icv.tsp.ota.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ota.model.compose.RegionInfo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;

/**
 * @ClassName: FotaObjectDo
 * @Description: OTA升级对象指需要升级的一个完整对象， 在车联网中指一辆车 通常拿车的vin作为升级的ID dao层
 * @author xxc
 * @since 2020-07-06
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface FotaObjectMapper extends BaseMapper<FotaObjectPo> {

	@Select("select distinct(current_code), current_area from tb_fota_object where del_flag = 0 and tree_node_id = #{treeNodeId} group by current_code")
	@Results({
			@Result(column = "current_code", property = "regionCode"),
			@Result(column = "current_area", property = "regionName")
	})
	public List<RegionInfo> listRegion(@Param("treeNodeId") Long treeNodeId);

}