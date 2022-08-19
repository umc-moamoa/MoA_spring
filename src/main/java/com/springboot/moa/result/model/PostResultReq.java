package com.springboot.moa.result.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class PostResultReq {

    long postId;
    long userId;
//    List<PostDetailResultReq> postDetailResults;
    List<String[]> postDetailResults;

    public PostResultReq(){

    }
    public PostResultReq(long postId, List<String[]> postDetailResults){
        this.postId = postId;
        this.postDetailResults = postDetailResults;
    }

}