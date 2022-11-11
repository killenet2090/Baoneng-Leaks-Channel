package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.adam.data.redis.IRedisProvider;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.model.resp.feign.UserInfoVo;
import com.bnmotor.icv.tsp.ota.service.IAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @ClassName: AuthorizationUtil.java AuthorizationUtil
 * @Description: 从token中解析username(token后面会加密处理，无法通过解析取出username)
 *               目前的解决方法：直接通过查询redis查询用户信息
 *               ms-operation-system可能会在后面的时间提供接口查询用户信息
 * @author eyanlong2
 * @since 2020-9-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService {

	@Qualifier("otaRedisProvider")
	@Autowired
	private IRedisProvider redisProvider;

	@Override
	public com.bnmotor.icv.tsp.ota.model.resp.feign.UserInfoVo findUserInfo(String userId) {
		if (StringUtils.isBlank(userId)) {
			return null;
		}

		try {
			return redisProvider.getObject(CommonConstant.TSP_USER_KEY_PREFIX + userId, UserInfoVo.class);
		} catch (Exception e) {
			log.error("查询用户信息异常|{} userId|{}", userId, e.getMessage(), e);
		}
		return null;
	}

}