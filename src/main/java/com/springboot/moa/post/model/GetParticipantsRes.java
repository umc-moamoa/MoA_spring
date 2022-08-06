package com.springboot.moa.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetParticipantsRes {
    long postId;
    int point;
    String title;
    // 등록된 질문의 개수
    int qCount;
    String status;
}
