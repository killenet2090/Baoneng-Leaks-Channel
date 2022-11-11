/*
* Copyright 2020 The JA-SIG Collaborative. All rights reserved.
* distributed with thi file and available online at
*/
package gaea.user.center.service.service.impl;

import com.bnmotor.icv.adam.core.exception.AdamException;
import gaea.user.center.service.dao.RoleMapper;
import gaea.user.center.service.dao.UserMapper;
import gaea.user.center.service.models.domain.User;
import gaea.user.center.service.models.domain.UserRoleRel;
import gaea.user.center.service.models.enums.BusinessStatusEnum;
import gaea.user.center.service.models.query.UserQuery;
import gaea.user.center.service.models.request.UserReq;
import gaea.user.center.service.models.vo.UserDetailVO;
import gaea.user.center.service.service.UserService;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *  默认业务实现类,建议流程控制/业务流程在API实现.
 *	如果需要项目内复用的业务代码,可以考虑在该层实现.
 *  不在统一实现事务配置,需要业务请在下面接口注解,需要注意,事务是整个类代理,所以请在public 入口方法注解.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Slf4j
@AllArgsConstructor
@Service("userService")
public class UserServiceImpl implements UserService {
    /**
     * 持久操作对象
     */
    private final UserMapper userMapper;

	private final RoleMapper roleMapper;



    @Override
	public List<User> queryPage(UserQuery query) {
		int count = userMapper.countPage(query);
		query.setTotalItem(count);
		if (count == 0) {
			return Collections.emptyList();
		} else {
			return userMapper.queryPage(query);
		}
	}

	@Override
	public UserDetailVO queryById(Long userId) {
		UserDetailVO userDetailVO = userMapper.queryById(userId);
		userDetailVO.setRoles(roleMapper.queryRoleIdsByUserId(userId));
		return userDetailVO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long insertUser(UserReq userReq) throws AdamException {

		User user = new User();
		try {
			BeanUtils.copyProperties(userReq, user);
			//初始化password 暂时写死
			user.setLoginPwd("bnmotors@2020");
			userMapper.insertUser(user);
			if(user.getId()<1){
				log.error("新增账户写入失败. 用户信息 : [ {} ]", user.getLoginName());
				throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
			}
			if(!userReq.getRoles().isEmpty()){
				UserRoleRel userRoleRel = new UserRoleRel();
				userRoleRel.setUserId(user.getId());
				userRoleRel.setRoles(userReq.getRoles());
				//int a = 1/0;
				int status = roleMapper.insertUserRelRole(userRoleRel);
				if(status<1){
					log.error("新增账户时角色写入失败. 用户ID : [ {} ]", user.getId());
					throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
				}
			}
		}catch (Throwable e){
			if(!(e instanceof AdamException)){
				throw new AdamException(BusinessStatusEnum.SYSTEM_ERROR.getCode(),BusinessStatusEnum.SYSTEM_ERROR.getDescription());
			}
		}
		return user.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateUser(UserReq userReq) {
		User user = new User();
		try {
			BeanUtils.copyProperties(userReq, user);
			int status = userMapper.updateUser(user);
			if(status<1){
				log.error("更新用户失败. 用户ID : [ {} ]", user.getId());
				throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
			}
			if(!userReq.getRoles().isEmpty()){
				UserRoleRel userRoleRel = new UserRoleRel();
				userRoleRel.setUserId(user.getId());
				userRoleRel.setRoles(userReq.getRoles());
				int deleteStatus = roleMapper.deleteUserRelRoleByUserId(userRoleRel);
				int status1 = roleMapper.insertUserRelRole(userRoleRel);
				if(deleteStatus<1 & status1<1){
					log.error("更新账户时角色写入失败. 用户ID : [ {} ]", user.getId());
					throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
				}
			}
		}catch (Throwable e){
			if(!(e instanceof AdamException)){
				throw new AdamException(BusinessStatusEnum.SYSTEM_ERROR.getCode(),BusinessStatusEnum.SYSTEM_ERROR.getDescription());
			}
		}
		return user.getId().intValue();
	}

	@Override
	public int deleteById(Long userId) {
		int status1 = 0;
		try {
			UserRoleRel userRoleRel = new UserRoleRel();
			userRoleRel.setUserId(userId);
			status1 = userMapper.deleteById(userId);
			int deleteStatus = roleMapper.deleteUserRelRoleByUserId(userRoleRel);
			if(deleteStatus<1 & status1<1){
				log.error("删除账户时角色写入失败. 用户ID : [ {} ]", userId);
				throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
			}
		}catch (Throwable e){
			if(!(e instanceof AdamException)){
				throw new AdamException(BusinessStatusEnum.SYSTEM_ERROR.getCode(),BusinessStatusEnum.SYSTEM_ERROR.getDescription());
			}
		}
		return status1;
	}

}
