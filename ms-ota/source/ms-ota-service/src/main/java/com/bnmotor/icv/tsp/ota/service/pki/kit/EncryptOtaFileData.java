package com.bnmotor.icv.tsp.ota.service.pki.kit;

import lombok.Data;

/**
 * @ClassName: EncryptOtaFileData.java EncryptOtaFileData
 * @Description: 加密过程参数
 * @author E.YanLonG
 * @since 2020-11-16 9:30:56
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class EncryptOtaFileData {

//	private String msg;
//
//	private Integer state;
//
//	private String encryptOtaFileDataPath;
//
//	private byte[] versionData;
//
//	private byte[] signData;
//
//	// 名称后的base64字符串
//	private String signDataStr;
//
//	// 加密密码
//	private String keyScrect;
//
//	private Long encryptOtaFileDataLength;
//
//	public static final Integer MAX_OTA_FILE_SIZE = Integer.valueOf(524288000);
//
//	public static final Integer STATUS_SUCCESS = Integer.valueOf(0);
//
//	public static final Integer STATUS_FILE_ENCRYPTED = Integer.valueOf(1);
//
//	public static final Integer STATUS_HASH_ERROR = Integer.valueOf(2);
//
//	public static final Integer STATUS_OPEN_OTAFILE_ERROR = Integer.valueOf(3);
//
//	public static final Integer STATUS_FILE_SIGNED_ERROR = Integer.valueOf(4);
//
//	public static final Integer ENCRYPT_FILE_FAILED = Integer.valueOf(5);
//
//	public static final Integer GENERATE_KEY_FAILED = Integer.valueOf(6);
	
	 private String msg;
	  
	  private Integer state;
	  
	  private String encryptOtaFileDataPath;
	  
	  private byte[] versionData;
	  
	  private byte[] signData;
	  
	  private long encryptOtaFileDataLength;
	  
	  public static final Integer MAX_OTA_FILE_SIZE = Integer.valueOf(524288000);
	  
	  public static final Integer STATUS_SUCCESS = Integer.valueOf(0);
	  
	  public static final Integer STATUS_FILE_ENCRYPTED = Integer.valueOf(1);
	  
	  public static final Integer STATUS_HASH_ERROR = Integer.valueOf(2);
	  
	  public static final Integer STATUS_OPEN_OTAFILE_ERROR = Integer.valueOf(3);
	  
	  public static final Integer STATUS_FILE_SIGNED_ERROR = Integer.valueOf(4);
	  
	  public static final Integer ENCRYPT_FILE_FAILED = Integer.valueOf(5);
	  
	  public static final Integer GENERATE_KEY_FAILED = Integer.valueOf(6);

}