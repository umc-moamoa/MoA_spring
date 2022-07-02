package com.springboot.moa.involve.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetInvolvesRes {

    int postDetailId;
    String question;
    int type;

}
