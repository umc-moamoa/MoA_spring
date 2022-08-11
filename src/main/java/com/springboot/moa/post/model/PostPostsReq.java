package com.springboot.moa.post.model;
import lombok.*;

import java.util.Date;
import java.util.List;
@Getter
@Setter
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
    public PostPostsReq(long categoryId, int addAmount, int subAmount, String title, String content,
                        Date deadline, List<PostDetailsReq> postDetails){
        this.categoryId = categoryId;
        this.addAmount = addAmount;
        this.subAmount = subAmount;
        this.title = title;
        this.content = content;
        this.deadline = deadline;
        this.postDetails = postDetails;
    }
}
