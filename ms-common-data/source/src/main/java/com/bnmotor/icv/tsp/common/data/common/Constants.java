package com.bnmotor.icv.tsp.common.data.common;

/**
 * @ClassName: Constants
 * @Description: 常量类
 * @author: zhangjianghua1
 * @date: 2020/7/11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface Constants {
    /**GEO类型_省份_ID*/
    Long GEO_TYPE_PROVINCE_ID = 32811736214147075L;
    /**GEO类型_直辖市_ID*/
    Long GEO_TYPE_MUNICIPALITY_ID = 32811736214147073L;
    /**GEO类型_特别行政区_ID*/
    Long GEO_TYPE_SPECIAL_REGION_ID = 32811736214147074L;
    /**GEO类型_市_ID*/
    Long GEO_TYPE_CITY_ID = 32811736214147076L;
    /**GEO类型_县区_ID*/
    Long GEO_TYPE_DISTRICT_ID = 32811736214147077L;
    /**中国_GEO_ID*/
    Long CHINA_GEO_ID = 32811736214147078L;
    /**中国_GEO_CODE*/
    String CHINA_GEO_CODE = "100000";
    /**中国行政区域编码校验正则*/
    String REGEX_CHINA_GEO_CODE = "^[1-8][0-7]\\d{4}$";

    /**GEO最近更新时间KEY*/
    String GEO_LAST_MODIFIED_KEY = "common-data:geo-last-modified";
}
