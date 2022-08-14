package com.springboot.moa.post.model;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
public class PostPostsReq {
    long userId;
    long categoryId;
    int shortCount;
    int longCount;
    String title;
    String content;
    Date deadline;
    List<String[]> postDetails;

    public PostPostsReq(){

    }
    public PostPostsReq(long categoryId, int shortCount, int longCount, String title, String content,
                        Date deadline, List<String[]> postDetails){
        this.categoryId = categoryId;
        this.shortCount = shortCount;
        this.longCount = longCount;
        this.title = title;
        this.content = content;
        this.deadline = deadline;
        this.postDetails = postDetails;
    }
}
