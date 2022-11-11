package gaea.user.center.service.controller.inner;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import gaea.user.center.service.model.response.UserVo;
import gaea.user.center.service.service.IUserService;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * @Description
 * @Author liming1
 * @Date 2020/11/12
 */
@RestController
@RequestMapping("/inner/os/useropen")
@Api(value = "对接内部系统用户相关接口", tags = {"对接内部系统用户相关接口管理"})
@Slf4j
public class UserOpenController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "资源管理位:  获取用户基础信息", notes = "资源管理位:  获取用户基础信息")
    @PostMapping("/getUserInfoByIds")
    public ResponseEntity getUserInfoByIds(@RequestBody List<String> userIds){
        if (CollectionUtils.isEmpty(userIds)) {
            throw new AdamException(RespCode.USER_REQUIRED_PARAMETER_EMPTY);
        }
        List<UserVo> vehInfoVos = userService.getUserInfoByIds(userIds);
        return RestResponse.ok(vehInfoVos);
    }
}
