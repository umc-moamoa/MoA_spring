package com.springboot.moa.post;

import com.springboot.moa.post.model.GetPostsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetPostsRes> selectPosts(int categoryId){
        String selectPostsQuery = "\n" +
                "        SELECT p.post_id as postId,\n" +
                "            p.user_id as userId,\n" +
                "            p.point as point,\n" +
                "            p.title as title,\n" +
                "            p.content as content,\n" +
                "            c.category_id as categoryId " +
                "        FROM post as p\n" +
                "            join category as c on p.categoryId = c.categoryId\n";
        int selectPostsParam = categoryId;
        return this.jdbcTemplate.query(selectPostsQuery,
                (rs,rowNum) -> new GetPostsRes(
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getInt("point"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("category_id")
                ), selectPostsParam);
    }
    public int checkCategoryExist(int categoryId){
        String checkCategoryExistQuery = "select exists(select category_id from category where category_id = ?)";
        int checkCategoryExistParams = categoryId;
        return this.jdbcTemplate.queryForObject(checkCategoryExistQuery,
                int.class,
                checkCategoryExistParams);

    }

}
