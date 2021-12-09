package com.shiro.demo.service.service;

import com.shiro.demo.service.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Cxx
 * @since 2021-12-06
 */
public interface RoleMenuService extends IService<RoleMenu> {
    Integer delete(String columnName, String columnValue);
}
