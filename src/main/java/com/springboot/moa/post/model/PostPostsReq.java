package com.springboot.moa.post.model;
import lombok.*;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class PostPostsReq {
    int userId;
    int point;
    int categoryId;
    String title;
    String content;
    int deadline;
    List<PostDetailsReq> postDetails;

    public PostPostsReq(){

    }
}
