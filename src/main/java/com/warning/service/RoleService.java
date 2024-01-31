package com.warning.service;


import com.warning.common.DataGridView;
import com.warning.entity.Role;
import com.warning.entity.RoleMenu;

import java.util.List;

public interface RoleService {

    //查询说有用户
    List<Role> loadAllRole(Role role);

    //添加角色
    int insertRole(Role role);

    //修改角色
    int updateRole(Role role);

    //删除角色
    void deleteRole(Integer roleid);

    //初始角色菜单
    DataGridView initRoleMenuTreeJson(Integer roleid);

    //保存角色与菜单的关系
    void saveRoleMenu(RoleMenu roleMenu);
}
