package com.springboot.moa.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetParticipantsRes {
    int point;
    String title;
    String status;
    // 등록된 질문의 개수
    int qcount;
}
