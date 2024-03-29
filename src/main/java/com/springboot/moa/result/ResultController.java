package com.springboot.moa.result;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.config.BaseResponseStatus;
import com.springboot.moa.result.model.*;
import com.springboot.moa.user.UserService;
import com.springboot.moa.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/results")
public class ResultController {

    @Autowired
    private final ResultProvider resultProvider;

    @Autowired
    private final ResultService resultService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final JwtService jwtService;

    public ResultController(ResultProvider resultProvider, ResultService resultService, UserService userService, UserService userService1, JwtService jwtService) {
        this.resultProvider = resultProvider;
        this.resultService = resultService;
        this.userService = userService1;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostResultRes> createResults(@RequestBody PostResultReq postResultReq) {

        // 답변 내용이 없을 때
        if (postResultReq.getPostDetailResults() == null) {
            return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_CONTENTS);
        }
        try {
            long userIdByJwt = jwtService.getUserId();
            postResultReq.setUserId(userIdByJwt);

            PostResultRes postResultRes = resultService.createResults(postResultReq);
            int point = postResultRes.getPoint();
            userService.addPointHistory(postResultReq.getUserId(), point, 0);

            return new BaseResponse<>(postResultRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    // localhost:9000/results/1
    @ResponseBody
    @GetMapping("/{postDetailId}")
    public BaseResponse<GetResultRes> getResultStatistics(@PathVariable("postDetailId") long postDetailId) throws BaseException {
        try {
            GetResultRes getResultRes = resultProvider.retrieveResultStatistics(postDetailId);
            return new BaseResponse<>(getResultRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/count/{postId}")
    public BaseResponse<GetResultNumberRes> getResultNumber (@PathVariable("postId") long postId) throws BaseException {
        try {
            GetResultNumberRes getResultNumberRes = resultProvider.countResultByPostId(postId);
            return new BaseResponse<>(getResultNumberRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/repeat/{postId}")
    public BaseResponse<GetResultPostDetailIdRes> getResultPostDetailId (@PathVariable("postId") long postId) throws BaseException {
        try {
            GetResultPostDetailIdRes getResultPostDetailIdRes = resultProvider.getResultPostDetailId(postId);
            return new BaseResponse<>(getResultPostDetailIdRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}