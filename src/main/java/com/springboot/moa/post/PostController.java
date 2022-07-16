package com.springboot.moa.post;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private final PostProvider postProvider;

    @Autowired
    private final PostService postService;

    @Autowired
    private final UserController userController;

    public PostController(PostProvider postProvider, PostService postService, UserController userController) {
        this.postProvider = postProvider;
        this.postService = postService;
        this.userController = userController;

    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetPostsRes>> getPosts(@RequestParam int categoryId) {
        try {
            List<GetPostsRes> getPostsRes = postProvider.retrievePosts(categoryId);
            return new BaseResponse<>(getPostsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{postId}")
    public BaseResponse<List<GetPostDetailRes>> getPostDetail(@PathVariable("postId") int postId) {
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

            if (postPostsReq.getPoint() < 0)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_POINT);

            if (postPostsReq.getDeadline() < 0)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_DEADLINE);

//            if(getUserInfoRes.getPoint()-100<0)
//                return new BaseResponse<>(BaseResponseStatus.UPDATE_FAILED_USER_POINT);
//
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

    @ResponseBody
    @GetMapping("/content/{postId}")
    public BaseResponse<List<GetPostContentRes>> getPostContent(@PathVariable("postId") int postId) {
        try {
            List<GetPostContentRes> getPostContentRes = postProvider.retrievePostContent(postId);
            return new BaseResponse<>(getPostContentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
