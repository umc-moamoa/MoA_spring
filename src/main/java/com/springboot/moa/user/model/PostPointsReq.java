package com.springboot.moa.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class PostPointsReq {
    int userId;
    int addAmount;
    int subAmount;

    public PostPointsReq(){

    }
}
