package com.springboot.moa.post.model;
import lombok.*;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class PostPostsReq {
    long userId;
    long categoryId;
    String title;
    String content;
    int deadline;
    List<PostDetailsReq> postDetails;

    public PostPostsReq(){

    }
}
