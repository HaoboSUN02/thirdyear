package com.warning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.warning.entity.Consumer;

public interface ConsumerService extends IService<Consumer> {

    /**
     * 查询用户信息
     *
     * @param username
     * @return
     */
    Consumer queryByUsername(String username);

}
