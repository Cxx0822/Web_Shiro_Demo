package com.shiro.demo.service.entity.front;

import com.shiro.demo.service.entity.Operation;
import lombok.Data;

import java.io.Serializable;

@Data
public class FrontPermission implements Serializable {
    private Integer id;

    private String permissionName;

    private String permissionDescription;

    private Integer resourceId;

    private Integer operationId;

    private Operation operation;

    private FrontResource resource;

    private static final long serialVersionUID = 1L;
}
