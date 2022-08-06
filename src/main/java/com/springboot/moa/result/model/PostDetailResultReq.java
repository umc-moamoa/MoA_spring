package com.springboot.moa.result.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
public class PostDetailResultReq {
    long resultId;
    long postDetailId;
    String result;

    public PostDetailResultReq(){

    }
}
