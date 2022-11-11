package com.bnmotor.icv.tsp.vehstatus.common.enums;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: VehStatusEnum
 * @Description: 车况枚举
 * @author: huangyun1
 * @date: 2020/5/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum VehStatusEnum implements BaseEnum<String> {

    /**
     *GPS时间戳,UNIX时间戳,毫秒
     */
    GPS_TS("f1", "gpsTs", "gpsInfo"),
    /**
     *GPS位置有效状态
     */
    GPS_POSITION_STATUS("f2", "gpsPositionStatus", "gpsInfo"),
    /**
     *GPS东西经状态
     */
    LATITUDE_STATUS("f3", "latitudeType", "gpsInfo"),
    /**
     *GPS南北纬状态
     */
    LOGITUDE_STATUS("f4", "longitudeType", "gpsInfo"),
    /**
     *经度
     */
    LONGITUDE("f5", "longitude", "gpsInfo"),
    /**
     *纬度
     */
    LATITUDE("f6", "latitude", "gpsInfo"),
    /**
     *GPS高度
     */
    ALTITUDE("f7", "altitude", "gpsInfo"),
    /**
     *GPS速度
     */
    GPS_SPEED("f8", "gpsSpeed", "gpsInfo"),
    /**
     *GPS方向
     */
    GPS_DIRECTION("f9", "gpsDirection", "gpsInfo"),
    /**
     *GPS卫星数量
     */
    SATELLITE("f10", "satellite", "gpsInfo"),
    /**
     *GPS坐标系
     */
    GPS_COORDINATE("f11", "gpsCoordinate", "gpsInfo"),
    /**
     * 发动机转速
     */
    ENGINE_RPM("f21", "engineRpm", "ecmInfo"),
    /**
     * 加速踏板行程
     */
    RAW_SENSOR("f22", "rawSensor", "ecmInfo"),
    /**
     * 离合踏板
     */
    CLUTCH_SWITCH("f23", "clutchSwitch", "ecmInfo"),
    /**
     * 发动机状态
     */
    ENGINE_STATUS("f24", "engineStatus", "ecmInfo"),
    /**
     * 发动机冷却液温度
     */
    ENGINE_COOLANT_TEMP("f25", "engineCoolantTemp", "ecmInfo"),
    /**
     * 瞬时油耗
     */
    FUEL_CONSUMPTION("f26", "fuelConsumption", "ecmInfo"),
    /**
     * 屏蔽信号
     */
    DIAG_MUX_ON("f27", "diagMuxOn", "ecmInfo"),
    /**
     * 指示灯
     */
    ECONOMY_MODE_LAMP("f28", "economyModeLamp", "ecmInfo"),
    /**
     * 车辆驱动防滑功能
     */
    ASR_IN_REGULATION("f29", "asrInRegulation", "ecmInfo"),
    /**
     * 纵向加速度
     */
    LONGITUDINAL_ACCELERATION_PROC("f30", "longitudinalAccelerationProc", "ecmInfo"),
    /**
     * 横向加速度
     */
    TRANSVERSAL_ACCELERATION("f31", "transversalAcceleration", "ecmInfo"),
    /**
     * 偏航角速度
     */
    YAW_RATE("f32", "yawRate", "ecmInfo"),
    /**
     * 紧急刹车灯
     */
    STOP_LAMP_REQUEST_IDM("f33", "stopLampRequestIdm", "ecmInfo"),
    /**
     * 车速
     */
    VEHICLE_SPEED_CLUSTER("f34", "vehicleSpeedCluster", "meterInfo"),
    /**
     * 平均油耗
     */
    AVERAGE_FUEL_ECONOMY("f35", "averageFuelEconomy", "meterInfo"),
    /**
     * 续航里程
     */
    DISTANCE_TO_EMPTY("f36", "distanceToEmpty", "meterInfo"),
    /**
     * 行使总里程
     */
    DISTANCE_TOTALIZER("f37", "distanceTotalizer", "meterInfo"),
    /**
     * 剩余油量
     */
    FUEL_REMAIN("f38", "fuelRemain", "meterInfo"),
    /**
     * 油量警报
     */
    FUEL_WARNING_LAMP("f39", "fuelWarningLamp", "meterInfo"),
    /**
     * 单次行程平均油耗
     */
    AVERAGE_FUEL_ECONOMY_SINGLE("f40", "averageFuelEconomySingle", "meterInfo"),
    /**
     * 电子手刹状态
     */
    ERB_STATUS("f41", "erbStatus", "meterInfo"),
    /**
     * 后备箱锁状态
     */
    TRUNK_SWITCH_STATUS("f242", "trunkSwitchStatus", "doorInfo"),
    /**
     * BCM 睡眠唤醒指令
     */
    BCM_WAKE_UP_SLEEP_CMD("f43", "bcmWakeUpSleepCmd", "bcmInfo"),
    /**
     * 左前门状态
     */
    FRONT_LEFT_DOOR_STATUS("f44", "frontLeftDoorStatus", "doorInfo"),
    /**
     * 右前门状态
     */
    FRONT_RIGHT_DOOR_STATUS("f45", "frontRightDoorStatus", "doorInfo"),
    /**
     * 左后门状态
     */
    REAR_LEFT_DOOR_STATUS("f46", "rearLeftDoorStatus", "doorInfo"),
    /**
     * 右后门状态
     */
    REAR_RIGHT_DOOR_STATUS("f47", "rearRightDoorStatus", "doorInfo"),
    /**
     * 后门状态
     */
    BACK_DOOR_STATUS("f48", "backDoorStatus", "doorInfo"),
    /**
     * 四门锁状态
     */
    DOOR_LOCK_STATUS("f49", "doorLockStatus", "doorInfo"),
    /**
     * 后门锁状态
     */
    BACK_DOOR_LOCKED_STATUS("f50", "backDoorLockedStatus", "doorInfo"),
    /**
     * 近光灯状态
     */
    LOW_BEAM_REQUEST("f51", "lowBeamRequest", "lightInfo"),
    /**
     * 闪光灯状态
     */
    FLASHING_INDICATOR_STATUS("f52", "flashingIndicatorStatus", "lightInfo"),
    /**
     * 远光灯状态
     */
    HIGH_BEAM_REQUEST("f53", "highBeamRequest", "lightInfo"),
    /**
     * 示宽灯状态
     */
    POSITION_LIGHTS_REQUEST("f54", "positionLightsRequest", "lightInfo"),
    /**
     * 前雾灯状态
     */
    FRONT_FOG_LIGHTS_REQUEST("f55", "frontFogLightsRequest", "lightInfo"),
    /**
     * 后雾灯状态
     */
    REAR_FOG_LIGHT_STATUS("f56", "rearFogLightStatus", "lightInfo"),
    /**
     * 日间行驶灯状态
     */
    DAY_TIME_RUNNING_LIGHT_REQUEST("f57", "dayTimeRunningLightRequest", "lightInfo"),
    /**
     * 右前窗状态
     */
    FRONT_RIGHT_WINDOW_STATUS("f58", "frontRightWindowStatus", "windowInfo"),
    /**
     * 左前窗状态
     */
    FRONT_LEFT_WINDOW_STATUS("f59", "frontLeftWindowStatus", "windowInfo"),
    /**
     * 右后窗状态
     */
    REAR_RIGHT_WINDOW_STATUS("f60", "rearRightWindowStatus", "windowInfo"),
    /**
     * 左后窗状态
     */
    REAR_LEFT_WINDOW_STATUS("f61", "rearLeftWindowStatus", "windowInfo"),
    /**
     * 远程启动发动机标志位
     */
    REMOTE_ENGING_RUN_FLAG("f62", "remoteEngingRunFlag", "bcmInfo"),
    /**
     * 天窗玻璃状态
     */
    SUNROOF_WINDOW_STATUS("f63", "sunroofWindowStatus", "windowInfo"),
    /**
     * 天窗遮阳板状态
     */
    SUNROOF_SHADE_STATUS("f64", "sunroofShadeStatus", "windowInfo"),
    /**
     * 远程寻车是否被打断
     */
    IS_FINDCAR_BREAKED("f65", "isFindcarBreaked", "bcmInfo"),
    /**
     * 远程启动状态反馈
     */
    RES_STATUS("f66", "resStatus", "bcmInfo"),
    /**
     * 左转向灯
     */
    LEFT_TURN_LIGHT("f67", "leftTurnLight", "lightInfo"),
    /**
     * 右转向灯
     */
    RIGHT_TURN_LIGHT("f68", "rightTurnLight", "lightInfo"),
    /**
     * R 档信号（硬线信号）
     */
    REVERSE_GEAR("f69", "reverseGear", "hardwareSignalInfo"),
    /**
     * N 档信号（硬线信号）
     */
    NEUTRAL_GEAR("f70", "neutralGear", "hardwareSignalInfo"),
    /**
     * 手刹状态
     */
    HAND_BRAKE_SWITCH("f71", "handBrakeSwitch", "hardwareSignalInfo"),
    /**
     * ACC状态
     */
    ACC_STATUS("f72", "accStatus", "hardwareSignalInfo"),
    /**
     * IGN 点火开关状态
     */
    IGN_STATUS("f73", "ignStatus", "hardwareSignalInfo"),
    /**
     * 安全指示灯（硬线信号）
     */
    SECURITY_INDICATOR("f74", "securityIndicator", "hardwareSignalInfo"),
    /**
     * 蓄电池电压
     */
    BATTERY_STATUS("f75", "batteryStatus", "hardwareSignalInfo"),
    /**
     * DA 心跳状态
     */
    DA_HEART_BEAT("f76", "daHeartBeat", "hardwareSignalInfo"),
    /**
     * 转向角
     */
    STEERING_ANGLE("f77", "steeringAngle", "drivingCaseInfo"),
    /**
     * elocity 转向角速度
     */
    STEERING_ANGLE_VELOCITY("f78", "steeringAngleVelocity", "drivingCaseInfo"),
    /**
     * 急加速
     */
    RAPID_ACCELERATION("f79", "rapidAcceleration", "drivingCaseInfo"),
    /**
     * 急减速
     */
    RAPID_DECELERATION("f80", "rapidDeceleration", "drivingCaseInfo"),
    /**
     * 急转弯
     */
    SHARP_TURN("f81", "sharpTurn", "drivingCaseInfo"),
    /**
     * 频繁换道
     */
    FREQUENTLY_CHANGE_TRACK("f82", "frequentlyChangeTrack", "drivingCaseInfo"),
    /**
     * 主驾安全带状态
     */
    DRIVER_SEAT_BELT_STATUS("f83", "driverSeatBeltStatus", "drivingCaseInfo"),

    /**
     * 副驾安全带状态
     */
    PASSENGER_SEAT_BELT_STATUS("f84", "passengerSeatBeltStatus", "drivingCaseInfo"),
    /**
     * 转向未打转向灯次数
     */
    TURN_NO_OPEN_LIGHT_NUM("f85", "turnNoOpenLightNum", "drivingCaseInfo"),
    /**
     * 转向角
     */
    STEE_RANGLE("f86", "steerAngle", "drivingCaseInfo"),
    /**
     * 空调模式状态
     */
    HAVC_MODE("f87", "havcMode", "havcInfo"),
    /**
     * 空调循环状态
     */
    HAVC_CIRCULATION("f88", "havcCirculation", "havcInfo"),
    /**
     * 空调温度状态
     */
    HAVC_TEMPERATURE("f89", "havcTemperature", "havcInfo"),
    /**
     * 空调风量状态
     */
    HAVC_AIR_VOLUME("f90", "havcAirVolume", "havcInfo"),
    /**
     * DVR 工作状态
     */
    DVR_WORK_STATUS("f91", "dvrWorkStatus", "commonInfo"),
    /**
     * 主驾座椅通风状态
     */
    SHVM_DRV_SEAT_VENT("f92", "shvmDrvSeatVent", "commonInfo"),
    /**
     * 副驾座椅通风状态
     */
    SHVM_PAS_SEAT_VENT("f93", "shvmPasSeatVent", "commonInfo"),
    /**
     * 车外温度
     */
    EXTERNAL_TEMP("f94", "externalTemp", "temperatureInfo"),
    /**
     * 主驾驶温度
     */
    AIR_CONDITION_STATUS_DRIVER_TEMP("f95", "airConditionStatusDriverTemp", "temperatureInfo"),
    /**
     * 副驾驶温度
     */
    AIR_CONDITION_STATUS_PASSENGER_TEMP("f96", "airConditionStatusPassengerTemp", "temperatureInfo"),
    /**
     * 风量
     */
    AIR_CONDITION_STATUS_FAN_VOLUME("f97", "airConditionStatusFanVolume", "airInfo"),
    /**
     * 压缩机开关状态
     */
    AIR_CONDITION_STATUS_AC_IND("f98", "airConditionStatusAcInd", "airInfo"),
    /**
     * 循环模式
     */
    AIR_CONDITION_STATUS_CIRCULATE_IND("f99", "airConditionStatusCirculateInd", "airInfo"),
    /**
     * 空调开关状态
     */
    AIR_CONDITION_STATUS_OFF_ON("f100", "airConditionStatusOffOn", "airInfo"),
    /**
     * 自动模式开关
     */
    AIR_CONDITION_STATUS_AUTO_IND("f101", "airConditionStatusAutoInd", "airInfo"),
    /**
     * 分区模式开关
     */
    AIR_CONDITION_STATUS_DUAL_IND("f102", "airConditionStatusDualInd", "airInfo"),
    /**
     * 同步模式开关
     */
    AIR_CONDITION_STATUS_SYNC_IND("f103", "airConditionStatusSyncInd", "airInfo"),
    /**
     * 前除霜开关
     */
    AIR_CONDITION_STATUS_FRONT_DEFROST_IND("f104", "airConditionStatusFrontDefrostInd", "airInfo"),
    /**
     * 后除霜开关
     */
    AIR_CONDITION_STATUS_REAR_DEFROST_IND("f105", "airConditionStatusRearDefrostInd", "airInfo"),
    /**
     * 空调模式
     */
    AIR_CONDITION_STATUS_MODE("f106", "airConditionStatusMode", "airInfo"),
    /**
     * 方向盘加热开关
     */
    STEERING_HEAT_STATUS_SW("f107", "steeringHeatStatusSw", "heatInfo"),
    /**
     * 主驾驶座椅加热开关
     */
    SEAT_HEAT_STATUS_DRIVER("f108", "seatHeatStatusDriver", "heatInfo"),
    /**
     * 副驾驶座椅加热开关
     */
    SEAT_HEAT_STATUS_PASSENAGER("f109", "seatHeatStatusPassenager", "heatInfo"),
    /**
     * SOC 剩余电量
     */
    ELECTRICITY_REMAIN("f110", "electricityRemain", "chargingInfo"),
    /**
     * 剩余能量
     */
    REMAIN_ENERGY("f111", "remainEnergy", "extendInfo"),
    /**
     *挡位
     */
    GEAR("f112", "gear", "extendInfo"),

    /**
     * 充电电流
     */
    CHARGING_A("f113", "chargingA", "chargingInfo"),
    /**
     * 充电电压
     */
    CHARGING_U("f114", "chargingU", "chargingInfo"),
    /**
     * 充电状态
     */
    CHARGING_STATE("f115", "chargingState", "chargingInfo"),
    /**
     * 车内温度
     */
    IN_VEH_TEMP("f116", "inVehTemp", "temperatureInfo"),
    /**
     * 剩余时间，SOC未达80%时，充电至80%所需时间;格式：hour:minute
     */
    CHARGING_TIME_FOR_EIGHTY("f117", "chargingTimeForEighty", "chargingInfo"),
    /**
     * SOC>=80%，充电至100%所需时间；格式：hour:minute
     */
    CHARGING_TIME_FOR_FULL("f118", "chargingTimeForFull", "chargingInfo"),
    /**
     * 整车锁定后无法再使用物理钥匙、数字钥匙等方式启动车辆
     */
    CAR_LOCK("f119", "carLock", "extendInfo"),
    /**
     * 开闭锁状态
     */
    LOCK_STATE("f120", "lockState", "gbInfo"),
    /**
     * 车辆状态【电车也需要上报，不从国标解析】
     */
    VEH_STATE("f121", "vehState", "gbInfo"),
    /**
     * 运行模式
     */
    RUNNING_MODE("f122", "runningMode", "gbInfo"),
    /**
     * 总电压
     */
    TOTAL_VOLTAGE("f123", "totalVoltage", "gbInfo"),
    /**
     * 总电流
     */
    TOTAL_ELECTRICITY("f124", "totalElectricity", "gbInfo"),
    /**
     * DC/DC状态
     */
    DC_STATE("f125", "dcState", "gbInfo"),
    /**
     * 绝缘电阻
     */
    INSULATION_RESISTANCE("f126", "insulationResistance", "gbInfo"),
    /**
     * 曲轴转速
     */
    CRANKSHAFT_SPEED("f127", "crankshaftSpeed", "gbInfo"),
    /**
     * 发动机燃料消耗率
     */
    ENGINE_FUEL_CONSUMPTION_RATE("f128", "engineFuelConsumptionRate", "gbInfo"),
    /**
     * 碰撞
     */
    COLLISION("f129", "collision", "gbInfo"),
    /**
     * 制动踏板状态
     */
    BRAKE_PEDAL_STATE("f130", "brakePedalState", "gbInfo"),
    /**
     * 燃料电池电压
     */
    FUEL_CELL_VOLTAGE("f131", "fuelCellVoltage", "gbInfo"),
    /**
     * 燃料电池电流
     */
    FUEL_CELL_ELECTRICITY("f132", "fuelCellElectricity", "gbInfo"),
    /**
     * 氢系统中最高温度
     */
    HYDROGEN_SYS_MAX_TEMP("f133", "hydrogenSysMaxTemp", "gbInfo"),
    /**
     * 氢系统中最高温度探针代号
     */
    HYDROGEN_SYS_MAX_TEMP_NO("f134", "hydrogenSysMaxTempNo", "gbInfo"),
    /**
     * 氢气最高浓度
     */
    HYDROGEN_SYS_MAX_THICKNESS("f135", "hydrogenSysMaxThickness", "gbInfo"),
    /**
     * 氢气最高浓度传感器代号
     */
    HYDROGEN_SYS_MAX_THICKNESS_NO("f136", "hydrogenSysMaxThicknessNo", "gbInfo"),
    /**
     * 氢气最高压力
     */
    HYDROGEN_MAX_PRESSURE("f137", "hydrogenMaxPressure", "gbInfo"),
    /**
     * 氢气最高压力 传感器代号
     */
    HYDROGEN_MAX_PRESSURE_NO("f138", "hydrogenMaxPressureNo", "gbInfo"),
    /**
     * 高压DC/DC状态
     */
    HIGH_PRESSURE_DC_STATE("f139", "highPressureDcState", "gbInfo"),
    /**
     * 最高电压电池子系统号
     */
    MAX_VOLTAGE_CELL_SUBSYSTEM_NO("f140", "maxVoltageCellSubsystemNo", "gbInfo"),
    /**
     * 最高电压电池单体代号
     */
    MAX_VOLTAGE_CELL_MONOMER_NO("f141", "maxVoltageCellMonomerNo", "gbInfo"),
    /**
     * 电池单体电压最高值
     */
    CELL_MONOMER_VOLTAGE_MAX_VALUE("f142", "cellMonomerVoltageMaxValue", "gbInfo"),
    /**
     * 最低电压电池子系统号
     */
    MIN_VOLTAGE_CELL_SUBSYSTEM_NO("f143", "minVoltageCellSubsystemNo", "gbInfo"),
    /**
     * 最低电压电池单体代号
     */
    MIN_VOLTAGE_CELL_MONOMER_NO("f144", "minVoltageCellMonomerNo", "gbInfo"),
    /**
     * 电池单体电压最低值
     */
    CELL_MONOMER_VOLTAGE_MIN_VALUE("f145", "cellMonomerVoltageMinValue", "gbInfo"),
    /**
     *最高温度子系统号
     */
    MAX_TEMP_SUBSYSTEM_NO("f146", "maxTempSubsystemNo", "gbInfo"),
    /**
     * 最高温度探针序号
     */
    MAX_TEMP_PROBE_NO("f147", "maxTempProbeNo", "gbInfo"),
    /**
     * 最高温度值
     */
    MAX_TEMP_VALUE("f148", "maxTempValue", "gbInfo"),
    /**
     * 最低温度子系统号
     */
    MIN_TEMP_SUBSYSTEM_NO("f149", "minTempSubsystemNo", "gbInfo"),
    /**
     * 最低温度探针序号
     */
    MIN_TEMP_PROBE_NO("f150", "minTempProbeNo", "gbInfo"),
    /**
     * 最低温度值
     */
    MIN_TEMP_VALUE("f151", "minTempValue", "gbInfo"),
    /**
     * 最高报警等级
     */
    MAX_ALARM_LEVEL("f152", "maxAlarmLevel", "gbInfo"),
    /**
     * 温度差异报警
     */
    TEMP_DIFF_ALARM("f153", "tempDiffAlarm", "gbInfo"),
    /**
     * 电池高温报警
     */
    CELL_HIGH_tEMP_ALARM("f154", "cellHighTempAlarm", "gbInfo"),
    /**
     * 车载储能装置类型过压报警
     */
    VEH_ENERGY_DEVICE_TYPE_OVER_VOLTAGE_ALARM("f155", "vehEnergyDeviceTypeOverVoltageAlarm", "gbInfo"),
    /**
     * 车载储能装置类型欠压报警
     */
    VEH_ENERGY_DEVICE_TYPE_UNDER_VOLTAGE_ALARM("f156", "vehEnergyDeviceTypeUnderVoltageAlarm", "gbInfo"),
    /**
     *SOC低报警
     */
    LOW_SOC_ALARM("f157", "lowSocAlarm", "gbInfo"),
    /**
     * 单体电池过压报警
     */
    MONOMER_CELL_OVER_VOLTAGE_ALARM("f158", "monomerCellOverVoltageAlarm", "gbInfo"),
    /**
     * 单体电池欠压报警
     */
    MONOMER_CELL_UNDER_VOLTAGE_ALARM("f159", "monomerCellUnderVoltageAlarm", "gbInfo"),
    /**
     * soc过高报警
     */
    HIGH_SOC_ALARM("f160", "highSocAlarm", "gbInfo"),
    /**
     * soc跳变报警
     */
    JUMPING_SOC_ALARM("f161", "jumpingSocAlarm", "gbInfo"),
    /**
     * 可充电储能系统不匹配报警
     */
    ENERGY_STORAGE_SYS_MISMATCH_ALARM("f162", "energyStorageSysMismatchAlarm", "gbInfo"),
    /**
     * 电池单体一致性差报警
     */
    MONOMER_CELL_POOR_CONSISTENCY_ALARM("f163", "monomerCellPoorConsistencyAlarm", "gbInfo"),
    /**
     * 绝缘报警
     */
    INSULATION_ALARM("f164", "insulationAlarm", "gbInfo"),
    /**
     * DC-DC温度报警
     */
    DC_TEMP_ALARM("f165", "dcTempAlarm", "gbInfo"),
    /**
     * 制动系统报警
     */
    BRAKING_SYSTEM_ALARM("f166", "brakingSystemAlarm", "gbInfo"),
    /**
     * DC-DC状态报警
     */
    DC_STATE_ALARM("f167", "dcStateAlarm", "gbInfo"),
    /**
     * 驱动电机控制器温度报警
     */
    DRIVE_MOTOR_CONtROLLER_TEMP_ALARM("f168", "driveMotorControllerTempAlarm", "gbInfo"),
    /**
     * 高压互锁状态报警
     */
    HIGH_VOLTAGE_INTERLOCK_STATE_ALARM("f169", "highVoltageInterlockStateAlarm", "gbInfo"),
    /**
     * 驱动电机温度报警
     */
    DRIVER_MOTOR_TEMP_ALARM("f170", "driveMotorTempAlarm", "gbInfo"),
    /**
     * 车载储能装置类型过充
     */
    VEH_ENERGY_DEVICE_TYPE_OVERCHARGED("f171", "vehEnergyDeviceTypeOvercharged", "gbInfo"),

    /**
     * 燃料电池燃料消耗率
     */
    FUEL_CONSUMPTION_RATE("f185", "fuelConsumptionRate", "gbInfo"),
    /**
     * ReadyInd指示
     */
    READY_IND_INSTRUCTIONS("f186", "readyIndInstructions", "gbInfo"),
    /**
     * 非法移车标志
     */
    ILLEGAL_MOVING_VEH_FLAG("f187", "illegalMovingVehFlag", "gbInfo"),
    /**
     * 充电口盖状态(预留)
     */
    CHARGING_DOOR_STATE("f188", "chargingDoorState", "chargingInfo"),
    /**
     * 充电枪锁止状态
     */
    CHARGING_GUN_LOCK_STATE("f189", "chargingGunLockState", "chargingInfo"),
    /**
     * BMSWorkStas
     */
    BMS_WORK_STATE("f190", "bmsWorkState", "bmsInfo"),
    /**
     * TBox自检参数1
     */
    T_BOX_CHECK_PARAM_ONE("f191", "tBoxCheckParamOne", "gbInfo"),
    /**
     * TBox自检参数1
     */
    T_BOX_CHECK_PARAM_TWO("f192", "tBoxCheckParamTwo", "gbInfo"),
    /**
     * 司机账号ID
     */
    VEH_ACCOUNT_ID("f201", "vehAccountId", "extendInfo"),
    /**
     * 剩余充电时间
     */
    CHARGING_TIME_FOR_REMAINDER("f230", "chargingTimeForRemainder", "chargingInfo"),
    /**
     * 充充电枪连接状态
     */
    CHARGING_GUN_CONNECT_STATE("f238", "chargingGunConnectState", "chargingInfo"),
    /**
     * 充电状态
     */
    BMS_CHARGING_STATE("f239", "bmsChargingState", "chargingInfo"),
    /**
     * soc值
     */
    SOC_LIMIT_VALUE("f240", "socLimitValue", "chargingInfo"),
    /**
     * ota升级标识
     */
    OTA_UPGRADE_FLAG("-f9991", "otaUpgradeFlag", "extendInfo"),
    /**
     * 空调工作时长
     */
    AIR_CONDITIONER_WORK_TIME("-f9997", "airConditionerWorkTime", "airInfo"),
    /**
     * 车窗状态
     */
    WINDOE_STATUS("-f9998", "windowStatus", "windowInfo"),
    /**
     * 接收时间戳
     */
    TIMESTAMP("-f9999", "timestamp", "extendInfo"),

    ;


    /**
     * 编码
     */
    private String code;

    /**
     * 字段名称
     */
    private String columnName;
    /**
     * 字段名称
     */
    private String groupName;

    VehStatusEnum(String code, String columnName, String groupName) {
        this.code = code;
        this.columnName = columnName;
        this.groupName = groupName;
    }

    public String getCode() {
        return this.code;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public String getDescription() {
        return this.columnName;
    }

    @Override
    public String getValue() {
        return this.code;
    }
}
