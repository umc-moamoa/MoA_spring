package com.springboot.moa.post.model;
import lombok.*;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class PostPostsReq {
    long userId;
    long categoryId;
    int addAmount;
    int subAmount;
    String title;
    String content;
    Date deadline;
    List<PostDetailsReq> postDetails;

    public PostPostsReq(){

    }
}
