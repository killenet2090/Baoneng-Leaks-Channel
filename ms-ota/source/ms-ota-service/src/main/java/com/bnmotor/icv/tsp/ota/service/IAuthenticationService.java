package com.bnmotor.icv.tsp.ota.service;


/**
 * @ClassName: IAuthenticationService.java IAuthenticationService
 * @Description: 用户鉴权 查询用户信息 ms-operation-system会提供接口
 * @author eyanlong2
 * @since 2020-9-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IAuthenticationService {
	/**
	 * 获取tsp用户信息
	 * @param userId
	 * @return
	 */
	com.bnmotor.icv.tsp.ota.model.resp.feign.UserInfoVo findUserInfo(String userId);
}