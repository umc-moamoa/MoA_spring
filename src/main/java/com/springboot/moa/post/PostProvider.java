package com.springboot.moa.post;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.post.model.GetParticipantsRes;
import com.springboot.moa.post.model.GetPostDetailRes;
import com.springboot.moa.post.model.GetPostsRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.springboot.moa.config.BaseResponseStatus.*;

@Service
public class PostProvider {
    private final PostDao postDao;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PostProvider(PostDao postDao) {
        this.postDao = postDao;
    }

    public List<GetPostsRes> retrievePosts(int categoryId) throws BaseException {
        if(checkCategoryExist(categoryId) == 0)
            throw new BaseException(POSTS_EMPTY_CATEGORY_ID);
        try{

            List<GetPostsRes> getPostsRes = postDao.selectPosts(categoryId);

            return getPostsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkCategoryExist(int categoryId) throws BaseException{
        try{
            return postDao.checkCategoryExist(categoryId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPostDetailRes> retrievePostDetail(int postId) throws BaseException {
        if (checkPostExist(postId) == 0)
            throw new BaseException(POSTS_EMPTY_POST_ID);
        try {
            List<GetPostDetailRes> getPostDetailRes = postDao.selectPostDetail(postId);
            return getPostDetailRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }
    public int checkPostExist(int postId) throws BaseException{
        try {
            return postDao.checkPostDetailExist(postId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetParticipantsRes> retrieveParticipantAsc() throws BaseException {
        try {
            List<GetParticipantsRes> getParticipantsRes = postDao.selectParticipantsAsc();
            return getParticipantsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetParticipantsRes> retrieveParticipantDesc() throws BaseException {
        try {
            List<GetParticipantsRes> getParticipantsRes = postDao.selectParticipantsDesc();
            return getParticipantsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
