package com.springboot.moa.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserPartPostRes {
    long postId;
    int point;
    String title;
    int qCount;
    String status;
}
