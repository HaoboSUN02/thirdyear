package com.warning.utils.normalize;

import org.springframework.stereotype.Component;

@Component
public class TextStandardizationUtils {

    private enum CharType {
        /**
         * 空白字符
         */
        Blank,
        /**
         * 中文基本字符，不包括标点
         */
        ChineseChar,
        /**
         * ascii英文和数字
         */
        EnglishOrNumber,
        /**
         * 其他，比如标点符号、emoji以及其他各种文字等
         */
        Other
    }

    private static CharType detectCharType(Character c) {
        int charCode = CharacterUtility.charToAscii(c);
        CharType charType;
        if (Character.isWhitespace(charCode) || Character.isISOControl(charCode) || charCode == 12288 /* 全角空格 */) {
            charType = CharType.Blank;
        } else if (charCode >= 49 && charCode <= 57/* 数字 */ || charCode >= 97 && charCode <= 122/* 大写字母 */ || charCode >= 65 && charCode <= 90/* 小写字母 */) {
            charType = CharType.EnglishOrNumber;
        } else if (CharacterUtility.isChineseChar(c)) {
            charType = CharType.ChineseChar;
        } else {
            charType = CharType.Other;
        }
        return charType;
    }

    /**
     * 标准化一段文字
     * <p>
     * 具体规则如下：
     * 文字开头与结尾不会出现空白字符
     * 英文数字/符号间，去掉所有连续的空白字符，只保留一个
     * 中文之间不保留任何空白字符
     * 中文与英文数字以及特殊符号之间不保留任何空白字符
     */
//    引用变量被final修饰之后，虽然不能再指向其他对象，但是它指向的对象的内容是可变的
    private final StringBuilder sb = new StringBuilder(1024 * 2);

    // sb缓冲区里最后一个字符的类型
    private CharType lastCharInSbBuffer = CharType.Blank;

    // 上一个处理的字符的类型
    private CharType previousCharType = CharType.Blank;

    private void appendChar(Character c, CharType charType) {
        sb.append(c);
        lastCharInSbBuffer = charType;
    }

    public String normalizeText(String text) {
        // 使用setLength方法清空
        sb.setLength(0);
        char[] chars = text.toCharArray();

        for (Character c : chars) {
            CharType charType = detectCharType(c);
            switch (charType) {
                case Blank:
                    break;
                case ChineseChar: {
                    appendChar(c, charType);
                    break;
                }
                case EnglishOrNumber:
                case Other: {
                    if ((lastCharInSbBuffer == CharType.EnglishOrNumber || lastCharInSbBuffer == CharType.Other)
                            && previousCharType == CharType.Blank) {
                        // 连续空白只保留一个
                        appendChar(' ', CharType.Blank);
                    }
                    appendChar(c, charType);
                    break;
                }
            }
            previousCharType = charType;
        }
        return sb.toString();
    }
}
