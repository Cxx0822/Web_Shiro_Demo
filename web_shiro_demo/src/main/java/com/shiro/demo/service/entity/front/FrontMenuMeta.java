package com.shiro.demo.service.entity.front;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FrontMenuMeta implements Serializable {
    private Integer id;

    private String title;

    private String icon;

    private Short noCache;

    private Short affix;

    private Short breadcrumb;

    private String activeMenu;

    private List<String> roles;

    private static final long serialVersionUID = 1L;
}
