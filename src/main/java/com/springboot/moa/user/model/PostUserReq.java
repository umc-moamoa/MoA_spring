package com.springboot.moa.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    String email;
    String nick;
    String pwd;
    String socialType = "none";
    String socialAccessToken = "none";
    public PostUserReq(){

    }
}
