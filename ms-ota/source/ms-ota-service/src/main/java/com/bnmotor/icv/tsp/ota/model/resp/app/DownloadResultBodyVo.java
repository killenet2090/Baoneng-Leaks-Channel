package com.bnmotor.icv.tsp.ota.model.resp.app;

import lombok.Data;

/**
 * @ClassName: DownloadResultBodyVo
 * @Description： 新版本检查对应结果
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
public class DownloadResultBodyVo extends RemoteDownloadCallbackBodyVo{
    /*@ApiModelProperty(value = "安装异常情况分类：1=磁盘空间不够，2=下载后的包校验失败，3=一般情况的失败", example = "1", required = true)
    private Integer  failType;*/
    /**
     * 预计升级时间
     */
    private Integer estimateInstalledTime;
    /**
     * 预计升级时间
     */
    private Integer installedRemainTime;
}
