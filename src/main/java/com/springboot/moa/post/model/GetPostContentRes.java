package com.springboot.moa.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter

public class GetPostContentRes {
    long postUserId;
    String title;
    String content;
    int qCount;
    int deadline;
    boolean isLike;
    boolean isMyPost;

    public GetPostContentRes(){

    }

    public GetPostContentRes(long postUserId, String title, String content, int qCount, int deadline) {
        this.postUserId = postUserId;
        this.title = title;
        this.content = content;
        this.qCount = qCount;
        this.deadline = deadline;
    }
}
