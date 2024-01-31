package com.warning.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.warning.common.DataGridView;
import com.warning.common.ResponseResult;
import com.warning.config.UserCredentialsMatcher;
import com.warning.entity.User;
import com.warning.entity.UserRole;
import com.warning.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("selectAllUser")
    @ResponseBody
    public ResponseResult selectAllUser(Integer page, Integer limit, User user) {
        PageHelper.startPage(page, limit);
        List<User> allUser = userService.selectAllUser(user);
        PageInfo pageInfo = new PageInfo(allUser);
        return new ResponseResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @RequestMapping("updateUser")
    @ResponseBody
    public Object updateUser(User user) {
        if (userService.updateUser(user) > 0) {
            return "修改成功";
        } else {
            return "修改失败";
        }
    }


    /**
     * 删除用户
     *
     * @param userid
     * @return
     */
    @RequestMapping("deleteUser")
    @ResponseBody
    public Object deleteUser(Integer userid) {
        userService.deleteUser(userid);
        return "删除成功";
    }


    /**
     * 初始化用户分配角色
     *
     * @param userid
     * @return
     */
    @RequestMapping("initUserRole")
    @ResponseBody
    public DataGridView initUserRole(Integer userid) {
        return userService.queryUserRole(userid);
    }


    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @RequestMapping("addUser")
    @ResponseBody
    public Object addUser(User user) {
        int i1 = userService.checkUser(user);
        if (i1 == 1) {
            return "当前登陆名已存在";
        } else {
            //加盐
            String salt = UserCredentialsMatcher.generateSalt(6);
            String pwd = user.getPwd();
            //MD5加密迭代两次
            user.setPwd(UserCredentialsMatcher.encryptPassword("md5", pwd, salt, 2));
            user.setType(2);
            user.setSalt(salt);
            System.out.println(salt);
            int i = userService.addUser(user);
            if (i > 0) {
                return "添加成功";
            } else {
                return "添加失败";
            }
        }
    }


    /**
     * 重置密码
     *
     * @param user
     * @param userid
     * @return
     */
    @RequestMapping("resetUserPwd")
    @ResponseBody
    public Object resetUserPwd(User user, Integer userid) {
        user.setUserid(userid);
        //加盐
        String salt = UserCredentialsMatcher.generateSalt(6);
        //MD5加密迭代两次
        user.setPwd(UserCredentialsMatcher.encryptPassword("md5", "123456", salt, 2));
        user.setType(2);
        user.setSalt(salt);
        userService.resetUserPwd(user);
        return "重置成功";

    }


    /**
     * 保存用户和角色的关系
     *
     * @param userRole
     * @return
     */
    @RequestMapping("saveUserRole")
    @ResponseBody
    public Object saveUserRole(UserRole userRole) {
        userService.saveUserRole(userRole);
        return "分配成功";
    }


    /**
     * 修改资料
     *
     * @param user
     * @return
     */
    @RequestMapping("editLogin")
    @ResponseBody
    public Object editLogin(User user) {
        int i = userService.editLogin(user);
        if (i == 1) {
            return "修改成功";
        } else {
            return "修改失败";
        }

    }


    /**
     * 修改密码
     *
     * @param user
     * @param loginname
     * @param pwd
     * @param pwd1
     * @return
     */
    @RequestMapping("editPwd")
    @ResponseBody
    public Object editPwd(User user, String loginname, String pwd, String pwd1) {
        //第一步：建立subject
        Subject subject = SecurityUtils.getSubject();
        //第二步：封装token  凭证
        UsernamePasswordToken token = new UsernamePasswordToken(loginname, pwd);
        try {
            //只要能通过认证就能通过了
            subject.login(token);
            //加盐
            String salt = UserCredentialsMatcher.generateSalt(6);
            //MD5加密迭代两次
            user.setPwd(UserCredentialsMatcher.encryptPassword("md5", pwd1, salt, 2));
            user.setSalt(salt);
            userService.editPwd(user);
            return "1";//修改成功
        } catch (IncorrectCredentialsException e) {
            return "0";//密码错误
        }

    }
}
