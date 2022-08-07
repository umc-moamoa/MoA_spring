package com.springboot.moa.post;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.config.BaseResponseStatus;
import com.springboot.moa.post.model.*;
import com.springboot.moa.user.UserController;
import com.springboot.moa.user.UserService;
import com.springboot.moa.user.model.GetUserInfoRes;
import com.springboot.moa.user.model.PostPointsReq;
import com.springboot.moa.user.model.PostPointsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.basic.BasicEditorPaneUI;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private final PostProvider postProvider;

    @Autowired
    private final PostService postService;

    @Autowired
    private final UserService userService;

    public PostController(PostProvider postProvider, PostService postService, UserService userService) {
        this.postProvider = postProvider;
        this.postService = postService;
        this.userService = userService;

    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetPostsRes>> getPosts(@RequestParam long categoryId) {
        try {
            List<GetPostsRes> getPostsRes = postProvider.retrievePosts(categoryId);
            return new BaseResponse<>(getPostsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{postId}")
    public BaseResponse<List<GetPostDetailRes>> getPostDetail(@PathVariable("postId") long postId) {
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
        try {
            if (postPostsReq.getTitle().length() > 30)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_TITLE);

            if (postPostsReq.getContent().length() > 500)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_CONTENTS);

            if (postPostsReq.getDeadline() < 0)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_DEADLINE);

            PostPostsRes postPostsRes = postService.createPosts(postPostsReq.getUserId(), postPostsReq);
            return new BaseResponse<>(postPostsRes);

        } catch (BaseException exception) {
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

    // localhost:9000/posts/1/1
    @ResponseBody
    @PostMapping("/{postId}/{userId}")
    public BaseResponse<PostInterestRes> postInterests(@PathVariable("postId") long postId, @PathVariable("userId") long userId) {
        try {
            long postInterestRes = postProvider.retrieveDuplicateInterest(postId, userId);
            return new BaseResponse(postInterestRes);
        } catch (BaseException exception) {
            return new BaseResponse(exception.getStatus());
        }
    }
    @ResponseBody
    @GetMapping("/content/{postId}")
    public BaseResponse<List<GetPostContentRes>> getPostContent(@PathVariable("postId") long postId) {
        try {
            List<GetPostContentRes> getPostContentRes = postProvider.retrievePostContent(postId);
            return new BaseResponse<>(getPostContentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{postId}/status")
    public BaseResponse<String> deletePost(@PathVariable ("postId") long postId){
        try{
            postService.deletePost(postId);
            String result = "삭제를 성공했습니다.";
            return new BaseResponse<>(result);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{postId}")
    public BaseResponse<String> modifyPost(@PathVariable ("postId") long postId, @RequestBody PatchPostsReq patchPostsReq) {
        try{
            if(patchPostsReq.getContent().length() > 450)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_TITLE);
            postService.modifyContent(patchPostsReq.getUserId(), postId, patchPostsReq);
            String result = "게시물 정보 수정을 완료하였습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}