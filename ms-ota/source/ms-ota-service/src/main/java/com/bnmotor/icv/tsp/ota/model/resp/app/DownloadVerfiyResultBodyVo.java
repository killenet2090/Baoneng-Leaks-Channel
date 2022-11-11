package com.bnmotor.icv.tsp.ota.model.resp.app;

import com.bnmotor.icv.tsp.ota.common.enums.AppEnums;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @ClassName: DownloadVerfiyResultVo
 * @Description： 下载确认结果对象(对应内容)
 * @Author: xuxiaochang1
 * @Date: 2020/8/15 18:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 **/
@Data
@ToString(callSuper = true)
public class DownloadVerfiyResultBodyVo extends RemoteDownloadCallbackBodyVo{

    /**
     *
     */
    public DownloadVerfiyResultBodyVo(){
        this.setDownloadProcessType(AppEnums.DownloadProcessType4AppEnum.DOWNLOAD_VERIFY_RESULT.getType());
    }

    @ApiModelProperty(value = "确认结果：1=确认下载，2=取消下载", example = "1", required = true)
    private Integer verifyResult;

    @ApiModelProperty(value = "确认来源：1=Hu端，2=APP端", example = "2", required = true)
    private Integer verifySource;

    @ApiModelProperty(value = "TBOX端是否会立即开启下载：1=立即开启，2=延迟开始下载", example = "1", required = true)
    private Integer immediateDownload;

    @ApiModelProperty(value = "预计安装时间", example = "100", required = true)
    private Integer estimateInstalledTime;

    private Long downloadTotal;
    private Long downloadFinished;
    private Integer downloadPercent;
    private Integer downloadRemainTime;

}
