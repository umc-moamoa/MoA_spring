package com.springboot.moa.post;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.post.model.GetPostDetailRes;
import com.springboot.moa.post.model.GetPostsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private final PostProvider postProvider;

    public PostController(PostProvider postProvider){
        this.postProvider = postProvider;
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
}
