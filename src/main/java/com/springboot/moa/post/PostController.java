package com.springboot.moa.post;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.config.BaseResponseStatus;
import com.springboot.moa.post.model.*;
import com.springboot.moa.user.UserController;
import com.springboot.moa.user.UserProvider;
import com.springboot.moa.user.UserService;
import com.springboot.moa.user.model.GetUserInfoRes;
import com.springboot.moa.user.model.PostPointsReq;
import com.springboot.moa.user.model.PostPointsRes;
import com.springboot.moa.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.basic.BasicEditorPaneUI;
import java.util.List;

import static com.springboot.moa.config.BaseResponseStatus.INVALID_USER_JWT;
import static com.springboot.moa.config.BaseResponseStatus.POSTS_EMPTY_POST_ID;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private final PostProvider postProvider;

    @Autowired
    private final PostService postService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final JwtService jwtService;

    public PostController(PostProvider postProvider, PostService postService, UserService userService, UserProvider userProvider, JwtService jwtService) {
        this.postProvider = postProvider;
        this.postService = postService;
        this.userService = userService;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

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
            long userIdByJwt = jwtService.getUserId();
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
//            if(userProvider.retrieveUser(postPostsReq.getUserId()).getPoint() - postPostsReq.getSubAmount() < 0)
//                return new BaseResponse<>(BaseResponseStatus.POSTS_FAILED_UPLOAD);

            if (postPostsReq.getTitle().length() > 30)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_TITLE);

            if (postPostsReq.getContent().length() > 500)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_CONTENTS);

            PostPostsRes postPostsRes = postService.createPosts(postPostsReq.getUserId(), postPostsReq);
            userService.addPointHistory(postPostsReq.getUserId(), 0, postPostsReq.getSubAmount());
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

    // localhost:9000/posts/1
    @ResponseBody
    @PostMapping("/interest/{postId}")
    public BaseResponse<PostInterestRes> postInterests(@PathVariable("postId") long postId) {
        try {
            long userIdByJwt = jwtService.getUserId();
            long postInterestRes = postProvider.retrieveDuplicateInterest(postId, userIdByJwt);
            return new BaseResponse(postInterestRes);
        } catch (BaseException exception) {
            return new BaseResponse(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/content/{postId}")
    public BaseResponse<GetPostContentRes> getPostContent(@PathVariable("postId") long postId) {
        try {
            GetPostContentRes getPostContentRes = postProvider.retrievePostContent(postId);
            if(getPostContentRes == null)
                return new BaseResponse<>(POSTS_EMPTY_POST_ID);
            long userIdByJwt = jwtService.getUserId();
            if(getPostContentRes.getPostUserId() == userIdByJwt) {
                getPostContentRes.setMyPost(true);
            } else{
                boolean isLike = postProvider.checkUserLikePost(postId, userIdByJwt);
                getPostContentRes.setLike(isLike);
            }
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