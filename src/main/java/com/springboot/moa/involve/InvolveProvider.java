package com.springboot.moa.involve;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.involve.model.GetInvolvesRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.springboot.moa.config.BaseResponseStatus.DATABASE_ERROR;
import static com.springboot.moa.config.BaseResponseStatus.POSTS_EMPTY_POST_ID;

@Service
public class InvolveProvider {
    private final InvolveDao involveDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public InvolveProvider(InvolveDao involveDao) {
        this.involveDao = involveDao;
    }

    public List<GetInvolvesRes> retrieveInvolve(int postId) throws BaseException {
        if (checkPostIdExist(postId) == 0)
            throw new BaseException(POSTS_EMPTY_POST_ID);
        try {
            List<GetInvolvesRes> getInvolvesRes = involveDao.selectInvolve(postId);
            return getInvolvesRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }
    public int checkPostIdExist(int postId) throws BaseException{
        try {
            return involveDao.checkInvolveExist(postId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
