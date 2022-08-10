package com.springboot.moa.user;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.post.PostDao;
import com.springboot.moa.post.PostProvider;
import com.springboot.moa.user.model.*;
import com.springboot.moa.utils.JwtService;
import com.springboot.moa.utils.SHA256;
import io.jsonwebtoken.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import static com.springboot.moa.config.BaseResponseStatus.*;

@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }

    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getPwd());
            postUserReq.setPwd(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            long userId = userDao.createUser(postUserReq);
            userDao.addPointHistory(userId,0,0);
            String jwt = jwtService.createJwt(userId);
            return new PostUserRes(jwt, userId);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostPointsRes addPointHistory(long userId, int addAmount, int subAmount) throws BaseException {
        try {
            long pointId = userDao.addPointHistory(userId,addAmount, subAmount);
            userDao.updateUserPoint(userId,addAmount, subAmount);
            return new PostPointsRes(pointId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public DeleteUserRes deleteUser(DeleteUserReq deleteUserReq) throws BaseException{
        try{
            DeleteUserRes deleteUsersRes = userDao.deleteUser(deleteUserReq);
            return deleteUsersRes;
        }
        catch(Exception exception){
            throw new BaseException(USERS_FAILED_USER_ID);
        }
    }
}
