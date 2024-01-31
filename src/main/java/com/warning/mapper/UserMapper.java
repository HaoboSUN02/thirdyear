package com.warning.mapper;

import com.warning.entity.Role;
import com.warning.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    User loginname(String loginname);

    List<User> selectAllUser(User user);

    int updateUser(User user);

    //添加用户
    int addUser(User user);


    int deleteUserById(Integer userid);

    int deleteRoleUser(Integer userid);

    //重置密码
    int resetUserPwd(User user);

    //查询所有角色
    List<Role> queryAllRole();

    //按照用户id查询角色
    List<Role> queryRoleById(Integer uid);

    //保存用户和角色
    void insertUserRole(@Param("uid") Integer userid, @Param("rid") Integer rid);

    List<User> updateLogin(Integer userid);

    int editLogin(User user);

    int editPwd(User user);

    //检查唯一
    int checkUser(User user);

}
