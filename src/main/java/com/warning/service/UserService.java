package com.warning.service;


import com.warning.common.DataGridView;
import com.warning.entity.User;
import com.warning.entity.UserRole;

import java.util.List;

public interface UserService {

    //登陆
    User loginname(String loginname);

    //查询所有用户
    List<User> selectAllUser(User user);

    //修改用户
    int updateUser(User user);

    //添加用户
    int addUser(User user);

    //删除用户
    void deleteUser(Integer userid);

    //重置密码
    int resetUserPwd(User user);

    //查询所有用户角色
    DataGridView queryUserRole(Integer userid);

    //保存用户和角色的关系
    void saveUserRole(UserRole userRole);

    //修改个人资料
    List<User> updateLogin(Integer userid);

    int editLogin(User user);

    //修改密码
    int editPwd(User user);

    //检查唯一
    int checkUser(User user);
}
