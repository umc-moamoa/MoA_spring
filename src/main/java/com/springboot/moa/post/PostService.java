package com.springboot.moa.post;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.post.model.PostDetailsReq;
import com.springboot.moa.post.model.PostPostsReq;
import com.springboot.moa.post.model.PostPostsRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.springboot.moa.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class PostService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostDao postDao;
    private final PostProvider postProvider;

    @Autowired
    public PostService(PostDao postDao, PostProvider postProvider) {
        this.postDao = postDao;
        this.postProvider = postProvider;
    }


    public PostPostsRes createPosts(int userIdx, PostPostsReq postPostsReq) throws BaseException {
        try{
            int postIdx = postDao.insertPosts(userIdx, postPostsReq);
            for(int i = 0; i < postPostsReq.getPostDetails().size(); i++) {
                PostDetailsReq postDetailsReq = postPostsReq.getPostDetails().get(i);
                int postDetailId = postDao.insertPostDetails(postIdx, postDetailsReq);
                for(int j = 0; j < postDetailsReq.getPostFormat().size(); j++){
                    postDao.insertPostFormats(postDetailId, postDetailsReq.getPostFormat().get(j));
                }
            }
            return new PostPostsRes(postIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
