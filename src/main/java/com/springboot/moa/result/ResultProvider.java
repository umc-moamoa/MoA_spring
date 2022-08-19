package com.springboot.moa.result;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.result.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.springboot.moa.config.BaseResponseStatus.*;

@Service
public class ResultProvider {
    private final ResultDao resultDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    public ResultProvider(ResultDao resultDao) {
        this.resultDao = resultDao;
    }

    public GetResultRes retrieveResultStatistics(long postDetailId) throws BaseException{
        if (checkAnswerExist(postDetailId) == 0) {
            throw new BaseException(POST_EMPTY_POST_DETAIL_ID);
        }
        // 질문은 존재하나 답변이 존재하지 않는 경우
        if (countResult(postDetailId) == 0) {
            throw new BaseException(EMPTY_RESULT);
        }
        try {
            GetResultRes getResultRes = new GetResultRes();
            getResultRes.setFormat(resultDao.checkResultType(postDetailId));
            getResultRes.setQuestion(resultDao.selectQuestion(postDetailId));

            List<GetResultStatisticsRes> getResultStatisticsRes = resultDao.selectResult(postDetailId);
            getResultRes.setGetResultStatisticsRes(getResultStatisticsRes);

            if ((resultDao.checkResultType(postDetailId) == 1) || (resultDao.checkResultType(postDetailId) == 2)) {
                double [] countAnswer = new double[8];
                for (int i = 0; i < getResultStatisticsRes.size(); i++) {
                    String answer = getResultStatisticsRes.get(i).getResult();
                    switch (answer) {
                        case "1":
                            countAnswer[0] += 1;
                            break;
                        case "2":
                            countAnswer[1] += 1;
                            break;
                        case "3":
                            countAnswer[2] += 1;
                            break;
                        case "4":
                            countAnswer[3] += 1;
                            break;
                        case "5":
                            countAnswer[4] += 1;
                            break;
                        case "6":
                            countAnswer[5] += 1;
                            break;
                        case "7":
                            countAnswer[6] += 1;
                            break;
                        case "8":
                            countAnswer[7] += 1;
                            break;
                    }
                }
//                 변수에 저장된 결과 출력
                for (int i = 0; i < 5; i++){
                    countAnswer[i] = countAnswer[i] / getResultStatisticsRes.size() * 100;
                }
                // list 로 리펙토링 필요
                // 현재 setter 로 값 받아와서 for 문 못돌림
                getResultRes.setCase1(countAnswer[0]);
                getResultRes.setCase2(countAnswer[1]);
                getResultRes.setCase3(countAnswer[2]);
                getResultRes.setCase4(countAnswer[3]);
                getResultRes.setCase5(countAnswer[4]);
                getResultRes.setCase6(countAnswer[5]);
                getResultRes.setCase7(countAnswer[6]);
                getResultRes.setCase8(countAnswer[7]);

                List<GetResultItems> getResultItems = resultDao.selectItems(postDetailId);
                getResultRes.setGetResultItems(getResultItems);
            }
            return getResultRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public int checkAnswerExist(long postDetailId) throws BaseException{
        try {
            return resultDao.checkResultPostDetailId(postDetailId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkDuplicatedResult(long postId, long userId) throws BaseException {
        try {
            return resultDao.checkDuplicatedResult(postId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int countResult(long postDetailId) throws BaseException {
        try {
            return resultDao.countResult(postDetailId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetResultNumberRes countResultByPostId(long postId) throws BaseException {
        if(resultDao.checkPostPostId(postId) == 0) {
            throw new BaseException(POSTS_EMPTY_POST_ID);
        }
        if(resultDao.checkResultPostId(postId) == 0) {
            throw new BaseException(EMPTY_RESULT);
        }
        try {
            GetResultNumberRes getResultNumberRes = resultDao.countResultByPostId(postId);
            return getResultNumberRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetResultPostDetailIdRes getResultPostDetailId(long postId) throws BaseException {
        if(resultDao.checkPostPostId(postId) == 0) {
            throw new BaseException(POSTS_EMPTY_POST_ID);
        }
        if(resultDao.checkResultPostId(postId) == 0) {
            throw new BaseException(EMPTY_RESULT);
        }
        try {
            GetResultPostDetailIdRes getResultPostDetailIdRes = new GetResultPostDetailIdRes();
            int start = resultDao.startPostDetailId(postId);
            int count = resultDao.countPostDetail(postId);
            int end = start + count -1;
            getResultPostDetailIdRes.setStartPostDetailId(start);
            getResultPostDetailIdRes.setEndPostDetailId(end);
            return getResultPostDetailIdRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkResultType(long postDetailId) throws BaseException{
        return resultDao.checkResultType(postDetailId);
    }
}