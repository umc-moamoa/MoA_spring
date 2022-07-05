package com.springboot.moa.post.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
@Getter
@Setter
@AllArgsConstructor
public class PostDetailsReq {
    String question;
    int type;
    List<String> postFormat;

    public PostDetailsReq(){

    }
}
