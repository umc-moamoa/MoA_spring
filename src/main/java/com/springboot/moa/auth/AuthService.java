package com.springboot.moa.auth;

import com.springboot.moa.auth.model.PostLoginReq;
import com.springboot.moa.auth.model.PostLoginRes;
import com.springboot.moa.auth.model.User;
import com.springboot.moa.config.BaseException;
import com.springboot.moa.utils.JwtService;
import com.springboot.moa.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.springboot.moa.config.BaseResponseStatus.FAILED_TO_LOGIN;
import static com.springboot.moa.config.BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR;

@Service
public class AuthService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthDao authDao;
    private final AuthProvider authProvider;
    private final JwtService jwtService;

    @Autowired
    public AuthService(AuthDao authDao, AuthProvider authProvider, JwtService jwtService) {
        this.authDao = authDao;
        this.authProvider = authProvider;
        this.jwtService = jwtService;
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException {
        User user = authDao.getUserInfo(postLoginReq);
        String encryptPwd;
        if(user == null){
            throw new BaseException(FAILED_TO_LOGIN);
        } else {
            try {
                encryptPwd = new SHA256().encrypt(postLoginReq.getPwd());
            } catch (Exception exception) {
                throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
            }
            if (user.getPwd().equals(encryptPwd)) {
                long userId = user.getUserId();
                String jwt = jwtService.createJwt(userId);
                return new PostLoginRes(userId, jwt);
            } else {
                throw new BaseException(FAILED_TO_LOGIN);
            }
        }
    }
}
