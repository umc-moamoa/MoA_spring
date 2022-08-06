package com.springboot.moa.result.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetResultStatisticsReq {
    long postId;
    long postDetailId;
}
