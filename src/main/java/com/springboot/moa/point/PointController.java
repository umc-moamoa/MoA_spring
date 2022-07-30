package com.springboot.moa.point;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.config.BaseResponseStatus;
import com.springboot.moa.user.UserProvider;
import com.springboot.moa.user.UserService;
import com.springboot.moa.user.model.*;
import com.springboot.moa.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.springboot.moa.config.BaseResponseStatus.POINT_HISTORY_INPUT_FAILED;


@RestController
@RequestMapping("/point")
public class PointController {
    @Autowired
    private final PointProvider pointProvider;
    @Autowired
    private final PointService pointService;
    @Autowired
    private final JwtService jwtService;

    public PointController(PointProvider pointProvider, PointService pointService, JwtService jwtService){
        this.pointProvider = pointProvider;
        this.pointService = pointService;
        this.jwtService = jwtService;
    }


//    @ResponseBody
//    @GetMapping("/{userId}")
//    public BaseResponse<List<GetUserPostRes>> getPointHistory(@PathVariable("userId")int userId) {
//        try{
//            List<GetUserPostRes> getUserPostRes = pointProvider.retrieveUserPosts(userId);
//            return new BaseResponse<>(getUserPostRes);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
//
    @ResponseBody
    @PostMapping("/point")
    public BaseResponse<PostPointsRes> addPointHistory(@RequestBody PostPointsReq postPointsReq) throws BaseException {
        if((postPointsReq.getAddAmount()==0 & postPointsReq.getSubAmount()==0)|(postPointsReq.getAddAmount()!=0&postPointsReq.getSubAmount()!=0)){
            return new BaseResponse<>(POINT_HISTORY_INPUT_FAILED);
        }
        try {
            PostPointsRes postPointsRes = pointService.addPointHistory(postPointsReq);
            return new BaseResponse<>(postPointsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
