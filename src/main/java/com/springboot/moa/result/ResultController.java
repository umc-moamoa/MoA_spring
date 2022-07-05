package com.springboot.moa.result;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.config.BaseResponseStatus;
import com.springboot.moa.result.model.PostDetailResultReq;
import com.springboot.moa.result.model.PostDetailResultRes;
import com.springboot.moa.result.model.PostResultReq;
import com.springboot.moa.result.model.PostResultRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/results")
public class ResultController {

    @Autowired
    private final ResultProvider resultProvider;

    @Autowired
    private final ResultService resultService;

    public ResultController(ResultProvider resultProvider, ResultService resultService) {
        this.resultProvider = resultProvider;
        this.resultService = resultService;
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostResultRes> createResults(@RequestBody PostResultReq postResultReq) {

        // 답변 내용이 없을 때
        if(postResultReq.getPostDetailResults() == null){
            return new BaseResponse<>(BaseResponseStatus.POST_POSTS_INVALID_CONTENTS);
        }

        // for 문을 돌며 postResultReq에 있는 postDetailResult 객체를 받아옴
        // postDetailResult 객체 안의 result 변수의 값을 확인
        for (int i = 0; i < postResultReq.getPostDetailResults().size(); i++){
            PostDetailResultReq postDetailResultReq = postResultReq.getPostDetailResults().get(i);

            // 답변이 일정 길이를 초과했을 때
            if(postDetailResultReq.getResult().length()> 450){
                return new BaseResponse<>(BaseResponseStatus.POST_POSTS_INVALID_CONTENTS);
            }
            // 답변이 입력되지 않았을 때
            if(postDetailResultReq.getResult().length() < 1){
                return new BaseResponse<>(BaseResponseStatus.POST_POSTS_INVALID_CONTENTS);
            }
        }

        try {
            PostResultRes postResultRes = resultService.createResults(postResultReq.getPostId(), postResultReq);
            return new BaseResponse<>(postResultRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
