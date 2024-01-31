package com.warning.service.Impl;

import com.warning.common.DataGridView;
import com.warning.common.TreeNode;
import com.warning.entity.Menu;
import com.warning.entity.Role;
import com.warning.entity.RoleMenu;
import com.warning.mapper.MenuMapper;
import com.warning.mapper.RoleMapper;
import com.warning.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<Role> loadAllRole(Role role) {
        return roleMapper.loadAllRole(role);
    }

    @Override
    public int insertRole(Role role) {
        return roleMapper.insertRole(role);
    }

    @Override
    public int updateRole(Role role) {
        return roleMapper.updateRole(role);
    }

    @Transactional
    @Override
    public void deleteRole(Integer roleid) {
        //删除角色
        roleMapper.deleteRoleId(roleid);
        //删除角色与菜单的关系
        roleMapper.deleteRoleMenuId(roleid);
        //删除角色与用户的关系
        roleMapper.deleteRoleUserId(roleid);
    }

    @Transactional
    @Override
    public DataGridView initRoleMenuTreeJson(Integer roleid) {
        //查询所有可用菜单
        List<Menu> allmenus = menuMapper.loadAvailableMenu();
        //按照角色id查询菜单
        List<Menu> roleMenu = menuMapper.queryMenuByRoleId(roleid);
        //如果roleMenu菜单的id与allmenus菜单的id相等那么可以查询该角色已有的菜单 复选框为true 1为选中
        List<TreeNode> data = new ArrayList<>();
        for (Menu m1 : allmenus) {
            String checkArr = "0";
            for (Menu m2 : roleMenu) {
                if (m1.getId() == m2.getId()) {
                    checkArr = "1";//选中
                    break;
                }
            }
            Integer id = m1.getId();
            Integer pid = m1.getPid();
            String title = m1.getTitle();
            Boolean spread = m1.getSpread() == true;
            data.add(new TreeNode(id, pid, title, spread, checkArr));
        }
        return new DataGridView(data);
    }

    @Transactional
    @Override
    public void saveRoleMenu(RoleMenu roleMenu) {
        Integer rid = roleMenu.getRoleid();
        Integer[] mids = roleMenu.getIds();
        //根据用户id删除角色拥有的菜单
        roleMapper.deleteRoleMenuId(rid);
        //循环添加角色分配的菜单
        for (Integer mid : mids) {
            roleMapper.insertRoleMenu(rid, mid);
        }
    }

}
