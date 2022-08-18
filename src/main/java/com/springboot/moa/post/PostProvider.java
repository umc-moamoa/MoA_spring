package com.springboot.moa.post;

import com.springboot.moa.config.BaseException;

import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.post.model.*;
import com.springboot.moa.post.model.GetParticipantsRes;
import com.springboot.moa.post.model.GetPostContentRes;
import com.springboot.moa.post.model.GetPostDetailRes;
import com.springboot.moa.post.model.GetPostsRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

    public List<GetPostsRes> retrievePosts(long categoryId) throws BaseException {
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
    public int checkCategoryExist(long categoryId) throws BaseException{
        try{
            return postDao.checkCategoryExist(categoryId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPostDetailRes> retrievePostDetail(long postId) throws BaseException {
        if (checkPostExist(postId) == 0)
            throw new BaseException(POSTS_EMPTY_POST_ID);
        try {
            List<GetPostDetailRes> getPostDetailRes = new ArrayList<>();
            List<GetPostQuestionRes> getPostQuestionRes = postDao.selectPostDetail(postId);

            for (int i = 0; i < getPostQuestionRes.size(); i++) {
                GetPostDetailRes postDetailInfo = new GetPostDetailRes();
                GetPostQuestionRes questionInfo = getPostQuestionRes.get(i);

                postDetailInfo.setPostDetailId(questionInfo.getPostDetailId());
                postDetailInfo.setQuestion(questionInfo.getQuestion());
                postDetailInfo.setFormat(questionInfo.getFormat());

                long postDetailId = questionInfo.getPostDetailId();

                List<String> postItems = postDao.selectPostItems(postDetailId);
                String[] items = new String[postItems.size()];
                if(postItems.size() > 0) {
                    for (int j = 0; j < postItems.size(); j++) {
                        items[j] = postItems.get(j);
                    }
                }
                postDetailInfo.setItems(items);

                getPostDetailRes.add(postDetailInfo);
            }

            return getPostDetailRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPostExist(long postId) throws BaseException{
        try {
            return postDao.checkPostDetailExist(postId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPostIdExist(long postId) throws BaseException {
        try {
            return postDao.checkPostIdExist(postId);
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


    public long retrieveDuplicateInterest(long postId, long userId) throws BaseException{
        if (checkPostExist(postId) == 0)
            throw new BaseException(POSTS_EMPTY_CATEGORY_ID);
        if(checkUserIdExist(userId) == 0)
            throw new BaseException(USERS_EMPTY_USER_ID);
        if(checkDuplicateInterest(postId, userId) == 1)
            throw new BaseException(DUPLICATED_INTEREST);
        try {
            long postInterestRes = postDao.insertInterest(postId, userId);
            return postInterestRes;
        }catch (Exception exception)
        {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkDuplicateInterest(long postId, long userId) throws BaseException{
        try {
            return postDao.checkDuplicateInterest(postId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private int checkUserIdExist(long userId) throws BaseException{
        try {
            return postDao.checkUserIdExist(userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }


    public GetPostContentRes retrievePostContent(long postId) throws BaseException {
        if (checkPostExist(postId) == 0)
            throw new BaseException(POSTS_EMPTY_POST_ID);
        try {
            GetPostContentRes getPostContentRes = postDao.selectPostContent(postId);
            return getPostContentRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserExist(long userId, long postId) throws BaseException{
        try{
            return postDao.checkPostUserExist(userId, postId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkStatus(long postId) throws BaseException{
        try {
            String result = postDao.checkStatus(postId);
            if (result.equals("ACTIVE"))
                return 1;
            else
                return 0;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean checkUserLikePost(long postId, long userId) throws BaseException{
        try{
            return postDao.checkIsLiked(postId, userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public long retrieveUserId(long postId) throws BaseException{
        try{
            Long userId = postDao.getUserId(postId);
            if(userId == null)
                throw new BaseException(POSTS_EMPTY_POST_ID);
            return userId;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}