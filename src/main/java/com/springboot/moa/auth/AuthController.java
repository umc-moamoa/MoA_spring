package com.springboot.moa.auth;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.springboot.moa.auth.model.PostLoginReq;
import com.springboot.moa.auth.model.PostLoginRes;
import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.config.BaseResponseStatus;
import com.springboot.moa.user.model.PostUserReq;
import com.springboot.moa.user.model.PostUserRes;
import com.springboot.moa.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.springboot.moa.config.BaseResponseStatus.INVALID_JWT;

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

    @ResponseBody
    @GetMapping("/refresh")
    public BaseResponse<String> getAccessToken() {
        try {
            //refresh-token 유효성 검사
            jwtService.isValid();

            //refresh-token으로 access-token 발급
            String refreshToken = jwtService.getRefreshToken();
            String accessToken = authProvider.retrieveAccessToken(refreshToken);
            if (accessToken == null)
                throw new BaseException(INVALID_JWT);
            else return new BaseResponse<>(accessToken);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/kakaoLogin/{accessToken}")
    public BaseResponse<PostLoginRes> kakaoLogin(@PathVariable("accessToken") String accessToken) throws BaseException {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonElement element = JsonParser.parseString(result);

            String id = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            String pwd = element.getAsJsonObject().get("id").getAsString();

            PostLoginReq kakaoLoginReq = new PostLoginReq(id,pwd);

            PostLoginRes kakaoLoinRes = authService.logIn(kakaoLoginReq);

            br.close();
            return new BaseResponse<>(kakaoLoinRes);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}