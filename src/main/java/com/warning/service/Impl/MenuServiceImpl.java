package com.warning.service.Impl;

import com.warning.entity.Menu;
import com.warning.mapper.MenuMapper;
import com.warning.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<Menu> queryMenuByUid(Integer userid) {
        return menuMapper.queryMenuByUid(userid);
    }

    @Override
    public List<Menu> loadAvailableMenu() {
        return menuMapper.loadAvailableMenu();
    }


    @Override
    public List<Menu> queryMenuAllList(Menu menu) {
        return menuMapper.queryMenuAllList(menu);
    }

    @Override
    public int addMenu(Menu menu) {
        return menuMapper.addMenu(menu);
    }

    @Override
    public int deleteMenu(Integer id) {
        return menuMapper.deleteMenu(id);
    }

    @Override
    public int checkMenuHasChildren(Integer pid) {
        return menuMapper.checkMenuHasChildren(pid);
    }

    @Override
    public int updateMenu(Menu menu) {
        return menuMapper.updateMenu(menu);
    }
}
