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

@Service
public class ResultService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ResultDao resultDao;
    private final ResultProvider resultProvider;

    private final UserService userService;

    @Autowired
    public ResultService(ResultDao resultDao, ResultProvider resultProvider, UserService userService) {
        this.resultDao = resultDao;
        this.resultProvider = resultProvider;
        this.userService = userService;
    }

    public PostResultRes createResults(long postId, PostResultReq postResultReq) throws BaseException {
        try {
            long resultId = resultDao.insertResults(postId, postResultReq);
            for (int i = 0; i < postResultReq.getPostDetailResults().size(); i++) {
                PostDetailResultReq postDetailResultReq = postResultReq.getPostDetailResults().get(i);
                resultDao.insertResultDetails(resultId, postDetailResultReq);
            }

            int addAmount = resultDao.selectPostPoint(postId);
            System.out.println(addAmount);
            userService.addPointHistory(postResultReq.getUserId(),addAmount,0);

            return new PostResultRes(resultId);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

}
