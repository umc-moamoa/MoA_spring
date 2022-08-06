package com.springboot.moa.post;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.post.model.*;
import com.springboot.moa.user.model.GetUserPartPostRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.springboot.moa.config.BaseResponseStatus.POSTS_FAILED_UPLOAD;

@Repository

public class PostDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetPostsRes> selectPosts(long categoryId) {
        String selectPostsQuery = "\n" +
                "        SELECT p.post_id as post_id,\n" +
                "            p.user_id as user_id,\n" +
                "            p.point as point,\n" +
                "            p.title as title,\n" +
                "            p.content as content,\n" +
                "            p.deadline as deadline " +
                "        FROM post as p\n" +
                "            join category as c on p.category_id = c.category_id\n" +
                "where p.category_id=? and status = 'ACTIVE'";
        long selectPostsParam = categoryId;
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

    public int checkCategoryExist(long categoryId) {
        String checkCategoryExistQuery = "select exists(select category_id from category where category_id = ?)";
        long checkCategoryExistParams = categoryId;
        return this.jdbcTemplate.queryForObject(checkCategoryExistQuery,
                int.class,
                checkCategoryExistParams);

    }

    public List<GetPostDetailRes> selectPostDetail(long postId) {
        String selectPostDetailQuery = "\n" +
                "        SELECT \n" +
                "            pd.post_detail_id as post_detail_id,\n" +
                "            pd.question as question,\n" +
                "            pd.format as format\n" +
                "        FROM post_detail as pd left join post as p on pd.post_id = p.post_id\n" +
                "where pd.post_id=? and p.status = 'ACTIVE'";
        long selectPostDetailParam = postId;


        return this.jdbcTemplate.query(selectPostDetailQuery,
                (rs, rowNum) -> new GetPostDetailRes(
                        rs.getLong("post_detail_id"),
                        rs.getString("question"),
                        rs.getInt("format")
                ), selectPostDetailParam);

    }

    public int checkPostDetailExist(long postId) {
        String checkPostDetailQuery = "select exists(select post_id from post_detail where post_id = ?)";
        long checkPostDetailParams = postId;
        return this.jdbcTemplate.queryForObject(checkPostDetailQuery,
                int.class,
                checkPostDetailParams);

    }

