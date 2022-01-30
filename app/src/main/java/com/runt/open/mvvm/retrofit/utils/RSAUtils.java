package com.runt.open.mvvm.retrofit.utils;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


public class RSAUtils {

    public static final String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALmG22Y82CPx/n7yE6Y/lh33ZoQxWdp9LeWH7DN07dp/5Od9PY6Eix2XBjI4mRWoRv71gtbA3+nq9aaztibWGvjD/akMGTTgWD2BXQI3kw4CrGI0P1YzoFFM0oGUqd9duUzJpl8Lpq/3JpB1vo+c6EsVqggWJoPpIJNb2pcEBC3fAgMBAAECgYEAmmd5C3KR9AB/7Qjtf4wmFJSFNnmYLXXHD+N6miyzlAii6mHaLFV1LlA1sPBXv6WISMLtfGuTLywD/BRPAa1pTbrQTz3BDvtHHrCaL124ZdIr1j49TGyyYRCplwDoP9QWKTtEcZ6mP0P0rzh2Zfe2nBaJDn2qFPmYd9P4WBGMW1kCQQDop0oMK/oDc7gFQqXf0CnEETsOjJrvVzOuRue54WLl1H62PXhsGFzNvlm4J4OqMlLpnX6et7dzgX2MAVlJKP3jAkEAzCTqOo1l/E5bN00HFcQ3odCr5Pi985OK17R1ueAiLoY6g3I6O88zDWCiB5XmQPMSJ6QcNPIxAsJI6JGVqMZQ1QJACyhStci6IcY/8gijOdua1StaYLU/jPDqqpX98P0tKAaL2SOTjeORN9DELr++YcAuF8QU2XnIE4MHSVqbNJYBrwJBAKxkwCiSH/3hbcZVlhYbjZ9oyMCkDkUT47wk+QXu8O65C9DVNbgsUcCKSkp9m+RdYId5XxiXLixWRZug1fGhB8ECQQDTPTxN9RhunrUaT0brQsd+0btrYMrm/suaBzJyBpspFzjxRcM+AhlAHa1szkMqE6r01ZHCFQopaZEWijeh4Kgf";

    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVj4BkEYtlv8z4quUUrkRvW4xuWQvXegMuLKPQZky8LObbxfFZniSvQ4gllFlyFuCjeeInjyQFPC3ARdbihV3P88drBsB2gCG9lwlCkgMjZfSc/hxC4VirsHbGGSIN5oPyCZMQNAUnIojpKBRlE0TJmHvP+FpAe46Yb+oPs8R5DQIDAQAB";


    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    static final String KEY_RSA = "RSA"; //android标准 “RSA/ECB/PKCS1Padding” 服务端标准 “RSA”

    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_RSA);
        generator.initialize(1024);
        return generator.generateKeyPair();
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        byte[] decodedKey = Base64.decode(privateKey.getBytes(), Base64.URL_SAFE);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        byte[] decodedKey = Base64.decode(publicKey.getBytes(), Base64.URL_SAFE);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密
     *
     * @param data 待加密数据
     * @param key 密钥
     * @return
     */
    public static String encrypt(String data, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return Base64.encodeToString(encryptedData,Base64.URL_SAFE | Base64.NO_WRAP);
    }

    /**
     * RSA解密
     *
     * @param data 待解密数据
     * @param key 密钥
     * @return
     */
    public static String decrypt(String data, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] dataBytes = Base64.decode(data.getBytes(), Base64.URL_SAFE);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, "UTF-8");
    }

    /**
     * 签名
     *
     * @param data 待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return  Base64.encodeToString(signature.sign(),Base64.DEFAULT);
    }

    /**
     * 验签
     *
     * @param srcData 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decode(sign.getBytes(),Base64.DEFAULT));
    }

}
