package com.springboot.moa.result;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponseStatus;
import com.springboot.moa.result.model.PostDetailResultReq;
import com.springboot.moa.result.model.PostResultReq;
import com.springboot.moa.result.model.PostResultRes;
import com.springboot.moa.user.UserProvider;
import com.springboot.moa.user.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import static com.springboot.moa.config.BaseResponseStatus.*;

@Service
public class ResultService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ResultDao resultDao;
    private final ResultProvider resultProvider;


    @Autowired
    public ResultService(ResultDao resultDao, ResultProvider resultProvider) {
        this.resultDao = resultDao;
        this.resultProvider = resultProvider;
    }

    public PostResultRes createResults(PostResultReq postResultReq) throws BaseException {
        // 중복 방지
        if(resultProvider.checkDuplicatedResult(postResultReq.getPostId(), postResultReq.getUserId()) == 1) {
            throw new BaseException(DUPLICATED_RESULT);
        }
        // 존재하지 않는 설문 답변 방지
        if(resultDao.checkPostPostId(postResultReq.getPostId()) == 0) {
            throw new BaseException(POSTS_EMPTY_POST_ID);
        }
        try {
            long resultId = resultDao.insertResults(postResultReq);
            for (int i = 0; i < postResultReq.getPostDetailResults().size(); i++) {
                String[] postResultsReqs = postResultReq.getPostDetailResults().get(i);
                int postDetailId = Integer.parseInt(postResultsReqs[0]);
                if(postResultsReqs[0].length() > 45) {
                    throw new BaseException(POST_INPUT_FAILED_CONTENTS);
                }
                String result = postResultsReqs[1];
                resultDao.insertResultDetails(resultId, postDetailId, result);
            }
            int point = resultDao.selectPostPoint(postResultReq.getPostId());
            return new PostResultRes(resultId,point);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }



}