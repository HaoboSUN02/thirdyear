package com.warning.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.warning.common.DataGridView;
import com.warning.common.ResponseResult;
import com.warning.common.TreeNode;
import com.warning.entity.Menu;
import com.warning.entity.User;
import com.warning.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/sel")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 加载首页左边导航栏
     *
     * @param request
     * @return
     */
    @RequestMapping("toTreeLoad")
    @ResponseBody
    public List<TreeNode> toTreeLoad(HttpServletRequest request) {
        //获取用户登陆id根据不同的用户有不同菜单
        User user = (User) request.getSession().getAttribute("user");
        ///获取用户登陆id根据不同的用户有不同菜单
        List<Menu> list = menuService.queryMenuByUid(user.getUserid());
        //创建list集合
        //把list放入nodes
        List<TreeNode> nodes = new ArrayList<>();
        for (Menu menus : list) {
            Integer id = menus.getId();
            Integer pid = menus.getPid();
            String title = menus.getTitle();
            String icon = menus.getIcon();
            String href = menus.getHref();
            Boolean spread = menus.getSpread() == true;
            String target = menus.getTarget();
            nodes.add(new TreeNode(id, pid, title, icon, href, spread, target));
        }
        //组装菜单
        List<TreeNode> treeNodes = new ArrayList<>();
        //n1.getPid() == 1 为父级菜单
        for (TreeNode n1 : nodes) {
            if (n1.getPid() == 1) {
                treeNodes.add(n1);
            }
            //将n2放入n1的子级中   id为子级
            for (TreeNode n2 : nodes) {
                if (n2.getPid() == n1.getId()) {
                    n1.getChildren().add(n2);
                }
            }
        }
        return treeNodes;
    }

    /**
     * 加载菜单管理左边的菜单树
     *
     * @param menu
     * @return
     */
    @RequestMapping("loadMenuMangerLeftTreeJson")
    @ResponseBody
    public DataGridView loadMenuMangerLeftTreeJson(Menu menu) {
        //查询所有菜单
        List<Menu> list = menuService.queryMenuAllList(menu);
        //将查询的菜单循环放入TreeNode
        List<TreeNode> nodes = new ArrayList<>();
        for (Menu menus : list) {
            Integer id = menus.getId();
            Integer pid = menus.getPid();
            String title = menus.getTitle();
            String icon = menus.getIcon();
            String href = menus.getHref();
            Boolean spread = menus.getSpread() == true;
            String target = menus.getTarget();
            nodes.add(new TreeNode(id, pid, title, icon, href, spread, target));
        }
        return new DataGridView(nodes);
    }


    /**
     * 查询所有菜单MenuRight
     *
     * @param menu
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("queryMenuAllList")
    @ResponseBody
    public ResponseResult queryMenuAllList(Menu menu, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Menu> menus = menuService.queryMenuAllList(menu);
        PageInfo pageInfo = new PageInfo(menus);
        return new ResponseResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 添加菜单
     *
     * @param menu
     * @return
     */
    @RequestMapping("addMenu")
    @ResponseBody
    public String addMenu(Menu menu) {
        if (menuService.addMenu(menu) == 1) {
            return "添加成功";
        } else {
            return "添加失败";
        }
    }

    /**
     * 检查菜单
     *
     * @param id
     * @return
     */
    @RequestMapping("checkMenuHasChildren")
    @ResponseBody
    public String checkMenuHasChildren(Integer id) {
        if (menuService.checkMenuHasChildren(id) <= 0) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    @RequestMapping("deleteMenu")
    @ResponseBody
    public String deleteMenu(Integer id) {
        menuService.deleteMenu(id);
        return "true";
    }

    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    @RequestMapping("updateMenu")
    @ResponseBody
    public Object updateMenu(Menu menu) {
        menuService.updateMenu(menu);
        return "修改成功";
    }
}
