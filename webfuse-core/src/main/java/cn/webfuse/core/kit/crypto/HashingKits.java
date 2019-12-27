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
     * 32位的MD5
     *
     * @param input
     * @return
     */
    public static String md5Hex(String input) {
        return DigestUtils.md5Hex(input);
    }

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
}
