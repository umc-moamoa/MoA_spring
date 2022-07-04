package com.springboot.moa.user;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.user.model.GetUserInfoRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.springboot.moa.config.BaseResponseStatus.*;

@Service
public class UserProvider {
    private final UserDao userDao;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<GetUserInfoRes> retrieveUser(int userId) throws BaseException {
        if(checkUserExist(userId) == 0)
            throw new BaseException(USERS_EMPTY_USER_ID);
        try{
            List<GetUserInfoRes> getUserInfoRes = userDao.selectUser(userId);
            return getUserInfoRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkUserExist(int userId) throws BaseException{
        try{
            return userDao.checkUserExist(userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
