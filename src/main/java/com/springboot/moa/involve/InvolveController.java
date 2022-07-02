package com.springboot.moa.involve;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.involve.model.GetInvolvesRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/involves")
public class InvolveController {
    @Autowired
    private final InvolveProvider involveProvider;

    public InvolveController(InvolveProvider involveProvider) {
        this.involveProvider = involveProvider;
    }

    // 답변할 내용 확인
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetInvolvesRes>> getInvolves(@RequestParam int postId) {
        try {
            List<GetInvolvesRes> getInvolvesRes = involveProvider.retrieveInvolve(postId);
            return new BaseResponse<>(getInvolvesRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }





}
