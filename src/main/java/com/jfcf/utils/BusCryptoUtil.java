package com.jfcf.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

/**
 * 调用bus系统使用的加密方式
 * 
 * <p>
 * 创建日期：2016年8月29日
 * </p>
 * 
 * @version V1.0
 * @author luyj
 * @see
 */
public class BusCryptoUtil {
	public static String publicEnc(String content, String pk) throws Exception {
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		PublicKey pubkey = null;
		InputStream is = new ByteArrayInputStream(pk.getBytes("utf-8"));

		byte[] pubbytes = new byte[new Long(pk.length()).intValue()];
		is.read(pubbytes);

		X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(
				Base64.decode(new String(pubbytes)));

		pubkey = keyf.generatePublic(pubX509);
		cipher.init(1, pubkey);
		byte[] cipherText = cipher.doFinal(content.getBytes());

		return Base64.encode(cipherText);
	}

	public static String privateDec(String content, String prikeyStr)
			throws Exception {
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		PrivateKey privkey = null;
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		InputStream key = new ByteArrayInputStream(prikeyStr.getBytes("utf-8"));
		byte[] pribytes = new byte[new Long(prikeyStr.length()).intValue()];
		key.read(pribytes);

		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
				Base64.decode(new String(pribytes)));
		privkey = keyf.generatePrivate(priPKCS8);
		cipher.init(2, privkey);
		byte[] newPlainText = cipher.doFinal(Base64.decode(content));

		return new String(newPlainText);
	}

	public static String getDecString(String strMi, String strKey)
			throws Exception {
		SecretKey secretKey = getKey(strKey);
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(2, key);

		byte[] bytes = Base64.decode(strMi);

		byte[] result = cipher.doFinal(bytes);
		String strMing = new String(result, "utf-8");

		return strMing;
	}

	public static SecretKey getKey(String strKey) throws Exception {
		KeyGenerator _generator = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(strKey.getBytes());
		_generator.init(128, secureRandom);
		return _generator.generateKey();
	}

	public static String getEncString(String strContent, String strKey)
			throws Exception {
		SecretKey secretKey = getKey(strKey);
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		byte[] byteContent = strContent.getBytes("utf-8");
		cipher.init(1, key);
		byte[] result = cipher.doFinal(byteContent);

		return Base64.encode(result);
	}

	public static String randChar(int charCount) {
		String charValue = "";

		for (int i = 0; i < charCount; i++) {
			char c = (char) randomInt(33, 128);
			charValue = charValue + String.valueOf(c);
		}
		return charValue;
	}

	public static int randomInt(int from, int to) {
		return from + new Random().nextInt(to - from);
	}

	public static void createKeyPairs(String path) {
		FileOutputStream out = null;
		FileOutputStream outPub = null;
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024, new SecureRandom());
			KeyPair pair = generator.generateKeyPair();
			PublicKey pubKey = pair.getPublic();
			PrivateKey privKey = pair.getPrivate();
			byte[] pk = pubKey.getEncoded();
			byte[] privk = privKey.getEncoded();

			String strpk = new String(Base64.encode(pk));
			String strprivk = new String(Base64.encode(privk));

			File priKeyfile = new File(path + "rsa_pri_key.pem");
			out = new FileOutputStream(priKeyfile);
			out.write(strprivk.getBytes());

			File pubKeyfile = new File(path + "rsa_pub_key.pem");
			outPub = new FileOutputStream(pubKeyfile);
			outPub.write(strpk.getBytes());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (outPub != null)
					outPub.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}