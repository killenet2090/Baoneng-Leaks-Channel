package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.UpgradeLogProcessEnum;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaVersionCheckVerifyPo;
import com.bnmotor.icv.tsp.ota.model.page.PageResult;
import com.bnmotor.icv.tsp.ota.model.query.FotaUpgradeLogQuery;
import com.bnmotor.icv.tsp.ota.model.resp.FotaUpgradeLogVo;
import com.bnmotor.icv.tsp.ota.model.tbox.upgrade.UpgradeLog;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import com.bnmotor.icv.tsp.ota.util.ZipCompressingUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName: FotaUpgradeLogServiceImpl.java FotaUpgradeLogServiceImpl
 * @Description: TBOX升级日志下载
 * @author E.YanLonG
 * @since 2020-10-22 12:00:53
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class FotaUpgradeLogServiceImpl implements IFotaUpgradeLogService {

	@Autowired
	OssService ossService;

	@Autowired
	IFotaPlanService fotaPlanService;

	@Autowired
	IFotaPlanDbService fotaPlanDbService;

	@Autowired
	private IFotaVersionCheckVerifyDbService fotaVersionCheckVerifyDbService;

	@Override
	public Page<FotaUpgradeLogVo> queryFotaUpgradeLogPage(FotaUpgradeLogQuery fotaUpgradeLogQuery) {

		IPage<FotaVersionCheckVerifyPo> fotaVersionCheckVerifyPos = fotaVersionCheckVerifyDbService.queryPage4UpgradeLog(fotaUpgradeLogQuery);

		Long total = fotaVersionCheckVerifyPos.getTotal();

		List<FotaVersionCheckVerifyPo> fotaVersionCheckVerifyPosList = fotaVersionCheckVerifyPos.getRecords();

		List<FotaUpgradeLogVo> fotaUpgradeLogVoList = fotaVersionCheckVerifyPosList.stream()
				.map(verify -> buildFotaUpgradeLogVo(verify)).collect(Collectors.toList());
		fotaUpgradeLogVoList.stream().sorted(Comparator.comparing(BasePo::getCreateTime));
		
		paddingPlanName(fotaUpgradeLogVoList);

		Page<FotaUpgradeLogVo> fotaUpgradeLogVoPage = new Page<>(fotaUpgradeLogQuery.getCurrent(),
				fotaUpgradeLogQuery.getPageSize());
		fotaUpgradeLogVoPage.setRecords(fotaUpgradeLogVoList);
		fotaUpgradeLogVoPage.setTotal(total);

		return fotaUpgradeLogVoPage;
	}

	@Override
	public PageResult<FotaUpgradeLogVo> queryFotaUpgradeLogPageResult(FotaUpgradeLogQuery fotaUpgradeLogQuery) {

		IPage<FotaVersionCheckVerifyPo> fotaVersionCheckVerifyPos = fotaVersionCheckVerifyDbService.queryPage4UpgradeLog(fotaUpgradeLogQuery);

		Long total = fotaVersionCheckVerifyPos.getTotal();

		List<FotaVersionCheckVerifyPo> fotaVersionCheckVerifyPosList = fotaVersionCheckVerifyPos.getRecords();

		List<FotaUpgradeLogVo> fotaUpgradeLogVoList = fotaVersionCheckVerifyPosList.stream()
				.map(verify -> buildFotaUpgradeLogVo(verify)).collect(Collectors.toList());
		fotaUpgradeLogVoList.stream().sorted(Comparator.comparing(BasePo::getCreateTime));

		paddingPlanName(fotaUpgradeLogVoList);

		PageResult<FotaUpgradeLogVo> fotaUpgradeLogVoPage = new PageResult(fotaUpgradeLogQuery, fotaUpgradeLogVoList);
		fotaUpgradeLogVoPage.setTotalItem(total.intValue());
		/*fotaUpgradeLogVoPage.setRecords(fotaUpgradeLogVoList);
		fotaUpgradeLogVoPage.setTotal(total);*/

		return fotaUpgradeLogVoPage;
	}

	/**
	 * 补全文件名
	 * @param fotaUpgradeLogVoList
	 */
	public void paddingPlanName(List<FotaUpgradeLogVo> fotaUpgradeLogVoList) {
		
		if (CollectionUtils.isEmpty(fotaUpgradeLogVoList)) {
			return;
		}
		
		List<Long> planIdList = fotaUpgradeLogVoList.stream().map(FotaUpgradeLogVo::getPlanId).distinct().collect(Collectors.toList());
		
		List<FotaPlanPo> fotaPlanPoList = fotaPlanDbService.listByIds(planIdList);
		Map<Long, String> idnamemap = fotaPlanPoList.stream().collect(Collectors.toMap(FotaPlanPo::getId, FotaPlanPo::getPlanName, (k1, k2) -> k1));
		
		fotaUpgradeLogVoList.stream().forEach(log -> {
			log.setPlanName(idnamemap.get(log.getPlanId()));
		});
	}

	/**
	 * 构建FotaUpgradeLogVo类
	 * @param fotaVersionCheckVerifyPo
	 * @return
	 */
	public FotaUpgradeLogVo buildFotaUpgradeLogVo(FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo) {
		FotaUpgradeLogVo fotaUpgradeLogVo = new FotaUpgradeLogVo();
		fotaUpgradeLogVo.setTransId(fotaVersionCheckVerifyPo.getId());
		fotaUpgradeLogVo.setPlanId(fotaVersionCheckVerifyPo.getOtaPlanId());
		fotaUpgradeLogVo.setVin(fotaVersionCheckVerifyPo.getVin());
		fotaUpgradeLogVo.setCreateTime(fotaVersionCheckVerifyPo.getCreateTime());
		fotaUpgradeLogVo.setUpdateTime(fotaVersionCheckVerifyPo.getUpdateTime());
		String filename = fotaUpgradeLogVoFilename(fotaVersionCheckVerifyPo);
		Integer installedCompleteStatus = fotaVersionCheckVerifyPo.getInstalledCompleteStatus();
		if (Objects.isNull(installedCompleteStatus)) {
			// 未知
			installedCompleteStatus = -1;
		}
		fotaUpgradeLogVo.setFilename(filename);
		fotaUpgradeLogVo.setInstallCompleteStatus(installedCompleteStatus);
		
		UpgradeLogProcessEnum.Pair target = UpgradeLogProcessEnum.selectUpgradeState(fotaVersionCheckVerifyPo);
		String processState = target.toString();
		fotaUpgradeLogVo.setProcessState(processState);
		
		// 对应的升级日志是否已上传
		Integer existLog = StringUtils.isBlank(fotaVersionCheckVerifyPo.getUpgradeLog()) ? 0: 1;
		fotaUpgradeLogVo.setExistsLog(existLog);
		fotaUpgradeLogVo.setCreateBy(fotaVersionCheckVerifyPo.getCreateBy());
		fotaUpgradeLogVo.setUpdateBy(fotaVersionCheckVerifyPo.getUpdateBy());
		fotaUpgradeLogVo.setDelFlag(fotaVersionCheckVerifyPo.getDelFlag());
		return fotaUpgradeLogVo;

	}

	/**
	 * 组装OTA升级日志文件名称
	 * @param fotaVersionCheckVerifyPo
	 * @return
	 */
	public String fotaUpgradeLogVoFilename(FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo) {
		String vin = fotaVersionCheckVerifyPo.getVin();
		Long transId = fotaVersionCheckVerifyPo.getId();
		String filename = StringUtils.format("%s-%d.zip", vin, transId);
		return filename;
	}


	@Override
	public void downloadUpgradeLog(Long transId, HttpServletResponse httpServletResponse) {

		FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.getBaseMapper().selectById(transId);
		MyAssertUtil.notNull(fotaVersionCheckVerifyPo, OTARespCodeEnum.DATA_NOT_FOUND);

		UpgradeLog upgradeLog = reverseObject(fotaVersionCheckVerifyPo);
		if (Objects.isNull(upgradeLog)) {
			return;
		}

		String bucketName = upgradeLog.getBucketName();
		List<String> objectNameList = upgradeLog.getObjectNames();

		String zipfilename = fotaUpgradeLogVoFilename(fotaVersionCheckVerifyPo);
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			File directory = download(bucketName, transId, objectNameList); // 下载多份文件
			ZipCompressingUtil.zipFileChannel(directory, baos); // 压缩成一个文件
			clean(directory); // 删除文件
			output(httpServletResponse, baos.toByteArray(), zipfilename); // 直接输出到HttpServletResponse
		} catch (Exception e) {
			log.error("下载文件异常.e.getMessage={}", e.getMessage(), e);
		}
	}

	/**
	 * 直接输出zip流
	 * @param bucketName
	 * @param transId
	 * @param objectNameList
	 * @return
	 * @throws Exception
	 */
	public File download(String bucketName, Long transId, List<String> objectNameList) {
		String directorybase = "upgrade/download/";
		String directorystr = StringUtils.format(directorybase + "%d", transId);
		File directory = new File(directorystr);
		log.info("file absolute path is|{}", directory.getAbsolutePath());

		if (!directory.exists()) {
			directory.mkdirs();
		}

		log.info("共下载文件{}|{}", objectNameList.size(), objectNameList);
		for (String objectname : objectNameList) {
			
			String filename = FilenameUtils.getName(objectname);
			try {
				log.info("开始下载文件|{}", objectname);
				ossService.downloadFile(bucketName, new File(directory, filename), objectname);
			} catch (Exception e) {
				log.error("下载失败|{}", e.getMessage(), e);
				throw e;
			}
		}
		
		return directory;
	}

	/**
	 * 清楚临时文件
	 * @param directory
	 * @throws Exception
	 */
	public void clean(File directory) throws Exception {
		FileUtils.deleteDirectory(directory);
	}

	/**
	 * 对象装换： FotaVersionCheckVerifyPo --> UpgradeLog
	 * @param fotaVersionCheckVerifyPo
	 * @return
	 */
	public UpgradeLog reverseObject(FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo) {
		Long transId = fotaVersionCheckVerifyPo.getId();
		String logjson = fotaVersionCheckVerifyPo.getUpgradeLog();
		UpgradeLog upgradeLog = null;
		try {
			upgradeLog = JsonUtil.toObject(logjson, UpgradeLog.class);
			if (Objects.isNull(upgradeLog)) {
				log.info("不存在对应的升级日志信息|{}", transId);
			}
		} catch (IOException e) {
			log.error("对象转换异常.fotaVersionCheckVerifyPo={}", fotaVersionCheckVerifyPo, e);
		}

		return upgradeLog;
	}

	/**
	 * 输出
	 * @param httpServletResponse
	 * @param buffer
	 * @param filename
	 */
	public void output(HttpServletResponse httpServletResponse, byte[] buffer, String filename) {
		
		try (OutputStream toClient = new BufferedOutputStream(httpServletResponse.getOutputStream())) {
			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
			httpServletResponse.addHeader("Content-Length", "" + (buffer.length));
			httpServletResponse.setHeader("filename", filename);
			toClient.write(buffer);
			toClient.flush();
		} catch (Exception e) {
			 log.error("output error|{}", e.getMessage(), e);
		}
	}

}