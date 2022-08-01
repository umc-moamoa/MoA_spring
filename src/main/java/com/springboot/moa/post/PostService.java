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


    public PostPostsRes createPosts(int userId, PostPostsReq postPostsReq) throws BaseException {
        try {
            int postId = postDao.insertPosts(userId, postPostsReq);

            for (int i = 0; i < postPostsReq.getPostDetails().size(); i++) {
                PostDetailsReq postDetailsReq = postPostsReq.getPostDetails().get(i);
                int postDetailId = postDao.insertPostDetails(postId, postDetailsReq);
                for (int j = 0; j < postDetailsReq.getPostFormat().size(); j++) {
                    PostFormatReq postFormatReq = postDetailsReq.getPostFormat().get(j);
                    postDao.insertPostFormats(postDetailId, postFormatReq);
                }
                if(i == postPostsReq.getPostDetails().size()-1) {
                    postDao.setPostPoints(postId);
                    int subAmount = postDao.usePoints(postId);
                    if (userProvider .retrieveUser(userId).getPoint() - subAmount < 0)
                        throw new BaseException(POSTS_FAILED_UPLOAD);
                    userService.addPointHistory(userId, 0, subAmount);
                }
            }
            return new PostPostsRes(postId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostInterestRes insertInterests(int postId, int userId) throws BaseException {
        try {
            int interestId = postDao.insertInterest(postId, userId);
            return new PostInterestRes(interestId);
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
