package com.jfcf.utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Hex;

/**
 * @author ducongcong
 * @date 2016年5月23日
 */
public class RSACoder {

	//非对称加密秘钥算法
	public static final String KEY_ALGORITHM = "RSA";
	/**
	 * RSA秘钥长度默认1024位，密钥长度必须是64的倍数，范围在512-65536位之间
	 */
	private static final int KEY_SIZE = 512;
	/**
	 * 私钥解密
	 * @updateDate 
	 * @param data 待解密数据
	 * @param key 私钥
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data,byte[] key) throws Exception{
		return privateKey(Cipher.DECRYPT_MODE,data, key);
	}
	/**
	 * 公钥解密
	 * @updateDate 
	 * @param data 待解密数据
	 * @param key 公钥
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data,byte[] key) throws Exception{
		return publicKey(Cipher.DECRYPT_MODE, data, key);
	}
	/**
	 * 公钥加密
	 * @param data 待加密数据
	 * @param key 公钥
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data,byte[] key) throws Exception{
		return publicKey(Cipher.ENCRYPT_MODE, data, key);
	}
	/**
	 * 私钥加密
	 * @param data 待加密数据
	 * @param key 私钥
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data,byte[] key) throws Exception{
		return privateKey(Cipher.ENCRYPT_MODE,data, key);
	}
	/**
	 * 公钥加解密数据
	 * @param mode 模式 Cipher.DECRYPT_MODE 解密  Cipher.ENCRYPT_MODE 加密
	 * @param data 数据
	 * @param key 公钥
	 * @return
	 * @throws Exception
	 */
	private static byte[] publicKey(int mode,byte[] data,byte[] key) throws Exception{
		//取得公钥
		KeySpec keySpec = new X509EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		//生成公钥
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		//对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(mode, publicKey);
		return cipher.doFinal(data);
	}
	/**
	 * 私钥加密解密方法
	 * @param mode 模式 Cipher.DECRYPT_MODE 解密  Cipher.ENCRYPT_MODE 加密
	 * @param data 原始数据
	 * @param key 私钥
	 * @return
	 * @throws Exception
	 */
	private static byte[] privateKey(int mode,byte[] data,byte[] key) throws Exception{
		//获取私钥
		KeySpec  keySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		//生成私钥
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		//对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(mode, privateKey);
		return cipher.doFinal(data);
	}
	 @SuppressWarnings("unused")
	private static byte[] toBytes(String str) {
	        if(str == null) {
	            throw new IllegalArgumentException("binary string is null");
	        }
	        char[] chs = str.toCharArray();
	        byte[] bys = new byte[chs.length / 2];
	        int offset = 0;
	        int k = 0;
	        while(k < chs.length) {
	            bys[offset++] = (byte)((toInt(chs[k++]) << 4) | toInt(chs[k++]));
	        }
	        return bys;
	    }
	    
	    private static int toInt(char a) {
	        if(a >= '0' && a <= '9') {
	            return a - '0';
	        }
	        if(a >= 'a' && a <= 'f') {
	            return a - 'a' + 10;
	        }
	        if(a >= 'A' && a <= 'F') {
	            return a - 'A' + 10;
	        }
	        throw new IllegalArgumentException("parameter \"" + a + "\"is not hex number!");
	    }
	/**
	 * 初始化密钥对
	 * @return
	 * @throws Exception
	 */
	public static void initKey() throws Exception{
		//实例化密钥对生成器
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		//初始化密钥对生成器
		keyPairGenerator.initialize(KEY_SIZE);
		//生成密钥对
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		//公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		//私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		StringBuffer sb = new StringBuffer();
		sb.append("------publicKey------\n")
		  .append(Hex.encodeHexString(publicKey.getEncoded()))
		  .append("\n")
		  .append("------privateKey------\n")
		  .append(Hex.encodeHexString(privateKey.getEncoded()))
		  ;
	}

}