    public long insertPosts(long userId, PostPostsReq postPostsReq) {
        String insertPostQuery = "INSERT INTO post(user_id, category_id,title,content,deadline ) VALUES (?,?,?,?,?)";
        Object[] insertPostParams = new Object[]{userId, postPostsReq.getCategoryId(),
                postPostsReq.getTitle(), postPostsReq.getContent(), postPostsReq.getDeadline()};
        this.jdbcTemplate.update(insertPostQuery,
                insertPostParams);
        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, long.class);
    }

    public long insertPostDetails(long postId, PostDetailsReq postDetailsReq) {
        String insertPostDetailsQuery = "INSERT INTO post_detail(post_id,question,format) VALUES (?,?,?)";
        Object[] insertPostDetailsParams = new Object[]{postId, postDetailsReq.getQuestion(), postDetailsReq.getType()};
        this.jdbcTemplate.update(insertPostDetailsQuery,
                insertPostDetailsParams);

        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, long.class);
    }

    public long insertPostFormats(long detailId, PostFormatReq postFormatReq) {
        String insertPostFormatQuery = "INSERT INTO format(post_detail_id,content) VALUES (?,?)";
        Object[] insertPostFormatParams = new Object[]{detailId, postFormatReq.getContent()};
        this.jdbcTemplate.update(insertPostFormatQuery,
                insertPostFormatParams);
        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, long.class);
    }

    public List<GetParticipantsRes> selectParticipantsDesc() {
        String selectParticipantsDescQuery = "\n" +
                "        SELECT p.point as point,\n" +
                "            p.title as title,\n" +
                "            p.status as status,\n" +
                "            count(distinct post_detail_id) as qcount\n" +
                "        FROM post as p\n" +
                "             left join post_detail as pd on p.post_id=pd.post_id\n" +
                "                 left join result as r on p.post_id=r.post_id\n" +
                "        WHERE p.status='ACTIVE'\n" +
                "        GROUP BY pd.post_id, p.point, p.title, p.status\n" +
                "        ORDER BY count(distinct r.user_id) DESC" +
                "        LIMIT 3";
        return this.jdbcTemplate.query(selectParticipantsDescQuery,
                (rs, rowNum) -> new GetParticipantsRes(
                        rs.getInt("point"),
                        rs.getString("title"),
                        rs.getString("status"),
                        rs.getInt("qcount")
                ));
    }

    public List<GetParticipantsRes> selectParticipantsAsc() {
        String selectParticipantsAscQuery = "\n" +
                "        SELECT p.point as point,\n" +
                "            p.title as title,\n" +
                "            p.status as status,\n" +
                "            count(distinct post_detail_id) as qcount\n" +
                "        FROM post as p\n" +
                "             left join post_detail as pd on p.post_id=pd.post_id\n" +
                "                 left join result as r on p.post_id=r.post_id\n" +
                "        WHERE p.status='ACTIVE'\n" +
                "        GROUP BY pd.post_id, p.point, p.title, p.status\n" +
                "        ORDER BY count(distinct r.user_id) ASC" +
                "        LIMIT 3";
        return this.jdbcTemplate.query(selectParticipantsAscQuery,
                (rs, rowNum) -> new GetParticipantsRes(
                        rs.getInt("point"),
                        rs.getString("title"),
                        rs.getString("status"),
                        rs.getInt("qcount")
                ));
    }


    public long insertInterest(long postId, long userId) {
        String insertInterestQuery = "INSERT INTO interest (post_id, user_id) VALUES (?, ?)";
        Object[] insertInterestParams = new Object[]{postId, userId};
        this.jdbcTemplate.update(insertInterestQuery, insertInterestParams);
        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, long.class);
    }

    public int checkUserIdExist(long userId) {
        String checkUserIdExistQuery = "select exists(select user_id from user where user_id = ?)";
        long checkUserIdExistParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserIdExistQuery,
                int.class,
                checkUserIdExistParams);
    }

    public int checkDuplicateInterest(long postId, long userId) {
        String checkDuplicateInterestQuery = "select exists(select post_id, user_id from interest where post_id = ? and user_id = ?)";
        long checkDuplicateInterestParams1 = postId;
        long checkDuplicateInterestParams2 = userId;
        return this.jdbcTemplate.queryForObject(checkDuplicateInterestQuery, int.class, checkDuplicateInterestParams1, checkDuplicateInterestParams2);

    }


    public List<GetPostContentRes> selectPostContent(long postId) {
        String selectPostContentQuery = "\n" +
                "select p.title as title,\n" +
                "p.content as content,\n" +
                "count(distinct pd.post_detail_id) as qCount, \n" +
                "p.user_id as postUserId\n" +
                "from post as p\n" +
                "left join post_detail as pd on pd.post_id = p.post_id\n" +
                "left join result as r on r.post_id = p.post_id \n" +
                "where p.post_id = ? and p.status = 'ACTIVE'\n";

        long selectPostContentParam = postId;

        return this.jdbcTemplate.query(selectPostContentQuery,
                (rs, rowNum) -> new GetPostContentRes(
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("qCount"),
                        rs.getInt("postUserId")
                ), selectPostContentParam);
    }

    public int usePoints(long postId){
        String usePointsQuery = "\n"+
                "select mu + du as point\n" +
                "from (select count(case when pd.format = 1 then 1 end )*3 as mu,\n" +
                "count(case when pd.format = 2 then 1 end )*5 as du\n"+
                "from post_detail as pd left join post as p on pd.post_id = p.post_id\n"+
                "where pd.post_id = ? and p.status = 'ACTIVE') as cnt";
        long usePointsParam = postId;

        return this.jdbcTemplate.queryForObject(usePointsQuery,
                int.class, usePointsParam);
    }

    public void setPostPoints(long postId){
        String setPostPointsQuery = "\n"+
                "select mu + du as point\n" +
                "from" +
                "(select count(case when pd.format = 1 then 1 end ) as mu,\n" +
                "count(case when pd.format = 2 then 1 end )*3 as du\n"+
                "from post_detail as pd left join post as p on pd.post_id = p.post_id\n"+
                "where pd.post_id = ? and p.status = 'ACTIVE') as cnt";
        long setPostPointsParam = postId;
        int point = this.jdbcTemplate.queryForObject(setPostPointsQuery,int.class,setPostPointsParam);

        String updatePostPointQuery = "update post set point = ? where post_id = ?";
        Object[] updatePostPointParam = new Object[]{point, postId};

        this.jdbcTemplate.update(updatePostPointQuery,
                updatePostPointParam);
    }

    public int deletePost(long postId){
        String deletePostQuery = "UPDATE Post SET status='INACTIVE' WHERE post_id = ?";
        Object[] deletePostParams = new Object[]{postId};
        return this.jdbcTemplate.update(deletePostQuery,
                deletePostParams);
    }

    public int updateContent(long postId, String content) {
        String updateContentQuery = "UPDATE post SET content=? WHERE post_id=?";
        Object[] updateContentParams = new Object[]{content, postId};
        return this.jdbcTemplate.update(updateContentQuery,
                updateContentParams);
    }

    public int checkPostUserExist(long userId, long postId){
        String checkUserExistQuery = "select exists(select user_id from post where user_id= ? and post_id = ?);";
        Object[] checkUserExistParams =  new Object[]{userId, postId};
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);

    }

    public String checkStatus(long postId) {
        String checkStatusQuery = "select status from post where post_id = ?";
        long checkStatusParams = postId;
        return this.jdbcTemplate.queryForObject(checkStatusQuery,
                String.class,
                checkStatusParams);
    }

}