package com.springboot.moa.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLoginReq {
    private String email;
    private String pwd;

    public PostLoginReq() {

    }
}
