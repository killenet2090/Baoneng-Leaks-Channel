package com.bnmotor.icv.tsp.vehicle.auth.util;

import com.bnmotor.icv.adam.core.constant.AdamConstant;
import com.bnmotor.icv.adam.core.utils.StringUtil;

import java.security.InvalidParameterException;
import java.util.regex.Pattern;

/**
 * @ClassName: ClientIdUtils
 * @Description:
 * @author: wuhao1
 * @data: 2020-06-09
 * @Copyrigght: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class ClientIdUtils {

    /**
     * 根据clientId获取vin号
     *
     * @param clientId
     * @return
     * @throws InvalidParameterException
     */
    public static String getVinFromClientId(String clientId) {
        String[] strs = StringUtil.split(clientId, AdamConstant.SEPARATOR_SHORT_LINE, true);
        String vin = clientId;
        if (strs.length == 4) {
            vin = strs[1];
        }
        return vin;
    }

    /**
     * 根据clientId获取SN号
     * @param clientId
     * @return
     */
    public static String getSNFromClientId(String clientId) {
        String[] strs = StringUtil.split(clientId, AdamConstant.SEPARATOR_SHORT_LINE, true);
        String sn = clientId;
        if (strs.length == 4) {
            sn = strs[3];
        }
        return sn;
    }

    /**
     * 是否是T-Box的clientId
     * @param clientId
     * @return
     */
    public static boolean isClientIdTbox(String clientId) {
        String pattern = "^vehicle-.*-tbox-.*";
        return Pattern.matches(pattern, clientId);
    }

    /**
     * 是否是IHU的clientId
     * @param clientId
     * @return
     */
    public static boolean isClientIdIHU(String clientId) {
        String pattern = "^vehicle-.*-ihu-.*";
        return Pattern.matches(pattern, clientId);
    }
}
