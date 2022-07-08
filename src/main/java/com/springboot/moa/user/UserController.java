package com.springboot.moa.user;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.user.model.GetUserInfoRes;
import com.springboot.moa.user.model.GetUserPartPostRes;
import com.springboot.moa.user.model.GetUserPostRes;
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
    public BaseResponse<GetUserInfoRes> getUser(@RequestParam int userId) {
        try{
            GetUserInfoRes getUserInfoRes = userProvider.retrieveUser(userId);
            return new BaseResponse<>(getUserInfoRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetUserPostRes>> getUserPost(@PathVariable("userId")int userId) {
        try{
            List<GetUserPostRes> getUserPostRes = userProvider.retrieveUserPosts(userId);
            return new BaseResponse<>(getUserPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/post")
    public BaseResponse<List<GetUserPartPostRes>> getUserPartPost(@PathVariable("userId")int userId) {
        try{
            List<GetUserPartPostRes> getUserPartPostRes = userProvider.retrieveUserPartPosts(userId);
            return new BaseResponse<>(getUserPartPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
