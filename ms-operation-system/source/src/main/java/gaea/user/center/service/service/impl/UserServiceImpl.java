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
 *  ?????????????????????,??????????????????/???????????????API??????.
 *	??????????????????????????????????????????,???????????????????????????.
 *  ??????????????????????????????,????????????????????????????????????,????????????,????????????????????????,????????????public ??????????????????.
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
	 * ????????????key?????????
	 */
	private static final String PRIVILEGE_PREFIX = "system:user:privilege:";
	/**
	 * ????????????key?????????
	 */
	private static final String USER_PREFIX = "system:user:info:";
	/**
	 * ????????????key?????????
	 */
	private static final String TOKEN_PREFIX = "system:user:token:";
	/**
	 * ??????token????????????
	 */
	@Value("${jwt.token.expire_time:3600000}")
	private String tokenExpireTime;
	/**
	 * ????????????????????????
	 */
	@Value("${jwt.privilege.expire_time:3600000}")
	private String privilegeExpireTime;
	/**
	 * ???????????????????????????????????????
	 */
	@Value("${spring.phone.expire.minutes}")
	private Integer phoneExpireTime;
	/**
	 * ???????????????????????????????????????
	 */
	@Value("${spring.mail.expire.minutes}")
	private Integer mailExpireTime;
	/**
	 * ??????????????????
	 */
	@Value("${sms.activation.url}")
	private String url;

	@Value("${sms.redirect.url}")
	private String redirectUrl;

    /**
     * ??????????????????
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
	 * ????????????id??????????????????
	 * @param userId Id
	 * @return
	 */
	@Override
	public UserDetailVO queryById(Long userId) {
		UserDetailVO userDetailVO = userMapper.queryById(userId);
		userDetailVO.setRoles(roleMapper.queryRoleIdsByUserId(userId));
		List<UserProjectPo> projcetList = userProjectMapper.queryUserProjectListByUserId(userId);
		//???????????????????????????
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
	 * ????????????
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
		//??????????????????????????????
		User user = new User();
		user.setEmail(userReq.getEmail());
		user.setDelFlag(0);
		User userOld = userMapper.queryByUserFiled(user);
		if(null != userOld){
			throw new AdamException(BusinessStatusEnum.USER_EMAIL_ALREADY_EXISTS.getCode(),BusinessStatusEnum.USER_EMAIL_ALREADY_EXISTS.getDescription());
		}
		//?????????????????????????????????
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
		//??????SHA256???????????????
		String password = this.getRandomCode();
		user.setPassword(SHA256Util.getSHA256Encode(SHA256Util.getSHA256Encode(password)));
		//userMapper.insertUser(user);
		UserPo userPo = new UserPo();
		BeanUtils.copyProperties(user, userPo);
		userPo.setUpdateTime(LocalDateTime.now());
		userPo.setUpdateTime(LocalDateTime.now());
		userMapper.insert(userPo);
		//??????????????????????????????????????????????????????
		new Thread() {
			@Override
			public void run() {
			try {
				//????????????????????????????????????
				Boolean result = emailUtils.sendEmail(userPo.getDisplayName(),userPo.getEmail(),"????????????????????????????????????????????????",password);
				EmailRecordPo emailRecordPo = new EmailRecordPo();
				emailRecordPo.setEmail(userPo.getEmail());
				emailRecordPo.setKeyWord(password);
				emailRecordPo.setType(EmailEnums.Status.REGISTER.getValue());
				emailRecordPo.setIsSend(result ? 1 : 0);
				emailRecordPo.setExpireTime(DateCommonUtils.afterMinutes(mailExpireTime));
				emailRecordPo.setSubject("????????????????????????????????????????????????");
				emailRecordPo.setDisplayName(userPo.getDisplayName());
				emailRecordService.insertEmailRecord(emailRecordPo);
				log.info("EmailRecordServiceImpl -> initPassword sucess");
			} catch (Exception e) {
				log.info("EmailRecordServiceImpl-> initPassword error", e);
			}
		};
	}.start();

		//????????????-???????????????
		if((null != userReq.getModels()&& userReq.getModels().size() > 0) ||(null != userReq.getLabels() && userReq.getLabels().size() >0)){
			List<UserCarAssemblyPo> userCarAssemblyPoList = this.getUserCarAssemblyList(userReq.getModels(),userReq.getLabels(),userPo.getId());
			//??????????????????????????????
			userCarAssemblyService.saveBatchUserCarAssembly(userCarAssemblyPoList);
			/**
			 * ??????????????????????????????
			 * 1??????????????????????????????????????????????????????????????????kafka???
			 * 2???????????????kafka????????????????????????????????????
			 * 3??????????????????????????????-????????????
			 */
			userFeignService.producerMessageToKafka(userPo.getId(),userReq.getModels(),userReq.getLabels());
		}
		if(userPo.getId()<1) {
			log.error("????????????????????????. ???????????? : [ {} ]", userPo.getName());
			throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
		}
		if(null!=userReq.getRoles()&&!userReq.getRoles().isEmpty()) {
			UserRoleRel userRoleRel = new UserRoleRel();
			userRoleRel.setUserId(userPo.getId());
			userRoleRel.setRoles(userReq.getRoles());
			int status = roleMapper.insertUserRelRole(userRoleRel);
			if(status<1) {
				log.error("?????????????????????????????????. ??????ID : [ {} ]", userPo.getId());
				throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
			}
		}
		if(null!=userReq.getProjects()&&!userReq.getProjects().isEmpty()) {
			UserProjectRel userProjectRel = new UserProjectRel();
			userProjectRel.setUserId(userPo.getId());
			userProjectRel.setProjects(userReq.getProjects());
			int status = userProjectMapper.insertUserRelProject(userProjectRel);
			if(status<1) {
				log.error("???????????????????????????????????????. ??????ID : [ {} ]", userPo.getId());
				throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
			}
		}
		return userPo.getId();
	}

	/**
	 * ????????????
	 * @param userReq
	 */
	private static void validateParam(UserReq userReq){
		if(!StringUtils.isEmpty(userReq.getEmail())&& !Pattern.matches(CommonConstant.email_pattern, userReq.getEmail().trim())){
			throw new AdamException(BusinessStatusEnum.USER_EMAIL_PATTERN_INCORRECT.getCode(),BusinessStatusEnum.USER_EMAIL_PATTERN_INCORRECT.getDescription());
		}
	}
	/**
	 * ??????????????????
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
	 * ???????????????????????????-???????????????
	 * @param models ??????-???????????????
	 * @param lables ??????-???????????????
	 * @param userId ??????id
	 * @return
	 */
	public List<UserCarAssemblyPo> getUserCarAssemblyList(List<Long> models,List<Long> lables,Long userId) throws  AdamException{
		List<UserCarAssemblyPo> userCarAssemblyPoList = new ArrayList<>();
		//???????????????-???????????????
		if(null != models && models.size() > 0 ){
			for(Long model:models){
				UserCarAssemblyPo userCarAssemblyPo = new UserCarAssemblyPo();
				userCarAssemblyPo.setCid(model);
				//??????
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
		//???????????????-???????????????
		if(null != lables && lables.size() > 0 ){
			for (Long lable:lables){
				UserCarAssemblyPo userCarAssemblyPo = new UserCarAssemblyPo();
				userCarAssemblyPo.setCid(lable);
				//??????
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
	 * ??????????????????
	 * @param userReq
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateUser(UserReq userReq) {
		User user = new User();
		//??????????????????????????????
		user.setEmail(userReq.getEmail().toLowerCase());
		user.setDelFlag(0);
		User userOld = userMapper.queryByUserFiled(user);
		if(null != userOld){
			if(!userOld.getId().equals(userReq.getId())){
				throw new AdamException(BusinessStatusEnum.USER_EMAIL_ALREADY_EXISTS.getCode(),BusinessStatusEnum.USER_EMAIL_ALREADY_EXISTS.getDescription());
			}
		}
		//?????????????????????????????????
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
			//??????????????????????????????????????????????????????????????????????????????????????????
			if(user.getExpireTime().getTime() > (new Date().getTime())){
				user.setIsEnable(1);
			}
			int status = userMapper.updateUser(user);
			/**
			 * ??????????????????-???????????????
			 * 1???????????????????????????????????????
			 * 2???????????????????????????-???????????????
			 * 3???????????????????????????????????????-????????????
			 */
			if((null != userReq.getModels() && userReq.getModels().size() > 0) ||(null != userReq.getLabels() && userReq.getLabels().size() > 0) ) {
				userCarAssemblyService.deleteBatchUserAssembly(userReq.getId());
				//userCarService.deleteBatchUserCarByUserId(userReq.getId());
				userVehicleService.deleteUserId(String.valueOf(userReq.getId()),null);
				List<UserCarAssemblyPo> userCarAssemblyPoList = this.getUserCarAssemblyList(userReq.getModels(), userReq.getLabels(), userReq.getId());
				userCarAssemblyService.saveBatchUserCarAssembly(userCarAssemblyPoList);
				/**
				 * ??????????????????????????????
				 * 1??????????????????????????????????????????????????????????????????kafka???
				 * 2???????????????kafka????????????????????????????????????
				 * 3??????????????????????????????-????????????
				 */
				userFeignService.producerMessageToKafka(userReq.getId(),userReq.getModels(),userReq.getLabels());
			}else{//???????????????????????????????????????????????????????????????????????????
				if(null!=userReq.getId()){
					userCarAssemblyService.deleteBatchUserAssembly(userReq.getId());
					//userCarService.deleteBatchUserCarByUserId(userReq.getId());
					userVehicleService.deleteUserId(String.valueOf(userReq.getId()),null);
				}
			}
			if(status<1)
			{
				log.error("??????????????????. ??????ID : [ {} ]", user.getId());
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
					log.error("?????????????????????????????????. ??????ID : [ {} ]", user.getId());
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
					log.error("?????????????????????????????????. ??????ID : [ {} ]", user.getId());
					throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
				}
			}
			//??????????????????
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
	 * ??????????????????
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
			//?????????????????????????????????
			userCarAssemblyService.deleteBatchUserAssembly(userId);
			//??????????????????????????????
			//userCarService.deleteBatchUserCarByUserId(userId);
			userVehicleService.deleteUserId(String.valueOf(userId),null);
			int deleteStatus = roleMapper.deleteUserRelRoleByUserId(userRoleRel);
			UserProjectRel userProjectRel = new UserProjectRel();
			userProjectRel.setUserId(userId);
			int deleteProjectStatus = userProjectMapper.deleteUserRelProjectByUserId(userProjectRel);
			if(deleteStatus<1 & status1<1 & deleteProjectStatus<1)
			{
				log.error("???????????????????????????. ??????ID : [ {} ]", userId);
				throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
			}
			//??????????????????
			redisProvider.delete(PRIVILEGE_PREFIX+userId);
			//????????????????????????
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
	 * ????????????Token
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
		//???????????????????????????
		if(null != user && user.getIsEnable() == 0){
			throw new AdamException(BusinessStatusEnum.USER_IS_DISABLED.getCode(),BusinessStatusEnum.USER_IS_DISABLED.getDescription());
		}

		try {
			//???????????????????????????
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
	 * ????????????
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
		//???????????????????????????
		user = userMapper.queryByUserFiled(param);
		if(null == user){
			throw new AdamException(BusinessStatusEnum.USER_PHONE_NOT_EXIST.getCode(),BusinessStatusEnum.USER_PHONE_NOT_EXIST.getDescription());
		}
		//????????????????????????????????????
		PhoneRecordPo phoneRecordPo = phoneRecordService.getPhoneRecordEffective(phone,1);
		if(null != phoneRecordPo && code.equals(phoneRecordPo.getKeyWord())){
			log.info("SHA256Util???????????????{}",Md5Utils.getMd5(password));
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
	 * ??????????????????
	 * @param user
	 * @param code
	 */
	public void sendEmailSync(User user,String code){
		new Thread() {
			@Override
			public void run() {
				try {
					//????????????????????????????????????
					Boolean result = emailUtils.sendEmail(user.getDisplayName(),user.getEmail(),"TSP????????????????????????",code);
					EmailRecordPo emailRecordPo = new EmailRecordPo();
					emailRecordPo.setEmail(user.getEmail());
					emailRecordPo.setKeyWord(code);
					emailRecordPo.setType(EmailEnums.Status.PASSWORD_RESET.getValue());
					emailRecordPo.setIsSend(result ? 1 : 0);
					emailRecordPo.setExpireTime(DateCommonUtils.afterMinutes(mailExpireTime));
					emailRecordPo.setSubject("TSP????????????????????????");
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
	 * ??????????????????
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
				log.error("????????????????????????. ??????ID : [ {} ]", user.getId());
				throw new AdamException(BusinessStatusEnum.DATA_NOT_UPDATED.getCode(),BusinessStatusEnum.DATA_NOT_UPDATED.getDescription());
			}
			return true;
		}
		return false;
	}

	/**
	 * ????????????id???????????????????????????
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
	 * ???????????????????????????????????????????????????
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
	 * ??????????????????????????????????????????
	 * @param userId
	 * @return
	 */
	@Override
	public Integer enableUserById(Long userId)  throws AdamException{
		UserPo userPo = userMapper.selectById(userId);
		if(null != userPo){
			//??????????????????????????????
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
	 * ????????????
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
			//??????????????????
			redisProvider.delete(PRIVILEGE_PREFIX+userId);
			//????????????????????????
			redisProvider.delete(USER_PREFIX+userId);
		}
		return userId.intValue();
	}

	/**
	 * ????????????
	 * ???????????????????????????????????????????????????????????????????????????
	 */
	@Transactional(rollbackFor = Exception.class)
	public void userExpireTimeTask(){
		log.info("???????????????????????????????????????");
		UserQuery userQuery = new UserQuery();
		userQuery.setExpireTime(new Date());
		userQuery.setIsEnable(1);
		List<UserPo> userPoList = userMapper.getUserList(userQuery);
		for(UserPo po : userPoList){
			po.setIsEnable(0);
			po.setUpdateTime(LocalDateTime.now());
			userMapper.updateById(po);
			//??????????????????
			redisProvider.delete(PRIVILEGE_PREFIX+po.getId());
			//????????????????????????
			redisProvider.delete(USER_PREFIX+po.getId());
		}
	}

	/**
	 * ???????????????????????????????????????
	 * @param email
	 * @param id
	 * @return
	 */
	@Override
	public String userParamValidate(String email,String phone,Long id) throws AdamException{
		String message = "";
		User user = new User();
		//??????????????????????????????
		if(!StringUtils.isEmpty(email)){
			user.setEmail(email);
			user.setDelFlag(0);
			User dbUserEmailOld = userMapper.queryByUserFiled(user);
			if(null == id && null != dbUserEmailOld){
				//??????
				message = "???????????????!";
			}else if(null != id && null != dbUserEmailOld && !id.equals(dbUserEmailOld.getId())){
				//??????
				message = "???????????????!";
			}
		}
		//????????????????????????????????????
		if(!StringUtils.isEmpty(phone)){
			//?????????????????????????????????
			user.setPhone(phone);
			User dbUserPhoneOld = userMapper.queryByUserFiled(user);
			if(null == id && null != dbUserPhoneOld){
				message = "?????????????????????";
			}else if(null != id && null != dbUserPhoneOld && !id.equals(dbUserPhoneOld.getId())){
				message = "?????????????????????";
			}
		}
		return message;
	}

	/**
	 * ????????????
	 * ???????????????????????????????????????60??????????????????????????????????????????????????????
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void userLastLoginTimeoutTask() {
		log.debug("????????????????????????????????????????????????");
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
	 * ????????????
	 */
	@Override
	public void loginout() throws AdamException{
		if(null!=CurrentUserUtils.getCurrentUser()){
			//??????????????????
			redisProvider.delete(PRIVILEGE_PREFIX+CurrentUserUtils.getCurrentUser().getUserId());
			//????????????????????????
			redisProvider.delete(USER_PREFIX+CurrentUserUtils.getCurrentUser().getUserId());
		}
		CurrentUserUtils.clearCurrentUser();
/*		if(null == CurrentUserUtils.getCurrentUser()){
			throw new AdamException(BusinessStatusEnum.USER_IS_NOT_LOGIN.getCode(),BusinessStatusEnum.USER_IS_NOT_LOGIN.getDescription());
		}*/
	}

//	/**
//	 * ????????????-????????????????????????????????????
//	 * @param email ????????????
//	 * @param phone ???????????????
//	 * @return
//	 */
//	@Override
//	public Integer checkCode(String email, String phone) throws AdamException{
//		User user = new User();
//		user.setPhone(phone);
//		user.setEmail(email);
//		User userExist = userMapper.queryByUserFiled(user);
//		//???????????????
//		if(userExist == null){
//			throw new AdamException(BusinessStatusEnum.USER_EMAIL_PHONE_NOT_MATCH.getCode(),BusinessStatusEnum.USER_EMAIL_PHONE_NOT_MATCH.getDescription());
//		}
//		//???????????????????????????
//		if(userExist.getIsEnable() != 0){
//			//???????????????????????????????????????????????????
//			String code = null;
//			PhoneRecordPo phoneRecordPoExist = phoneRecordService.getPhoneRecordEffective(phone, 0);
//			if(phoneRecordPoExist != null){
//				code = phoneRecordPoExist.getKeyWord();
//				//?????????????????????
//				sendPhoneRpc(code,phoneRecordPoExist.getPhone());
//			}else{
//				code = this.getRandomCode();
//				//????????????????????????????????????
//				PhoneRecordPo phoneRecordPo = new PhoneRecordPo();
//				phoneRecordPo.setExpireTime(DateCommonUtils.afterMinutes(phoneExpireTime));
//				phoneRecordPo.setKeyWord(code);
//				phoneRecordPo.setPhone(phone);
//				//??????????????????
//				phoneRecordPo.setType(0);
////				phoneRecordPo.setType(type);
//				phoneRecordService.insertPhoneRecord(phoneRecordPo);
//				//?????????????????????
//				sendPhoneRpc(code,phone);
//			}
//		}else{
//			throw new AdamException(BusinessStatusEnum.USER_IS_NOT_ENABLED.getCode(),BusinessStatusEnum.USER_IS_NOT_ENABLED.getDescription());
//		}
//		return userExist.getId().intValue();
//	}

	/**
	 * ?????????????????????????????????
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
	 * ????????????-?????????????????????????????????
	 * @param code ???????????????
	 * @param phone ???????????????
	 * @param email ????????????
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
			//????????????,??????????????????
			new Thread() {
				@Override
				public void run() {
				try {
					//??????????????????????????????
					Boolean result = emailUtils.sendActivitionEmail(email,"TSP????????????????????????",url.replace("&{phone}",phone));
					EmailRecordPo emailRecordPo = new EmailRecordPo();
					emailRecordPo.setExpireTime(DateCommonUtils.afterMinutes(mailExpireTime));
					//????????????
					emailRecordPo.setType(EmailEnums.Status.ACTIVIATION.getValue());
					emailRecordPo.setIsSend(result ? 1 : 0);
					emailRecordPo.setEmail(email);
					emailRecordPo.setKeyWord(phone);
					emailRecordPo.setSubject("TSP????????????????????????");
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
	 * ??????????????????
	 *
	 * @param phone ?????????
	 * @return
	 */
	@Override
	public String activationUrl(String phone) throws Exception{
		User param = new User();
		param.setPhone(phone);
		User user = userMapper.queryByUserFiled(param);
		if(null != user){
			//?????????????????????
			user.setIsEnable(1);
			user.setUpdateTime(new Date());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//????????????????????????
			Date expireTime = sdf.parse("2099-12-31 00:00:00");
			user.setExpireTime(expireTime);
			int status = userMapper.updateUser(user);
		}
		return "redirect:"+redirectUrl;
	}

	/**
	 * ???????????????
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
			//?????????????????????
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
	 * ?????????????????????
	 * @param phone
	 * @return
	 */
	@Override
	public Integer getCheckCodeNonLogin(String phone,String email,Integer type) {
		//?????????????????????
		User user = new User();
		//???????????????????????????
		user.setPhone(phone);
		user.setEmail(email);
		user.setDelFlag(0);
		User userOld = userMapper.queryByUserFiled(user);
		//???????????????????????????
		if(null == userOld){
			throw new AdamException(BusinessStatusEnum.USER_EMAIL_PHONE_NOT_MATCH.getCode(),BusinessStatusEnum.USER_EMAIL_PHONE_NOT_MATCH.getDescription());
		}
		//??????????????????????????????
		if(type == 0 && userOld.getIsEnable() != 0){
			throw new AdamException(BusinessStatusEnum.USER_IS_NOT_ENABLED.getCode(),BusinessStatusEnum.USER_IS_NOT_ENABLED.getDescription());
		}
		//???????????????????????????????????????????????????
		String code = null;
		Integer result = 0;
		PhoneRecordPo phoneRecordPoExist = phoneRecordService.getPhoneRecordEffective(phone, type);
		if(phoneRecordPoExist != null){
			result = phoneRecordPoExist.getId().intValue();
			code = phoneRecordPoExist.getKeyWord();
			//?????????????????????
			sendPhoneRpc(code,phoneRecordPoExist.getPhone());
		}else{
			code = this.getRandomCode();
			//????????????????????????????????????
			PhoneRecordPo phoneRecordPo = new PhoneRecordPo();
			phoneRecordPo.setExpireTime(DateCommonUtils.afterMinutes(phoneExpireTime));
			phoneRecordPo.setKeyWord(code);
			phoneRecordPo.setPhone(phone);
			phoneRecordPo.setType(type);
			phoneRecordService.insertPhoneRecord(phoneRecordPo);
			result = phoneRecordPo.getId().intValue();
			//?????????????????????
			sendPhoneRpc(code,phone);
		}
		return result;
	}

	/**
	 * ????????????????????????????????????
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
		//???????????????????????????
		if(null == userOld){
			throw new AdamException(BusinessStatusEnum.USER_EMAIL_PASSWORD_NOT_MATCH.getCode(),BusinessStatusEnum.USER_EMAIL_PASSWORD_NOT_MATCH.getDescription());
		}
		//???????????????????????????
		user.setPhone(phone);
		user.setPassword(null);
		user.setEmail(null);
		user.setDelFlag(0);
		User userExist = userMapper.queryByUserFiled(user);
		if(null != userExist){
			throw new AdamException(BusinessStatusEnum.USER_PHONE_ALREADY_EXISTS.getCode(),BusinessStatusEnum.USER_PHONE_ALREADY_EXISTS.getDescription());
		}
		Integer result = 0;
		//?????????????????????
		String code = "";
		//????????????????????????????????????
		PhoneRecordPo phoneRecordPoExist = phoneRecordService.getPhoneRecordEffective(phone, 2);
		if(null != phoneRecordPoExist){
			result = phoneRecordPoExist.getId().intValue();
			code = phoneRecordPoExist.getKeyWord();
			//?????????????????????
			sendPhoneRpc(code,phoneRecordPoExist.getPhone());
		}else{
			code = this.getRandomCode();
			PhoneRecordPo phoneRecordPo = new PhoneRecordPo();
			phoneRecordPo.setExpireTime(DateCommonUtils.afterMinutes(phoneExpireTime));
			phoneRecordPo.setKeyWord(code);
			phoneRecordPo.setPhone(phone);
			//???????????????
			phoneRecordPo.setType(2);
			phoneRecordService.insertPhoneRecord(phoneRecordPo);
			result = phoneRecordPo.getId().intValue();
			//?????????????????????
			sendPhoneRpc(code,phone);
		}
		return result;
	}

	/**
	 * ?????????????????????????????????
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
	 * ????????????Token
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
		//???????????????????????????
		if(null != user && user.getIsEnable() == 0){
			throw new AdamException(BusinessStatusEnum.USER_IS_DISABLED.getCode(),BusinessStatusEnum.USER_IS_DISABLED.getDescription());
		}
		try {
			//???????????????????????????
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
	 * ??????Token????????????
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
	 * ??????tonken???????????????
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
	 * ????????????id??????????????????
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
