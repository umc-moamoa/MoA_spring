package com.springboot.moa.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor

public class GetPointHistoryFormerRes {
    int addAmount;
    int subAmount;
    Date created;
}
