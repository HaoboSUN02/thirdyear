package com.warning.controller.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.warning.entity.Consumer;
import com.warning.entity.Post;
import com.warning.entity.Warn;
import com.warning.service.PostService;
import com.warning.service.WarnService;
import com.warning.utils.ACTrieUtils;
import com.warning.utils.normalize.KeywordMatcher;
import com.warning.utils.normalize.TextStandardizationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户帖子
 */
@Controller
@RequestMapping("/web")
public class ConsumerPostController {

    @Autowired
    private PostService postService;

    @Autowired
    private WarnService warnService;

    @Autowired
    private TextStandardizationUtils textStandardizationUtils;

    /**
     * 用户论坛管理列表内容展示
     *
     * @param session
     * @param model
     * @param pageNum
     * @return
     */
    @GetMapping("/post")
    public String list(HttpSession session, Model model, @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) {
        Consumer user = (Consumer) session.getAttribute("consumers");
        if (user == null) {
            return "redirect:/web";
        }
        Post post = new Post();
        PageHelper.startPage(pageNum, 3);
        post.setUserid(user.getId());
        List<Post> allPost = postService.getPostBySearch(post);
        PageInfo<Post> pageInfo = new PageInfo<>(allPost);
        model.addAttribute("pageInfo", pageInfo);
        return "website/post";
    }


    /**
     * 用户论坛管理发帖搜索
     *
     * @param session
     * @param post
     * @param model
     * @param pageNum
     * @return
     */
    @PostMapping("/post/search")
    public String search(HttpSession session, Post post, Model model, @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) {
        Consumer user = (Consumer) session.getAttribute("consumers");
        if (user == null) {
            return "redirect:/web";
        }
        post.setUserid(user.getId());
        List<Post> postBySearch = postService.getPostBySearch(post);
        PageHelper.startPage(pageNum, 3);
        PageInfo<Post> pageInfo = new PageInfo<>(postBySearch);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("message", "查询成功");
        return "website/post";
    }

    /**
     * 跳转用户论坛管理新增页面
     *
     * @param session
     * @return
     */
    @GetMapping("/post/input")
    public String toAdd(HttpSession session) {
        Consumer user = (Consumer) session.getAttribute("consumers");
        if (user == null) {
            return "redirect:/web";
        }
        return "website/post-input";
    }

    /**
     * 新增用户论坛管理发帖信息
     *
     * @param session
     * @param post
     * @param attributes
     * @return
     */
    @PostMapping("/addPost")
    public String add(HttpSession session, Post post, RedirectAttributes attributes) {
        Consumer user = (Consumer) session.getAttribute("consumers");
        if (user != null) {
            Post matchPost = matchPost(post);
            matchPost.setUserid(user.getId());
            matchPost.setUsername(user.getUsername());
            matchPost.setCreatetime(new Date());
            //todo 此处进行审核，根据涉密信息存储审核状态
            postService.saveOrUpdate(matchPost);
            attributes.addFlashAttribute("message", "新增成功");
            return "redirect:/web/post";
        } else {
            return "redirect:/web";
        }
    }

    /**
     * 匹配异常发布内容
     *
     * @param post
     * @return
     */
    private Post matchPost(Post post) {
        List<String> wordsList = warnService.list().stream().map(Warn::getWords).collect(Collectors.toList());
        String originTitle = post.getTitle();
        String matchTitle = post.getTitle();
        String originContent = post.getContent();
        String matchContent = post.getContent();
        for (String s : wordsList) {
            if (!StringUtils.isEmpty(matchText(s, matchContent))) {
                matchContent = matchText(s, matchContent);
            }
            if (!StringUtils.isEmpty(matchText(s, matchTitle))) {
                matchTitle = matchText(s, matchTitle);
            }
        }
        if (!originTitle.equals(matchTitle)) {
            post.setTitle(matchTitle);
            post.setStatus(2);
        }
        if (!originContent.equals(matchContent)) {
            post.setContent(matchContent);
            post.setStatus(2);
        }
//        if (!StringUtils.isEmpty(post.getImage())) {
//            String imageContent = IdentifyImageContentUtils.checkFile(FileUtils.FILE_PATH + post.getImage());
//            JSONArray results = JSON.parseObject(imageContent).getJSONArray("words_result");
//            if (!CollectionUtils.isEmpty(results)) {
//                List<String> words = results.stream().map(e -> JSON.parseObject(e.toString()).getString("words")).collect(Collectors.toList());
//                StringBuilder sb = new StringBuilder();
//                for (String str : words) {
//                    sb.append(str);
//                }
//                String totalString = sb.toString();
//                String matchImgContent = totalString;
//                for (String s : wordsList) {
//                    if (!StringUtils.isEmpty(matchText(s, matchImgContent))) {
//                        matchImgContent = matchText(s, matchImgContent);
//                    }
//                }
//                if (!totalString.equals(matchImgContent)) {
//                    post.setImgError(matchImgContent);
//                    post.setStatus(2);
//                }
//            }
//        }
        return post;
    }

    /**
     * 多模字符串匹配算法匹配
     *
     * @param words 关键词
     * @param text  匹配文本
     * @return
     */
    private String matchText(String words, String text) {
        ACTrieUtils trie = new ACTrieUtils();
        trie.addKeyword(words);
        Collection<ACTrieUtils.Emit> emits = trie.parseText(textStandardizationUtils.normalizeText(text));
        String matchText = "";
        if (!emits.isEmpty()) {
            matchText = KeywordMatcher.matchTextHighLight(text, emits);
        }
        return matchText;
    }


    /**
     * 将数据返回跳转用户论坛管理编辑页面
     *
     * @param session
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/post/{id}/input")
    public String toUpdate(HttpSession session, @PathVariable Long id, Model model) {
        Consumer user = (Consumer) session.getAttribute("consumers");
        if (user == null) {
            return "redirect:/web";
        }
        Post postById = postService.getById(id);
        model.addAttribute("post", postById);
        return "website/post-update";
    }


    /**
     * 修改用户论坛管理发帖信息
     *
     * @param showPost
     * @param attributes
     * @return
     */
    @PostMapping("/post/update")
    public String editPost(Post showPost, RedirectAttributes attributes) {
        postService.saveOrUpdate(showPost);
        attributes.addFlashAttribute("message", "修改成功");
        return "redirect:/web/post";
    }

    /**
     * 删除用户发帖论坛管理信息
     *
     * @param session
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/post/{id}/delete")
    public String delete(HttpSession session, @PathVariable Long id, RedirectAttributes attributes) {
        Consumer user = (Consumer) session.getAttribute("consumers");
        if (user == null) {
            return "redirect:/web";
        }
        postService.removeById(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/web/post";
    }
}
