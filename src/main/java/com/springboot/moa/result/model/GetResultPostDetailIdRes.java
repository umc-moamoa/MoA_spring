package com.springboot.moa.result.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetResultPostDetailIdRes {
    long startPostDetailId;
    long endPostDetailId;

    public GetResultPostDetailIdRes() {

    }
}
