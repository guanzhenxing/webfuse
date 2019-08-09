package cn.webfuse.core.kit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * 加解密工具类
 */
public class CryptoKits {

    /**
     * 32位的MD5加密
     */
    public String md5Hex(String input) {
        return DigestUtils.md5Hex(input);
    }

    /**
     * SHA1加密
     */
    public String sha1Hex(String input) {
        return DigestUtils.sha1Hex(input);
    }

    /**
     * SHA256加密
     */
    public String sha256Hex(String input) {
        return DigestUtils.sha256Hex(input);
    }

    /**
     * SHA384加密
     */
    public String sha384Hex(String input) {
        return DigestUtils.sha384Hex(input);
    }

    /**
     * SHA512加密
     */
    public String sha512Hex(String input) {
        return DigestUtils.sha512Hex(input);
    }

    /**
     * HMAC_MD5加密
     *
     * @param input 需要加密的数据
     * @param key   秘钥
     * @return Hex编码后的加密结果
     */
    public String hmacMd5Hex(String input, String key) {
        byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_MD5, key).hmac(input);
        return EncodeKits.encodeHex(hmac);
    }

    /**
     * HMAC_SHA_1加密
     *
     * @param input 需要加密的数据
     * @param key   秘钥
     * @return Hex编码后的加密结果
     */
    public String hmacSha1Hex(String input, String key) {
        byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key).hmac(input);
        return EncodeKits.encodeHex(hmac);
    }

    /**
     * HMAC_SHA_224加密
     *
     * @param input 需要加密的数据
     * @param key   秘钥
     * @return Hex编码后的加密结果
     */
    public static String hmacSha224Hex(String input, String key) {
        byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_224, key).hmac(input);
        return EncodeKits.encodeHex(hmac);
    }

    /**
     * HMAC_SHA_256加密
     *
     * @param input 需要加密的数据
     * @param key   秘钥
     * @return Hex编码后的加密结果
     */
    public static String hmac256Hex(String input, String key) {
        byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmac(input);
        return EncodeKits.encodeHex(hmac);
    }

    /**
     * HMAC_SHA_384加密
     *
     * @param input 需要加密的数据
     * @param key   秘钥
     * @return Hex编码后的加密结果
     */
    public static String hmacSha384Hex(String input, String key) {
        byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key).hmac(input);
        return EncodeKits.encodeHex(hmac);
    }


    /**
     * HMAC_SHA_512加密
     *
     * @param input 需要加密的数据
     * @param key   秘钥
     * @return Hex编码后的加密结果
     */
    public static String hmacSha512Hex(String input, String key) {
        byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key).hmac(input);
        return EncodeKits.encodeHex(hmac);
    }


    /**
     * DES-Base64加密
     *
     * @param input 需要加密的数据
     * @param key   秘钥
     * @return DES加密后的结果
     */
    public static String encryptDESBase64(String input, String key) {
        try {
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherData = cipher.doFinal(input.getBytes());
            return EncodeKits.encodeBase64(cipherData);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException |
                NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw ExceptionKits.unchecked(e);
        }
    }

    /**
     * DES-Base64解密
     *
     * @param input 需要解密的数据
     * @param key   秘钥
     * @return DES解密后的结果
     */
    public static String decryptDESBase64(String input, String key) {
        try {
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherData = cipher.doFinal(input.getBytes());
            return new String(cipherData);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException |
                NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw ExceptionKits.unchecked(e);
        }
    }


    /**
     * AES-ECB Base64加密
     *
     * @param input 需要加密的数据
     * @param key   秘钥
     * @return AES加密后的结果(base64加密后的字符串)
     */
    public static String encryptAesEcbBase64(String input, String key) {

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));
            byte[] bytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return EncodeKits.encodeBase64(bytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw ExceptionKits.unchecked(e);
        }

    }

    /**
     * AES-ECB解密
     *
     * @param input 需要解密的数据(base64加密后的字符串)
     * @param key   秘钥
     * @return AES解密后的结果
     */
    public static String decryptAesEcbBase64(String input, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));
            byte[] bytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return new String(bytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw ExceptionKits.unchecked(e);
        }

    }

    /**
     * AES/CBC/PKCS5Padding加密，然后进行base64加密
     *
     * @param plainText
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptAesCbcBase64(String plainText, String key) throws Exception {
        byte[] clean = plainText.getBytes();
        //Cipher
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // Generating IV.
        int ivSize = cipher.getBlockSize();
        byte[] iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        // Encrypt.
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(clean);
        // Combine IV and encrypted part.
        byte[] encryptedIVAndText = new byte[ivSize + encrypted.length];
        System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize);
        System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.length);
        return Base64.getEncoder().encodeToString(encryptedIVAndText);
    }

    /**
     * base64解密后再进行AES/CBC/PKCS5Padding解密
     *
     * @param encryptedIvText
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptAesCbcBase64(String encryptedIvText, String key) throws Exception {
        byte[] encryptedIvTextBytes = Base64.getDecoder().decode(encryptedIvText);
        //Cipher
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // Extract IV.
        int ivSize = cipher.getBlockSize();
        byte[] iv = new byte[ivSize];
        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        // Extract encrypted part.
        int encryptedSize = encryptedIvTextBytes.length - ivSize;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(encryptedIvTextBytes, ivSize, encryptedBytes, 0, encryptedSize);
        // Decrypt.
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] original = cipher.doFinal(encryptedBytes);
        String originalString = new String(original);
        return originalString;
    }


    /**
     * 生成AES密钥,返回字节数组, 默认长度为128位(16字节).
     */
    public static byte[] generateAesKey() {
        return generateAesKey(128);
    }

    /**
     * 生成AES密钥,可选长度为128,192,256位.
     */
    public static byte[] generateAesKey(int keysize) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(keysize);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (GeneralSecurityException e) {
            throw ExceptionKits.unchecked(e);
        }
    }

    /**
     * 生成HMAC-SHA1密钥,返回字节数组,长度为160位(20字节). HMAC-SHA1算法对密钥无特殊要求, RFC2401建议最少长度为160位(20字节).
     */
    public static byte[] generateHmacSha1Key() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(HmacAlgorithms.HMAC_SHA_1.toString());
            keyGenerator.init(160);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (GeneralSecurityException e) {
            throw ExceptionKits.unchecked(e);
        }
    }


}
