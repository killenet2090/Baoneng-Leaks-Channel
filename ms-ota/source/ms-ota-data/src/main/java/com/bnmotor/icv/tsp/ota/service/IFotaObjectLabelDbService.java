package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectLabelPo;

import java.util.List;

/**
 * @ClassName: FotaObjectLabelPo
 * @Description: OTA升级对象指需要升级的一个完整对象，
在车联网中指一辆车
通常拿车的vin作为升级的ID
                                          服务类
 * @author xuxiaochang1
 * @since 2020-11-27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaObjectLabelDbService extends IService<FotaObjectLabelPo> {

	/**
	 *
	 * @param objectIds
	 * @return
	 */
	List<LabelInfo> listLabels(List<Long> objectIds);
	
	/**
	 * 2021-03-02
	 * @return
	 */
	List<LabelInfo> listFullLabels();

	/**
	 *
	 * @param labels
	 * @return
	 */
	List<FotaObjectLabelPo> selectByLables(List<String> labels);

	/**
	 *
	 * @param objectIds
	 * @return
	 */
	List<FotaObjectLabelPo> selectByObjectIds(List<Long> objectIds);

	/**
	 *
	 * @param vins
	 * @return
	 */
	List<FotaObjectLabelPo> selectLableByVin(List<String> vins);

	/**
	 *
	 * @param vin
	 * @param labelKey
	 * @return
	 */
    List<FotaObjectLabelPo> list(String vin, String labelKey);

	/**
	 * 物理删除记录
	 * @param vin
	 * @param tagIds
	 */
	void delByVinAndKeysPyhsical(String vin, List<String> tagIds);
}