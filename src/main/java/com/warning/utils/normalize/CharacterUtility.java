package com.warning.utils.normalize;

public class CharacterUtility {
    /**
     * 是否是星号
     */
    public static Boolean isStar(Character c) {
        int value = charToAscii(c);
        return value == 0x2B50 || value == 0x2605 || value == 0x2606;
    }

    /**
     * 是否是标点
     */
    public static Boolean isPunctuation(Character c) {
        int value = charToAscii(c);
        return (value >= 0x21 && value <= 0x2f) ||
                (value >= 0x3a && value <= 0x40) ||
                (value >= 0x5b && value <= 0x60) ||
                (value >= 0x7b && value <= 0x7e) ||
                (value >= 0x2011 && value <= 0x205e) ||
                (value >= 0x3001 && value <= 0x301f) ||
                (value >= 0xff01 && value <= 0xff0f) ||
                (value >= 0xff1a && value <= 0xff20) ||
                (value >= 0xff3b && value <= 0xff40) ||
                (value >= 0xff5b && value <= 0xff65);

    }

    /**
     * 校验一个字符是否是基本汉字
     * <p>
     * 不包括拼音、标点及部首等字符
     *
     * @param c 被校验的字符
     * @return true代表是汉字
     */
    public static Boolean isChineseChar(Character c) {
        int value = charToAscii(c);
        return (value >= 0x4E00 && value <= 0x9FA5);
    }

    /**
     * 字符转换为Ascii
     *
     * @param c
     * @return
     */
    public static int charToAscii(Character c) {
        return (int) c;
    }

    /**
     * Ascii转换为字符
     *
     * @param charCode
     * @return
     */
    public static int AsciiToChar(int charCode) {
        return (char) charCode;
    }
}
