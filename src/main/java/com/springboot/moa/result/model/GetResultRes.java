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
    List<GetResultItems> getResultItems;

    List<GetResultStatisticsRes> getResultStatisticsRes;

    // android 용
    double case1;
    double case2;
    double case3;
    double case4;
    double case5;
    double case6;
    double case7;
    double case8;

    // web 용
    double[] statistics;

    public GetResultRes() {

    }
}
