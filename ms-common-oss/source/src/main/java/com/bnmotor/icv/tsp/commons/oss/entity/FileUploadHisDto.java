package com.bnmotor.icv.tsp.commons.oss.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * @author :luoyang
 * @date_time :2020/9/29 9:09
 * @desc: 列表搜索条件输入
 */

@ApiModel(value = "FileUploadHisDto", description = "列表条件搜索条件")
public class FileUploadHisDto {


    private final static String timeFomatter = "yyyy-MM-dd";


    /*
     *用户id
     */
    @Setter
    @Getter
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /*
     *文件所属桶
     */
    @Setter
    @Getter
    @ApiModelProperty(value = "文件所属桶")
    private String bucket;

    /*
     *文件所属组
     */
    @Setter
    @Getter
    @ApiModelProperty(value = "文件所属组", name = "fileGroup")
    private String fileGroup;

    /*
     * 上传时间字符串区间开始时间 （YYYY-MM-dd）
     */
    @Getter
    @ApiModelProperty(value = "上传时间区间开始时间", name = "startTimeStr")
    private String startTimeStr;

    /*
     * 上传时间字符串区间结束时间 （YYYY-MM-dd）
     */
    @Getter
    @ApiModelProperty(value = "上传时间区间结束时间", name = "endTimeStr")
    private String endTimeStr;

    /*
     * 上传时间区间开始时间
     */
    @Getter
    private LocalDateTime uploadTimeStart;

    /*
     * 上传时间区间结束时间
     */
    @Getter
    private LocalDateTime uploadTimeEnd;

    public void setStartTimeStr(String startTimeStr) {
        if (StringUtils.isEmpty(startTimeStr)) return;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFomatter);
        LocalDate parse = LocalDate.parse(startTimeStr, formatter);
        this.uploadTimeStart = parse.atStartOfDay();
        this.startTimeStr = startTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        if (StringUtils.isEmpty(endTimeStr)) return;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFomatter);
        LocalDate parse = LocalDate.parse(endTimeStr, formatter);
        this.uploadTimeEnd = parse.atTime(23,59,59);
        this.endTimeStr = endTimeStr;
    }

}
