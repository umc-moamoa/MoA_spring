package com.springboot.moa.post;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.post.model.PostDetailsReq;
import com.springboot.moa.post.model.PostPostsReq;
import com.springboot.moa.post.model.PostPostsRes;
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
        if (userProvider.retrieveUser(userId).getPoint() - 100 < 0)
            throw new BaseException(UPDATE_FAILED_USER_POINT);
        try {
//            PostPointsReq postPointsReq = new PostPointsReq(postPostsReq.getUserId(), 0, 100);
//            userService.addPointHistory(postPointsReq);
            int postId = postDao.insertPosts(userId, postPostsReq);
            for (int i = 0; i < postPostsReq.getPostDetails().size(); i++) {
                PostDetailsReq postDetailsReq = postPostsReq.getPostDetails().get(i);
                int postDetailId = postDao.insertPostDetails(postId, postDetailsReq);

                for (int j = 0; j < postDetailsReq.getPostFormat().size(); j++) {
                    postDao.insertPostFormats(postDetailId, postDetailsReq.getPostFormat().get(j));
                }
            }
            return new PostPostsRes(postId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
