package com.springboot.moa.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor

public class DeleteUserRes {
    String nick;
    String id;
    String pwd;
    int point;
}
