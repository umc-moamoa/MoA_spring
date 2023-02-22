package com.springboot.moa.post;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.config.BaseResponseStatus;
import com.springboot.moa.post.model.*;
import com.springboot.moa.user.UserController;
import com.springboot.moa.user.UserProvider;
import com.springboot.moa.user.UserService;
import com.springboot.moa.user.model.*;
import com.springboot.moa.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.basic.BasicEditorPaneUI;
import java.util.List;

import static com.springboot.moa.config.BaseResponseStatus.*;

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
            String postStatus = postProvider.checkPostStatus(postId);

            if (postStatus.equals("INACTIVE"))
                return new BaseResponse<>(BaseResponseStatus.FAILED_INACTIVE_POST);

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
            long userIdByJwt = jwtService.getUserId();

            postPostsReq.setUserId(userIdByJwt);
            int subAmount = postPostsReq.getShortCount() * 3 + postPostsReq.getLongCount() * 5;

            if (userProvider.retrieveUser(postPostsReq.getUserId()).getPoint() - subAmount < 0)
                return new BaseResponse<>(BaseResponseStatus.POSTS_FAILED_UPLOAD);

            if (postPostsReq.getTitle().length() > 30)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_TITLE);

            if (postPostsReq.getContent().length() > 500)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_CONTENTS);

            PostPostsRes postPostsRes = postService.createPosts(postPostsReq.getUserId(), postPostsReq);
            userService.addPointHistory(postPostsReq.getUserId(), 0, subAmount);
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
            if (getPostContentRes == null)
                return new BaseResponse<>(POSTS_EMPTY_POST_ID);
            long userIdByJwt = jwtService.getUserId();
            if (getPostContentRes.getPostUserId() == userIdByJwt) {
                getPostContentRes.setMyPost(true);
                getPostContentRes.setParticipation(true);
            } else {
                if (postProvider.checkDuplicatedResult(postId, userIdByJwt) == 1) {
                    getPostContentRes.setParticipation(true);
                }
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
    public BaseResponse<String> deletePost(@PathVariable("postId") long postId) {
        try {
            long userIdByJwt = jwtService.getUserId();
            long postUserId = postProvider.retrieveUserId(postId);
            if (userIdByJwt != postUserId) {
                return new BaseResponse<>(USERS_FAILED_POST_ID);
            }
            postService.deletePost(postId);
            String result = "삭제를 성공했습니다.";
            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("")
    public BaseResponse<String> modifyPost(@RequestBody PatchPostsReq patchPostsReq) {
        try {
            long userIdByJwt = jwtService.getUserId();
            if (userIdByJwt != patchPostsReq.getPostUserId())
                return new BaseResponse<>(USERS_FAILED_POST_ID);
            if (patchPostsReq.getTitle().length() > 30)
                return new BaseResponse<>(BaseResponseStatus.POST_INPUT_FAILED_TITLE);
            if (patchPostsReq.getContent().length() > 450)
                return new BaseResponse<>(POST_INPUT_FAILED_CONTENTS);
            postService.modifyContent(patchPostsReq);
            String result = "게시물 정보 수정을 완료하였습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @DeleteMapping("/interest/{postId}")
    public BaseResponse<String> deleteUserInterest(@PathVariable("postId") long postId) {
        try {
            long userIdByJwt = jwtService.getUserId();
            DeleteInterestReq deleteInterestReq = new DeleteInterestReq(userIdByJwt);
            postService.deleteInterest(deleteInterestReq.getUserId(), postId);
            String result = "관심있는 설문을 삭제했습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/content/{postId}/share")
    public BaseResponse<GetPostUrlShareRes> getPostURL(@PathVariable("postId") long postId) {
        GetPostUrlShareRes getPostUrlShareRes = new GetPostUrlShareRes();
        String url = "http://seolmunzip.shop/posts/content/" + postId;
        getPostUrlShareRes.setUrl(url);
        return new BaseResponse<>(getPostUrlShareRes);
    }
}