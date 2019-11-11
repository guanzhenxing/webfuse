package cn.webfuse.core.kit.crypto;

import cn.webfuse.core.kit.ExceptionKits;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * 哈希算法
 */
public class HashingKits {


    /**
     * SHA1加密
     */
    public static String sha1Hex(String input) {
        return DigestUtils.sha1Hex(input);
    }


    /**
     * SHA224加密
     *
     * @param input
     * @return
     */
    public static String sha224Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw ExceptionKits.unchecked(e);
        }
    }

    /**
     * SHA256加密
     *
     * @param input
     * @return
     */
    public static String sha256Hex(String input) {
        return DigestUtils.sha256Hex(input);
    }

    /**
     * SHA384加密
     *
     * @param input
     * @return
     */
    public static String sha384Hex(String input) {
        return DigestUtils.sha384Hex(input);
    }

    /**
     * SHA512加密
     *
     * @param input
     * @return
     */
    public static String sha512Hex(String input) {
        return DigestUtils.sha512Hex(input);
    }

    /**
     * 32位的MD5
     *
     * @param input
     * @return
     */
    public static String md5Hex(String input) {
        return DigestUtils.md5Hex(input);
    }

    /**
     * HmacSHA1加密
     *
     * @param input 需要加密的数据
     * @param key   秘钥
     * @return Hex编码后的加密结果
     */
    public static String hmacSha1Hex(String input, String key) {
        byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key).hmac(input);
        return Hex.encodeHexString(hmac);
    }


    /**
     * HmacSHA224加密
     *
     * @param input 需要加密的数据
     * @param key   秘钥
     * @return Hex编码后的加密结果
     */
    public static String hmacSha224Hex(String input, String key) {
        byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_224, key).hmac(input);
        return Hex.encodeHexString(hmac);
    }

    /**
     * HmacSHA256加密
     *
     * @param input 需要加密的数据
     * @param key   秘钥
     * @return Hex编码后的加密结果
     */
    public static String hmacSha256Hex(String input, String key) {
        byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmac(input);
        return Hex.encodeHexString(hmac);
    }

    /**
     * HmacSHA384加密
     *
     * @param input 需要加密的数据
     * @param key   秘钥
     * @return Hex编码后的加密结果
     */
    public static String hmacSha384Hex(String input, String key) {
        byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key).hmac(input);
        return Hex.encodeHexString(hmac);
    }

    /**
     * HmacSHA512加密
     *
     * @param input 需要加密的数据
     * @param key   秘钥
     * @return Hex编码后的加密结果
     */
    public static String hmacSha512Hex(String input, String key) {
        byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key).hmac(input);
        return Hex.encodeHexString(hmac);
    }

    /**
     * HmacMD5加密
     *
     * @param input 需要加密的数据
     * @param key   秘钥
     * @return Hex编码后的加密结果
     */
    public static String hmacMd5Hex(String input, String key) {
        byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_MD5, key).hmac(input);
        return Hex.encodeHexString(hmac);
    }

    /**
     * 使用pbkdf2加密
     *
     * @param input          需要加密的数据
     * @param salt           盐
     * @param iterationCount 迭代次数
     * @param keyLength      key长度（8的倍数，如：8，16，32，64，128，256，512等）
     * @return Hex编码后的加密结果
     */
    private static String pbkdf2(String input, String salt, int iterationCount, int keyLength) {
        try {
            PBEKeySpec spec = new PBEKeySpec(input.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), iterationCount, keyLength);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] value = skf.generateSecret(spec).getEncoded();
            return Hex.encodeHexString(value);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw ExceptionKits.unchecked(e);
        }
    }

}
