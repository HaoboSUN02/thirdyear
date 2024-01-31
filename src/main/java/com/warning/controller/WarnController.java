package com.warning.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.warning.common.ResponseResult;
import com.warning.entity.Warn;
import com.warning.service.WarnService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 信息
 */
@RestController
@RequestMapping("warn")
public class WarnController {

    @Resource
    private WarnService warnService;

    /**
     * 查询信息
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("selectAllWarn")
    public ResponseResult selectAllWarn(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Warn> list = warnService.list();
        PageInfo pageInfo = new PageInfo(list);
        return new ResponseResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 添加信息
     *
     * @param warn
     * @return
     */
    @PostMapping("addWarn")
    public ResponseResult addWarn(Warn warn) {
        if (warnService.save(warn)) {
            return ResponseResult.success("添加成功");
        } else {
            return ResponseResult.failed("添加失败");
        }
    }

    /**
     * 修改信息
     *
     * @param warn
     * @return
     */
    @PostMapping("updateWarn")
    public ResponseResult updateWarn(Warn warn) {
        if (warnService.updateById(warn)) {
            return ResponseResult.success("修改成功");
        } else {
            return ResponseResult.failed("修改失败");
        }
    }

    /**
     * 删除信息
     *
     * @param id 编号
     * @return
     */
    @PostMapping("deleteWarn")
    public ResponseResult deleteWarn(@RequestParam("id") Integer id) {
        if (warnService.removeById(id)) {
            return ResponseResult.success("删除成功");
        } else {
            return ResponseResult.failed("删除失败");
        }
    }

}
