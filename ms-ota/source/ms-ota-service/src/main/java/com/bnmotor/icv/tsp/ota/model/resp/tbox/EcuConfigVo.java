package com.bnmotor.icv.tsp.ota.model.resp.tbox;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: EcuConfigVo
 * @Description: ecu配置清单
 * @author: xuxiaochang1
 * @date: 2020/9/14 21:08
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class EcuConfigVo {

	@ApiModelProperty(value = "零件代码", example = "BNQC12499", required = true)
	String ecuId;

	@ApiModelProperty(value = "零件名称", example = "12零件名称", required = true)
	String ecuName;
	
	@ApiModelProperty(value = "诊断id", example = "ID892893", required = true)
	String ecuDid;
	
	@ApiModelProperty(value = "软件id", example = "lhx-test-118", required = true)
	String ecuSwid;
	
	@ApiModelProperty(value = "响应id", example = "", required = false)
	String ecuResponseId;
	
	@ApiModelProperty(value = "功能id", example = "", required = false)
	String ecuFunctionId;
	
	@ApiModelProperty(value = "安装类型", example = "")
	String installType;

	@ApiModelProperty(value = "信息采集受控对象", example = "升级对象/单元由谁负责采集其基础、更新信息", required = false)
	private Integer infoCollCtrlObj;

	@ApiModelProperty(value = "下载受控对象", example = "升级对象/单元的软件包由哪个零件进行下载", required = false)
	private Integer dlCtrlObj;

	@ApiModelProperty(value = "信息采集受控对象", example = "升级对象/单元的软件包由哪个零件进行传输", required = false)
	private Integer instTxCtrlObj;

	@ApiModelProperty(value = "安装受控对象", example = "升级对象/单元的软件包由哪个零件负责安装", required = false)
	private Integer instCtrlObj;

	@ApiModelProperty(value = "安装约束（零件需要处于低压或高压条件下才可以安装）", example = "0 低压 1高压", required = false)
	private Integer instCondition;

	@ApiModelProperty(value = "总线类型", example = "1 can节点 2 以太网节点", required = false)
	private Integer busType;

	@ApiModelProperty(value = "信息采集脚本类型", example = "升级对象信息采集时所需要用到的脚本类型", required = false)
	private Integer infoCollScriptType;

	@ApiModelProperty(value = "安装算法类型（指SA算法的类型）", example = "", required = false)
	private Integer instAlgorithmType;

}