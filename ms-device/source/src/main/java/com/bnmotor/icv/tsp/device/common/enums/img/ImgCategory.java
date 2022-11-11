package com.bnmotor.icv.tsp.device.common.enums.img;

/**
 * @ClassName: ImgCategory
 * @Description: 图片分类
 * @author: zhangjianghua1
 * @date: 2020/11/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum ImgCategory {
    APPEARANCE(1, "外观"),
    INTERIOR(2, "内饰"),
    SPACE(3, "空间"),
    OFFICIAL(4, "官方"),
    OWNER(5, "车主");

    private final Integer category;
    private final String desp;

    ImgCategory(Integer category, String desp) {
        this.category = category;
        this.desp = desp;
    }

    public static ImgCategory valueOf(Integer category) {
        if (category == null) {
            return null;
        }
        for (ImgCategory imgCategory : ImgCategory.values()) {
            if (imgCategory.category.equals(category)) {
                return imgCategory;
            }
        }
        return null;
    }

    public String getDesp() {
        return desp;
    }
}
