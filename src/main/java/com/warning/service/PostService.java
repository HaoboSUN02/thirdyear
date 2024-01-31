package com.warning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.warning.entity.Post;

import java.util.List;

public interface PostService extends IService<Post> {
    List<Post> getPostBySearch(Post post);
}
