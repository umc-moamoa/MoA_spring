package com.springboot.moa.post;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.post.model.*;
import com.springboot.moa.result.model.PostDetailResultReq;
import com.sun.source.tree.CatchTree;
import com.springboot.moa.user.UserProvider;
import com.springboot.moa.user.UserService;
import com.springboot.moa.user.model.PostPointsReq;
import com.springboot.moa.user.model.PostPointsRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.catalog.Catalog;

import java.security.DrbgParameters;

import static com.springboot.moa.config.BaseResponseStatus.*;

@Service
public class PostService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostDao postDao;
    private final PostProvider postProvider;
    private final UserProvider userProvider;
    private final UserService userService;

    @Autowired
    public PostService(PostDao postDao, PostProvider postProvider, UserProvider userProvider, UserService userService) {
        this.postDao = postDao;
        this.postProvider = postProvider;
        this.userProvider = userProvider;
        this.userService = userService;
    }


    public PostPostsRes createPosts(long userId, PostPostsReq postPostsReq) throws BaseException {
        try {
            long postId = postDao.insertPosts(userId, postPostsReq);

            for (int i = 0; i < postPostsReq.getPostDetails().size(); i++) {
                PostDetailsReq postDetailsReq = postPostsReq.getPostDetails().get(i);
                long postDetailId = postDao.insertPostDetails(postId, postDetailsReq);
                for (int j = 0; j < postDetailsReq.getPostFormat().size(); j++) {
                    PostFormatReq postFormatReq = postDetailsReq.getPostFormat().get(j);
                    postDao.insertPostFormats(postDetailId, postFormatReq);
                }
            }
            return new PostPostsRes(postId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostInterestRes insertInterests(long postId, long userId) throws BaseException {
        try {
            long interestId = postDao.insertInterest(postId, userId);
            return new PostInterestRes(interestId);
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public long retrieveDuplicateInterest(long postId, long userId) throws BaseException{
        if (postProvider.checkPostExist(postId) == 0)
            throw new BaseException(POSTS_EMPTY_POST_ID);

        if(postProvider.checkDuplicateInterest(postId, userId) == 1)
            throw new BaseException(DUPLICATED_INTEREST);
        try {
            long postInterestRes = postDao.insertInterest(postId, userId);
            return postInterestRes;
        }catch (Exception exception)
        {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deletePost(long postIdx) throws BaseException {
        try{
            int result = postDao.deletePost(postIdx);
            if(result == 0)
                throw new BaseException(DELETE_FAIL_POST);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyContent(long userId, long postId, PatchPostsReq patchPostsReq) throws BaseException {
        if (postProvider.checkUserExist(userId, postId) == 0) {
            throw new BaseException(USERS_FAILED_POST_ID);
        }
        if (postProvider.checkPostExist(postId) == 0) {
            throw new BaseException(POSTS_EMPTY_POST_ID);
        }
        if(postProvider.checkStatus(postId) == 0) {
            throw new BaseException(POST_STATUS_INACTIVE);
        }
        try {
            int result = postDao.updateContent(postId, patchPostsReq.getContent());
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_POST);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteInterest(long userId, long postId) throws BaseException{
        // postId가 존재하지 않는 경우
        if(postProvider.checkPostIdExist(postId) == 0) {
            throw new BaseException(POSTS_EMPTY_POST_ID);
        }
        // postId와 userId의 쌍이 존재하지 않는 경우
        if(postProvider.checkDuplicateInterest(postId, userId) == 0) {
            throw new BaseException(EMPTY_INTEREST);
        }
        try {
            postDao.deleteInterest(userId, postId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}