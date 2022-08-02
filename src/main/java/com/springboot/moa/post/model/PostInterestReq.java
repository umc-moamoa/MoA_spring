package com.springboot.moa.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostInterestReq {
    long postId;
    long userId;

    public PostInterestReq(){

    }
}
