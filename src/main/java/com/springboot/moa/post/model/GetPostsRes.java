package com.springboot.moa.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPostsRes {
    int postId;
    int userId;
    int point;
    String title;
    String content;
    int deadline;
}
