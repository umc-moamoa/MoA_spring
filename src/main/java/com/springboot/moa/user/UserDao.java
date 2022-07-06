package com.springboot.moa.user;

import com.springboot.moa.user.model.GetUserInfoRes;
import com.springboot.moa.user.model.GetUserPostRes;
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
        System.out.println(selectUserPostsQuery);
        int selectUserPostsParam = userId;
        return this.jdbcTemplate.query(selectUserPostsQuery,
                (rs,rowNum) -> new GetUserPostRes(
                        rs.getString("postTitle"),
                        rs.getInt("point"),
                        rs.getInt("postResultCount"),
                        rs.getInt("qCount")
                ), selectUserPostsParam);
    }
}
