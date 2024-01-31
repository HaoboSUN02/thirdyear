package com.warning.utils.normalize;

import com.warning.utils.ACTrieUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.Collection;

import static java.lang.Math.min;

public class KeywordMatcher {

    /**
     * 获取摘要文本(不是精确匹配)
     *
     * @param text    源文本
     * @param summary 摘要长度
     * @param start   关键字开始位置
     * @param end     关键字结束位置
     */
    public static SummaryBody getSummary(String text, Integer summary, Integer start, Integer end) {
        Integer kwLen = end - start + 1;
        int middle = (summary - kwLen) / 2;
        if (text.length() <= summary) {
            return new SummaryBody(text, start, end);
        }
        int startPos;
        if (start < middle) {
            startPos = 0;
        } else {
            int endLen = text.length() - end;
            if (endLen < middle) {
                startPos = text.length() - summary;
            } else {
                startPos = start - middle;
            }
        }
        int endPos = min(startPos + summary, text.length());
        return new SummaryBody(text.substring(startPos, endPos), start - startPos, end - startPos);
    }

    /**
     * 高亮
     *
     * @param start 开始位置
     * @param end   结束位置
     * @param text  被处理文本
     */
    public static String highlight(String text, Integer start, Integer end) {
        if (start >= text.length()) {
            return text;
        }
        return StringEscapeUtils.escapeHtml4(text.substring(0, start)) +
                "<b style='color: red;'>" +
                StringEscapeUtils.escapeHtml4(text.substring(start, end + 1)) +
                "</b>" +
                StringEscapeUtils.escapeHtml4(text.substring(end + 1));
    }

    /**
     * 匹配文本高亮
     *
     * @param text  被处理文本
     * @param emits 匹配文本坐标
     */
    public static String matchTextHighLight(String text, Collection<ACTrieUtils.Emit> emits) {
        for (ACTrieUtils.Emit emit : emits) {
            if (emit.getStart() >= text.length()) {
                return text;
            }
            text = text.substring(0, emit.getStart()) +
                    "<b style='color:red;'>" +
                    text.substring(emit.getStart(), emit.getEnd() + 1) +
                    "</b>" +
                    text.substring(emit.getEnd() + 1);
        }
        return text;
    }

}



