package com.springboot.moa.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetPostContentRes {
    String title;
    String content;
    int qCount;
    int postUserId;
}
