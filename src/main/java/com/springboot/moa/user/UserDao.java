package com.springboot.moa.user;

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

    public GetUserInfoRes selectUser(int userId){
        String selectUserQuery = "\n" +
                "        SELECT u.nick as nick,\n" +
                "            u.point as point,\n" +
                "            COUNT(*) as postCount\n" +
                "        FROM user as u\n" +
                "            join post as p on u.user_id = p.user_id\n" +
                "        WHERE u.user_id=?\n" +
                "        GROUP BY u.nick, u.point\n";
        System.out.println(selectUserQuery);
        int selectUserParam = userId;
        return this.jdbcTemplate.queryForObject(selectUserQuery,
                (rs,rowNum) -> new GetUserInfoRes(
                        rs.getString("nick"),
                        rs.getInt("point"),
                        rs.getInt("postCount")
                ), selectUserParam);
    }
    public int checkUserExist(int userId){
        String checkUserExistQuery = "select exists(select user_id from user where user_id = ?)";
        int checkUserExistParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);

    }

    public List<GetUserPostRes> selectUserPosts(int userId){
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
        int selectUserPostsParam = userId;
        return this.jdbcTemplate.query(selectUserPostsQuery,
                (rs,rowNum) -> new GetUserPostRes(
                        rs.getString("postTitle"),
                        rs.getInt("point"),
                        rs.getInt("postResultCount"),
                        rs.getInt("qCount")
                ), selectUserPostsParam);
    }

    public List<GetUserPartPostRes> selectUserPartPosts(int userId){
        String selectUserPostsQuery = "SELECT p.title as postTitle,\n" +
                "p.point as point,\n" +
                "COUNT(distinct pd.post_detail_id) as qCount\n" +
                "from post as p\n" +
                "left join post_detail as pd on p.post_id=pd.post_id\n" +
                "left join result as r on p.post_id=r.post_id\n" +
                "where p.status = 'ACTIVE' " +
                "and r.user_id=?\n" +
                "group by pd.post_id";
        int selectUserPostsParam = userId;
        return this.jdbcTemplate.query(selectUserPostsQuery,
                (rs,rowNum) -> new GetUserPartPostRes(
                        rs.getString("postTitle"),
                        rs.getInt("point"),
                        rs.getInt("qCount")
                ), selectUserPostsParam);
    }

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into user(name, id, nick, pwd) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getName(), postUserReq.getId(), postUserReq.getNick(), postUserReq.getPwd()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        String userInitialPointQuery ="insert into point(user_id, add_amount, sub_amount) values (?,?,?)";

        int userIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
        Object[] userInitialPointParam = new Object[]{userIdx, 0, 0};
        this.jdbcTemplate.update(userInitialPointQuery, userInitialPointParam);

        return userIdx;
    }

    public int addPointHistory(PostPointsReq postPointsReq){
        String addPointHistoryQuery = "insert into point(user_id, add_amount, sub_amount) values(?,?,?)";
        Object addPointHistoryParams = new Object[] {postPointsReq.getUserId(),postPointsReq.getAddAmount(), postPointsReq.getSubAmount()};
        System.out.println("df");
        this.jdbcTemplate.update(addPointHistoryQuery, addPointHistoryParams);
        System.out.println("13245");
        Object[] addUserPointParam = null;
        String addUserPointQuery = "update user set point = point + ? where user_id = ?";
        if(postPointsReq.getAddAmount() == 0)
            addUserPointParam = new Object[] { -(postPointsReq.getSubAmount()), postPointsReq.getUserId()};
        else if(postPointsReq.getSubAmount() == 0)
            addUserPointParam = new Object[]{ postPointsReq.getAddAmount(), postPointsReq.getUserId()};

        this.jdbcTemplate.update(addUserPointQuery,addUserPointParam);
        String lastInsertIdxQuery = "select last_insert_id()";

        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

}
