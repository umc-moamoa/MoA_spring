package com.springboot.moa.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPostDetailRes {
    long postDetailId;
    String question;
    int format;
    String[] items;

    public GetPostDetailRes() {

    }

    public void setQuestion(long postDetailId, int format, String question) {

    }
}
