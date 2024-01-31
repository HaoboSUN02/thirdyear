package com.warning.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.warning.entity.Post;
import com.warning.service.PostService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户发帖模块
 */
@RestController
@RequestMapping("post")
public class PostController {

    @Resource
    private PostService postService;

    /**
     * 查询用户发帖信息
     *
     * @param page
     * @param limit
     * @param post
     * @return
     */
    @RequestMapping("selectAllPost")
    public Object selectAllPost(Integer page, Integer limit, Post post) {
        PageHelper.startPage(page, limit);
        //默认显示未审核的信息，可根据审核状态选择审核完成的物资信息
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        if (post.getStatus() != null) {
            queryWrapper.eq("status", post.getStatus());
        } else {
            queryWrapper.eq("status", 0);
        }
        queryWrapper.eq(!StringUtils.isEmpty(post.getTitle()), " title", post.getTitle());
        List<Post> allPost = postService.list(queryWrapper);
        PageInfo pageInfo = new PageInfo(allPost);
        Map<String, Object> tableData = new HashMap<String, Object>();
        //这是layui要求返回的json数据格式
        tableData.put("code", 0);
        tableData.put("msg", "");
        //将全部数据的条数作为count传给前台（一共多少条）
        tableData.put("count", pageInfo.getTotal());
        //将分页后的数据返回（每页要显示的数据）
        tableData.put("data", pageInfo.getList());
        return tableData;
    }

    /**
     * 审核用户发帖状态
     *
     * @param post
     * @return
     */
    @RequestMapping("changePostStatus")
    public String changePostStatus(Post post) {
        boolean updateStatus = postService.lambdaUpdate()
                .eq(Post::getId, post.getId())
                .set(Post::getStatus, post.getStatus())
                .update();
        if (updateStatus) {
            return "审核成功";
        } else {
            return "审核失败";
        }
    }

    /**
     * 删除用户发帖信息
     *
     * @param id
     * @return
     */
//    @RequestMapping("deletePost")
//    public String deletePost(@RequestParam("id") Integer id) {
//        if (postService.removeById(id)) {
//            return "删除成功";
//        } else {
//            return "删除失败";
//        }
//    }
}
