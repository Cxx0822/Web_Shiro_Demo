package com.shiro.demo.service.entity.front;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FrontResource implements Serializable {
    private Integer id;

    private String resourceName;

    private String resourceDescription;

    private List<FrontPermission> permissionList;

    private static final long serialVersionUID = 1L;
}
