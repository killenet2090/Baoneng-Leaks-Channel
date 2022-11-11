package com.bnmotor.icv.tsp.device.common.excel;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * @ClassName: CascadeRelation
 * @Description: 级联关系
 * @author: zhangwei2
 * @date: 2020/12/9
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class CascadeRelation {
    /**
     * 隐藏表名称
     */
    private String hideSheetName = "hide";
    /**
     * 第一列
     */
    private Integer firstColumn = 2;
    /**
     * 级联数据从哪列开始
     */
    private char offset = 'C';
    /**
     * 级联数据从哪列开始
     */
    private Integer offsetColumn = 4;
    /**
     * 总限制行数
     */
    private Integer totalRow = 2000;
    /**
     * 总级别
     */
    private Integer totalLevel;
    /**
     * 根集合
     */
    private List<String> roots = new ArrayList<>();

    /**
     * 名称字典,要求数据必须符合excel名称管理系统规范
     */
    private List<String> names = new ArrayList<>();
    /**
     * 层级关系
     */
    private Map<String, List<String>> dataRelation = new HashMap<>();

    /**
     * 根据级联数据组装级联关系
     */
    public CascadeRelation builder(CascadeData... cascadeData) {
        List<CascadeData> cascadeDatas = Arrays.asList(cascadeData);
        totalLevel = cascadeDatas.size();
        int i = 0;
        Map<String, String> parent = new HashMap<>();
        for (CascadeData temp : cascadeDatas) {
            boolean processSelf = temp.isProcessSelf();
            if (i == 0) {
                for (CascadeDataItem item : temp.getCascadeDataItems()) {
                    String tempName = NameRule.escapeName(item.getName(processSelf));
                    if (!roots.contains(tempName)) {
                        roots.add(tempName);
                        names.add(tempName);
                        parent.put(item.getUniqKey(), tempName);
                    }
                }
            }

            if (i != cascadeDatas.size() - 1) {
                for (CascadeDataItem item : temp.getCascadeDataItems()) {
                    if (!parent.containsKey(item.getParent())) {
                        continue;
                    }
                    String tempName = NameRule.escapeName(item.getName(processSelf));
                    if (!names.contains(tempName)) {
                        names.add(tempName);
                    }
                }
            }

            if (i != 0) {
                parent = processCascadeData(temp, parent);
            }

            i++;
        }
        return this;
    }

    private Map<String, String> processCascadeData(CascadeData cascadeData, Map<String, String> parent) {
        if (CollectionUtils.isEmpty(cascadeData.getCascadeDataItems())) {
            return Collections.EMPTY_MAP;
        }

        Map<String, String> uniqKeyToDisplay = new HashMap<>();
        for (CascadeDataItem cascadeDataItem : cascadeData.getCascadeDataItems()) {
            String temp = cascadeDataItem.getParent();
            if (!parent.containsKey(temp)) {
                continue;
            }

            String display = parent.get(temp);
            if (cascadeData.isProcessParent()) {
                temp = NameRule.escapeName(temp + "__" + display);
            } else {
                temp = display;
            }

            List<String> data = dataRelation.computeIfAbsent(temp, k -> new ArrayList<>());

            String name = NameRule.escapeName(cascadeDataItem.getName(cascadeData.isProcessSelf()));
            if (!data.contains(name)) {
                data.add(name);
            }
            uniqKeyToDisplay.put(cascadeDataItem.getUniqKey(), cascadeDataItem.getDisplay());
        }

        return uniqKeyToDisplay;
    }

}
