package com.shiro.demo.service.entity.front;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FrontRole implements Serializable {
    private Integer id;

    private String roleName;

    private String roleDescription;

    private List<FrontMenu> routes;

    private List<FrontPermission> permissionList;

    private static final long serialVersionUID = 1L;
}
