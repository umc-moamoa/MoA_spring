package com.springboot.moa.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPostsRes {
    long postId;
    long userId;
    int point;
    String title;
    String content;
    int deadline;
}
