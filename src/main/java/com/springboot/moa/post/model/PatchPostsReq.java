package com.springboot.moa.post.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PatchPostsReq {
    private int userId;
    private String content;
    public PatchPostsReq(){

    }
}
