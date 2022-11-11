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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;
import com.bnmotor.icv.tsp.ota.model.compose.VehicleLabelAreaInfo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectLabelPo;
import com.bnmotor.icv.tsp.ota.model.page.PageResult;
import com.bnmotor.icv.tsp.ota.model.query.FotaVehicleAssociateQuery;

/**
 * @ClassName: FotaObjectLabelPo
* @Description: OTA升级对象指需要升级的一个完整对象，
在车联网中指一辆车
通常拿车的vin作为升级的ID
                                          dao层
 * @author xuxiaochang1
 * @since 2020-11-27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface FotaObjectLabelMapper extends BaseMapper<FotaObjectLabelPo> {


	@Select("<script> select distinct(label_key), label_value from tb_fota_object_label where del_flag = 0 and object_id in <foreach collection='list' open='(' close=')' item='item' separator=','>#{item}  </foreach> </script>")
	@Results({
		@Result(column = "label_key", property = "labelKey"),
		@Result(column = "label_value", property = "labelVal"),
		
		@Result(column = "label_group_id", property = "labelGroupId"),
		@Result(column = "label_group_name", property = "labelGroupName"),
		
	})
	List<LabelInfo> listLabels(@Param("list") List<Long> objectIds);

	/**
	 *
	 * @param vin
	 * @param labelKeys
	 */
    void delByVinAndKeysPyhsical(@Param("vin") String vin, @Param("lableKeys") List<String> labelKeys);
    
    @Select("<script> select label_group_id, label_group_name, label_key, label_value from tb_fota_object_label where del_flag = 0 group by label_group_id, label_key </script>")
    @Results({
    	@Result(column = "label_group_id", property = "labelGroupId"),
    	@Result(column = "label_group_name", property = "labelGroupName"),
    	@Result(column = "label_key", property = "labelKey"),
		@Result(column = "label_value", property = "labelVal"),
	})
    List<LabelInfo> listFullLabels();
}
