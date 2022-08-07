package com.springboot.moa.user;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.config.BaseResponseStatus;
import com.springboot.moa.user.model.*;
import com.springboot.moa.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.springboot.moa.config.BaseResponseStatus.POINT_HISTORY_INPUT_FAILED;

import static com.springboot.moa.config.BaseResponseStatus.USERS_EMPTY_USER_ID;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetUserInfoRes> getUser(@RequestParam long userId) {
        try{
            GetUserInfoRes getUserInfoRes = userProvider.retrieveUser(userId);
            return new BaseResponse<>(getUserInfoRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetUserPostRes>> getUserPost(@PathVariable("userId")long userId) {
        try{
            List<GetUserPostRes> getUserPostRes = userProvider.retrieveUserPosts(userId);
            return new BaseResponse<>(getUserPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/post")
    public BaseResponse<List<GetUserPartPostRes>> getUserPartPost(@PathVariable("userId")long userId) {
        try{
            List<GetUserPartPostRes> getUserPartPostRes = userProvider.retrieveUserPartPosts(userId);
            return new BaseResponse<>(getUserPartPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        try{
            if(postUserReq.getId().length() > 15 || postUserReq.getId().length() < 7)
                return new BaseResponse<>(BaseResponseStatus.USERS_USERS_FAILED_ID);
            if(postUserReq.getNick().length() > 15 || postUserReq.getNick().length() < 7)
                return new BaseResponse<>(BaseResponseStatus.USERS_USERS_FAILED_NICK);
            if(postUserReq.getPwd().length() > 15 || postUserReq.getPwd().length() < 7)
                return new BaseResponse<>(BaseResponseStatus.USERS_USERS_FAILED_PWD);
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // localhost:9000/users/1/interest
    @ResponseBody
    @GetMapping("{userId}/interest")
    public BaseResponse<List<GetUserInterestRes>> getUserInterests(@PathVariable("userId")long userId){
        try {
            List<GetUserInterestRes> getUserInterestRes = userProvider.retrieveUserInterest(userId);
            return new BaseResponse<>(getUserInterestRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/point")
    public BaseResponse<List<GetPointHistoryRes>> getPointHistory(@RequestParam long userId) throws BaseException {
        try {
            List<GetPointHistoryRes> getPointHistoryRes = userProvider.getPointHistory(userId);
            return new BaseResponse<>(getPointHistoryRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @DeleteMapping("/{userId}")
    public BaseResponse<DeleteUserRes> deleteUser(@PathVariable("userId")long userId) {
        try{
            DeleteUserReq deleteUserReq = new DeleteUserReq(userId);
            DeleteUserRes deleteUsersRes = userService.deleteUser(deleteUserReq);
            return new BaseResponse<>(deleteUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
