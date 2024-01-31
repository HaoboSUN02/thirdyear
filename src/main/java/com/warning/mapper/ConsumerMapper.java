package com.warning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.warning.entity.Consumer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ConsumerMapper extends BaseMapper<Consumer> {

    @Select("select * from tb_consumer where username = #{username}")
    Consumer queryByUsername(@Param("username") String username);

}
