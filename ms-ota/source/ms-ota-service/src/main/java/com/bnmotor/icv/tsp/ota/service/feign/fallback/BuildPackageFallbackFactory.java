package com.bnmotor.icv.tsp.ota.service.feign.fallback;

import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.model.req.feign.BuildPackageDto;
import com.bnmotor.icv.tsp.ota.model.req.feign.QueryPackageStatusDto;
import com.bnmotor.icv.tsp.ota.model.resp.feign.BuildResultInfo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.QueryResultInfo;
import com.bnmotor.icv.tsp.ota.service.feign.BuildPackageService;
import feign.hystrix.FallbackFactory;


/**
 * @ClassName: BuildPackageFallbackFactory
 * @Description: 做包服务服务接口工厂类
 * @author: xuxiaochang1
 * @date: 2020/9/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class BuildPackageFallbackFactory implements FallbackFactory<BuildPackageService> {
    @Override
    public BuildPackageService create(Throwable throwable) {
        return new BuildPackageService(){
            @Override
            public BuildResultInfo createTask(BuildPackageDto packageDto){
                BuildResultInfo buildResultInfo = new BuildResultInfo();
                buildResultInfo.setCode(Enums.CodeEnum.PACKAGE_FAIL.getCode());
                buildResultInfo.setComment(Enums.CodeEnum.PACKAGE_FAIL.getComment());
                return buildResultInfo;
            }

            @Override
            public QueryResultInfo queryStatus(QueryPackageStatusDto packageDto) {
                QueryResultInfo queryResultInfo = new QueryResultInfo();
                queryResultInfo.setCode(Enums.CodeEnum.PACKAGE_FAIL.getCode());
                queryResultInfo.setComment(Enums.CodeEnum.PACKAGE_FAIL.getComment());
                return queryResultInfo;
            }
        };
    }
}
