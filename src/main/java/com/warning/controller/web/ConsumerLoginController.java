package com.warning.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.warning.entity.Consumer;
import com.warning.entity.Post;
import com.warning.service.ConsumerService;
import com.warning.service.PostService;
import com.warning.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 用户登录管理
 */
@Controller
@RequestMapping("/web")
public class ConsumerLoginController {

    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private PostService postService;

    @GetMapping
    public String loginPage() {
        return "website/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session, Model model,
                        RedirectAttributes attributes) {
        QueryWrapper<Consumer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", MD5Utils.code(password));
        Consumer consumer = consumerService.getOne(queryWrapper);
        if (consumer != null) {
            //不要把密码传到前端页面
            consumer.setPassword(null);
            session.setAttribute("consumers", consumer);
            Post post = new Post();
            PageHelper.startPage(1, 3);
            post.setUserid(consumer.getId());
            List<Post> allPost = postService.getPostBySearch(post);
            PageInfo<Post> pageInfo = new PageInfo<>(allPost);
            model.addAttribute("pageInfo", pageInfo);
            return "website/post";
        } else {
            //因为这是redirect到index页面，如果用model会得不到这个数据
            attributes.addFlashAttribute("message", "用户名和密码错误");
            return "redirect:/web";
        }
    }


    @GetMapping("/manages")
    public String manage(HttpSession session) {
        Consumer consumer = (Consumer) session.getAttribute("consumers");
        if (consumer != null) {
            session.setAttribute("consumers", consumer);
            return "website/post";
        } else {
            //跳转到用户登录页面
            return "website/login";
        }
    }


    @PostMapping("/register")
    public String register(Consumer consumer, RedirectAttributes attributes) {
        QueryWrapper<Consumer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", consumer.getUsername());
        Consumer isConsumer = consumerService.getOne(queryWrapper);
        if (isConsumer == null) {
            consumer.setPassword(MD5Utils.code(consumer.getPassword()));
            boolean saveStatus = consumerService.save(consumer);
            if (saveStatus) {
                return "website/login";
            } else {
                //因为这是redirect到index页面，如果用model会得不到这个数据
                attributes.addFlashAttribute("message", "注册失败");
                return "redirect:/web";
            }
        } else {
            //因为这是redirect到index页面，如果用model会得不到这个数据
            attributes.addFlashAttribute("message", "该用户已注册");
            return "redirect:/web";
        }
    }


    @GetMapping("/register")
    public String register() {
        return "website/register";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("consumers");
        return "redirect:/web";
    }
}
