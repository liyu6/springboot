package site.fxqn.zcl.utils.crypto;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class CryptoUtils {
    private CryptoUtils() {
    }

    public static final Integer CODE_TYPE_NUMBER_ONLY = 0;
    public static final Integer CODE_TYPE_LETTER_ONLY = 1;
    public static final Integer CODE_TYPE_ALL = 2;
    private static final Map<Integer, String> verifyCodeLetters;
    private static final Random rand;

    static {
        rand = new Random();
        verifyCodeLetters = new HashMap<>();
        verifyCodeLetters.put(CODE_TYPE_NUMBER_ONLY, "0123456789");
        verifyCodeLetters.put(CODE_TYPE_LETTER_ONLY, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        verifyCodeLetters.put(CODE_TYPE_ALL, "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * 创建验证码
     *
     * @param bit      位数
     * @param codeType 验证码类型 0：仅数字  1：仅字母 2：包含数字和字母
     * @return
     */
    public static String newVerifyCode(Integer bit, Integer codeType) {
        StringBuilder sb = new StringBuilder();
        String letters = verifyCodeLetters.get(codeType);
        Integer len = letters.length();
        for (int i = 0; i < bit; i++) {
            int idx = rand.nextInt(len);
            sb.append(letters.charAt(idx));
        }
        return sb.toString();
    }


    /**
     * 生成基于时间戳的uuid
     *
     * @return uuid
     */
    public static String timestampUUID() {
        return System.currentTimeMillis() + newVerifyCode(4, CODE_TYPE_NUMBER_ONLY);
    }


    /**
     * 生成uuid
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 计算带盐的加密字符串
     * 算法按盐的长度除以4获取四个
     *
     * @param str  源字符串
     * @param salt 盐
     * @return 加密后的字符串
     */
    public static String encryptWithSalt(String str, String salt) {
        int len = salt.length() / 4;
        String s1 = str + salt.substring(0, len);
        String s2 = str + salt.substring(len, len * 2);
        String s3 = str + salt.substring(len * 2, len * 3);
        String s4 = str + salt.substring(len * 3);
        str = new StringBuilder(s1).append(s2).append(s3).append(s4).toString();
        return sha1(str);
    }


    /**
     * 计算字符串的sha1值
     *
     * @param str 源字符串
     * @return sha1值
     */
    public static String sha1(String str) {
        if (null == str || 0 == str.length()) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}