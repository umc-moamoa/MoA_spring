package com.springboot.moa.auth;

import com.springboot.moa.auth.model.PostLoginReq;
import com.springboot.moa.auth.model.PostLoginRes;
import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.config.BaseResponseStatus;
import com.springboot.moa.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final AuthProvider authProvider;
    @Autowired
    private final AuthService authService;
    @Autowired
    private final JwtService jwtService;

    public AuthController(AuthProvider authProvider, AuthService authService, JwtService jwtService) {
        this.authProvider = authProvider;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) {
        try {
            if (postLoginReq.getId() == null)
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_ID);
            if (postLoginReq.getPwd() == null)
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_PASSWORD);

            PostLoginRes postLoginRes = authService.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}