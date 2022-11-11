package com.bnmotor.icv.tsp.ota.service.pki.kit;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;

@Slf4j
public class PackageUpgradeEncryptKit {

	static final String BC = "BC";
//	static final String TRANSFORMATION = "AES/ECB/PKCS7Padding";
	public static final String TRANSFORMATION = "AES/ECB/NoPadding";
	public static final String ENCRYPT_ALGORITHM = "AES";
	public static final Integer FILE_BLOCK_SIZE = Integer.valueOf(16);
	public static final int BUFFER_SIZE = 1048576;
	public static final String SIGN_ALG = "SHA-256";
	public static final String SHA256WithRSA = "SHA256WithRSA";
	
	static {
		if (Security.getProvider(BC) == null) {
			Security.addProvider((Provider) new BouncyCastleProvider());
		}
	}
	
	/**
	 * 计算文件hash
	 * @param otaFileInputPath
	 * @return
	 */
	@Deprecated
	public static String signFileHash0(String otaFileInputPath) {
		byte[] hash = getMessageDigest(otaFileInputPath);
		String base64hash = Base64.encodeBase64String(hash);
		return base64hash;
	}
	
	public static byte[] signFileHash(String otaFileInputPath) {
		byte[] hash = getMessageDigest(otaFileInputPath);
		return hash;
	}
	
