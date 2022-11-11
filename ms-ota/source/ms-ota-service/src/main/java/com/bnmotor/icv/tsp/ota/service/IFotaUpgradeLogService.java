package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.tsp.ota.model.page.PageResult;
import com.bnmotor.icv.tsp.ota.model.query.FotaUpgradeLogQuery;
import com.bnmotor.icv.tsp.ota.model.resp.FotaUpgradeLogVo;

import javax.servlet.http.HttpServletResponse;


/**
 * @ClassName: IFotaUpgradeLogService.java IFotaUpgradeLogService
 * @Description: TBOX升级日志
 * @author E.YanLonG
 * @since 2020-10-22 9:27:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IFotaUpgradeLogService {

	/**
	 * 查询升级日志文件记录
	 * @param fotaUpgradeLogQuery
	 * @return
	 */
	Page<FotaUpgradeLogVo> queryFotaUpgradeLogPage(FotaUpgradeLogQuery fotaUpgradeLogQuery);

	/**
	 * 查询升级日志文件记录
	 * @param fotaUpgradeLogQuery
	 * @return
	 */
	PageResult<FotaUpgradeLogVo> queryFotaUpgradeLogPageResult(FotaUpgradeLogQuery fotaUpgradeLogQuery);

	/**
	 * 下载升级日志文件
	 * @param transId
	 * @param response
	 */
	void downloadUpgradeLog(Long transId, HttpServletResponse response);
}