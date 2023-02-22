package com.springboot.moa.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetUserAnswersRes {
    int format;
    String[] result;
    String[] result2;
    public GetUserAnswersRes() {

    }
}