package com.springboot.moa.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserSignUpReq {
    String id;
    String nick;
    String pwd;
    String socialType = "none";
    String socialAccessToken = "none";
    String email;
    public PostUserSignUpReq(){

    }
}
