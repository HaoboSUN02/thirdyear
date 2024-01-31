package com.warning.controller;


import com.warning.entity.User;
import com.warning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class SysController {

    @Autowired
    private UserService userService;

    /*
     * 跳转管理菜单
     * */
    @RequestMapping("toMenuManager")
    public String toMenuManager() {
        return "view/menu/menuManager";
    }

    /*
     * 跳转加载菜单管理左边的菜单树
     * */
    @RequestMapping("toMenuLeft")
    public String toMenuLeft() {
        return "view/menu/menuLeft";
    }

    /*
     * 跳转加载菜单管理左边的增删改
     * */
    @RequestMapping("toMenuRight")
    public String toMenuRight() {
        return "view/menu/menuRight";
    }

    /*
     * 跳转加载角色
     * */
    @RequestMapping("toLoadAllRole")
    public String toLoadAllRole() {
        return "view/role/roleManager";
    }

    /*
     * 跳转加载用户
     * */
    @RequestMapping("toLoadAllUser")
    public String toLoadAllUser() {
        return "view/user/userManager";
    }

    /*
     * 跳转加载icon.html
     * */
    @RequestMapping("icon")
    public String icon() {
        return "view/center/icon";
    }

    /*
     * 跳转加载updateLogin.html
     * */
    @RequestMapping("toUpdateLogin")
    public String toUpdateLogin(Integer userid, Model model) {
        List<User> users = userService.updateLogin(userid);
        model.addAttribute("usersLogin", users);
        return "view/user/updateLogin";
    }

    /*
     * 跳转加载main.html
     * */
    @RequestMapping("toMain")
    public String toMain() {
        return "view/main/main";
    }

    /*
     * 跳转管理页面
     * */
    @RequestMapping("toKnowledge")
    public String toKnowledge() {
        return "view/knowledge/knowledgeManager";
    }


    /*
     * 跳转管理页面
     * */
    @RequestMapping("toPost")
    public String toPost() {
        return "view/post/postManager";
    }

    /*
     * 跳转管理页面
     * */
    @RequestMapping("toWarn")
    public String toWarn() {
        return "view/warn/warnManager";
    }

}
