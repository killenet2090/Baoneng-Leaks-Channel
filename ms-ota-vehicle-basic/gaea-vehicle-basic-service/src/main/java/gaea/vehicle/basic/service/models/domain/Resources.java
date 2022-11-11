/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.models.domain;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

/**
 * <pre>
 * 	<b>表名</b>：resources
 *  服务资源,服务功能，其中一些通用字段在ModelDO和BaseDO里面，该对象基本上只用于数据保存使用。
 * </pre>
 *
 * @author jiankang.xia
 * @version 1.0.0
 */
@Getter
@Setter
@Api("服务资源,服务功能")
public class Resources extends AbstractBase {
    /**
     * <pre>
     * 数据库字段: id
     * 描述: 主键;字段长度:11,是否必填:是。
     * </pre>
     */
    @ApiModelProperty("主键")
    private Integer id;
    /**
     * <pre>
     * 数据库字段: module
     * 描述: 模块;字段长度:32,是否必填:是。
     * </pre>
     */
    @ApiModelProperty("模块")
    private String module;
    /**
     * <pre>
     * 数据库字段: name
     * 描述: 资源名称;字段长度:64,是否必填:是。
     * </pre>
     */
    @ApiModelProperty("资源名称")
    private String name;
    /**
     * <pre>
     * 数据库字段: uri
     * 描述: 资源地址;字段长度:128,是否必填:是。
     * </pre>
     */
    @ApiModelProperty("资源地址")
    private String uri;
    /**
     * <pre>
     * 数据库字段: method
     * 描述: 方法类型,POST,PUT,GET,DELETE;字段长度:8,是否必填:是。
     * </pre>
     */
    @ApiModelProperty("方法类型,POST,PUT,GET,DELETE")
    private String method;
    /**
     * <pre>
     * 数据库字段: parent_uri_code
     * 描述: 父资源编码,提供建立关系;字段长度:128,是否必填:否。
     * </pre>
     */
    @ApiModelProperty("父资源编码,提供建立关系")
    private String parentUriCode;
    /**
     * <pre>
     * 数据库字段: uri_code
     * 描述: 资源编码,必须唯一;字段长度:128,是否必填:是。
     * </pre>
     */
    @ApiModelProperty("资源编码,必须唯一")
    private String uriCode;
    /**
     * <pre>
     * 数据库字段: data_flag
     * 描述: 数据权限,0-否,1-是;字段长度:4,是否必填:是。
     * </pre>
     */
    @ApiModelProperty("数据权限,0-否,1-是")
    private Integer dataFlag;
    /**
     * <pre>
     * 数据库字段: leaf_flag
     * 描述: 是否叶子,0-否,1-是;字段长度:4,是否必填:是。
     * </pre>
     */
    @ApiModelProperty("是否叶子,0-否,1-是")
    private Integer leafFlag;
    /**
     * <pre>
     * 数据库字段: type_flag
     * 描述: 资源类型,0-模块,1-子模块,2-服务;字段长度:4,是否必填:是。
     * </pre>
     */
    @ApiModelProperty("资源类型,0-模块,1-子模块,2-服务")
    private Integer typeFlag;
    /**
     * 子菜单
     */
    @ApiModelProperty("子菜单")
    private List<Resources> sonList;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
