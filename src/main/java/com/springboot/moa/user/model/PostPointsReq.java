package com.springboot.moa.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor

public class PostPointsReq {
    int userId;
    int addAmount;
    int subAmount;
}
