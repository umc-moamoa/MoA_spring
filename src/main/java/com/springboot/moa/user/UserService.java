package com.springboot.moa.user;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.user.model.*;
import com.springboot.moa.utils.JwtService;
import com.springboot.moa.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // 중복 처리
        if (userProvider.checkIdExist(postUserReq.getEmail()) == 1) {
            throw new BaseException(USERS_DUPLICATED_ID);
        }
        if (userProvider.checkNickExist(postUserReq.getNick()) == 1) {
            throw new BaseException(USERS_DUPLICATED_NICK);
        }
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getPwd());
            postUserReq.setPwd(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            long userId = userDao.createUser(postUserReq);
            userDao.addPointHistory(userId,20,0);
            String accessToken = jwtService.createAccessToken(userId);
            String refreshToken = jwtService.createRefreshToken(userId);
            userDao.addRefreshToken(refreshToken, userId);
            return new PostUserRes(accessToken, refreshToken, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostUserRes createKakaoUser(PostUserReq postUserReq) throws BaseException {
        String pwd;
        // 중복 처리
        if (userProvider.checkIdExist(postUserReq.getEmail()) == 1) {
            throw new BaseException(USERS_DUPLICATED_ID);
        }
        if (userProvider.checkNickExist(postUserReq.getNick()) == 1) {
            throw new BaseException(USERS_DUPLICATED_NICK);
        }
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getPwd());
            postUserReq.setPwd(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            long userId = userDao.createUser(postUserReq);
            userDao.addPointHistory(userId,20,0);
            String accessToken = jwtService.createAccessToken(userId);
            String refreshToken = jwtService.createRefreshToken(userId);
            userDao.addRefreshToken(refreshToken, userId);
            return new PostUserRes(accessToken, refreshToken, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostPointsRes addPointHistory(long userId, int addAmount, int subAmount) throws BaseException {
        try {
            long pointId = userDao.addPointHistory(userId,addAmount, subAmount);
            userDao.updateUserPoint(userId,addAmount, subAmount);
            return new PostPointsRes(pointId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public DeleteUserRes deleteUser(DeleteUserReq deleteUserReq) throws BaseException{
        try{
            DeleteUserRes deleteUsersRes = userDao.deleteUser(deleteUserReq);
            return deleteUsersRes;
        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchUserNickNameRes patchUserNickName(long userId, PatchUserNickNameReq patchUserNickNameReq) throws BaseException {
        try {
            PatchUserNickNameRes patchUserNickNameRes = userDao.patchUserNick(userId, patchUserNickNameReq);
            return patchUserNickNameRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 비밀번호 변경
    public PostUserRes updatePwd(UpdatePwdReq updatePwdReq) throws BaseException {
        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(updatePwdReq.getPwd());
            updatePwdReq.setPwd(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            String id = updatePwdReq.getEmail();
            Long userId = userDao.getUserId(id);
            String accessToken = jwtService.createAccessToken(userId);
            String refreshToken = jwtService.createRefreshToken(userId);
            userDao.addRefreshToken(refreshToken, userId);
            userDao.updatePwd(userId, pwd);
            return new PostUserRes(accessToken, refreshToken, userId);
        } catch (Exception exception) {
            throw new BaseException(USERS_NONEXISTENT_ID);
        }
    }
}
