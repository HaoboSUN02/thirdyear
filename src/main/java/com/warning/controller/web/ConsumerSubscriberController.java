package com.warning.controller.web;

import com.warning.entity.Consumer;
import com.warning.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 用户个人信息
 */
@Controller
@RequestMapping("/web")
public class ConsumerSubscriberController {

    @Autowired
    private ConsumerService consumerService;

    @GetMapping("/subscribers")
    public String list(HttpSession session, Model model) {
        Consumer consumer = (Consumer) session.getAttribute("consumers");
        if (consumer == null) {
            return "redirect:/web";
        }
        model.addAttribute("consumers", consumer);
        return "website/subscribers";
    }

    /**
     * 修改个人信息
     *
     * @param consumer
     * @param attributes
     * @return
     */
    @PostMapping("/subscribers/update")
    public String editPost(Consumer consumer, HttpSession session, RedirectAttributes attributes) {
        if (consumerService.saveOrUpdate(consumer)) {
            session.setAttribute("consumers", consumer);
            attributes.addFlashAttribute("message", "修改成功");
        } else {
            attributes.addFlashAttribute("message", "修改失败");
        }
        return "redirect:/web/subscribers";
    }
}
