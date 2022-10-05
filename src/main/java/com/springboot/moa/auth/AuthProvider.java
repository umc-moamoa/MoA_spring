package com.springboot.moa.auth;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.springboot.moa.config.BaseResponseStatus.INVALID_JWT;

@Service
public class AuthProvider {

    private final AuthDao authDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AuthProvider(AuthDao authDao, JwtService jwtService) {
        this.authDao = authDao;
        this.jwtService = jwtService;
    }

    public String retrieveAccessToken(String refreshToken){
        long userId = authDao.isValidToken(refreshToken);
        String accessToken = null;
        if(userId != -1)
            accessToken = jwtService.createAccessToken(userId);
        return accessToken;
    }
}