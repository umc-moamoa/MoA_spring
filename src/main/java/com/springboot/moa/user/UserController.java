

package com.springboot.moa.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.springboot.moa.config.BaseException;
import com.springboot.moa.config.BaseResponse;
import com.springboot.moa.config.BaseResponseStatus;
import com.springboot.moa.user.model.*;
import com.springboot.moa.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.springboot.moa.config.BaseResponseStatus.USERS_DUPLICATED_ID;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetUserInfoRes> getUser() {
        try{
            long userIdByJwt = jwtService.getUserId();
            GetUserInfoRes getUserInfoRes = userProvider.retrieveUser(userIdByJwt);
            return new BaseResponse<>(getUserInfoRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/userPost")
    public BaseResponse<List<GetUserPostRes>> getUserPost() {
        try{
            long userIdByJwt = jwtService.getUserId();
            List<GetUserPostRes> getUserPostRes = userProvider.retrieveUserPosts(userIdByJwt);
            return new BaseResponse<>(getUserPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/partPost")
    public BaseResponse<List<GetUserPartPostRes>> getUserPartPost() {
        try{
            long userIdByJwt = jwtService.getUserId();
            List<GetUserPartPostRes> getUserPartPostRes = userProvider.retrieveUserPartPosts(userIdByJwt);
            return new BaseResponse<>(getUserPartPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        try{
            if(postUserReq.getId().length() > 15 || postUserReq.getId().length() < 7)
                return new BaseResponse<>(BaseResponseStatus.USERS_USERS_FAILED_ID);
            if(postUserReq.getNick().length() > 15 || postUserReq.getNick().length() < 7)
                return new BaseResponse<>(BaseResponseStatus.USERS_USERS_FAILED_NICK);
            if(postUserReq.getPwd().length() > 15 || postUserReq.getPwd().length() < 7)
                return new BaseResponse<>(BaseResponseStatus.USERS_USERS_FAILED_PWD);
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // localhost:9000/users/interest
    @ResponseBody
    @GetMapping("/interest")
    public BaseResponse<List<GetUserInterestRes>> getUserInterests(){
        try {
            long userIdByJwt = jwtService.getUserId();
            List<GetUserInterestRes> getUserInterestRes = userProvider.retrieveUserInterest(userIdByJwt);
            return new BaseResponse<>(getUserInterestRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/point/recent")
    public BaseResponse<GetPointHistoryRes> getPointHistoryRecent() throws BaseException{
        try{
            long userIdByJwt = jwtService.getUserId();

            GetPointHistoryRes getPointHistoryRes = userProvider.getPointHistoryRecent(userIdByJwt);
            return new BaseResponse<>(getPointHistoryRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/point/former")
    public BaseResponse<GetPointHistoryRes> getPointHistoryFormer() throws BaseException{
        try{
            long userIdByJwt = jwtService.getUserId();

            GetPointHistoryRes getPointHistoryRes = userProvider.getPointHistoryFormer(userIdByJwt);
            return new BaseResponse<>(getPointHistoryRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @DeleteMapping("")
    public BaseResponse<DeleteUserRes> deleteUser() {
        try{
            long userIdByJwt = jwtService.getUserId();
            DeleteUserReq deleteUserReq = new DeleteUserReq(userIdByJwt);
            DeleteUserRes deleteUsersRes = userService.deleteUser(deleteUserReq);
            return new BaseResponse<>(deleteUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/nick")
    public BaseResponse<PatchUserNickNameRes> ModifyUserNickName(@RequestBody PatchUserNickNameReq patchUserNickNameReq) {
        try{
            long userIdByJwt = jwtService.getUserId();
            PatchUserNickNameRes patchUserNickNameRes =
                    userService.patchUserNickName(userIdByJwt, patchUserNickNameReq);
            return new BaseResponse<>(patchUserNickNameRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/id/{id}")
    public BaseResponse<String> checkId(@PathVariable ("id") String id) throws BaseException{
        try {
            if (userProvider.checkIdExist(id) == 1) {
                throw new BaseException(BaseResponseStatus.USERS_DUPLICATED_ID);
            }
            String result = "중복되지 않은 아이디입니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/nick/{nick}")
    public BaseResponse<String> checkNick(@PathVariable ("nick") String nick) throws BaseException{
        try {
            if (userProvider.checkNickExist(nick) == 1) {
                throw new BaseException(BaseResponseStatus.USERS_DUPLICATED_NICK);
            }
            String result = "중복되지 않은 닉네임입니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PostMapping("/kakao/{accessToken}")
    public BaseResponse<PostUserRes> createKakaoUser(@PathVariable("accessToken") String accessToken) throws BaseException {
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
            String nick = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("profile").getAsJsonObject().get("nickname").getAsString();

            PostUserReq kakaoUserReq = new PostUserReq(id,nick,pwd);
            if (userProvider.checkIdExist(id) == 1) {
                return new BaseResponse<>(USERS_DUPLICATED_ID);
            }
            PostUserRes kakaoUserRes = userService.createUser(kakaoUserReq);

            if(kakaoUserRes != null){
                String message = "회원가입에 성공하였습니다.";
            }
            br.close();
            return new BaseResponse<>(kakaoUserRes);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}