/*
* Copyright 2020 The JA-SIG Collaborative. All rights reserved.
* distributed with thi file and available online at
*/
package gaea.user.center.service.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.utils.DateUtil;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import com.bnmotor.icv.adam.data.mysql.utils.PageUtil;
import com.bnmotor.icv.adam.data.redis.IRedisProvider;
import gaea.user.center.service.common.CommonConstant;
import gaea.user.center.service.common.CurrentUserUtils;
import gaea.user.center.service.common.annotation.LoginLogAnnotation;
import gaea.user.center.service.common.auth.TokenBuilder;
import gaea.user.center.service.common.auth.TokenDTO;
import gaea.user.center.service.common.auth.TokenUtils;
import gaea.user.center.service.common.auth.TokenVO;
import gaea.user.center.service.common.enums.BusinessStatusEnum;
import gaea.user.center.service.common.enums.EmailEnums;
import gaea.user.center.service.common.utils.DateCommonUtils;
import gaea.user.center.service.common.utils.EmailUtils;
import gaea.user.center.service.common.utils.Md5Utils;
import gaea.user.center.service.common.utils.SHA256Util;
import gaea.user.center.service.mapper.RoleMapper;
import gaea.user.center.service.mapper.UserMapper;
import gaea.user.center.service.mapper.UserProjectMapper;
import gaea.user.center.service.mapstuct.UserVoMapper;
import gaea.user.center.service.model.dto.TemplateSmsDto;
import gaea.user.center.service.model.dto.User;
import gaea.user.center.service.model.dto.UserProjectRel;
import gaea.user.center.service.model.dto.UserRoleRel;
import gaea.user.center.service.model.entity.*;
import gaea.user.center.service.model.request.PrivilegeQueryReq;
import gaea.user.center.service.model.request.UserBasic;
import gaea.user.center.service.model.request.UserQuery;
import gaea.user.center.service.model.request.UserReq;
import gaea.user.center.service.model.response.*;
import gaea.user.center.service.service.*;
import gaea.user.center.service.service.feign.PhoneFeignService;
import gaea.user.center.service.service.feign.UserFeignService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements IUserService
{
	/**
	 * 用户权限key的前缀
	 */
	private static final String PRIVILEGE_PREFIX = "system:user:privilege:";
	/**
	 * 用户信息key的前缀
	 */
	private static final String USER_PREFIX = "system:user:info:";
	/**
	 * 用户信息key的前缀
	 */
	private static final String TOKEN_PREFIX = "system:user:token:";
	/**
	 * 用户token过期时间
	 */
	@Value("${jwt.token.expire_time:3600000}")
	private String tokenExpireTime;
	/**
	 * 用户权限过期时间
	 */
	@Value("${jwt.privilege.expire_time:3600000}")
	private String privilegeExpireTime;
	/**
	 * 手机验证码过期时间，分钟数
	 */
	@Value("${spring.phone.expire.minutes}")
	private Integer phoneExpireTime;
	/**
	 * 邮箱验证码过期时间，分钟数
	 */
	@Value("${spring.mail.expire.minutes}")
	private Integer mailExpireTime;
	/**
	 * 账户激活路径
	 */
	@Value("${sms.activation.url}")
	private String url;

	@Value("${sms.redirect.url}")
	private String redirectUrl;

    /**
     * 持久操作对象
     */
	@Autowired
    private UserMapper userMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private UserProjectMapper userProjectMapper;

	@Autowired
	private IUserProjectService userProjectService;

	@Autowired
	private IPrivilegeService privilegeService;

	@Autowired
	private IRedisProvider redisProvider;

	@Autowired
	private ILoginLogService loginLogService;

	@Autowired
	private IUserCarAssemblyService userCarAssemblyService;

	//@Autowired
	//private IUserCarService userCarService;

	@Autowired
	private IUserVehicleService userVehicleService;

	@Autowired
	private EmailUtils emailUtils;

	@Autowired
	private IEmailRecordService emailRecordService;

	@Autowired
	private IPhoneRecordService phoneRecordService;

	@Autowired
	UserFeignService userFeignService;

	@Autowired
	PhoneFeignService phoneFeignService;

	@Override
	public Page<UserVo> queryPage(Pageable pageable, UserQuery query)
	{
		IPage iPage = PageUtil.map(pageable);
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserPageVo> userPoPage = userMapper.queryPage(iPage, query);
		Page<UserVo> page = UserVoMapper.INSTANCE.map(userPoPage);
		return page;
	}

	/**
	 * 根据用户id查询用户信息
	 * @param userId Id
	 * @return
	 */
	@Override
	public UserDetailVO queryById(Long userId) {
		UserDetailVO userDetailVO = userMapper.queryById(userId);
		userDetailVO.setRoles(roleMapper.queryRoleIdsByUserId(userId));
		List<UserProjectPo> projcetList = userProjectMapper.queryUserProjectListByUserId(userId);
		//查询用户所有车辆集
		List<UserCarAssemblyVO> userCarAssemblyVos = userCarAssemblyService.queryUserCarAssemblyListByUserId(userId);
		List<Long> projects = new ArrayList<>();
		for(UserProjectPo po :projcetList){
			projects.add(po.getProjectId());
		}
		userDetailVO.setProjects(projects);
		userDetailVO.setUserCarAssemblyVos(userCarAssemblyVos);
		return userDetailVO;
	}

	/**
	 * 新增用户
	 * @param userReq
	 * @return
	 * @throws AdamException
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long insertUser(UserReq userReq) throws AdamException {
		validateParam(userReq);
		userReq.setName(StringUtils.isEmpty(userReq.getEmail())?"":userReq.getEmail().toLowerCase().trim().split("@")[0]);
		userReq.setDisplayName(StringUtils.isEmpty(userReq.getDisplayName())?"":userReq.getDisplayName().trim());
		userReq.setEmail(StringUtils.isEmpty(userReq.getEmail())?"":userReq.getEmail().trim().toLowerCase());
		userReq.setPhone(StringUtils.isEmpty(userReq.getPhone())?"":userReq.getPhone().trim());
		//校验用户邮箱是否存在
		User user = new User();
		user.setEmail(userReq.getEmail());
		user.setDelFlag(0);
		User userOld = userMapper.queryByUserFiled(user);
		if(null != userOld){
			throw new AdamException(BusinessStatusEnum.USER_EMAIL_ALREADY_EXISTS.getCode(),BusinessStatusEnum.USER_EMAIL_ALREADY_EXISTS.getDescription());
		}
		//校验用户手机号是否存在
		user.setEmail(null);
		user.setPhone(userReq.getPhone());
		User userPhoneOld = userMapper.queryByUserFiled(user);
		if(null != userPhoneOld){
			throw new AdamException(BusinessStatusEnum.USER_PHONE_ALREADY_EXISTS.getCode(),BusinessStatusEnum.USER_PHONE_ALREADY_EXISTS.getDescription());
		}
		userReq.setDelFlag(0);
		userReq.setIsEnable(1);
		BeanUtils.copyProperties(userReq, user);
		String createBy = "";
		if(null!= CurrentUserUtils.getCurrentUser()){
			createBy = CurrentUserUtils.getCurrentUser().getUserName();
		}
		user.setCreateBy(createBy);
		user.setUpdateBy(createBy);
		//采用SHA256加密后保存
		String password = this.getRandomCode();
		user.setPassword(SHA256Util.getSHA256Encode(SHA256Util.getSHA256Encode(password)));
		//userMapper.insertUser(user);
		UserPo userPo = new UserPo();
		BeanUtils.copyProperties(user, userPo);
		userPo.setUpdateTime(LocalDateTime.now());
		userPo.setUpdateTime(LocalDateTime.now());
		userMapper.insert(userPo);
		//账户注册成功后发送初始密码到用户邮箱
		new Thread() {
			@Override
			public void run() {
			try {
				//异步发送账户注册初始密码
				Boolean result = emailUtils.sendEmail(userPo.getDisplayName(),userPo.getEmail(),"车联网云服务后台管理系统注册通知",password);
				EmailRecordPo emailRecordPo = new EmailRecordPo();
				emailRecordPo.setEmail(userPo.getEmail());
				emailRecordPo.setKeyWord(password);
				emailRecordPo.setType(EmailEnums.Status.REGISTER.getValue());
				emailRecordPo.setIsSend(result ? 1 : 0);
				emailRecordPo.setExpireTime(DateCommonUtils.afterMinutes(mailExpireTime));
				emailRecordPo.setSubject("车联网云服务后台管理系统注册通知");
				emailRecordPo.setDisplayName(userPo.getDisplayName());
				emailRecordService.insertEmailRecord(emailRecordPo);
				log.info("EmailRecordServiceImpl -> initPassword sucess");
			} catch (Exception e) {
				log.info("EmailRecordServiceImpl-> initPassword error", e);
			}
		};
	}.start();

		//获取用户-车辆集列表
		if((null != userReq.getModels()&& userReq.getModels().size() > 0) ||(null != userReq.getLabels() && userReq.getLabels().size() >0)){
			List<UserCarAssemblyPo> userCarAssemblyPoList = this.getUserCarAssemblyList(userReq.getModels(),userReq.getLabels(),userPo.getId());
			//保存用户与车辆集关系
			userCarAssemblyService.saveBatchUserCarAssembly(userCarAssemblyPoList);
			/**
			 * 异步生成用户车辆关系
			 * 1、通过远程调用接口，并发送一条全量更新消息到kafka中
			 * 2、异步消费kafka消息并据此同步更新数据库
			 * 3、去除重复生成的用户-车辆列表
			 */
			userFeignService.producerMessageToKafka(userPo.getId(),userReq.getModels(),userReq.getLabels());
		}
		if(userPo.getId()<1) {
			log.error("新增账户写入失败. 用户信息 : [ {} ]", userPo.getName());
			throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
		}
		if(null!=userReq.getRoles()&&!userReq.getRoles().isEmpty()) {
			UserRoleRel userRoleRel = new UserRoleRel();
			userRoleRel.setUserId(userPo.getId());
			userRoleRel.setRoles(userReq.getRoles());
			int status = roleMapper.insertUserRelRole(userRoleRel);
			if(status<1) {
				log.error("新增账户时角色写入失败. 用户ID : [ {} ]", userPo.getId());
				throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
			}
		}
		if(null!=userReq.getProjects()&&!userReq.getProjects().isEmpty()) {
			UserProjectRel userProjectRel = new UserProjectRel();
			userProjectRel.setUserId(userPo.getId());
			userProjectRel.setProjects(userReq.getProjects());
			int status = userProjectMapper.insertUserRelProject(userProjectRel);
			if(status<1) {
				log.error("新增账户时项目信息写入失败. 用户ID : [ {} ]", userPo.getId());
				throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
			}
		}
		return userPo.getId();
	}

	/**
	 * 入参校验
	 * @param userReq
	 */
	private static void validateParam(UserReq userReq){
		if(!StringUtils.isEmpty(userReq.getEmail())&& !Pattern.matches(CommonConstant.email_pattern, userReq.getEmail().trim())){
			throw new AdamException(BusinessStatusEnum.USER_EMAIL_PATTERN_INCORRECT.getCode(),BusinessStatusEnum.USER_EMAIL_PATTERN_INCORRECT.getDescription());
		}
	}
	/**
	 * 生成随机密码
	 */
	public String getRandomCode(){
		StringBuffer passwordBuffer = new StringBuffer();
		for(int i=0;i<6;i++){
			int radomNumber =  (int)(Math.random()*(10-1+1));
			passwordBuffer.append(radomNumber);
		}
		return passwordBuffer.toString();
	}

	/**
	 * 获取需要新增的用户-车辆集列表
	 * @param models 用户-配置车辆集
	 * @param lables 用户-标签车辆集
	 * @param userId 用户id
	 * @return
	 */
	public List<UserCarAssemblyPo> getUserCarAssemblyList(List<Long> models,List<Long> lables,Long userId) throws  AdamException{
		List<UserCarAssemblyPo> userCarAssemblyPoList = new ArrayList<>();
		//用户与配置-车辆集关系
		if(null != models && models.size() > 0 ){
			for(Long model:models){
				UserCarAssemblyPo userCarAssemblyPo = new UserCarAssemblyPo();
				userCarAssemblyPo.setCid(model);
				//配置
				userCarAssemblyPo.setType(1);
				userCarAssemblyPo.setUserId(userId);
				userCarAssemblyPo.setCreateBy("admin");
				userCarAssemblyPo.setCreateTime(LocalDateTime.now());
				userCarAssemblyPo.setUpdateBy("admin");
				userCarAssemblyPo.setUpdateTime(LocalDateTime.now());
				userCarAssemblyPo.setDelFlag(0);
				userCarAssemblyPoList.add(userCarAssemblyPo);
			}
		}
		//用户与标签-车辆集关系
		if(null != lables && lables.size() > 0 ){
			for (Long lable:lables){
				UserCarAssemblyPo userCarAssemblyPo = new UserCarAssemblyPo();
				userCarAssemblyPo.setCid(lable);
				//标签
				userCarAssemblyPo.setType(2);
				userCarAssemblyPo.setUserId(userId);
				userCarAssemblyPo.setCreateBy("admin");
				userCarAssemblyPo.setCreateTime(LocalDateTime.now());
				userCarAssemblyPo.setUpdateBy("admin");
				userCarAssemblyPo.setUpdateTime(LocalDateTime.now());
				userCarAssemblyPo.setDelFlag(0);
				userCarAssemblyPoList.add(userCarAssemblyPo);
			}
		}
		return userCarAssemblyPoList;
	}

	/**
	 * 更新用户信息
	 * @param userReq
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateUser(UserReq userReq) {
		User user = new User();
		//校验用户邮箱是否存在
		user.setEmail(userReq.getEmail().toLowerCase());
		user.setDelFlag(0);
		User userOld = userMapper.queryByUserFiled(user);
		if(null != userOld){
			if(!userOld.getId().equals(userReq.getId())){
				throw new AdamException(BusinessStatusEnum.USER_EMAIL_ALREADY_EXISTS.getCode(),BusinessStatusEnum.USER_EMAIL_ALREADY_EXISTS.getDescription());
			}
		}
		//校验用户手机号是否存在
		user.setEmail(null);
		user.setPhone(userReq.getPhone());
		User userPhoneOld = userMapper.queryByUserFiled(user);
		if(null != userPhoneOld){
			if(!userPhoneOld.getId().equals(userReq.getId())){
				throw new AdamException(BusinessStatusEnum.USER_PHONE_ALREADY_EXISTS.getCode(),BusinessStatusEnum.USER_PHONE_ALREADY_EXISTS.getDescription());
			}
		}
		try {
			BeanUtils.copyProperties(userReq, user);
			String createBy = "";
			if(null!= CurrentUserUtils.getCurrentUser()){
				createBy = CurrentUserUtils.getCurrentUser().getUserName();
			}
			user.setUpdateBy(createBy);
			//如果管理员延长了过期时间超过当前时间，则将状态变更为启用状态
			if(user.getExpireTime().getTime() > (new Date().getTime())){
				user.setIsEnable(1);
			}
			int status = userMapper.updateUser(user);
			/**
			 * 同步更新用户-车辆集数据
			 * 1、将原有用户绑定车辆集删除
			 * 2、重新添加新的用户-车辆集数据
			 * 3、同步删除该用户的所有用户-车辆关系
			 */
			if((null != userReq.getModels() && userReq.getModels().size() > 0) ||(null != userReq.getLabels() && userReq.getLabels().size() > 0) ) {
				userCarAssemblyService.deleteBatchUserAssembly(userReq.getId());
				//userCarService.deleteBatchUserCarByUserId(userReq.getId());
				userVehicleService.deleteUserId(String.valueOf(userReq.getId()),null);
				List<UserCarAssemblyPo> userCarAssemblyPoList = this.getUserCarAssemblyList(userReq.getModels(), userReq.getLabels(), userReq.getId());
				userCarAssemblyService.saveBatchUserCarAssembly(userCarAssemblyPoList);
				/**
				 * 异步生成用户车辆关系
				 * 1、通过远程调用接口，并发送一条全量更新消息到kafka中
				 * 2、异步消费kafka消息并据此同步更新数据库
				 * 3、去除重复生成的用户-车辆列表
				 */
				userFeignService.producerMessageToKafka(userReq.getId(),userReq.getModels(),userReq.getLabels());
			}else{//最新分配的用户车辆集为空，清除掉原有用户车辆集关系
				if(null!=userReq.getId()){
					userCarAssemblyService.deleteBatchUserAssembly(userReq.getId());
					//userCarService.deleteBatchUserCarByUserId(userReq.getId());
					userVehicleService.deleteUserId(String.valueOf(userReq.getId()),null);
				}
			}
			if(status<1)
			{
				log.error("更新用户失败. 用户ID : [ {} ]", user.getId());
				throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
			}
			if(null!=userReq.getRoles()&&!userReq.getRoles().isEmpty())
			{
				UserRoleRel userRoleRel = new UserRoleRel();
				userRoleRel.setUserId(user.getId());
				userRoleRel.setRoles(userReq.getRoles());
				int deleteStatus = roleMapper.deleteUserRelRoleByUserId(userRoleRel);
				int status1 = roleMapper.insertUserRelRole(userRoleRel);
				if(deleteStatus<1 & status1<1)
				{
					log.error("更新账户时角色写入失败. 用户ID : [ {} ]", user.getId());
					throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
				}
			}
			if(null!=userReq.getProjects()&&!userReq.getProjects().isEmpty())
			{
				UserProjectRel userProjectRel = new UserProjectRel();
				userProjectRel.setUserId(user.getId());
				userProjectRel.setProjects(userReq.getProjects());
				int deleteStatus = userProjectMapper.deleteUserRelProjectByUserId(userProjectRel);
				int status1 = userProjectMapper.insertUserRelProject(userProjectRel);
				if(deleteStatus<1 & status1<1)
				{
					log.error("更新账户时项目写入失败. 用户ID : [ {} ]", user.getId());
					throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
				}
			}
			//修改用户权限
			PrivilegeQueryReq privilegeQueryReq = new PrivilegeQueryReq();
			privilegeQueryReq.setUserId(user.getId());
			List<PrivilegeResp> listp = privilegeService.queryUserPrivilege(privilegeQueryReq);
			List<String> privileges = new ArrayList<>();
			for(PrivilegeResp res: listp){
				privileges.add(res.getPath());
			}
			redisProvider.setObject(PRIVILEGE_PREFIX+user.getId(), privileges, Long.valueOf(privilegeExpireTime)/1000, TimeUnit.SECONDS);
			Map<String, Object> map = new HashMap<>();
			map.put("userId",user.getId());
			map.put("userName",user.getName());
			map.put("orgId", user.getOrgId());
			redisProvider.setObject(USER_PREFIX+user.getId(), map, Long.valueOf(privilegeExpireTime)/1000, TimeUnit.SECONDS);
		}catch (Throwable e){
			log.error("SYSTEM_Exception : [ {} ]" , e.getMessage());
			if(!(e instanceof AdamException)){
				throw new AdamException(BusinessStatusEnum.SYSTEM_ERROR.getCode(),BusinessStatusEnum.SYSTEM_ERROR.getDescription());
			}
		}
		return user.getId().intValue();
	}

	/**
	 * 删除用户信息
	 * @param userId Id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteById(Long userId)
	{
		int status1 = 0;
		if(1L == userId)
		{
			throw new AdamException(BusinessStatusEnum.USER_CANT_DELETE.getCode(),BusinessStatusEnum.USER_CANT_DELETE.getDescription());
		}
		try {
			UserRoleRel userRoleRel = new UserRoleRel();
			userRoleRel.setUserId(userId);
			status1 = userMapper.deleteById(userId);
			//同步删除用户车辆集关系
			userCarAssemblyService.deleteBatchUserAssembly(userId);
			//同步删除用户车辆关系
			//userCarService.deleteBatchUserCarByUserId(userId);
			userVehicleService.deleteUserId(String.valueOf(userId),null);
			int deleteStatus = roleMapper.deleteUserRelRoleByUserId(userRoleRel);
			UserProjectRel userProjectRel = new UserProjectRel();
			userProjectRel.setUserId(userId);
			int deleteProjectStatus = userProjectMapper.deleteUserRelProjectByUserId(userProjectRel);
			if(deleteStatus<1 & status1<1 & deleteProjectStatus<1)
			{
				log.error("删除账户时角色失败. 用户ID : [ {} ]", userId);
				throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
			}
			//删除用户权限
			redisProvider.delete(PRIVILEGE_PREFIX+userId);
			//删除用户基本信息
			redisProvider.delete(USER_PREFIX+userId);
		}catch (Throwable e){
			log.error("SYSTEM_Exception : [ {} ]" , e.getMessage());
			if(!(e instanceof AdamException))
			{
				throw new AdamException(BusinessStatusEnum.SYSTEM_ERROR.getCode(),BusinessStatusEnum.SYSTEM_ERROR.getDescription());
			}
		}
		return status1;
	}

	/**
	 * 生成用户Token
	 * @param name
	 * @param password
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@LoginLogAnnotation
	public TokenVO login(String name,String password)
	{
		User user = new User();
		user.setName(name);
		user.setPassword(SHA256Util.getSHA256Encode(password));
		user = userMapper.queryByUserFiled(user);
		if(null == user) {
			throw new AdamException(BusinessStatusEnum.USER_NOT_FOUND.getCode(),BusinessStatusEnum.USER_NOT_FOUND.getDescription());
		}
		//已禁用账户提示信息
		if(null != user && user.getIsEnable() == 0){
			throw new AdamException(BusinessStatusEnum.USER_IS_DISABLED.getCode(),BusinessStatusEnum.USER_IS_DISABLED.getDescription());
		}

		try {
			//查询当前账号的权限
			PrivilegeQueryReq privilegeQueryReq = new PrivilegeQueryReq();
			privilegeQueryReq.setUserId(user.getId());
			List<PrivilegeResp> listp = privilegeService.queryUserPrivilege(privilegeQueryReq);
			List<String> privileges = new ArrayList<>();
			for(PrivilegeResp res: listp){
				privileges.add(res.getPath());
			}
			TokenDTO tokenDTO = TokenBuilder.getDefaultTokenDTO(Long.valueOf(tokenExpireTime));
			tokenDTO.setId(user.getId());
			tokenDTO.setSubject(user.getName());

			Map<String, Object> map = new HashMap<>();
			map.put("userId",user.getId().toString());
			map.put("userName",user.getName());
			map.put("displayName",user.getDisplayName());
			tokenDTO.setClaims(map);
			TokenVO tokenVO = TokenUtils.createToken(tokenDTO);

			//redisProvider.setObject(TOKEN_PREFIX+tokenVO.getToken(), tokenVO.getToken(), (2*Long.valueOf(tokenExpireTime))/1000, TimeUnit.SECONDS);
			redisProvider.setObject(PRIVILEGE_PREFIX+user.getId(), privileges, Long.valueOf(privilegeExpireTime)/1000, TimeUnit.SECONDS);
			redisProvider.setObject(USER_PREFIX+user.getId(), map, Long.valueOf(privilegeExpireTime)/1000, TimeUnit.SECONDS);
			return tokenVO;
		}catch (Throwable e){
			log.error("USER TOKEN CREATE FAILED. USER_NAME: [ {} ] , Exception : [ {} ]" , name, e.getMessage());
			if(!(e instanceof AdamException))
			{
				throw new AdamException(BusinessStatusEnum.SYSTEM_ERROR.getCode(),BusinessStatusEnum.SYSTEM_ERROR.getDescription());
			}
		}
		return null;
	}

	/**
	 * 忘记密码
	 * @param phone
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean forgetPassword(String phone,String code) {
		User user;
		String password = this.getRandomCode();
		User param = new User();
		param.setPhone(phone);
		//查询该账户是否存在
		user = userMapper.queryByUserFiled(param);
		if(null == user){
			throw new AdamException(BusinessStatusEnum.USER_PHONE_NOT_EXIST.getCode(),BusinessStatusEnum.USER_PHONE_NOT_EXIST.getDescription());
		}
		//校验手机号验证码是否有效
		PhoneRecordPo phoneRecordPo = phoneRecordService.getPhoneRecordEffective(phone,1);
		if(null != phoneRecordPo && code.equals(phoneRecordPo.getKeyWord())){
			log.info("SHA256Util加密结果：{}",Md5Utils.getMd5(password));
			user.setPassword(SHA256Util.getSHA256Encode(SHA256Util.getSHA256Encode(password)));
			String createBy = "";
			if(null!= CurrentUserUtils.getCurrentUser()){
				createBy = CurrentUserUtils.getCurrentUser().getUserName();
			}
			user.setUpdateBy(createBy);
			userMapper.updateUser(user);
		}else{
			throw new AdamException(BusinessStatusEnum.USER_PHONE_CODE_NOT_MATCH.getCode(),BusinessStatusEnum.USER_PHONE_CODE_NOT_MATCH.getDescription());
		}
		this.sendEmailSync(user,password);
		return true;
	}

	/**
	 * 异步发送邮件
	 * @param user
	 * @param code
	 */
	public void sendEmailSync(User user,String code){
		new Thread() {
			@Override
			public void run() {
				try {
					//异步发送账户注册重置密码
					Boolean result = emailUtils.sendEmail(user.getDisplayName(),user.getEmail(),"TSP平台账户重置密码",code);
					EmailRecordPo emailRecordPo = new EmailRecordPo();
					emailRecordPo.setEmail(user.getEmail());
					emailRecordPo.setKeyWord(code);
					emailRecordPo.setType(EmailEnums.Status.PASSWORD_RESET.getValue());
					emailRecordPo.setIsSend(result ? 1 : 0);
					emailRecordPo.setExpireTime(DateCommonUtils.afterMinutes(mailExpireTime));
					emailRecordPo.setSubject("TSP平台账户重置密码");
					emailRecordPo.setDisplayName(user.getDisplayName());
					emailRecordService.insertEmailRecord(emailRecordPo);
					log.info("EmailRecordServiceImpl -> initPassword sucess");
				} catch (Exception e) {
					log.info("EmailRecordServiceImpl-> initPassword error", e);
				}
			};
		}.start();
	}

	/**
	 * 修改用户密码
	 * @param userBasic
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean changePassword(UserBasic userBasic) {
		User user = new User();
		if(null == userBasic.getUserId()){
			userBasic.setUserId(CurrentUserUtils.getCurrentUser().getUserId());
		}
		user.setId(userBasic.getUserId());
		user = userMapper.queryByUserFiled(user);
		if(null != user) {
			if(!SHA256Util.getSHA256Encode(userBasic.getPassword()).equals(user.getPassword())){
				throw new AdamException(BusinessStatusEnum.PASSWORD_NOT_MATCH.getCode(),BusinessStatusEnum.PASSWORD_NOT_MATCH.getDescription());
			}
			user.setPassword(SHA256Util.getSHA256Encode(userBasic.getNewLoginPwd()));
			String createBy = "";
			if(null!= CurrentUserUtils.getCurrentUser()){
				createBy = CurrentUserUtils.getCurrentUser().getUserName();
			}
			user.setUpdateBy(createBy);
			int status = userMapper.updateUser(user);
			if(status<1) {
				log.error("修改密码写入失败. 用户ID : [ {} ]", user.getId());
				throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
			}
			return true;
		}
		return false;
	}

	/**
	 * 根据项目id查询项目拥有者列表
	 * @param projectId
	 * @return
	 * @throws AdamException
	 */
	@Override
	public List<User> queryUserListByProjectId(Long projectId) throws AdamException {
		List<User> userList = userMapper.queryUserListByProjectId(projectId);
		return userList;
	}

	/**
	 * 批量保存某用户下的所有项目列表信息
	 * @param userQuery
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer saveBatchUserProjects(UserQuery userQuery) throws  AdamException{
		Integer status = userProjectService.saveBatchUserProjects(userQuery.getUserId(),userQuery.getProjectIds());
		return status;
	}

	/**
	 * 启用被禁用或者休眠状态的用户
	 * @param userId
	 * @return
	 */
	@Override
	public Integer enableUserById(Long userId)  throws AdamException{
		UserPo userPo = userMapper.selectById(userId);
		if(null != userPo){
			//判断当前账户是否过期
			int gap = DateUtil.getDiscrepantDays(userPo.getExpireTime(),new Date());
			if(gap > 0){
				throw new AdamException(BusinessStatusEnum.USER_IS_EXPIRE_TIME_OVER.getCode(),BusinessStatusEnum.USER_IS_EXPIRE_TIME_OVER.getDescription());
			}
			userPo.setIsEnable(1);
			String updateBy = "";
			if(null!= CurrentUserUtils.getCurrentUser()){
				updateBy = CurrentUserUtils.getCurrentUser().getUserName();
			}
			userPo.setUpdateBy(updateBy);
			userPo.setUpdateTime(LocalDateTime.now());
			userMapper.updateById(userPo);
		}
		return userId.intValue();
	}

	/**
	 * 禁用账户
	 * @param userId
	 * @return
	 */
	@Override
	public Integer disEnableUserById(Long userId) throws AdamException {
		UserPo userPo = userMapper.selectById(userId);
		if(null != userPo){
			userPo.setIsEnable(0);
			String updateBy = "";
			if(null!= CurrentUserUtils.getCurrentUser()){
				updateBy = CurrentUserUtils.getCurrentUser().getUserName();
			}
			userPo.setUpdateBy(updateBy);
			userPo.setUpdateTime(LocalDateTime.now());
			userMapper.updateById(userPo);
			//删除用户权限
			redisProvider.delete(PRIVILEGE_PREFIX+userId);
			//删除用户基本信息
			redisProvider.delete(USER_PREFIX+userId);
		}
		return userId.intValue();
	}

	/**
	 * 定时任务
	 * 每天凌晨执行一次，将过期账户置为禁用状态，精确到天
	 */
	@Transactional(rollbackFor = Exception.class)
	public void userExpireTimeTask(){
		log.info("账户过期状态变更为禁用状态");
		UserQuery userQuery = new UserQuery();
		userQuery.setExpireTime(new Date());
		userQuery.setIsEnable(1);
		List<UserPo> userPoList = userMapper.getUserList(userQuery);
		for(UserPo po : userPoList){
			po.setIsEnable(0);
			po.setUpdateTime(LocalDateTime.now());
			userMapper.updateById(po);
			//删除用户权限
			redisProvider.delete(PRIVILEGE_PREFIX+po.getId());
			//删除用户基本信息
			redisProvider.delete(USER_PREFIX+po.getId());
		}
	}

	/**
	 * 异步校验用户参数唯一性接口
	 * @param email
	 * @param id
	 * @return
	 */
	@Override
	public String userParamValidate(String email,String phone,Long id) throws AdamException{
		String message = "";
		User user = new User();
		//校验用户邮箱是否存在
		if(!StringUtils.isEmpty(email)){
			user.setEmail(email);
			user.setDelFlag(0);
			User dbUserEmailOld = userMapper.queryByUserFiled(user);
			if(null == id && null != dbUserEmailOld){
				//新增
				message = "邮箱已存在!";
			}else if(null != id && null != dbUserEmailOld && !id.equals(dbUserEmailOld.getId())){
				//更新
				message = "邮箱已存在!";
			}
		}
		//校验用户电话号码是否存在
		if(!StringUtils.isEmpty(phone)){
			//校验用户手机号是否存在
			user.setPhone(phone);
			User dbUserPhoneOld = userMapper.queryByUserFiled(user);
			if(null == id && null != dbUserPhoneOld){
				message = "电话号码已存在";
			}else if(null != id && null != dbUserPhoneOld && !id.equals(dbUserPhoneOld.getId())){
				message = "电话号码已存在";
			}
		}
		return message;
	}

	/**
	 * 定时任务
	 * 每天凌晨执行一次，将长期（60天）未登录账户置为休眠状态，精确到天
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void userLastLoginTimeoutTask() {
		log.debug("账户长期未登录状态变更为休眠状态");
		LoginLogPo param = new LoginLogPo();
		param.setLoginTime(new Date());
		List<LoginLogPo> loginLogPoList = loginLogService.queryLoginLogList(param);
		for(LoginLogPo po : loginLogPoList){
			if(po.getLoginTime().getTime() < DateUtils.addDays(new Date(),-60).getTime()){
				UserPo userPo = userMapper.queryByUserName(po.getUserName());
				userPo.setIsEnable(-1);
				userPo.setUpdateTime(LocalDateTime.now());
				userMapper.updateById(userPo);
			}
		}
	}

	/**
	 * 用户登出
	 */
	@Override
	public void loginout() throws AdamException{
		if(null!=CurrentUserUtils.getCurrentUser()){
			//删除用户权限
			redisProvider.delete(PRIVILEGE_PREFIX+CurrentUserUtils.getCurrentUser().getUserId());
			//删除用户基本信息
			redisProvider.delete(USER_PREFIX+CurrentUserUtils.getCurrentUser().getUserId());
		}
		CurrentUserUtils.clearCurrentUser();
/*		if(null == CurrentUserUtils.getCurrentUser()){
			throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
		}*/
	}

