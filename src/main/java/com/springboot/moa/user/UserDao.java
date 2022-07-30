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
        System.out.println(createUserParams);

        this.jdbcTemplate.update(createUserQuery, createUserParams);
        String lastInsertIdQuery = "select last_insert_id()";
        int userIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        return userIdx;
    }

    public int addPointHistory(int userId, int addAmount, int subAmount){
        String addPointHistoryQuery = "insert into point(user_id, add_amount, sub_amount) values(?,?,?)";
        Object[] addPointHistoryParams = new Object[]{userId,addAmount,subAmount};
        String lastInsertIdxQuery = "select last_insert_id()";
        this.jdbcTemplate.update(addPointHistoryQuery,addPointHistoryParams);
        int pointId = this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
        return pointId;
    }

    public List<GetUserInterestRes> selectUserInterest(int userId) {
        String selectUserInterestQuery = "SELECT p.post_id as postId,\n" +
                "           p.point as point,\n" +
                "           p.title as title,\n" +
                "           count(pd.post_detail_id) as numberOfQuestion\n" +
                "FROM       interest as i, post as p, post_detail as pd\n" +
                "WHERE      i.post_id = p.post_id and p.post_id = pd.post_id and i.user_id=?\n" +
                "GROUP BY   p.post_id";


        int selectUserInterestParam = userId;
        return this.jdbcTemplate.query(selectUserInterestQuery,
                (rs, rowNum) -> new GetUserInterestRes(
                        rs.getInt("postId"),
                        rs.getInt("point"),
                        rs.getString("title"),
                        rs.getInt("numberOfQuestion")
                ), selectUserInterestParam);
    }

    public void updateUserPoint(int userId,int addAmount,int subAmount){
        Object[] addUserPointParam = null;
        String addUserPointQuery = "update user set point = point + ? where user_id = ?";
        if(addAmount == 0)
            addUserPointParam = new Object[] { -(subAmount),userId};
        else if(subAmount == 0)
            addUserPointParam = new Object[]{ addAmount,userId};
        this.jdbcTemplate.update(addUserPointQuery,addUserPointParam);
    }

//    public void updateUserPoint(PostPointsReq postPointsReq){
//        Object[] addUserPointParam = null;
//        String addUserPointQuery = "update user set point = point + ? where user_id = ?";
//        if(postPointsReq.getAddAmount() == 0)
//            addUserPointParam = new Object[] { -(postPointsReq.getSubAmount()),postPointsReq.getUserId()};
//        else if(postPointsReq.getSubAmount() == 0)
//            addUserPointParam = new Object[]{ postPointsReq.getAddAmount(),postPointsReq.getUserId()};
//        this.jdbcTemplate.update(addUserPointQuery,addUserPointParam);
//    }

}
