package com.shiro.demo.service.service.impl;

import com.shiro.demo.service.entity.Resource;
import com.shiro.demo.service.mapper.ResourceMapper;
import com.shiro.demo.service.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Cxx
 * @since 2021-12-06
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

}
