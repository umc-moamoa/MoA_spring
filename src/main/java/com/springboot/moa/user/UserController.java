package com.springboot.moa.user;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.user.model.GetUserInfoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserProvider userProvider;

    public UserController(UserProvider userProvider){
        this.userProvider = userProvider;
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetUserInfoRes>> getUser(@RequestParam int userId) {
        try{
            List<GetUserInfoRes> getUserInfoRes = userProvider.retrieveUser(userId);
            return new BaseResponse<>(getUserInfoRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
