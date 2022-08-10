package com.springboot.moa.user;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetUserInfoRes selectUser(long userId){
        String selectUserQuery = "\n" +
                "        SELECT u.nick as nick,\n" +
                "            u.point as point,\n" +
                "            COUNT(p.post_id) as postCount\n" +
                "        FROM user as u\n" +
                "            left join post as p on u.user_id = p.user_id\n" +
                "        WHERE u.status = 'ACTIVE' " +
                "        and u.user_id=?\n" +
                "        GROUP BY u.nick, u.point\n";

        long selectUserParam = userId;
        return this.jdbcTemplate.queryForObject(selectUserQuery,
                (rs,rowNum) -> new GetUserInfoRes(
                        rs.getString("nick"),
                        rs.getInt("point"),
                        rs.getInt("postCount")
                ), selectUserParam);
    }

    public int checkUserExist(long userId){
        String checkUserExistQuery = "select exists(select user_id from user where user_id = ?)";
        long checkUserExistParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);

    }

    public List<GetUserPostRes> selectUserPosts(long userId){
        String selectUserPostsQuery = "SELECT p.title as postTitle,\n" +
                "p.point as point,\n" +
                "COUNT(distinct r.user_id) as postResultCount,\n" +
                "COUNT(distinct pd.post_detail_id) as qCount\n" +
                "from post as p\n" +
                "left join post_detail as pd on p.post_id=pd.post_id\n" +
                "left join result as r on p.post_id=r.post_id\n" +
                "where p.status = 'ACTIVE' " +
                "and p.user_id=?\n" +
                "group by pd.post_id";
        long selectUserPostsParam = userId;
        return this.jdbcTemplate.query(selectUserPostsQuery,
                (rs,rowNum) -> new GetUserPostRes(
                        rs.getString("postTitle"),
                        rs.getInt("point"),
                        rs.getInt("postResultCount"),
                        rs.getInt("qCount")
                ), selectUserPostsParam);
    }

    public List<GetUserPartPostRes> selectUserPartPosts(long userId){
        String selectUserPostsQuery = "SELECT p.post_id as postId,\n" +
                "           p.point as point,\n" +
                "           p.title as title,\n" +
                "           COUNT(distinct pd.post_detail_id) as qCount,\n" +
                "           p.status as status\n" +
                "from post as p\n" +
                "left join post_detail as pd on p.post_id=pd.post_id\n" +
                "left join result as r on p.post_id=r.post_id\n" +
                "where p.status = 'ACTIVE' " +
                "and r.user_id=?\n" +
                "group by pd.post_id";
        long selectUserPostsParam = userId;
        return this.jdbcTemplate.query(selectUserPostsQuery,
                (rs,rowNum) -> new GetUserPartPostRes(
                        rs.getLong("postId"),
                        rs.getInt("point"),
                        rs.getString("title"),
                        rs.getInt("qCount"),
                        rs.getString("status")
                ), selectUserPostsParam);
    }

    public long createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into user(id, nick, pwd) VALUES (?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getId(), postUserReq.getNick(), postUserReq.getPwd()};

        this.jdbcTemplate.update(createUserQuery, createUserParams);
        String lastInsertIdQuery = "select last_insert_id()";
        long userIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery,long.class);

        return userIdx;
    }

    public long addPointHistory(long userId, int addAmount, int subAmount){
        String addPointHistoryQuery = "insert into point(user_id, add_amount, sub_amount) values(?,?,?)";
        Object[] addPointHistoryParams = new Object[]{userId,addAmount,subAmount};
        String lastInsertIdxQuery = "select last_insert_id()";
        this.jdbcTemplate.update(addPointHistoryQuery,addPointHistoryParams);
        long pointId = this.jdbcTemplate.queryForObject(lastInsertIdxQuery, long.class);
        return pointId;
    }

    public List<GetUserInterestRes> selectUserInterest(long userId) {
        String selectUserInterestQuery = "SELECT p.post_id as postId,\n" +
                "           p.point as point,\n" +
                "           p.title as title,\n" +
                "           count(pd.post_detail_id) as qCount,\n" +
                "           p.status as status\n" +
                "FROM       interest as i, post as p, post_detail as pd\n" +
                "WHERE      i.post_id = p.post_id and p.post_id = pd.post_id and i.user_id=? " +
                "and p.status = 'ACTIVE'\n" +
                "GROUP BY   p.post_id";


        long selectUserInterestParam = userId;
        return this.jdbcTemplate.query(selectUserInterestQuery,
                (rs, rowNum) -> new GetUserInterestRes(
                        rs.getLong("postId"),
                        rs.getInt("point"),
                        rs.getString("title"),
                        rs.getInt("qCount"),
                        rs.getString("status")
                ), selectUserInterestParam);
    }

    public void updateUserPoint(long userId, int addAmount, int subAmount){
        Object[] addUserPointParam = null;
        String addUserPointQuery = "update user set point = point + ? where user_id = ?";
        if(addAmount == 0)
            addUserPointParam = new Object[] { -(subAmount),userId};
        else if(subAmount == 0)
            addUserPointParam = new Object[]{ addAmount,userId};
        this.jdbcTemplate.update(addUserPointQuery,addUserPointParam);
    }

    public List<GetPointHistoryRes> selectPointHistory(long userId){
        String selectPointHistoryQeury ="select add_amount as addAmount, sub_amount as subAmount,\n"+
                "sum(add_amount-sub_amount) over(order by point_id) as point\n"+
                "from point where user_id = ? order by point_id desc;";
        long selectPointHistoryParam = userId;

        return this.jdbcTemplate.query(selectPointHistoryQeury,
                (rs, rowNum) -> new GetPointHistoryRes(
                        rs.getInt("point"),
                        rs.getInt("addAmount"),
                        rs.getInt("subAmount")
                ), selectPointHistoryParam);
    }


    public DeleteUserRes deleteUser(DeleteUserReq deleteUserReq){
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
}