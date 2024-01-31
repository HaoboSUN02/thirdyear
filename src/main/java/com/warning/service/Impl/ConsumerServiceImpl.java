package com.warning.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.warning.entity.Consumer;
import com.warning.mapper.ConsumerMapper;
import com.warning.service.ConsumerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ConsumerServiceImpl extends ServiceImpl<ConsumerMapper, Consumer> implements ConsumerService {

    @Resource
    private ConsumerMapper consumerMapper;

    @Override
    public Consumer queryByUsername(String username) {
        return consumerMapper.queryByUsername(username);
    }
}
