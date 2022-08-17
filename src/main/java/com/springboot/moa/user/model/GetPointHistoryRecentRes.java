package com.springboot.moa.user.model;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class GetPointHistoryRecentRes {
    int addAmount;
    int subAmount;
    Date created;
}
