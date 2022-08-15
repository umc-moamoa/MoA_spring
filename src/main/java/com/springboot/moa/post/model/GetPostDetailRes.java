package com.springboot.moa.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPostDetailRes {
    long postDetailId;
    String question;
    int format;
    String item;
}
