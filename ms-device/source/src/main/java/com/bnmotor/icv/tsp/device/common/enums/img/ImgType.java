package com.bnmotor.icv.tsp.device.common.enums.img;

/**
 * @ClassName: PicImgType
 * @Description: 图片类型
 * @author: zhangwei2
 * @date: 2020/11/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum ImgType {
    SMALL(1, "缩略图"),
    BIG(2, "大图");

    private final int type;
    private final String desp;

    ImgType(int type, String desp) {
        this.type = type;
        this.desp = desp;
    }

    public static ImgType valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        for (ImgType imgType : ImgType.values()) {
            if (imgType.type == type) {
                return imgType;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public String getDesp() {
        return desp;
    }
}