	public static EncryptOtaFileData encryptOtaFile(String otaFileInputPath, String otaFileVersion, String workpath, String signData) {
	    EncryptOtaFileData result = new EncryptOtaFileData();
	    try {
	    	if (isEncrypted(otaFileInputPath)) {
	    	log.info("ota文件已经做过文件加密处理|{}", otaFileInputPath);
			result.setState(EncryptOtaFileData.STATUS_FILE_SIGNED_ERROR);
			result.setMsg("ota文件已经做过文件加密处理");
	        return result;
	    	}
	     
	      File otaFile = new File(otaFileInputPath);
	      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(otaFile));
	      String temp = workpath + otaFile.getName() + ".data";
	      String url = workpath.endsWith("/") ? temp : ("/" + temp);
	      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(url)));
	      int len = 0;
	      byte[] buffer = new byte[1048576];
	      byte[] key = createSecurityKey(otaFileVersion);
	      Cipher cipher = Cipher.getInstance(TRANSFORMATION, BC);
	      cipher.init(1, new SecretKeySpec(key, ENCRYPT_ALGORITHM));
	      long total = otaFile.length();
	      bis.close();
	      int groupCount = (int)(total / buffer.length);
	      BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(otaFile));
	      int encryptDataLen = 0;
	      int processedGroupCount = 0;
	      if (total < buffer.length) {
	        while ((len = inputStream.read(buffer)) != -1) {
	          byte[] remain = new byte[len];
	          System.arraycopy(buffer, 0, remain, 0, len);
	          byte[] finalBytes = ByteUtils.pkcs7Padding(remain, FILE_BLOCK_SIZE.intValue());
	          encryptDataLen += finalBytes.length;
	          bos.write(cipher.doFinal(finalBytes));
	          bos.flush();
	        } 
	      } else {
	        while ((len = inputStream.read(buffer)) != -1) {
	          if (processedGroupCount >= groupCount || total - ((processedGroupCount + 1) * 1048576) == 0L) {
	            byte[] remain = new byte[len];
	            System.arraycopy(buffer, 0, remain, 0, len);
	            byte[] finalBytes = ByteUtils.pkcs7Padding(remain, FILE_BLOCK_SIZE.intValue());
	            encryptDataLen += finalBytes.length;
	            bos.write(cipher.doFinal(finalBytes));
	          } else {
	            encryptDataLen += len;
	            bos.write(cipher.update(buffer, 0, len));
	          } 
	          processedGroupCount++;
	          bos.flush();
	        } 
	        bos.close();
	      } 
	      result.setEncryptOtaFileDataPath(url);
	      result.setEncryptOtaFileDataLength(encryptDataLen);
	      result.setSignData(Base64.decodeBase64(signData));
	      result.setVersionData(otaFileVersion.getBytes());
	      result.setState(EncryptOtaFileData.STATUS_SUCCESS);
	      result.setMsg("SUCCESS");
	    } catch (Exception e) {
	    	result.setState(EncryptOtaFileData.ENCRYPT_FILE_FAILED);
			result.setMsg("加密ota 文件失败" + e.getMessage());
			log.error("加密ota文件失败|{}", e.getMessage(), e);
	    } 
	    return result;
	}

	public static EncryptOtaFileData encryptOtaFile0(String otaFileInputPath, String otaFileVersion, String workpath, String signData) {
		EncryptOtaFileData result = new EncryptOtaFileData();
		try {
			
			if (isEncrypted(otaFileInputPath)) {
				log.info("ota文件已经做过文件加密处理|{}", otaFileInputPath);
				result.setState(EncryptOtaFileData.STATUS_FILE_SIGNED_ERROR);
				result.setMsg("ota文件已经做过文件加密处理");
				return result;
			}

			File otaFile = new File(otaFileInputPath);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(otaFile));
			String temp = workpath + otaFile.getName() + ".data";
			String url = workpath.endsWith("/") ? temp : ("/" + temp);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(url)));
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			byte[] key = createSecurityKey(otaFileVersion);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION, BC);
			cipher.init(1, new SecretKeySpec(key, ENCRYPT_ALGORITHM));
			CipherInputStream cis = new CipherInputStream(bis, cipher);
			int total = 0;
			while ((len = cis.read(buffer)) != -1) {
				total += len;
				bos.write(buffer, 0, len);
				bos.flush();
			}
			bos.flush();
			bos.close();
			cis.close();
			result.setEncryptOtaFileDataPath(url);
			result.setEncryptOtaFileDataLength(total);
			result.setSignData(Base64.decodeBase64(signData));
			
			result.setVersionData(otaFileVersion.getBytes());
			result.setState(EncryptOtaFileData.STATUS_SUCCESS);
			result.setMsg("SUCCESS");
		} catch (Exception e) {
			result.setState(EncryptOtaFileData.ENCRYPT_FILE_FAILED);
			result.setMsg("加密ota 文件失败" + e.getMessage());
			log.error("加密ota文件失败|{}", e.getMessage(), e);
		}
		return result;
	}

	private static byte[] getMessageDigest(String otaFileInputPath) {
	    byte[] result = null;
	    try {
	      MessageDigest messageDigest = MessageDigest.getInstance(SIGN_ALG, BC);
	      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(otaFileInputPath)));
	      int len = 0;
	      byte[] buffer = new byte[BUFFER_SIZE];
	      while ((len = bis.read(buffer)) != -1)
	        messageDigest.update(buffer, 0, len); 
	      result = messageDigest.digest();
	      bis.close();
	    } catch (Exception e) {
	      log.error("", e);
	    } 
	    return result;
	  }
	  
	  private static byte[] getMessageDigest(byte[] content) {
	    byte[] result = null;
	    try {
	      MessageDigest messageDigest = MessageDigest.getInstance(SIGN_ALG, BC);
	      messageDigest.update(content);
	      result = messageDigest.digest();
	    } catch (Exception e) {
	      log.error("", e);
	    } 
	    return result;
	  }

	  private static byte[] createSecurityKey(String otaFileVersion) {
		    char[] chars = {'b', 'a', 'o', 'n', 'e', 'n', 'g', 'm', 'o', 't', 'o', 'r' };
		    String keySource = otaFileVersion + String.valueOf(chars);
		    byte[] keyBytes = keySource.getBytes();
		    byte[] hash = getMessageDigest(keyBytes);
		    if (hash == null)
		      return null; 
		    byte[] before16Hash = new byte[16];
		    System.arraycopy(hash, 0, before16Hash, 0, 16);
		    return before16Hash;
		  }

	  public static boolean isEncrypted(String otaFileInputPath) {
		    boolean result = false;
		    try {
		      File f = new File(otaFileInputPath);
		      RandomAccessFile fis = new RandomAccessFile(f, "r");
		      if (f.length() > 0L) {
		        byte[] F = new byte[1];
		        int len = fis.read(F, 0, 1);
		        if (F[0] == 70) {
		          byte[] fLenBytes = new byte[4];
		          fis.seek(1L);
		          fis.read(fLenBytes);
		          int fLen = ByteUtils.bytes2int(fLenBytes);
		          byte[] V = new byte[1];
		          fis.seek((5 + fLen));
		          fis.read(V);
		          if (V[0] == 86) {
		            byte[] S = new byte[1];
		            byte[] vLenBytes = new byte[1];
		            fis.seek((5 + fLen + 1));
		            fis.read(vLenBytes);
		            int vLen = vLenBytes[0];
		            fis.seek((5 + fLen + 2 + vLen));
		            fis.read(S);
		            if (S[0] == 83)
		              result = true; 
		          } 
		        } 
		      } 
		    } catch (Exception e) {
		      log.info("verify file success,file is not encrypted file");
		    } 
		    return result;
		  }
	
	public static void makeEncryptFile(String encryptedFilePath, long encryptedFileLength, byte[] versionData, byte[] signedData, String outputFilePath) throws Exception {
	    byte[] versionDataStruct = new byte[2 + versionData.length];
	    System.arraycopy(new byte[] { 86 }, 0, versionDataStruct, 0, 1);
	    System.arraycopy(new byte[] { ByteUtils.int2OneByte(versionData.length) }, 0, versionDataStruct, 1, 1);
	    System.arraycopy(versionData, 0, versionDataStruct, 2, versionData.length);
	    byte[] signedDataStruct = new byte[3 + signedData.length];
	    System.arraycopy(new byte[] { 83 }, 0, signedDataStruct, 0, 1);
	    System.arraycopy(ByteUtils.int2Twobytes(signedData.length), 0, signedDataStruct, 1, 2);
	    System.arraycopy(signedData, 0, signedDataStruct, 3, signedData.length);
	    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(outputFilePath)));
	    bos.write(new byte[] { 70 });
	    bos.write(ByteUtils.long2Bytes(encryptedFileLength));
	    File f = new File(encryptedFilePath);
	    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
	    byte[] buffer = new byte[1048576];
	    int len = 0;
	    while ((len = bis.read(buffer)) != -1) {
	      bos.write(buffer, 0, len);
	      bos.flush();
	    } 
	    bos.write(versionDataStruct);
	    bos.write(signedDataStruct);
	    bos.flush();
	    bos.close();
	    bis.close();
	    f.delete();
	  }
}
