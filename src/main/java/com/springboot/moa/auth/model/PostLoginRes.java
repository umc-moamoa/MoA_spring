package com.springboot.moa.auth.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLoginRes {
    private long userId;
    private String jwt;
}
