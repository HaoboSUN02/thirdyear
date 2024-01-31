package com.warning;

import com.warning.utils.normalize.TextStandardizationUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ApplicationTests {

    @Resource
    private TextStandardizationUtils textStandardizationUtils;

//
//    @Test
//    void doctorSearch() {
//        List<String> warnList = new ArrayList<>();
//        warnList.add("消化不良");
//        warnList.add("黄疸");
//        List<Doctor> doctorList = knowledgeService.list();
//        List<Doctor> matchDoctorList = new ArrayList<>();
//        if (doctorList.size() > 0) {
//            for (Doctor doctor : doctorList) {
//                String originGoodAt = doctor.getGoodat();
//                String match = doctor.getGoodat();
//                for (String s : warnList) {
//                    if (!StringUtils.isEmpty(matchText(s, match))) {
//                        match = matchText(s, match);
//                        doctor.setGoodat(match);
//                    }
//                }
//                if (!originGoodAt.equals(match)) {
//                    matchDoctorList.add(doctor);
//                }
//            }
//            System.out.println(matchDoctorList);
//        }
//    }
//
//    /**
//     * 多模字符串匹配算法匹配
//     *
//     * @param words 关键词
//     * @param text  匹配文本
//     * @return
//     */
//    private String matchText(String words, String text) {
//        ACTrie trie = new ACTrie();
//        trie.addKeyword(words);
//        Collection<ACTrie.Emit> emits = trie.parseText(textStandardizationUtil.normalizeText(text));
//        String matchText = "";
//        if (!emits.isEmpty()) {
//            matchText = KeywordMatcher.matchTextHighLight(text, emits);
//        }
//        return matchText;
//    }
}
