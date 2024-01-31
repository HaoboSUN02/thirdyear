package com.warning.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.warning.entity.Post;
import com.warning.mapper.PostMapper;
import com.warning.service.PostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {


    @Resource
    private PostMapper postMapper;

    @Override
    public List<Post> getPostBySearch(Post post) {
        return postMapper.getPostBySearch(post);
    }
}
