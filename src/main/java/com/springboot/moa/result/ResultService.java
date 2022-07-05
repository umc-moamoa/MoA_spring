package com.springboot.moa.result;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponseStatus;
import com.springboot.moa.result.model.PostDetailResultReq;
import com.springboot.moa.result.model.PostResultReq;
import com.springboot.moa.result.model.PostResultRes;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

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

    public PostResultRes createResults(int postId, PostResultReq postResultReq) throws BaseException {
        try {
            int resultId = resultDao.insertResults(postId, postResultReq);
            for (int i = 0; i < postResultReq.getPostDetailResults().size(); i++) {
                PostDetailResultReq postDetailResultReq = postResultReq.getPostDetailResults().get(i);
                resultDao.insertResultDetails(resultId, postDetailResultReq);
            }
            return new PostResultRes(resultId);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

}
