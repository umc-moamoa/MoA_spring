package com.springboot.moa.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserPostRes {
    long postId;
    String postTitle;
    int point;
    int postResultCount;
    int qCount;
    int dDay;
    String status;
}
