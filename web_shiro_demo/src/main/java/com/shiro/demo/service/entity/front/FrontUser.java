package com.shiro.demo.service.entity.front;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FrontUser implements Serializable {
    private Integer id;

    private String username;

    private String nickname;

    private String introduction;

    private String avatar;

    private List<FrontRole> roleList;

    private List<Integer> roles;

    private static final long serialVersionUID = 1L;
}
