package com.springboot.moa.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdatePwdReq {
    String id;
    String pwd;
    public UpdatePwdReq(){

    }
}
