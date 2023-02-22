package com.springboot.moa.user;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.user.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.springboot.moa.config.BaseResponseStatus.*;

@Service
public class UserProvider {
    private final UserDao userDao;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao) {
        this.userDao = userDao;
    }

    public GetUserInfoRes retrieveUser(long userId) throws BaseException {
        try{
            GetUserInfoRes getUserInfoRes = userDao.selectUser(userId);
            return getUserInfoRes;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserPostRes> retrieveUserPosts(long userId) throws BaseException{
        try{
            List<GetUserPostRes> getUserPosts = userDao.selectUserPosts(userId);

            return getUserPosts;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserPartPostRes> retrieveUserPartPosts(long userId) throws BaseException{
        try{
            List<GetUserPartPostRes> getUserPartPosts = userDao.selectUserPartPosts(userId);

            return getUserPartPosts;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserInterestRes> retrieveUserInterest(long userId) throws BaseException {
        try {
            List<GetUserInterestRes> getUserInterestRes = userDao.selectUserInterest(userId);
            return getUserInterestRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetPointHistoryRes getPointHistoryRecent(long userId) throws BaseException {
        try {
            GetPointHistoryRes getPointHistoryRes = new GetPointHistoryRes();
            getPointHistoryRes.setPoint(userDao.selectUser(userId).getPoint());

            List<GetPointHistoryRecentRes> getPointHistoryRecentRes = userDao.selectPointHistoryRecent(userId);
            getPointHistoryRes.setPointHistoryRecent(getPointHistoryRecentRes);

            getPointHistoryRes.setPointHistoryFormer(null);
            return getPointHistoryRes;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetPointHistoryRes getPointHistoryFormer(long userId) throws BaseException {
        try {
            GetPointHistoryRes getPointHistoryRes = new GetPointHistoryRes();
            getPointHistoryRes.setPoint(userDao.selectUser(userId).getPoint());

            List<GetPointHistoryFormerRes> getPointHistoryFormerRes = userDao.selectPointHistoryFormer(userId);
            getPointHistoryRes.setPointHistoryFormer(getPointHistoryFormerRes);

            getPointHistoryRes.setPointHistoryRecent(null);
            return getPointHistoryRes;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkIdExist(String id) throws BaseException{
        try {
            return userDao.checkUserIdExist(id);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkIdEmailExist(String id, String email) throws BaseException{
        try {
            return userDao.checkUserIdEmailExist(id, email);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkNickExist(String nick) throws BaseException{
        try {
            return userDao.checkUserNickExist(nick);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String checkSocialType(long userId) throws BaseException{
        try {
            return userDao.checkSocialType(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getSocialAccessToken(long userId) throws BaseException{
        try {
            return userDao.getSocialAccessToken(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserAnswersRes> retrieveUserAnswer(long userIdByJwt, long postId) throws BaseException {
        try {
            List<GetUserResultFormatRes> getUserResultFormatRes = userDao.selectUserAnswer(userIdByJwt, postId);
            List<GetUserAnswersRes> getUserAnswersRes = new ArrayList<>();

            for (int i = 0; i < getUserResultFormatRes.size(); i++) {
                GetUserAnswersRes userAnswerRes = new GetUserAnswersRes();
                GetUserResultFormatRes userResultRes = getUserResultFormatRes.get(i);

                int format = userResultRes.getFormat();
                int postDetailId = userResultRes.getPostDetailId();

                List<String> answerReuslt = userDao.selectUserResult(userIdByJwt, postDetailId);
                String[] result = new String[answerReuslt.size()];
                String[] result2 = new String[answerReuslt.size()];

                for (int j = 0; j < answerReuslt.size(); j++) {
                    result[j] = answerReuslt.get(j);
                }

                if(format == 1 || format == 2) {
                    for (int j = 0; j < answerReuslt.size(); j++) {
                        int row = Integer.parseInt(answerReuslt.get(j)) - 1;
                        result2[j] = userDao.selectQuestionItem(postDetailId, row);
                    }
                }
                userAnswerRes.setFormat(format);
                userAnswerRes.setResult(result);
                userAnswerRes.setResult2(result2);

                getUserAnswersRes.add(userAnswerRes);

            }
            return getUserAnswersRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserAnswerPostIdRes> retrieveUserAnswerList(long userIdByJwt) throws BaseException{
        try {
            List<GetUserAnswerPostIdRes> getPostId = userDao.selectGetPostId(userIdByJwt);
            return getPostId;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
