package com.bnmotor.icv.tsp.ota.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;
import com.bnmotor.icv.tsp.ota.model.compose.VehicleLabelAreaInfo;
import com.bnmotor.icv.tsp.ota.model.page.PageResult;
import com.bnmotor.icv.tsp.ota.model.query.FotaVehicleAssociateQuery;

/**
 * @ClassName: FotaMiscVehicleAssociateMapper.java
 * @Description: 组合查询
 * @author E.YanLonG
 * @since 2021-2-18 10:10:37
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Mapper
public interface FotaMiscVehicleAssociateMapper {

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Select("<script>" //
			+ "select count(1) as count from (select t.id " //
			+ "from tb_fota_object t left join tb_fota_object_label l " + "on t.id = l.object_id " //
			+ "<where> t.del_flag = 0 and l.del_flag = 0 and t.tree_node_id = #{query.treeNodeId} " //
			+ "<if test='query.labels != null and query.labels.size() > 0'> and l.label_key in <foreach collection='query.labels' open='(' close=')' item='item' separator=','>#{item.labelKey}  </foreach> </if>	"
			+ "<if test='query.regions != null and query.regions.size() > 0'> and t.current_code in <foreach collection='query.regions' open='(' close=')' item='item' separator=','>#{item.regionCode}  </foreach> </if>	"
			+ "</where> group by t.object_id " // 是否需要group by 如果使用group by ，需要再次查询label
			+ " ) k </script>") //
	@Results(value = { //
			@Result(column = "count", property = "count") //
	})
	Integer associateCount(@Param("query") FotaVehicleAssociateQuery query);

	@Select("<script>" //
			+ "select t.id as object_id, t.tree_node_id , t.object_id as vin , t.current_area , t.current_code, l.object_id , l.label_key ,l.label_value " //
			+ "from tb_fota_object t left join tb_fota_object_label l on t.id = l.object_id " //
			+ "<where>" + " t.del_flag = 0 and l.del_flag = 0 and t.tree_node_id = #{query.treeNodeId} " //
			+ "<if test='query.labels != null and query.labels.size() > 0'> and l.label_key in <foreach collection='query.labels' open='(' close=')' item='item' separator=','>#{item.labelKey}  </foreach> </if>	"
			+ "<if test='query.regions != null and query.regions.size() > 0'> and t.current_code in <foreach collection='query.regions' open='(' close=')' item='item' separator=','>#{item.regionCode}  </foreach> </if>	"
			+ "</where>" + "group by t.object_id " //
			+ "limit #{page.offset}, #{page.limit}" + "</script>") //
	@Results(value = { //
			@Result(column = "object_id", property = "objectId", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true), //
			@Result(column = "tree_node_id", property = "treeNodeId"), //
			@Result(column = "current_area", property = "region.regionName"), //
			@Result(column = "current_code", property = "region.regionCode"), //
			@Result(column = "object_id", property = "labels", many = @Many(select = "com.bnmotor.icv.tsp.ota.mapper.FotaMiscVehicleAssociateMapper.label", fetchType = FetchType.DEFAULT)), //
			@Result(column = "vin", property = "vin") //
	})
	List<VehicleLabelAreaInfo> test1Page(@Param("query") FotaVehicleAssociateQuery query, @Param("page") PageResult.Position position);
	
	@Select(value = "select r.object_id, r.label_group_id, label_group_name, r.label_key, r.label_value from tb_fota_object_label r where r.del_flag = 0 and r.object_id = #{objectId}")
	@Results({ //
		@Result(column = "object_id", property = "objectId"), //
		@Result(column = "label_group_id", property = "labelGroupId"), //
		@Result(column = "label_group_name", property = "labelGroupName"), //
		@Result(column = "label_key", property = "labelKey"), //
		@Result(column = "label_value", property = "labelVal") //
	})
	List<LabelInfo> label(@Param("objectId") Long objectId);
	
	
	@Select(value = "<script>select r.object_id, r.label_group_id, label_group_name, r.label_key, r.label_value from tb_fota_object_label r where r.del_flag = 0 and r.object_id in <foreach collection='list' open='(' close=')' item='item' separator=','>#{item}  </foreach> </script>")
	@Results({ //
		@Result(column = "object_id", property = "objectId"), //
		@Result(column = "label_group_id", property = "labelGroupId"), //
		@Result(column = "label_group_name", property = "labelGroupName"), //
		@Result(column = "label_key", property = "labelKey"), //
		@Result(column = "label_value", property = "labelVal") //
	})
	List<LabelInfo> listLabels(@Param("list") List<Long> objectIds);

}