package com.warning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.warning.entity.Post;

import java.util.List;

public interface PostMapper extends BaseMapper<Post> {
    List<Post> getPostBySearch(Post post);
}
