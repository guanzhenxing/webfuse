package cn.webfuse.ext.kit;

import cn.webfuse.core.kit.date.DateFormatKits;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 身份证验证辅助类
 * <p>
 * from top.ibase4j.core.util.IDCardUtil
 */
public class IDCardKits {

    private IDCardKits() {

    }


    /**
     * 大陆地区地域编码最大值
     **/
    public static final int MAX_MAINLAND_AREA_CODE = 659004;
    /**
     * 大陆地区地域编码最小值
     **/
    public static final int MIN_MAINLAND_AREA_CODE = 110000;
    /**
     * 香港地域编码值
     **/
    public static final int HK_AREA_CODE = 810000; // 香港地域编码值
    /**
     * 台湾地域编码值
     **/
    public static final int TAIWAN_AREA_CODE = 710000;
    /**
     * 澳门地域编码值
     **/
    public static final int MACAO_AREA_CODE = 820000;

    /**
     * 数字正则
     **/
    public static final String REGEX_NUM = "^[0-9]*$";
    /**
     * 闰年生日正则
     **/
    public static final String REGEX_BIRTHDAY_IN_LEAP_YEAR = "^((19[0-9]{2})|(200[0-9])|(201[0-5]))((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))$";
    /**
     * 平年生日正则
     **/
    public static final String REGEX_BIRTHDAY_IN_COMMON_YEAR = "^((19[0-9]{2})|(200[0-9])|(201[0-5]))((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))$";

    @SuppressWarnings("serial")
    private static final Set<String> BLACK_SET = new HashSet<String>() {
    };

    /**
     * <p>
     * 身份证格式强校验
     * </p>
     * <p>
     * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码， 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
     * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位） 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
     * 4、顺序码（第十五位至十七位） 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数） （1）十七位数字本体码加权求和公式 S =
     * Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和 Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5
     * 8 4 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
     * </p>
     */
    public static final boolean isIdentity(String idNumber) {
        if (StringUtils.isBlank(idNumber)) {
            return false;
        }
        idNumber = idNumber.trim();
        if (BLACK_SET.contains(idNumber)) {
            return false;
        }
        if (!checkIdNumberRegex(idNumber)) {
            return false;
        }
        if (!checkIdNumberArea(idNumber.substring(0, 6))) {
            return false;
        }
        idNumber = convertFifteenToEighteen(idNumber);
        if (!checkBirthday(idNumber.substring(6, 14))) {
            return false;
        }
        if (!checkIdNumberVerifyCode(idNumber)) {
            return false;
        }
        return true;
    }

    /**
     * 从身份证号中获取出生日期，身份证号可以为15位或18位
     *
     * @param identity 身份证号
     * @return 出生日期
     */
    public static final Date getBirthdayFromPersonIDCode(String identity) {
        String id = getIDCode(identity);
        Date birthday = DateFormatKits.parseDate("yyyyMMdd", id.substring(6, 14));
        return birthday;
    }

    /**
     * 将传入的身份证号码进行校验，并返回一个对应的18位身份证
     *
     * @param idCode 身份证号码
     * @return String 十八位身份证号码
     * @throws Throwable 无效的身份证号
     */
    public static final String getIDCode(String idCode) {
        if (idCode == null) {
            throw new RuntimeException("输入的身份证号无效，请检查");
        }

        if (idCode.length() == 18) {
            if (isIdentity(idCode)) {
                return idCode;
            } else {
                throw new RuntimeException("输入的身份证号无效，请检查");
            }
        } else if (idCode.length() == 15) {
            return convertFifteenToEighteen(idCode);
        } else {
            throw new RuntimeException("输入的身份证号无效，请检查");
        }
    }

    /**
     * 从身份证号获取性别
     *
     * @param identity 身份证号
     * @return 性别代码
     * @throws Throwable 无效的身份证号码
     */
    public static final Sex getGenderFromPersonIDCode(String identity) {
        String id = getIDCode(identity);
        char sex = id.charAt(16);
        return sex % 2 == 0 ? Sex.Female : Sex.Male;
    }

    /**
     * 身份证正则校验
     */
    private static boolean checkIdNumberRegex(String idNumber) {
        return Pattern.matches("^([0-9]{17}[0-9Xx])|([0-9]{15})$", idNumber);
    }

    /**
     * 身份证地区码检查
     */
    private static boolean checkIdNumberArea(String idNumberArea) {
        int areaCode = Integer.parseInt(idNumberArea);
        if (areaCode == HK_AREA_CODE || areaCode == MACAO_AREA_CODE || areaCode == TAIWAN_AREA_CODE) {
            return true;
        }
        if (areaCode <= MAX_MAINLAND_AREA_CODE && areaCode >= MIN_MAINLAND_AREA_CODE) {
            return true;
        }
        return false;
    }

    /**
     * 将15位身份证转换为18位
     */
    private static String convertFifteenToEighteen(String idNumber) {
        if (15 != idNumber.length()) {
            return idNumber;
        }
        idNumber = idNumber.substring(0, 6) + "19" + idNumber.substring(6, 15);
        idNumber = idNumber + getVerifyCode(idNumber);
        return idNumber;
    }

    static final char[] CODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'}; // 11个校验码字符
    static final int[] FACTOR = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1}; // 18个加权因子

    /**
     * 根据身份证前17位计算身份证校验码
     */
    private static String getVerifyCode(String idNumber) {
        if (!Pattern.matches(REGEX_NUM, idNumber.substring(0, 17))) {
            return null;
        }

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum = sum + Integer.parseInt(String.valueOf(idNumber.charAt(i))) * FACTOR[i];
        }
        return String.valueOf(CODE[sum % 11]);
    }

    /**
     * 身份证出生日期嘛检查
     */
    private static boolean checkBirthday(String idNumberBirthdayStr) {
        Integer year = null;
        try {
            year = Integer.valueOf(idNumberBirthdayStr.substring(0, 4));
        } catch (Exception e) {
        }
        if (null == year) {
            return false;
        }
        if (isLeapYear(year)) {
            return Pattern.matches(REGEX_BIRTHDAY_IN_LEAP_YEAR, idNumberBirthdayStr);
        } else {
            return Pattern.matches(REGEX_BIRTHDAY_IN_COMMON_YEAR, idNumberBirthdayStr);
        }
    }

    /**
     * 判断是否为闰年
     */
    private static boolean isLeapYear(int year) {
        return (year % 400 == 0) || (year % 100 != 0 && year % 4 == 0);
    }

    /**
     * 身份证校验码检查
     */
    private static boolean checkIdNumberVerifyCode(String idNumber) {
        return getVerifyCode(idNumber).equalsIgnoreCase(idNumber.substring(17));
    }

    /**
     * 性别
     *
     * @author ShenHuaJie
     */
    public enum Sex {
        /**
         * 未知
         */
        Other("未知", 0),
        /**
         * 男
         */
        Male("男", 1),
        /**
         * 女
         */
        Female("女", 2);

        private String name;
        private Integer value;

        Sex(String name, Integer value) {
            this.name = name;
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
