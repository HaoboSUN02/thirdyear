package com.warning.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.warning.common.DataGridView;
import com.warning.common.ResponseResult;
import com.warning.entity.Role;
import com.warning.entity.RoleMenu;
import com.warning.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("loadAllRole")
    @ResponseBody
    public ResponseResult loadAllRole(Role role, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Role> roles = roleService.loadAllRole(role);
        PageInfo pageInfo = new PageInfo(roles);
        return new ResponseResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    @RequestMapping("insertRole")
    @ResponseBody
    public String insertRole(Role role) {
        if (roleService.insertRole(role) == 1) {
            return "添加成功";
        } else {
            return "添加失败";
        }
    }

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    @RequestMapping("updateRole")
    @ResponseBody
    public String updateRole(Role role) {
        if (roleService.updateRole(role) == 1) {
            return "修改成功";
        } else {
            return "修改失败";
        }
    }

    /**
     * 删除角色及关系
     *
     * @param roleid
     * @return
     */
    @RequestMapping("deleteRole")
    @ResponseBody
    public String deleteRole(Integer roleid) {
        roleService.deleteRole(roleid);
        return "删除成功";
    }

    /**
     * 加载角色管理分配菜单
     *
     * @param roleid
     * @return
     */
    @RequestMapping("initRoleMenuTreeJson")
    @ResponseBody
    public DataGridView initRoleMenuTreeJson(Integer roleid) {
        return roleService.initRoleMenuTreeJson(roleid);
    }

    /**
     * 保存角色和菜单的关系
     *
     * @param roleMenu
     * @return
     */
    @RequestMapping("saveRoleMenu")
    @ResponseBody
    public Object saveRoleMenu(RoleMenu roleMenu) {
        roleService.saveRoleMenu(roleMenu);
        return "分配成功";
    }
}
