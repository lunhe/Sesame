package com.helun.menu.boot;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.google.common.primitives.Bytes;

public class EncryptHandler {


	public static String encryptByPem( String str, String publicKeyPemPath ) throws Exception{
		ClassLoader classLoader = DecryptHandler.class.getClassLoader() ;
		InputStream in = classLoader.getResourceAsStream(publicKeyPemPath) ;
		byte[] ret = new byte[0] ;
		byte[] buffer = new byte[1024] ;
		int n = -1 ;
		while( (n = in.read(buffer, 0, 1024)) != -1) {
			ret = Bytes.concat(ret , buffer) ;
		}
		in.close();
		String publicKey = new String(buffer) ;
		return encrypt(str, publicKey) ;
	}
	
	public static String encrypt( String str, String base64PublicKeyStr ) throws Exception{
		//base64编码的公钥
		byte[] publicKeyBytes = Base64.decodeBase64(base64PublicKeyStr);
		
		 X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        Key publicKey = keyFactory.generatePublic(x509KeySpec);

	        // 对数据加密
	        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
	        cipher.init(Cipher.ENCRYPT_MODE, publicKey);


		String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
		return outStr;
	}
	
	
	
	 public static byte[] aesEncrypt(String content, String password) {
	        try {
	            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者

	            kgen.init(128, new SecureRandom(password.getBytes()));// 利用用户密码作为随机数初始化出
	                                                                    // 128位的key生产者
	            //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行

	            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥

	            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
	                                                            // null。

	            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥

	            Cipher cipher = Cipher.getInstance("AES");// 创建密码器

	            byte[] byteContent = content.getBytes("utf-8");

	            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器

	            byte[] result = cipher.doFinal(byteContent);// 加密

	            return result;

	        } catch (NoSuchPaddingException e) {
	            e.printStackTrace();
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (InvalidKeyException e) {
	            e.printStackTrace();
	        } catch (IllegalBlockSizeException e) {
	            e.printStackTrace();
	        } catch (BadPaddingException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	 
}
