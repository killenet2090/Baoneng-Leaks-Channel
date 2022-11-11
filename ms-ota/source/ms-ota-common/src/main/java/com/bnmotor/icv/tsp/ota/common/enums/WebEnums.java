package com.bnmotor.icv.tsp.ota.common.enums;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.assertj.core.util.Lists;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: WebEnums
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/2 16:02
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public final class WebEnums {
    private WebEnums(){}

    /**
     * 数据逻辑删除状态枚举
     */
    public enum IsEnableEnum {
        /**
         * 保留
         */
        NOT_ENABLED(0),

        /**
         * 删除
         */
        ENABLED(1),
        ;

        @Getter
        private Integer flag;

        IsEnableEnum(Integer flag) {
            this.flag = flag;
        }
    }

    /**
     * 下载进度阶段枚举类型
     */
    public enum StrategyStatusEnum implements MyBaseEnumInt{
        NEW(0, "新建"),
        TO_AUDIT(1, "审核中"),
        AUDIT_PASSED(2, "审核通过"),
        AUDIT_REFUSE(3, "审核拒绝"),
        AUDIT_INVALID(4, "策略失效"),
        AUDIT_EVIEW(5, "审批驳回"),
        AUDIT_REVOKE(6, "撤回"),
        ;

        @Getter
        private Integer value;
        @Getter
        private String desc;



        StrategyStatusEnum(Integer value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }

    /**
     * 前置条件类型枚举
     */
    public enum PreConditionTypeEnum implements MyBaseEnumStr{
        POWER_STATE("1", "电源状况", buildPowerStatePreConditionValues(), 0, ArithmeticOperationValueTypeEnum.EQUAL.getValue()),
        GEAR_STATE("2", "档位状态", buildGearStatePreConditionValues(), 0, ArithmeticOperationValueTypeEnum.EQUAL.getValue()),
        CHARGE_UP_STATE("3", "车辆充电状态", buildChargeUpStatePreConditionValues(), 0, ArithmeticOperationValueTypeEnum.EQUAL.getValue()),
        ELECTROKINETIC_CELL_QUANTITY("4", "动力电池电量", buildElectrokineticCellQuantityPreConditionValues(), 0, ArithmeticOperationValueTypeEnum.GT_OR_EQUAL.getValue()),
        STORAGE_CELL_QUANTITY("5", "蓄电池电量", buildStorageCellQuantityPreConditionValues(), 0, ArithmeticOperationValueTypeEnum.GT_OR_EQUAL.getValue()),
        STORAGE_CELL_TENSION("6", "蓄电池电压", buildStorageCellTensionPreConditionValues(), 0, ArithmeticOperationValueTypeEnum.EQUAL.getValue()),
        EPB_STATE("7", "EPB状态", buildEpbStatePreConditionValues(), 0, ArithmeticOperationValueTypeEnum.EQUAL.getValue()),
        SPEED_STATE("8", "车速状态", buildSpeedStatePreConditionValues(), 0, ArithmeticOperationValueTypeEnum.EQUAL.getValue()),
        ;

        @Getter
        private String value;
        @Getter
        private String desc;

        @Getter
        private Integer operateType;

        @Getter
        private Integer operateValue;

        @Getter
        private List<PreConditionValue> preConditionValues;

        PreConditionTypeEnum(String value, String desc, List<PreConditionValue> preConditionValues, Integer operateType, Integer operateValue) {
            this.value = value;
            this.desc = desc;
            this.preConditionValues = preConditionValues;
            this.operateType = operateType;
            this.operateValue = operateValue;
        }

        /**
         * 电源状态
         * @return
         */
        private static List<PreConditionValue> buildPowerStatePreConditionValues(){
            List<PreConditionValue> list = Lists.newArrayList();
            list.add(PreConditionValue.builder().isDefault(1).value(1).valueDesc("ON").build());
            list.add(PreConditionValue.builder().isDefault(0).value(2).valueDesc("READY").build());
            list.add(PreConditionValue.builder().isDefault(0).value(3).valueDesc("IGN").build());
            list.add(PreConditionValue.builder().isDefault(0).value(4).valueDesc("CRANK IGN").build());
            return list;
        }

        /**
         * 档位状态
         * @return
         */
        private static List<PreConditionValue> buildGearStatePreConditionValues(){
            List<PreConditionValue> list = Lists.newArrayList();
            list.add(PreConditionValue.builder().isDefault(1).value(1).valueDesc("P档").build());
            /*list.add(PreConditionValue.builder().isDefault(0).value(1).valueDesc("R档").build());*/
            list.add(PreConditionValue.builder().isDefault(0).value(2).valueDesc("N档").build());
            list.add(PreConditionValue.builder().isDefault(0).value(3).valueDesc("D档").build());
            return list;
        }

        /**
         * 充电状态
         * @return
         */
        private static List<PreConditionValue> buildChargeUpStatePreConditionValues(){
            List<PreConditionValue> list = Lists.newArrayList();
            list.add(PreConditionValue.builder().isDefault(1).value(1).valueDesc("未充电").build());
            list.add(PreConditionValue.builder().isDefault(0).value(2).valueDesc("充电中").build());
            return list;
        }

        /**
         * 动力电池电量
         * @return
         */
        private static List<PreConditionValue> buildElectrokineticCellQuantityPreConditionValues(){
            List<PreConditionValue> list = Lists.newArrayList();
            list.add(PreConditionValue.builder().isDefault(1).value(70).valueDesc("70%").build());
            return list;
        }

        /**
         * 蓄电池电量
         * @return
         */
        private static List<PreConditionValue> buildStorageCellQuantityPreConditionValues(){
            List<PreConditionValue> list = Lists.newArrayList();
            list.add(PreConditionValue.builder().isDefault(1).value(30).valueDesc("30%").build());
            return list;
        }

        /**
         * 蓄电池电压
         * @return
         */
        private static List<PreConditionValue> buildStorageCellTensionPreConditionValues(){
            List<PreConditionValue> list = Lists.newArrayList();
            list.add(PreConditionValue.builder().isDefault(1).value(12).valueDesc("12V").build());
            return list;
        }

        /**
         * epb
         * @return
         */
        private static List<PreConditionValue> buildEpbStatePreConditionValues(){
            List<PreConditionValue> list = Lists.newArrayList();
            list.add(PreConditionValue.builder().isDefault(1).value(1).valueDesc("UP").build());
            list.add(PreConditionValue.builder().isDefault(0).value(2).valueDesc("DOWN").build());
            return list;
        }

        /**
         * 车速状态枚举
         * @return
         */
        private static List<PreConditionValue> buildSpeedStatePreConditionValues(){
            List<PreConditionValue> list = Lists.newArrayList();
            list.add(PreConditionValue.builder().isDefault(1).value(0).valueDesc("0").build());
            return list;
        }

        public static List<PreConditionTypeEnum> list(){
            return EnumSet.allOf(PreConditionTypeEnum.class).stream().collect(Collectors.toList());
        }
    }

    @Data
    @Builder
    public static class PreConditionValue{
        Integer isDefault;
        Integer value;
        String valueDesc;
    }

    /**
     * 算术运算枚举类型
     */
    public enum ArithmeticOperationValueTypeEnum implements MyBaseEnumInt{
        EQUAL(0, "="),
        NOT_EQUAL(1, "!="),
        GT(2, ">"),
        LT(3, "<"),
        GT_OR_EQUAL(4, ">="),
        LT_OR_EQUAL(5, "<="),
        ;
        @Getter
        private Integer value;
        @Getter
        private String desc;

        ArithmeticOperationValueTypeEnum(Integer value, String desc){
            this.value = value;
            this.desc = desc;
        }
    }

    /*public static void main(String[] args) {
        System.out.println(MyEnumUtil.getByValue(1, StrategyStatusEnum.class));
    }*/

    //1免审批，2待审批、3审批中、4已审批、5未通过

    public enum PlanApproveStateEnum implements MyBaseEnumInt{
        APPROVE_NO_NEED(1, "免审批"),
        APPROVE_WAIT(2, "待审批"),
        APPROVE_(3, "审批中"),
        PASSED(4, "已审批"),
        UNPASSED(5, "未通过"),
        REVIEW_APPROVAL(6, "驳回"),
        REVOKE_APPROVAl(7, "撤回"),
        ;

        @Getter
        private Integer value;
        @Getter
        private String desc;

        PlanApproveStateEnum(Integer value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }

    public enum PlanPublishStateEnum implements MyBaseEnumInt{
        //发布状态： 1待发布 2发布中 3已发布 4已失效 5延迟发布
        APPROVE_NO_NEED(1, "待发布"),
        APPROVE_WAIT(2, "发布中"),
        APPROVE_(3, "已发布"),
        PASSED(4, "已失效"),
        UNPASSED(5, "延迟发布"),
        SUSPEND(6, "暂停发布"), //
        ;

        @Getter
        private Integer value;
        @Getter
        private String desc;

        PlanPublishStateEnum(Integer value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }
}
