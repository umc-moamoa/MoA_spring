package com.springboot.moa.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class GetUserInfoRes {
    String nickName;
    int point;
    int postCount;
}
