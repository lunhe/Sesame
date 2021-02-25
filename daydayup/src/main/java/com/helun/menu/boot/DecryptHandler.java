package com.helun.menu.boot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.google.common.primitives.Bytes;

public class DecryptHandler {
	
	public static String readKeyPem( String pemPath) throws IOException {
		ClassLoader classLoader = DecryptHandler.class.getClassLoader() ;
		InputStream in = classLoader.getResourceAsStream(pemPath) ;
		byte[] ret = new byte[0] ;
		byte[] buffer = new byte[1024] ;
		int n = -1 ;
		while( (n = in.read(buffer, 0, 1024)) != -1) {
			ret = Bytes.concat(ret , buffer) ;
		}
		in.close();
		String key = new String(buffer) ;
		return key ;
	}
	
	public static String decryptByPem(String str, String privatePemPath) throws Exception{
		
		ClassLoader classLoader = DecryptHandler.class.getClassLoader() ;
		InputStream in = classLoader.getResourceAsStream(privatePemPath) ;
		byte[] ret = new byte[0] ;
		byte[] buffer = new byte[1024] ;
		int n = -1 ;
		while( (n = in.read(buffer, 0, 1024)) != -1) {
			ret = Bytes.concat(ret , buffer) ;
		}
		in.close();
		String privateKey = new String(buffer) ;
		return decrypt(str, privateKey) ;
	}
	
	public static String decrypt(String str, String privateKey) throws Exception{
		//64位解码加密后的字符串
		byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
		//base64编码的私钥
		byte[] decoded = Base64.decodeBase64(privateKey);  
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));  
		//RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		return outStr;
	}
	
	
	 public static byte[] aesDecrypt(byte[] content, String password) {
	        try {
	            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
	            kgen.init(128, new SecureRandom(password.getBytes()));
	            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
	            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
	            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
	            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
	            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
	            byte[] result = cipher.doFinal(content);  
	            return result; // 明文   
	            
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        } catch (NoSuchPaddingException e) {
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
