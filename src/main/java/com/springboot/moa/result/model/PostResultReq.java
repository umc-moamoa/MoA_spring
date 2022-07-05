package com.springboot.moa.result.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostResultReq {

    int postId;
    int userId;
    List<PostDetailResultReq> postDetailResults;

    public PostResultReq(){

    }

}
