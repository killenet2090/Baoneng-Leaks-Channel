package com.bnmotor.icv.tsp.ota.model.resp.v2;

import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.req.v2.VehicleUploadFileV2Req;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaPlanV2RespVo.java FotaPlanV2RespVo
 * @Description: 新建升级任务V2版本
 * @author E.YanLonG
 * @since 2020-11-30 10:56:13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class FotaPlanVehicleStateV2RespVo extends Page<FotaAssociateVehicleVo> {

//	@JsonSerialize(using = LongJsonSerializer.class)
//	@JsonDeserialize(using = LongJsonDeserializer.class)
//	@ApiModelProperty(value = "otaPlanId", example = "1316944117112332289")
//	Long otaPlanId;

	Long invalidVehicleCount;
	Long validVehicleCount;
	Long totalVehicleCount;
	String fileKey;
	
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	Long treeNodeId;
	
	public static FotaPlanVehicleStateV2RespVo wrap(Page<FotaAssociateVehicleVo> page) {
		FotaPlanVehicleStateV2RespVo fotaPlanVehicleStateV2RespVo = FotaPlanVehicleStateV2RespVo.of();
		fotaPlanVehicleStateV2RespVo.setRecords(page.getRecords());
		fotaPlanVehicleStateV2RespVo.setTotal(page.getTotal());
		fotaPlanVehicleStateV2RespVo.setCurrent(page.getCurrent());
		fotaPlanVehicleStateV2RespVo.setPages(page.getPages());
		fotaPlanVehicleStateV2RespVo.setSize(page.getSize());
		
		Long validCount = page.getRecords().stream().filter(it -> it.getCanAssociate() == 1).count();
		fotaPlanVehicleStateV2RespVo.setValidVehicleCount(validCount);
		fotaPlanVehicleStateV2RespVo.setInvalidVehicleCount(page.getTotal() - validCount);
		fotaPlanVehicleStateV2RespVo.setTotalVehicleCount(page.getTotal());
		return fotaPlanVehicleStateV2RespVo;
	}
	
	/**
	 * 手工分页
	 * @param vehicleUploadFileV2Req
	 * @param fotaPlanVehicleStateV2RespVo
	 */
	public void manualPage(VehicleUploadFileV2Req vehicleUploadFileV2Req) {
		
		List<FotaAssociateVehicleVo> fotaAssociateVehicleVoList = this.getRecords();
		Integer total = fotaAssociateVehicleVoList.size();
		Integer current = vehicleUploadFileV2Req.getCurrent();
		Integer pageSize = vehicleUploadFileV2Req.getPageSize();
		
		if (Objects.isNull(pageSize) || pageSize < 1) {
			pageSize = 20;
		}
		
		Integer totalPage = total% pageSize == 0 ? total /pageSize : total /pageSize + 1;
		
		if (Objects.isNull(current) || current < 1 || current > totalPage) {
			current = 1;
		}
		
		int start = (current - 1) * pageSize;
		int end = start + pageSize;
		
		if (start >= fotaAssociateVehicleVoList.size()) {
			start = 0;
		}
		
		if (end > fotaAssociateVehicleVoList.size()) {
			end = fotaAssociateVehicleVoList.size();
		}
		
		List<FotaAssociateVehicleVo> segment = Lists.newArrayList();
		for (int i = start; i < end; i++) {
			segment.add(fotaAssociateVehicleVoList.get(i));
		}
		
		this.setRecords(segment);
		this.setCurrent(current);
		this.setSize(pageSize);
		this.setPages(totalPage);
	}
	
}