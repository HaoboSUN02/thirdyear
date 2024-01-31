package com.warning.config;

import com.warning.utils.FileUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //文件磁盘图片url 映射
        //配置server虚拟路径，handler为前台访问的目录，locations为files相对应的本地路径
        String path = "file:" + FileUtils.getProjectRootPath().replaceAll("\\\\", "/") + "/src/main/resources/upload/";
        registry.addResourceHandler("/files/**").addResourceLocations(path);
    }
}
