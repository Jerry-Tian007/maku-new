package com.ruoyi.common.utils.encrypt;

import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtils {
    private static final String RSA = "RSA";
    private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArBYP+MKjC6JraTjRnC0/BR3wdL/S6+wY5oGwWL/FCORzKnCT7COzTDtjoCLdqwz4K7iDjRPpKMKawtukG+R6w7gI/bqtXxuK12w7fRPdjZKih4EyKcyOGLk99AfTpFu8Jl/qruJwf5/v20EQ+NsbGtFI9tQ1i1HBPbaoD5K+TVijDb6JgBEvjeE9oF8HhuZee7EvelwAEUCOeoCIyyQ/CNvzfPbDYOXF85K0+kGD8AjLqyu71u4scLXNfQJniv1eH4bOskxtGPLwck5DlwLakBP31INrrqkos4/uwz+iwXBQxC68h/2Lc+z6Y6oojL9+fLMi3iqnokXVwc3zEiehTQIDAQAB";
    private static final String PRIVATE_KEY = "MIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQCsFg/4wqMLomtpONGcLT8FHfB0v9Lr7BjmgbBYv8UI5HMqcJPsI7NMO2OgIt2rDPgruIONE+kowprC26Qb5HrDuAj9uq1fG4rXbDt9E92NkqKHgTIpzI4YuT30B9OkW7wmX+qu4nB/n+/bQRD42xsa0Uj21DWLUcE9tqgPkr5NWKMNvomAES+N4T2gXweG5l57sS96XAARQI56gIjLJD8I2/N89sNg5cXzkrT6QYPwCMurK7vW7ixwtc19AmeK/V4fhs6yTG0Y8vByTkOXAtqQE/fUg2uuqSizj+7DP6LBcFDELryH/Ytz7PpjqiiMv358syLeKqeiRdXBzfMSJ6FNAgMBAAECggEBAKVyEKj5eAgXf20KU3vzhENHJYcZZY+szcPPq9ung6eo+5Ax7AV7zUXdU0sCOlvZfD+Mhe181w15C5GL2Zt3bw/8P6ixcT2fHT0/sXHvR3J1KA1fCPtmzra2YfncDH5YM+wxC1BYJBCPk+sYA1k+TjDaRWwJWfE4CLFsiuduDcgNdi85An/YX2QCK4gIcq0lizjg26HfWq3/06k8aVcHBRghqtFWX/mxJPxxg3XKd9d151UzFWvkTTNLmwAgIEcGLTAN18WIGVwIewuWNZm0+QresOaD6C++PE8fvy2nXkRZ1nBYgp3CF90/oxV5K8S+IMMqxDcKJgAiF0on21ta/AECgYEA+R4N7vgzqIYS5tqI7UHlPtae/GBpocox33YxX5WZwcJjJTC+KTTD9AO5FtorrywDrBytNTSMhBFmq9IcRS8NW64FsvDIYnBvuIPosGH8nguPvlCn+GSQC/E+EzYB7FFi4zxOPZv5mkValVtP1yfI1c68K1RM2RNZ2/JjM8nYjpkCgYEAsNcvbS83PbdzQ+EO8NZDVRZlMi4elmPQI7vTT51bT0DakLkrRTUImjw1Jhdym0qHqdkEAicnDISnF/+wTme+Ia/NQ/tfgwkbPhcmxdkwziKmC7qCVIaouu7uVn3fZzX3vInlfUVyXgHhkpULZMZvSmnKNjLT8A95VlTQ6rHKXNUCgYEAosgIwsZcuxog78iNALu2WMFVUgSOAbJltGc/C/5tYjbrIjofiD16bdCg+VXGhch0/dofNE0KJKJaPJwm7O46BFZKUwsTZX+jASbwvZDJyVw0qV964syTc7pHnbYEu67StzmIxqlNCImZD0+DdEwC5/MW8hk7vfzvEtHoEMcsQsECgYEAgq+DrPynpkRcOl2n3PN67vaN8neCw0EgIEg98Sgownj7DNjK0WpP4St9W5kYaQq7Fxn5jF3Czc16bpHWiRrBt8NjvkmWXA/uwgP581x+ZNfWPfSnPDAHoZ3RJLoAzxVi70I5Bkq8KhzZxqm+scqzlBp6BXOqjE3jLZmgitC468ECgYEAoiEGA6iwxCdRt26rBQEZcDl42kCl6SthDSSbshBQq95q1p5I2bwwymKkiZHDeaoGFs+NbhK5+ZkYEa3Sz8NP6ZedGZFZdOyDA8zM9C4DUjX310dynrG30DedtdG7NUvGTZoOMpK6fN/nt5k8X4qg6TqmmWSHtOCEviTHtjwYKPQ=";
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static void main(String[] args) throws Exception {
        JSONUtil.parseArray("[{\"name\":\"AZURE_OPENAI_CHAT_ENDPOINT\",\"label\":\"AZURE_OPENAI_CHAT_ENDPOINT\",\"type\":\"input\",\"encrypt\":0,\"value\":[\"https://aiottest.openai.azure.com/\"]},{\"name\":\"AZURE_OPENAI_CHAT_API_VERSION\",\"label\":\"AZURE_OPENAI_CHAT_API_VERSION\",\"type\":\"input\",\"encrypt\":0,\"value\":[\"2024-05-01-preview\"]},{\"name\":\"AZURE_OPENAI_CHAT_API_KEY\",\"label\":\"AZURE_OPENAI_CHAT_API_KEY\",\"type\":\"input\",\"encrypt\":1,\"value\":[\"0726bb90d00742df93a133b36c89b130\"]},{\"name\":\"AZURE_OPENAI_CHAT_MODEL_NAME\",\"label\":\"AZURE_OPENAI_CHAT_MODEL_NAME\",\"type\":\"input\",\"encrypt\":0,\"value\":[\"gpt-4o\"]}]");
        String s = "0726bb90d00742df93a133b36c89b130";
        String s1 = encryptByPublicKey(s);
        String s2 = decryptByPrivateKey(s1);
        System.out.println("原文加密后: " + s1);
        System.out.println("原文解密后: " + s2);

    }


    /**
     * 生成密钥对
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(PUBLIC_KEY);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicK = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        // 可以使用UTF_8编码
//        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }


    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(PRIVATE_KEY);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
    }


    /**
     * 私钥加签
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String sign(String data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initSign(privateK);
        signature.update(data.getBytes());
        return Base64.getEncoder().encodeToString(signature.sign());
    }


    /**
     * 公钥验签
     *
     * @param data
     * @param sign
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static boolean verify(String data, String sign, String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicK = keyFactory.generatePublic(x509EncodedKeySpec);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initVerify(publicK);
        signature.update(data.getBytes());
        return signature.verify(Base64.getDecoder().decode(sign));
    }
}
