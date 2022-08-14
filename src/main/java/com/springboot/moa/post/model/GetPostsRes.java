package com.springboot.moa.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPostsRes {
    long postId;
    int point;
    String title;
    int qCount;
    int dDay;
    String status;

}
