package com.bnmotor.icv.tsp.ota.model.req;

import lombok.Data;

@Data
public class UpgradePkgReq{
    private int code;
    private int type;
    private Long packageId;
    private String filePath;
    private String fileHash;
    private String fileName;
    private Long fileSize;
    private String comment;
    private String fileKey;
}
