package com.springboot.moa.user.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor

public class GetPointHistoryRecentRes {
    int point;
    int addAmount;
    int subAmount;
    Date created;
}
