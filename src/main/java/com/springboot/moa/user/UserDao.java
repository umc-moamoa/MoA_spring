package com.springboot.moa.user;

import com.springboot.moa.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetUserInfoRes selectUser(long userId) {
        String selectUserQuery = "\n" +
                "        SELECT u.nick as nick,\n" +
                "            u.point as point\n" +
                "        FROM user as u\n" +
                "        WHERE u.status = 'ACTIVE' " +
                "        and u.user_id=?";

        long selectUserParam = userId;
        return this.jdbcTemplate.queryForObject(selectUserQuery,
                (rs, rowNum) -> new GetUserInfoRes(
                        rs.getString("nick"),
                        rs.getInt("point"),
                        getPostCount(userId)
                ), selectUserParam);
    }

    public int getPostCount(long userId){
        String getPostCountQuery = "\n" +
                "        SELECT u.user_id as userId,\n" +
                "            COUNT(p.post_id) as postCount\n" +
                "        FROM user as u\n" +
                "            left join post as p on u.user_id = p.user_id\n" +
                "        WHERE u.status = 'ACTIVE' and p.status = 'ACTIVE' " +
                "        and u.user_id=?\n" +
                "        GROUP BY u.user_id\n";
        long getPostCountParam = userId;
        try {
            GetPostCountRes result = this.jdbcTemplate.queryForObject(getPostCountQuery,
                    (rs, rowNum) -> new GetPostCountRes(
                            rs.getLong("userId"),
                            rs.getInt("postCount")
                    ), getPostCountParam);
            return result.getCount();
        } catch(EmptyResultDataAccessException e){
            return 0;
        }
    }

    public int checkUserExist(long userId) {
        String checkUserExistQuery = "select exists(select user_id from user where user_id = ?)";
        long checkUserExistParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);

    }

    public List<GetUserPostRes> selectUserPosts(long userId) {
        String selectUserPostsQuery = "SELECT p.post_id as postId, p.title as postTitle,\n" +
                "p.point as point,\n" +
                "COUNT(distinct r.user_id) as postResultCount,\n" +
                "COUNT(distinct pd.post_detail_id) as qCount,\n" +
                "p.d_day as dDay, p.status as status\n" +
                "from post as p\n" +
                "left join post_detail as pd on p.post_id=pd.post_id\n" +
                "left join result as r on p.post_id=r.post_id\n" +
                "where (p.status = 'ACTIVE' or p.status = 'CLOSED')" +
                "and p.user_id=?\n" +
                "group by p.post_id";
        long selectUserPostsParam = userId;
        return this.jdbcTemplate.query(selectUserPostsQuery,
                (rs, rowNum) -> new GetUserPostRes(
                        rs.getLong("postId"),
                        rs.getString("postTitle"),
                        rs.getInt("point"),
                        rs.getInt("postResultCount"),
                        rs.getInt("qCount"),
                        rs.getInt("dDay"),
                        rs.getString("status")
                ), selectUserPostsParam);
    }

    public List<GetUserPartPostRes> selectUserPartPosts(long userId) {
        String selectUserPostsQuery = "SELECT p.post_id as postId,\n" +
                "           p.point as point,\n" +
                "           p.title as title,\n" +
                "           COUNT(distinct pd.post_detail_id) as qCount," +
                "           p.d_day as dDay,\n" +
                "           p.status as status\n" +
                "from post as p\n" +
                "left join post_detail as pd on p.post_id=pd.post_id\n" +
                "left join result as r on p.post_id=r.post_id\n" +
                "where (p.status = 'ACTIVE' or p.status = 'CLOSED')" +
                "and r.user_id=?\n" +
                "group by p.post_id";
        long selectUserPostsParam = userId;
        return this.jdbcTemplate.query(selectUserPostsQuery,
                (rs, rowNum) -> new GetUserPartPostRes(
                        rs.getLong("postId"),
                        rs.getInt("point"),
                        rs.getString("title"),
                        rs.getInt("qCount"),
                        rs.getInt("dDay"),
                        rs.getString("status")
                ), selectUserPostsParam);
    }

    public long createUser(PostUserReq postUserReq) {
        String createUserQuery = "insert into user(id, nick, pwd,social_type, social_access_token) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getId(), postUserReq.getNick(), postUserReq.getPwd(),postUserReq.getSocialType(), postUserReq.getSocialAccessToken()};

        this.jdbcTemplate.update(createUserQuery, createUserParams);
        String lastInsertIdQuery = "select last_insert_id()";
        long userIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery, long.class);

        return userIdx;
    }

    public long addPointHistory(long userId, int addAmount, int subAmount) {
        String addPointHistoryQuery = "insert into point(user_id, add_amount, sub_amount) values(?,?,?)";
        Object[] addPointHistoryParams = new Object[]{userId, addAmount, subAmount};
        String lastInsertIdxQuery = "select last_insert_id()";
        this.jdbcTemplate.update(addPointHistoryQuery, addPointHistoryParams);
        long pointId = this.jdbcTemplate.queryForObject(lastInsertIdxQuery, long.class);
        return pointId;
    }

    public List<GetUserInterestRes> selectUserInterest(long userId) {
        String selectUserInterestQuery = "SELECT p.post_id as postId,\n" +
                "           p.point as point,\n" +
                "           p.title as title,\n" +
                "           count(pd.post_detail_id) as qCount,\n" +
                "           p.status as status,\n" +
                "           p.d_day as dDay\n" +
                "FROM       interest as i, post as p, post_detail as pd\n" +
                "WHERE      i.post_id = p.post_id and p.post_id = pd.post_id and i.user_id=? " +
                "and (p.status = 'ACTIVE' or p.status = 'CLOSED')\n" +
                "GROUP BY   p.post_id";


        long selectUserInterestParam = userId;
        return this.jdbcTemplate.query(selectUserInterestQuery,
                (rs, rowNum) -> new GetUserInterestRes(
                        rs.getLong("postId"),
                        rs.getInt("point"),
                        rs.getString("title"),
                        rs.getInt("qCount"),
                        rs.getString("status"),
                        rs.getInt("dDay")
                ), selectUserInterestParam);
    }

    public void updateUserPoint(long userId, int addAmount, int subAmount) {
        Object[] addUserPointParam = null;
        String addUserPointQuery = "update user set point = point + ? where user_id = ?";
        if (addAmount == 0)
            addUserPointParam = new Object[]{-(subAmount), userId};
        else if (subAmount == 0)
            addUserPointParam = new Object[]{addAmount, userId};
        this.jdbcTemplate.update(addUserPointQuery, addUserPointParam);
    }

    public List<GetPointHistoryRecentRes> selectPointHistoryRecent(long userId) {
        String selectPointHistoryQeury = "select created, add_amount as addAmount, sub_amount as subAmount,\n" +
                "sum(add_amount-sub_amount) over(order by point_id) as point\n" +
                "from point where user_id = ? order by point_id desc;";

        long selectPointHistoryParam = userId;
        return this.jdbcTemplate.query(selectPointHistoryQeury,
                (rs, rowNum) -> new GetPointHistoryRecentRes(
//                        rs.getInt("point"),
                        rs.getInt("addAmount"),
                        rs.getInt("subAmount"),
                        rs.getDate("created")
                ), selectPointHistoryParam);
    }

    public List<GetPointHistoryFormerRes> selectPointHistoryFormer(long userId) {
        String selectPointHistoryQeury = "select created, add_amount as addAmount, sub_amount as subAmount,\n" +
                "sum(add_amount-sub_amount) over(order by point_id) as point\n" +
                "from point where user_id = ? order by point_id;";

        long selectPointHistoryParam = userId;
        return this.jdbcTemplate.query(selectPointHistoryQeury,
                (rs, rowNum) -> new GetPointHistoryFormerRes(
                        rs.getInt("addAmount"),
                        rs.getInt("subAmount"),
                        rs.getDate("created")
                ), selectPointHistoryParam);
    }

    public DeleteUserRes deleteUser(DeleteUserReq deleteUserReq) {
        long userId = deleteUserReq.getUserId();
        long deleteUserParams = userId;
        String selectUserQuery = "select nick, id, pwd, point from user where user_id = ?";
        DeleteUserRes deleteUserRes = this.jdbcTemplate.queryForObject(selectUserQuery,
                (rs, rowNum) -> new DeleteUserRes(
                        rs.getString("nick"),
                        rs.getString("id"),
                        rs.getString("pwd"),
                        rs.getInt("point")),
                deleteUserParams);

        String deleteUserQuery = "delete from user where user_id = ?";
        this.jdbcTemplate.update(deleteUserQuery, deleteUserParams);
        return deleteUserRes;
    }

    // id 중복 확인
    public int checkUserIdExist(String id) {
        String checkIdExistQuery = "select exists(select id from user where id = ?)";
        String checkIdExistParams = id;
        return this.jdbcTemplate.queryForObject(checkIdExistQuery,
                int.class,
                checkIdExistParams);
    }

    // id, email 쌍이 존재하는지 확인
    public int checkUserIdEmailExist(String id, String email) {
        String checkIdExistQuery = "select exists(select email from user where id = ? and email = ?)";
        Object[] checkIdExistParams = new Object[]{id, email};
        return this.jdbcTemplate.queryForObject(checkIdExistQuery,
                int.class,
                checkIdExistParams);
    }

    // 닉네임 중복 확인
    public int checkUserNickExist(String nick) {
        String checkNickExistQuery = "select exists(select nick from user where nick = ?)";
        String checkNickExistParams = nick;
        return this.jdbcTemplate.queryForObject(checkNickExistQuery,
                int.class,
                checkNickExistParams);
    }

    public PatchUserNickNameRes patchUserNick(long userId, PatchUserNickNameReq patchUserNickNameReq) {
        String patchUserNickQuery = "update user set nick=? where user_id = ?";
        String newNick = patchUserNickNameReq.getNewNickName();
        Object[] patchUserNickParams = new Object[]{newNick, userId};

        this.jdbcTemplate.update(patchUserNickQuery, patchUserNickParams);

        long selectUserParam = userId;
        String selectUserQuery = "select nick, id from user where user_id = ?";
        return this.jdbcTemplate.queryForObject(selectUserQuery,
                (rs, rowNum) -> new PatchUserNickNameRes(
                        rs.getString("nick"),
                        rs.getString("id")),
                selectUserParam);
    }
    public void addRefreshToken(String token, long userId){
        String changeTokenQuery = "update user set refresh_token=? where user_id=?";
        Object[] changeTokenParams = new Object[]{token, userId};

        this.jdbcTemplate.update(changeTokenQuery, changeTokenParams);
    }

    public String checkSocialType(long userId) {
        String checkSocialTypeQuery = "select social_type from user where user_id = ?";
        long checkSocialTypeParam = userId;

        return this.jdbcTemplate.queryForObject(checkSocialTypeQuery,
                String.class,
                checkSocialTypeParam);
    }

    public String getSocialAccessToken(long userId) {
        String getSocialAccessTokenQuery = "select social_access_token from user where user_id = ?";
        long getSocialAccessTokenParam = userId;

        return this.jdbcTemplate.queryForObject(getSocialAccessTokenQuery,
                String.class,
                getSocialAccessTokenParam);
    }

    public List<GetUserAnswerPostIdRes> selectGetPostId(long userIdByJwt) {
        String selectPostIdQuery = "select distinct post_id as postId " +
                "from result_detail, result " +
                "where result.result_id = result_detail.result_id " +
                "and user_id = ?";
        long selectPostIdParam = userIdByJwt;
        return this.jdbcTemplate.query(selectPostIdQuery,
                (rs, rowNum) -> new GetUserAnswerPostIdRes(
                        rs.getInt("postId")
                ), selectPostIdParam);

    }

    public List<GetUserResultRes> selectAnswer(long userIdByJwt, long postId) {
        String selectAnswerQuery = "select format, result\n" +
                "from result_detail, result, post_detail\n" +
                "where  result.result_id = result_detail.result_id and\n" +
                "       result_detail.post_detail_id = post_detail.post_detail_id\n" +
                "  and user_id = ? and result.post_id =?;";
        Object[] selectAnswerParam = new Object[]{userIdByJwt, postId};
        return this.jdbcTemplate.query(selectAnswerQuery,
                (rs, rowNum) -> new GetUserResultRes(
                        rs.getInt("format"),
                        rs.getString("result")
                ), selectAnswerParam);

    }

    // id로 userId 찾기
    public Long getUserId(String id) {
        String getUserIdQuery = "select user_id from user where id = ?";
        String getUserIdParam = id;

        return this.jdbcTemplate.queryForObject(getUserIdQuery,
                Long.class,
                getUserIdParam);
    }


    public void updatePwd(Long user_id, String pwd) {

        String updatePwdQuery = "update user set pwd = ? where user_id = ?;";
        Object[] updatePwdParams = new Object[]{pwd, user_id};

        this.jdbcTemplate.update(updatePwdQuery, updatePwdParams);
    }
}
