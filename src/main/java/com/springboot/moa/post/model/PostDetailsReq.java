package com.springboot.moa.post.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
@Getter
@Setter
@AllArgsConstructor
public class PostDetailsReq {
    int format;
    String question;
    List<PostFormatReq> postFormat;

    public PostDetailsReq(){

    }
}
