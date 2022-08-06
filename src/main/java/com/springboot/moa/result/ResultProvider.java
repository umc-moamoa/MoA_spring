package com.springboot.moa.result;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.result.model.GetResultStatisticsRes;
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

    public List<GetResultStatisticsRes> retrieveResultStatistics(long postDetailId) throws BaseException{
        if (checkAnswerExist(postDetailId) == 0) {
            throw new BaseException(POST_EMPTY_POST_DETAIL_ID);
        }
        try {
            List<GetResultStatisticsRes> getResultStatisticsRes = resultDao.selectResult(postDetailId);
            return getResultStatisticsRes;
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
}

