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
public class QueryResultInfo {
    private int code;
    private int type;
    private String filePath;
    private String fileHash;
    private String fileName;
    private Long fileSize;
    private String comment;
    private String fileKey;

    public QueryResultInfo() {
    }
}
