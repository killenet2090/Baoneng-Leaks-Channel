package com.bnmotor.icv.tsp.ota.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaApprovalRecordPo;

/**
 * @ClassName: FotaApprovalRecordMapper.java 
 * @Description: 
 * @author E.YanLonG
 * @since 2021-3-24 16:39:04
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Mapper
public interface FotaApprovalRecordMapper extends BaseMapper<FotaApprovalRecordPo> {
	
	
	String Base_Column_List = "approval_type, business_key, description, form_title, id, ota_object_body, ota_object_key, process_instance_id, status, version";

	@Select("<script>"
	+ "	select t.* from tb_fota_approval_record t"
	+ "	<where>"
	+ "	<if test='item.approvalType != null'> and approval_type = #{item.approvalType}</if>	"
	+ "	<if test='item.businessKey != null'> and business_key = #{item.businessKey}</if>	"
	+ "	<if test='item.description != null'> and description = #{item.description}</if>	"
	+ "	<if test='item.formTitle != null'> and form_title = #{item.formTitle}</if>	"
	+ "	<if test='item.id != null'> and id = #{item.id}</if>	"
	+ "	<if test='item.otaObjectBody != null'> and ota_object_body = #{item.otaObjectBody}</if>	"
	+ "	<if test='item.otaObjectKey != null'> and ota_object_key = #{item.otaObjectKey}</if>	"
	+ "	<if test='item.processInstanceId != null'> and process_instance_id = #{item.processInstanceId}</if>	"
	+ "	<if test='item.status != null'> and status = #{item.status}</if>	"
	+ "	<if test='item.version != null'> and version = #{item.version}</if>	"
	+ "</where></script>")
	@Results({ //
		@Result(column = "approval_type" , property = "approvalType"), //
		@Result(column = "business_key" , property = "businessKey"), //
		@Result(column = "description" , property = "description"), //
		@Result(column = "form_title" , property = "formTitle"), //
		@Result(column = "id" , property = "id"), //
		@Result(column = "ota_object_body" , property = "otaObjectBody"), //
		@Result(column = "ota_object_key" , property = "otaObjectKey"), //
		@Result(column = "process_instance_id" , property = "processInstanceId"), //
		@Result(column = "status" , property = "status"), //
		@Result(column = "version" , property = "version"), //
	})
	List<FotaApprovalRecordPo> select(@Param("item") FotaApprovalRecordPo fotaApprovalRecordPo);

	@Insert("insert into tb_fota_approval_record( approval_type, business_key, description, form_title, id, ota_object_body, ota_object_key, process_instance_id, status, version ) values( #{item.approvalType}, #{item.businessKey}, #{item.description}, #{item.formTitle}, #{item.id}, #{item.otaObjectBody}, #{item.otaObjectKey}, #{item.processInstanceId}, #{item.status}, #{item.version} )") 
	@Options(useGeneratedKeys = true, keyProperty = "item.id", keyColumn = "id")
	Long insert0(@Param("item") FotaApprovalRecordPo fotaApprovalRecordPo);

	@Update("<script>"
	+ "	update tb_fota_approval_record	"
	+ "	<set>	"
	+ "	<if test='item.approvalType != null'> approval_type = #{item.approvalType}, </if>	"
	+ "	<if test='item.businessKey != null'> business_key = #{item.businessKey}, </if>	"
	+ "	<if test='item.description != null'> description = #{item.description}, </if>	"
	+ "	<if test='item.formTitle != null'> form_title = #{item.formTitle}, </if>	"
	+ "	<if test='item.id != null'> id = #{item.id}, </if>	"
	+ "	<if test='item.otaObjectBody != null'> ota_object_body = #{item.otaObjectBody}, </if>	"
	+ "	<if test='item.otaObjectKey != null'> ota_object_key = #{item.otaObjectKey}, </if>	"
	+ "	<if test='item.processInstanceId != null'> process_instance_id = #{item.processInstanceId}, </if>	"
	+ "	<if test='item.status != null'> status = #{item.status}, </if>	"
	+ "	<if test='item.version != null'> version = #{item.version}, </if>	"
	+ "	</set>"	
	+"	 where 1 = 1 and id = #{item.id}	"
	+"	 </script>")
	Integer update(@Param("item") FotaApprovalRecordPo fotaapprovalrecordpo);

	@Delete("<script> delete from tb_fota_approval_record where id in  <foreach collection='list' open='(' close=')' item='item' separator=','>#{item.id}  </foreach> </script>	")
	Integer delete(@Param("item") FotaApprovalRecordPo fotaApprovalRecordPo);


}
