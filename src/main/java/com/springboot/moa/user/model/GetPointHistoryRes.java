package com.springboot.moa.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor

public class GetPointHistoryRes {
    int point;
    int addAmount;
    int subAmount;
}
