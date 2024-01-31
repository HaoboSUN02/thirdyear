package com.warning.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.warning.entity.Warn;
import com.warning.mapper.WarnMapper;
import com.warning.service.WarnService;
import org.springframework.stereotype.Service;

@Service
public class WarnServiceImpl extends ServiceImpl<WarnMapper, Warn> implements WarnService {

}
