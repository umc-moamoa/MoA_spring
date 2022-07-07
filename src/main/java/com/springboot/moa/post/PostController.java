package com.springboot.moa.post;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.config.BaseResponseStatus;
import com.springboot.moa.post.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private final PostProvider postProvider;

    @Autowired
    private final PostService postService;

    public PostController(PostProvider postProvider, PostService postService){
        this.postProvider = postProvider;
        this.postService = postService;
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetPostsRes>> getPosts(@RequestParam int categoryId) {
        try{
            List<GetPostsRes> getPostsRes = postProvider.retrievePosts(categoryId);
            return new BaseResponse<>(getPostsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{postId}")
    public BaseResponse<List<GetPostDetailRes>> getPostDetail(@PathVariable("postId")int postId) {
        try {
            List<GetPostDetailRes> getPostDetailRes = postProvider.retrievePostDetail(postId);
            return new BaseResponse<>(getPostDetailRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostPostsRes> createPosts(@RequestBody PostPostsReq postPostsReq) {
        try{
            if(postPostsReq.getTitle().length() > 30)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_TITLE);

            if(postPostsReq.getContent().length() > 500)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_CONTENTS);

            if(postPostsReq.getPoint() < 0)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_POINT);

            if(postPostsReq.getDeadline()<0)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILD_DEADLINE);
            PostPostsRes postPostsRes = postService.createPosts(postPostsReq.getUserId(), postPostsReq);
            return new BaseResponse<>(postPostsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/asc")
    public BaseResponse<List<GetParticipantsRes>> getParticipantsAsc() {
        try {
            List<GetParticipantsRes> getParticipantsRes = postProvider.retrieveParticipantAsc();
            return new BaseResponse<>(getParticipantsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/desc")
    public BaseResponse<List<GetParticipantsRes>> getParticipantsDesc() {
        try {
            List<GetParticipantsRes> getParticipantsRes = postProvider.retrieveParticipantDesc();
            return new BaseResponse<>(getParticipantsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }



}
