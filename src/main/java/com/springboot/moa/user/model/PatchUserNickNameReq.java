package com.springboot.moa.user.model;

import lombok.*;

import javax.sound.midi.Patch;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserNickNameReq {
    String newNickName;

    public PatchUserNickNameReq(){

    }
}
