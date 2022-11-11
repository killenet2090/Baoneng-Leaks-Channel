package com.bnmotor.icv.tsp.ota.model.resp.v2;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaPlanV2RespVo.java FotaPlanV2RespVo
 * @Description: 分页查询
 * @author E.YanLonG
 * @since 2020-11-30 10:56:13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class QueryAssociatedObjectV2RespVo extends Page<FotaAssociateVehicleVo> {

	private static final long serialVersionUID = 1L;
	
//	Long invalidVehicleCount;
//	Long validVehicleCount;
//	Long totalVehicleCount;
//	String fileKey;
	
//	@JsonSerialize(using = LongJsonSerializer.class)
//	@JsonDeserialize(using = LongJsonDeserializer.class)
//	Long treeNodeId;
	
	public QueryAssociatedObjectV2RespVo wrap(IPage<?> page) {
		this.setTotal(page.getTotal());
		this.setCurrent(page.getCurrent());
		this.setPages(page.getPages());
		this.setSize(page.getSize());
		return this;
	}
	
}