package com.springboot.moa.post.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PatchPostsReq {
    private long postId;
    private String title;
    private String content;
    private Date deadline;
    public PatchPostsReq(){

    }
}
