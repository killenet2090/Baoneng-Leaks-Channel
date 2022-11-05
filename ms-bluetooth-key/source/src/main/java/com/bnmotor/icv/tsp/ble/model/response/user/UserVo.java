package com.bnmotor.icv.tsp.ble.model.response.user;


import lombok.Data;

/**
 * @ClassName: UserVo
 * @Description: 用户信息查询
 * @author: shuqi1
 * @date: 2020/7/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class UserVo {
    /**
     * 用户唯一标识
     */
    private long uid;
    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户名字
     */
    private String userName;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 性别 1-男；2-女
     */
    private Integer gender;
    /**
     * 图像
     */
    private String headImg;
    /**
     * 图像的URL
     */
    private String headImgUrl;
    /**
     * 省
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 区
     */
    private String area;

    /**
     * 是否设置密码
     */
    private Boolean existPassword;
}
