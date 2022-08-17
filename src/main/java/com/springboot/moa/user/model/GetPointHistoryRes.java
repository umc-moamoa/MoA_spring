package com.springboot.moa.user.model;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class GetPointHistoryRes {
    int point;
    List<GetPointHistoryRecentRes> pointHistoryRecent;
    List<GetPointHistoryFormerRes> pointHistoryFormer;

    public GetPointHistoryRes() {

    }
}
