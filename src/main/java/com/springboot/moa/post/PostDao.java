package com.springboot.moa.post;

import com.springboot.moa.post.model.*;
import com.springboot.moa.user.model.GetUserPartPostRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetPostsRes> selectPosts(int categoryId) {
        String selectPostsQuery = "\n" +
                "        SELECT p.post_id as post_id,\n" +
                "            p.user_id as user_id,\n" +
                "            p.point as point,\n" +
                "            p.title as title,\n" +
                "            p.content as content,\n" +
                "            p.deadline as deadline " +
                "        FROM post as p\n" +
                "            join category as c on p.category_id = c.category_id\n" +
                "where p.category_id=?";
        int selectPostsParam = categoryId;
        return this.jdbcTemplate.query(selectPostsQuery,
                (rs, rowNum) -> new GetPostsRes(
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getInt("point"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("deadline")
                ), selectPostsParam);
    }

    public int checkCategoryExist(int categoryId) {
        String checkCategoryExistQuery = "select exists(select category_id from category where category_id = ?)";
        int checkCategoryExistParams = categoryId;
        return this.jdbcTemplate.queryForObject(checkCategoryExistQuery,
                int.class,
                checkCategoryExistParams);

    }

    public List<GetPostDetailRes> selectPostDetail(int postId) {
        String selectPostDetailQuery = "\n" +
                "        SELECT \n" +
                "            pd.post_detail_id as post_detail_id,\n" +
                "            pd.question as question,\n" +
                "            pd.type as type\n" +
                "        FROM post_detail as pd\n" +
                "where pd.post_id=?";
        int selectPostDetailParam = postId;


        return this.jdbcTemplate.query(selectPostDetailQuery,
                (rs, rowNum) -> new GetPostDetailRes(
                        rs.getInt("post_detail_id"),
                        rs.getString("question"),
                        rs.getInt("type")
                ), selectPostDetailParam);

    }

    public int checkPostDetailExist(int postId) {
        String checkPostDetailQuery = "select exists(select post_id from post_detail where post_id = ?)";
        int checkPostDetailParams = postId;
        return this.jdbcTemplate.queryForObject(checkPostDetailQuery,
                int.class,
                checkPostDetailParams);

    }

    public int insertPosts(int userId, PostPostsReq postPostsReq) {
        String insertPostQuery = "INSERT INTO post(user_id, point, category_id,title,content,deadline ) VALUES (?, ?, ?,?,?,?)";
        Object[] insertPostParams = new Object[]{userId, postPostsReq.getPoint(), postPostsReq.getCategoryId(),
                postPostsReq.getTitle(), postPostsReq.getContent(), postPostsReq.getDeadline()};
        this.jdbcTemplate.update(insertPostQuery,
                insertPostParams);

        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    public int insertPostDetails(int postId, PostDetailsReq postDetailsReq) {
        String insertPostDetailsQuery = "INSERT INTO post_detail(post_id,question,type) VALUES (?,?,?)";
        Object[] insertPostDetailsParams = new Object[]{postId, postDetailsReq.getQuestion(), postDetailsReq.getType()};
        this.jdbcTemplate.update(insertPostDetailsQuery,
                insertPostDetailsParams);
        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    public int insertPostFormats(int detailId, String content) {
        String insertPostFormatQuery = "INSERT INTO format(post_detail_id,content) VALUES (?,?)";
        Object[] insertPostFormatParams = new Object[]{detailId, content};
        System.out.println("df");

        this.jdbcTemplate.update(insertPostFormatQuery,
                insertPostFormatParams);

        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    public List<GetParticipantsRes> selectParticipantsDesc() {
        String selectParticipantsDescQuery = "\n" +
                "        SELECT p.point as point,\n" +
                "            p.title as title,\n" +
                "            p.status as status,\n" +
                "            count(distinct post_detail_id) as numberOfQuestion\n" +
                "        FROM post as p\n" +
                "             left join post_detail as pd on p.post_id=pd.post_id\n" +
                "                 left join result as r on p.post_id=r.post_id\n" +
                "        WHERE p.status='ACTIVE'\n" +
                "        GROUP BY pd.post_id\n" +
                "        ORDER BY count(distinct r.user_id) DESC" +
                "        LIMIT 3";
        return this.jdbcTemplate.query(selectParticipantsDescQuery,
                (rs, rowNum) -> new GetParticipantsRes(
                        rs.getInt("point"),
                        rs.getString("title"),
                        rs.getString("status"),
                        rs.getInt("numberOfQuestion")
                ));
    }

    public List<GetParticipantsRes> selectParticipantsAsc() {
        String selectParticipantsAscQuery = "\n" +
                "        SELECT p.point as point,\n" +
                "            p.title as title,\n" +
                "            p.status as status,\n" +
                "            count(distinct post_detail_id) as numberOfQuestion\n" +
                "        FROM post as p\n" +
                "             left join post_detail as pd on p.post_id=pd.post_id\n" +
                "                 left join result as r on p.post_id=r.post_id\n" +
                "        WHERE p.status='ACTIVE'\n" +
                "        GROUP BY pd.post_id\n" +
                "        ORDER BY count(distinct r.user_id) ASC" +
                "        LIMIT 3";
        return this.jdbcTemplate.query(selectParticipantsAscQuery,
                (rs, rowNum) -> new GetParticipantsRes(
                        rs.getInt("point"),
                        rs.getString("title"),
                        rs.getString("status"),
                        rs.getInt("numberOfQuestion")
                ));
    }


    public int insertInterest(int postId, int userId) {
        String insertInterestQuery = "INSERT INTO interest (post_id, user_id) VALUES (?, ?)";
        Object[] insertInterestParams = new Object[]{postId, userId};
        this.jdbcTemplate.update(insertInterestQuery, insertInterestParams);
        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

//    public int checkPostIdExist(int postId) {
//        String checkPostIdExistQuery = "select exists(select post_id from post where post_id = ?)";
//        int checkPostIdExistParams = postId;
//        return this.jdbcTemplate.queryForObject(checkPostIdExistQuery,
//                int.class,
//                checkPostIdExistParams);
//    }

    public int checkUserIdExist(int userId) {
        String checkUserIdExistQuery = "select exists(select user_id from user where user_id = ?)";
        int checkUserIdExistParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserIdExistQuery,
                int.class,
                checkUserIdExistParams);
    }

    public int checkDuplicateInterest(int postId, int userId) {
        String checkDuplicateInterestQuery = "select exists(select post_id, user_id from interest where post_id = ? and user_id = ?)";
        int checkDuplicateInterestParams1 = postId;
        int checkDuplicateInterestParams2 = userId;
        return this.jdbcTemplate.queryForObject(checkDuplicateInterestQuery, int.class, checkDuplicateInterestParams1, checkDuplicateInterestParams2);

    }


    public List<GetPostContentRes> selectPostContent(int postId) {
        String selectPostContentQuery = "\n" +
                "select p.title as title,\n" +
                "p.content as content,\n" +
                "count(distinct pd.post_detail_id) as qCount, \n" +
                "p.user_id as postUserId\n" +
                "from post as p\n" +
                "left join post_detail as pd on pd.post_id = p.post_id\n" +
                "left join result as r on r.post_id = p.post_id \n" +
                "where p.post_id = ? \n";

        int selectPostContentParam = postId;

        return this.jdbcTemplate.query(selectPostContentQuery,
                (rs, rowNum) -> new GetPostContentRes(
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("qCount"),
                        rs.getInt("postUserId")
                ), selectPostContentParam);
    }

}
