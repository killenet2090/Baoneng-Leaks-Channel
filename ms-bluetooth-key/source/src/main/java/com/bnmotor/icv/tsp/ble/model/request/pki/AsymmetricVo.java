package com.bnmotor.icv.tsp.ble.model.request.pki;

import lombok.Builder;
import lombok.Data;

@Data
public class AsymmetricVo {
    private String cipherText;
    private byte[] cipherTextBytes;
}