//	/**
//	 * 账户激活-验证账户信息并发送验证码
//	 * @param email 账户邮箱
//	 * @param phone 账户手机号
//	 * @return
//	 */
//	@Override
//	public Integer checkCode(String email, String phone) throws AdamException{
//		User user = new User();
//		user.setPhone(phone);
//		user.setEmail(email);
//		User userExist = userMapper.queryByUserFiled(user);
//		//账户不存在
//		if(userExist == null){
//			throw new AdamException(BusinessStatusEnum.USER_EMAIL_PHONE_NOT_MATCH.getCode(),BusinessStatusEnum.USER_EMAIL_PHONE_NOT_MATCH.getDescription());
//		}
//		//账户为禁用状态状态
//		if(userExist.getIsEnable() != 0){
//			//查看当前手机号码是否存在有效验证码
//			String code = null;
//			PhoneRecordPo phoneRecordPoExist = phoneRecordService.getPhoneRecordEffective(phone, 0);
//			if(phoneRecordPoExist != null){
//				code = phoneRecordPoExist.getKeyWord();
//				//发送手机验证码
//				sendPhoneRpc(code,phoneRecordPoExist.getPhone());
//			}else{
//				code = this.getRandomCode();
//				//调用成功入库短信发送记录
//				PhoneRecordPo phoneRecordPo = new PhoneRecordPo();
//				phoneRecordPo.setExpireTime(DateCommonUtils.afterMinutes(phoneExpireTime));
//				phoneRecordPo.setKeyWord(code);
//				phoneRecordPo.setPhone(phone);
//				//账户激活类型
//				phoneRecordPo.setType(0);
////				phoneRecordPo.setType(type);
//				phoneRecordService.insertPhoneRecord(phoneRecordPo);
//				//发送手机验证码
//				sendPhoneRpc(code,phone);
//			}
//		}else{
//			throw new AdamException(BusinessStatusEnum.USER_IS_NOT_ENABLED.getCode(),BusinessStatusEnum.USER_IS_NOT_ENABLED.getDescription());
//		}
//		return userExist.getId().intValue();
//	}

	/**
	 * 发送手机验证码远程调用
	 */
	public String sendPhoneRpc(String code,String phone){
		String businessId = UUID.randomUUID().toString().replace("-","").toUpperCase();
		Map<String,String> extraMap = new HashMap<>();
		extraMap.put("authCode",code);
		extraMap.put("expiredTime",phoneExpireTime.toString());
		TemplateSmsDto templateSmsDto = new TemplateSmsDto();
		templateSmsDto.setBusinessId(businessId);
		templateSmsDto.setExtraData(extraMap);
		templateSmsDto.setFromType(1);
		templateSmsDto.setMappingTemplateId(400010001);
		templateSmsDto.setSendChannel(1);
		templateSmsDto.setSendPhone(phone);
		String result = phoneFeignService.sendPhone(templateSmsDto);
		return result;
	}


	/**
	 * 账户激活-验证手机验证码是否有效
	 * @param code 手机验证码
	 * @param phone 账户手机号
	 * @param email 账户邮箱
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer activation(String phone,String email, String code) throws  AdamException{
		User user = new User();
		user.setEmail(email);
		user.setPhone(phone);
		User userOld = userMapper.queryByUserFiled(user);
		PhoneRecordPo phoneRecordPo = phoneRecordService.getPhoneRecordEffective(phone,0);
		if(null != phoneRecordPo && code.equals(phoneRecordPo.getKeyWord())){
			//验证通过,发送激活邮件
			new Thread() {
				@Override
				public void run() {
				try {
					//异步发送账户激活邮件
					Boolean result = emailUtils.sendActivitionEmail(email,"TSP运营平台账户激活",url.replace("&{phone}",phone));
					EmailRecordPo emailRecordPo = new EmailRecordPo();
					emailRecordPo.setExpireTime(DateCommonUtils.afterMinutes(mailExpireTime));
					//激活邮箱
					emailRecordPo.setType(EmailEnums.Status.ACTIVIATION.getValue());
					emailRecordPo.setIsSend(result ? 1 : 0);
					emailRecordPo.setEmail(email);
					emailRecordPo.setKeyWord(phone);
					emailRecordPo.setSubject("TSP运营平台账户激活");
					emailRecordPo.setDisplayName(userOld.getDisplayName());
					emailRecordService.insertEmailRecord(emailRecordPo);
					log.info("EmailRecordServiceImpl -> initPassword sucess");
				} catch (Exception e) {
					log.info("EmailRecordServiceImpl-> initPassword error", e);
				}
			};
		}.start();
		}else{
			throw new AdamException(BusinessStatusEnum.DATA_NOT_FOUND.getCode(),BusinessStatusEnum.DATA_NOT_FOUND.getDescription());
		}
		return null;
	}

	/**
	 * 用户激活链接
	 *
	 * @param phone 手机号
	 * @return
	 */
	@Override
	public String activationUrl(String phone) throws Exception{
		User param = new User();
		param.setPhone(phone);
		User user = userMapper.queryByUserFiled(param);
		if(null != user){
			//修改为正常状态
			user.setIsEnable(1);
			user.setUpdateTime(new Date());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//设置默认过期时间
			Date expireTime = sdf.parse("2099-12-31 00:00:00");
			user.setExpireTime(expireTime);
			int status = userMapper.updateUser(user);
		}
		return "redirect:"+redirectUrl;
	}

	/**
	 * 修改手机号
	 * @param uid
	 * @param phone
	 * @return
	 */
	@Override
	public Integer changePhone(Long uid,String phone,String code,String password) throws AdamException{
		User user = new User();
		int status = 0;
		if(null == uid){
			uid = CurrentUserUtils.getCurrentUser().getUserId();
		}
		user.setId(uid);
		User userOld = userMapper.queryByUserFiled(user);
		if(null != userOld){
			if(!userOld.getPassword().equals(SHA256Util.getSHA256Encode(password))){
				throw new AdamException(BusinessStatusEnum.PASSWORD_NOT_MATCH.getCode(),BusinessStatusEnum.PASSWORD_NOT_MATCH.getDescription());
			}
			//验证码是否有效
			PhoneRecordPo phoneRecordPo = phoneRecordService.getPhoneRecordEffective(phone,2);
			if(null != phoneRecordPo && code.equals(phoneRecordPo.getKeyWord())){
				userOld.setPhone(phone);
				userOld.setUpdateTime(new Date());
				status = userMapper.updateUser(userOld);
			}else{
				throw new AdamException(BusinessStatusEnum.USER_PHONE_CODE_NOT_MATCH.getCode(),BusinessStatusEnum.USER_PHONE_CODE_NOT_MATCH.getDescription());
			}
		}else{
			throw new AdamException(BusinessStatusEnum.USER_NOT_FOUND.getCode(),BusinessStatusEnum.DATA_NOT_FOUND.getDescription());
		}
		return status;
	}

	/**
	 * 获取手机验证码
	 * @param phone
	 * @return
	 */
	@Override
	public Integer getCheckCodeNonLogin(String phone,String email,Integer type) {
		//生成随机验证码
		User user = new User();
		//忘记密码获取验证码
		user.setPhone(phone);
		user.setEmail(email);
		user.setDelFlag(0);
		User userOld = userMapper.queryByUserFiled(user);
		//校验手机号是否存在
		if(null == userOld){
			throw new AdamException(BusinessStatusEnum.USER_EMAIL_PHONE_NOT_MATCH.getCode(),BusinessStatusEnum.USER_EMAIL_PHONE_NOT_MATCH.getDescription());
		}
		//账户不为禁用状态状态
		if(type == 0 && userOld.getIsEnable() != 0){
			throw new AdamException(BusinessStatusEnum.USER_IS_NOT_ENABLED.getCode(),BusinessStatusEnum.USER_IS_NOT_ENABLED.getDescription());
		}
		//查看当前手机号码是否存在有效验证码
		String code = null;
		Integer result = 0;
		PhoneRecordPo phoneRecordPoExist = phoneRecordService.getPhoneRecordEffective(phone, type);
		if(phoneRecordPoExist != null){
			result = phoneRecordPoExist.getId().intValue();
			code = phoneRecordPoExist.getKeyWord();
			//发送手机验证码
			sendPhoneRpc(code,phoneRecordPoExist.getPhone());
		}else{
			code = this.getRandomCode();
			//调用成功入库短信发送记录
			PhoneRecordPo phoneRecordPo = new PhoneRecordPo();
			phoneRecordPo.setExpireTime(DateCommonUtils.afterMinutes(phoneExpireTime));
			phoneRecordPo.setKeyWord(code);
			phoneRecordPo.setPhone(phone);
			phoneRecordPo.setType(type);
			phoneRecordService.insertPhoneRecord(phoneRecordPo);
			result = phoneRecordPo.getId().intValue();
			//发送手机验证码
			sendPhoneRpc(code,phone);
		}
		return result;
	}

	/**
	 * 修改手机号获取手机验证码
	 * @param phone
	 * @param email
	 * @param password
	 * @return
	 */
	@Override
	public Integer getChangePhoneCode(String phone, String email, String password) {
		User user = new User();
		user.setEmail(email);
		user.setDelFlag(0);
		user.setPassword(SHA256Util.getSHA256Encode(password));
		User userOld = userMapper.queryByUserFiled(user);
		//验证原密码是否匹配
		if(null == userOld){
			throw new AdamException(BusinessStatusEnum.USER_EMAIL_PASSWORD_NOT_MATCH.getCode(),BusinessStatusEnum.USER_EMAIL_PASSWORD_NOT_MATCH.getDescription());
		}
		//验证手机号是否重复
		user.setPhone(phone);
		user.setPassword(null);
		user.setEmail(null);
		user.setDelFlag(0);
		User userExist = userMapper.queryByUserFiled(user);
		if(null != userExist){
			throw new AdamException(BusinessStatusEnum.USER_PHONE_ALREADY_EXISTS.getCode(),BusinessStatusEnum.USER_PHONE_ALREADY_EXISTS.getDescription());
		}
		Integer result = 0;
		//发送手机验证码
		String code = "";
		//调用成功入库短信发送记录
		PhoneRecordPo phoneRecordPoExist = phoneRecordService.getPhoneRecordEffective(phone, 2);
		if(null != phoneRecordPoExist){
			result = phoneRecordPoExist.getId().intValue();
			code = phoneRecordPoExist.getKeyWord();
			//发送手机验证码
			sendPhoneRpc(code,phoneRecordPoExist.getPhone());
		}else{
			code = this.getRandomCode();
			PhoneRecordPo phoneRecordPo = new PhoneRecordPo();
			phoneRecordPo.setExpireTime(DateCommonUtils.afterMinutes(phoneExpireTime));
			phoneRecordPo.setKeyWord(code);
			phoneRecordPo.setPhone(phone);
			//修改手机号
			phoneRecordPo.setType(2);
			phoneRecordService.insertPhoneRecord(phoneRecordPo);
			result = phoneRecordPo.getId().intValue();
			//发送手机验证码
			sendPhoneRpc(code,phone);
		}
		return result;
	}

	/**
	 * 校验用户名密码是否正确
	 * @param userId
	 * @param password
	 * @return
	 */
	public Integer checkUserPassword(Long userId, String password) {
		User user = new User();
		user.setId(userId);
		user.setPassword(password);
		User userPo = userMapper.queryByUserFiled(user);
		return null!=userPo?1:0;
	}
	/**
	 * 刷新用户Token
	 * @param userId
	 * @param token
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public String refreshToken(String userId, String token) {
		UserPo user = userMapper.selectById(userId);
		if(null == user) {
			throw new AdamException(BusinessStatusEnum.USER_NOT_FOUND.getCode(),BusinessStatusEnum.USER_NOT_FOUND.getDescription());
		}
		//已禁用账户提示信息
		if(null != user && user.getIsEnable() == 0){
			throw new AdamException(BusinessStatusEnum.USER_IS_DISABLED.getCode(),BusinessStatusEnum.USER_IS_DISABLED.getDescription());
		}
		try {
			//查询当前账号的权限
			PrivilegeQueryReq privilegeQueryReq = new PrivilegeQueryReq();
			privilegeQueryReq.setUserId(user.getId());
			List<PrivilegeResp> listp = privilegeService.queryUserPrivilege(privilegeQueryReq);
			List<String> privileges = new ArrayList<>();
			for(PrivilegeResp res: listp){
				privileges.add(res.getPath());
			}
			TokenDTO tokenDTO = TokenBuilder.getDefaultTokenDTO(Long.valueOf(tokenExpireTime));
			tokenDTO.setId(user.getId());
			tokenDTO.setSubject(user.getName());

			Map<String, Object> map = new HashMap<>();
			map.put("userId",user.getId().toString());
			map.put("userName",user.getName());
			map.put("displayName",user.getDisplayName());
			tokenDTO.setClaims(map);
			TokenVO tokenVO = TokenUtils.createToken(tokenDTO);

			//redisProvider.setObject(TOKEN_PREFIX+token, tokenVO.getToken(), (2*Long.valueOf(tokenExpireTime))/1000, TimeUnit.SECONDS);
			redisProvider.setObject(PRIVILEGE_PREFIX+user.getId(), privileges, Long.valueOf(privilegeExpireTime)/1000, TimeUnit.SECONDS);
			redisProvider.setObject(USER_PREFIX+user.getId(), map, Long.valueOf(privilegeExpireTime)/1000, TimeUnit.SECONDS);
			return tokenVO.getToken();
		}catch (Throwable e){
			log.error("REFRESH USER TOKEN CREATE FAILED. USER_ID: [ {} ] , Exception : [ {} ]" , userId, e.getMessage());
			if(!(e instanceof AdamException))
			{
				throw new AdamException(BusinessStatusEnum.SYSTEM_ERROR.getCode(),BusinessStatusEnum.SYSTEM_ERROR.getDescription());
			}
		}
		return null;
	}
	/**
	 * 校验Token是否有效
	 * @param token
	 * @return
	 */
	public String verifyToken(String token){
		try {
			TokenDTO tokenDTO = TokenBuilder.getDefaultTokenDTO(Long.valueOf(tokenExpireTime));
			Jws<Claims> jws = Jwts.parser().setSigningKey(tokenDTO.getSecret()).parseClaimsJws(token);
			return "1";
		} catch (Exception exception) {
			return "0";
		}
	}
	/**
	 * 获取tonken中相关信息
	 *
	 * @param token
	 * @return
	 */
	public String queryTokenBody(String token){
		try {
			TokenDTO tokenDTO = TokenBuilder.getDefaultTokenDTO(Long.valueOf(tokenExpireTime));
			Jws<Claims> jws = Jwts.parser().setSigningKey(tokenDTO.getSecret()).parseClaimsJws(token);
			return JSONObject.toJSONString(jws.getBody());
		} catch (Exception exception) {
			return null;
		}
	}
	/**
	 * 根据角色id查询账号列表
	 * @param roleIds
	 * @return
	 */
	public List<UserResp> queryUserListByRoleId(List<String> roleIds) {
		if(roleIds.isEmpty()){
			throw new AdamException(BusinessStatusEnum.ROLE_ID_IS_NULL.getCode(),BusinessStatusEnum.ROLE_ID_IS_NULL.getDescription());
		}
		return userMapper.queryUserListByRoleId(roleIds);
	}

	@Override
	public List<UserVo> getUserInfoByIds(List<String> userIds){
		return userMapper.getUserInfoByIds(userIds);
	}
}
