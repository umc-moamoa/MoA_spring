package com.springboot.moa.point;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.user.UserDao;
import com.springboot.moa.user.model.GetUserInfoRes;
import com.springboot.moa.user.model.GetUserInterestRes;
import com.springboot.moa.user.model.GetUserPartPostRes;
import com.springboot.moa.user.model.GetUserPostRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.springboot.moa.config.BaseResponseStatus.DATABASE_ERROR;
import static com.springboot.moa.config.BaseResponseStatus.USERS_EMPTY_USER_ID;

@Service
public class PointProvider {
    private final PointDao pointDao;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PointProvider(PointDao pointDao) {
        this.pointDao = pointDao;
    }

    public GetUserInfoRes retrieveUser(int userId) throws BaseException {
        if(checkUserExist(userId) == 0)
            throw new BaseException(USERS_EMPTY_USER_ID);
        try{
            GetUserInfoRes getUserInfoRes = pointDao.selectUser(userId);
            return getUserInfoRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkUserExist(int userId) throws BaseException{
        try{
            return pointDao.checkUserExist(userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
