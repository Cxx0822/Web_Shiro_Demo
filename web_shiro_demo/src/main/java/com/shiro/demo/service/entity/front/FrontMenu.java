package com.shiro.demo.service.entity.front;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FrontMenu implements Serializable {
    private Integer id;

    private String path;

    private String name;

    private String component;

    private String redirect;

    private Short hidden;

    private Short alwaysShow;

    private Integer parentId;

    private FrontMenuMeta meta;

    private List<FrontMenu> children;

    private static final long serialVersionUID = 1L;
}
