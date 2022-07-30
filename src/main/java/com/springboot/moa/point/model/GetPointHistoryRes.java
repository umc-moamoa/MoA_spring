package com.springboot.moa.point.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPointHistoryRes {
    int point;
    int addAmount;
    int subAmount;
}
