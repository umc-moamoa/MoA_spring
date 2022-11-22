

package com.springboot.moa.user;

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
        String reqURL = "https://kapi.kakao.com/v1/user/unlink";
        try {
            long userIdByJwt = jwtService.getUserId();

            if (!userProvider.checkSocialType(userIdByJwt).equals("none")) {
                URL url = new URL(reqURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                String accessToken = userProvider.getSocialAccessToken(userIdByJwt);
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);

                int responseCode = conn.getResponseCode();
                System.out.println("responseCode : " + responseCode);

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String result = "";
                String line = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }
            }
            DeleteUserReq deleteUserReq = new DeleteUserReq(userIdByJwt);
            DeleteUserRes deleteUsersRes = userService.deleteUser(deleteUserReq);
            return new BaseResponse<>(deleteUsersRes);
        } catch(BaseException | IOException exception){
            throw new RuntimeException(exception);
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

    // 사용자가 답변한 postId 값 반환
    @ResponseBody
    @GetMapping("/answerList")
    public BaseResponse<List<GetUserAnswerPostIdRes>> getAnswersList() throws BaseException {
        try {
            long userIdByJwt = jwtService.getUserId();
            List<GetUserAnswerPostIdRes> getUserAnswerPostIdRes = userProvider.retrieveUserAnswerList(userIdByJwt);
            return new BaseResponse<>(getUserAnswerPostIdRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 사용자가 답변한 값 반환
    @ResponseBody
    @GetMapping("/answer/{postId}")
    public BaseResponse<GetUserAnswersRes> getUserAnswers(@PathVariable ("postId") Long postId) throws BaseException {
        try {
            long userIdByJwt = jwtService.getUserId();
            GetUserAnswersRes getUserAnswersRes = userProvider.retrieveUserAnswer(userIdByJwt, postId);
            return new BaseResponse<>(getUserAnswersRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 회원 여부 확인
    @ResponseBody
    @GetMapping("/existence/{id}/{email}")
    public BaseResponse<String> existenceId(@PathVariable ("id") String id, @PathVariable ("email") String email) throws BaseException{
        try {
            if (userProvider.checkIdEmailExist(id, email) == 0) {
                throw new BaseException(BaseResponseStatus.USERS_NONEXISTENT_ID);
            }
            String result = "회원정보가 일치합니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 비밀번호 변경
    @ResponseBody
    @PostMapping("/pwd")
    public BaseResponse<PostUserRes> updatePwd (@RequestBody UpdatePwdReq updatePwdReq) {
        try{
            if(updatePwdReq.getPwd().length() > 15 || updatePwdReq.getPwd().length() < 7)
                return new BaseResponse<>(BaseResponseStatus.USERS_USERS_FAILED_PWD);
            PostUserRes postUserRes = userService.updatePwd(updatePwdReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}