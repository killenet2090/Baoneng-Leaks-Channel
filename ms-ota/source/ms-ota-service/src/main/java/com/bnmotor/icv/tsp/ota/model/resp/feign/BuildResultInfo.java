package com.bnmotor.icv.tsp.ota.model.resp.feign;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author fmq
 * @date 2020.08.25
 */
@Getter
@Setter
@ToString
public class BuildResultInfo {
    private int code;
    private String taskId;
    private String comment;
}
