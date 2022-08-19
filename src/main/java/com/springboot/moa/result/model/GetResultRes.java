package com.springboot.moa.result.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetResultRes {
    int format;
    String question;

    List<GetResultStatisticsRes> getResultStatisticsRes;

    double case1;
    double case2;
    double case3;
    double case4;
    double case5;
    double case6;
    double case7;
    double case8;
    double case9;
    double case10;
    public GetResultRes() {

    }
}
