package com.springboot.moa.post;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.post.model.*;
import com.springboot.moa.user.model.GetUserPartPostRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;
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
        String selectPostsQuery = "SELECT p.post_id as postId,\n" +
                "           p.point as point,\n" +
                "           p.title as title,\n" +
                "           p.d_day as dDay,\n" +
                "           COUNT(distinct pd.post_detail_id) as qCount,\n" +
                "           p.status as status\n" +
                "from post as p\n" +
                "left join post_detail as pd on p.post_id=pd.post_id\n" +
                "left join result as r on p.post_id=r.post_id\n" +
                "where (p.status = 'ACTIVE' or p.status = 'CLOSED')" +
                "and p.category_id=?\n" +
                "group by pd.post_id";
        long selectPostsParam = categoryId;
        return this.jdbcTemplate.query(selectPostsQuery,
                (rs, rowNum) -> new GetPostsRes(
                        rs.getLong("postId"),
                        rs.getInt("point"),
                        rs.getString("title"),
                        rs.getInt("qCount"),
                        rs.getInt("dDay"),
                        rs.getString("status")
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
                "SELECT pd.post_detail_id as post_detail_id,pd.question as question,pd.format as format, qd.item as item\n" +
                "FROM post_detail as pd left join post as p on pd.post_id = p.post_id\n" +
                "    left join question_detail as qd on pd.post_detail_id = qd.post_detail_id\n" +
                "where pd.post_id=? and p.status = 'ACTIVE'\n";

        long selectPostDetailParam = postId;
        return this.jdbcTemplate.query(selectPostDetailQuery,
                (rs, rowNum) -> new GetPostDetailRes(
                        rs.getLong("post_detail_id"),
                        rs.getString("question"),
                        rs.getInt("format"),
                        rs.getString("item")
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
        String calculateDdayQuery = "select datediff(?,curdate())";
        Date calculateDdayParam = postPostsReq.getDeadline();

        int dDay = this.jdbcTemplate.queryForObject(calculateDdayQuery,int.class,calculateDdayParam);

        int addAmount = postPostsReq.getShortCount() + postPostsReq.getLongCount()*3;
        String insertPostQuery = "INSERT INTO post(user_id, category_id, point, title,content,deadline ,d_day) VALUES (?,?,?,?,?,?,?)";
        Object[] insertPostParams = new Object[]{userId, postPostsReq.getCategoryId(),addAmount,
                postPostsReq.getTitle(), postPostsReq.getContent(), postPostsReq.getDeadline(),dDay};
        this.jdbcTemplate.update(insertPostQuery,
                insertPostParams);
        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, long.class);
    }

    public long insertPostDetails(long postId, String question, int format) {
        String insertPostDetailsQuery = "INSERT INTO post_detail(post_id,question,format) VALUES (?,?,?)";
        Object[] insertPostDetailsParams = new Object[]{postId, question, format};
        this.jdbcTemplate.update(insertPostDetailsQuery,
                insertPostDetailsParams);

        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, long.class);
    }

    public long insertPostFormats(long detailId, String item) {
        String insertPostFormatQuery = "INSERT INTO question_detail(post_detail_id,item) VALUES (?,?)";
        Object[] insertPostFormatParams = new Object[]{detailId, item};
        this.jdbcTemplate.update(insertPostFormatQuery,
                insertPostFormatParams);
        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, long.class);
    }

    public List<GetParticipantsRes> selectParticipantsDesc() {
        String selectParticipantsDescQuery = "\n" +
                "        SELECT p.post_id as postId,\n" +
                "            p.point as point,\n" +
                "            p.title as title,\n" +
                "            count(distinct post_detail_id) as qCount,\n" +
                "            p.d_day as dDay,\n" +
                "            p.status as status\n" +
                "        FROM post as p\n" +
                "             left join post_detail as pd on p.post_id=pd.post_id\n" +
                "                 left join result as r on p.post_id=r.post_id\n" +
                "        WHERE p.status='ACTIVE'\n" +
                "        GROUP BY p.post_id, p.point, p.title, p.status\n" +
                "        ORDER BY count(distinct r.user_id) DESC" +
                "        LIMIT 3";
        return this.jdbcTemplate.query(selectParticipantsDescQuery,
                (rs, rowNum) -> new GetParticipantsRes(
                        rs.getLong("postId"),
                        rs.getInt("point"),
                        rs.getString("title"),
                        rs.getInt("qCount"),
                        rs.getInt("dDay"),
                        rs.getString("status")
                ));
    }
    public List<GetParticipantsRes> selectParticipantsAsc() {
        String selectParticipantsAscQuery = "\n" +
                "        SELECT p.post_id as postId,\n" +
                "            p.point as point,\n" +
                "            p.title as title,\n" +
                "            count(distinct post_detail_id) as qCount,\n" +
                "            p.d_day as dDay,\n" +
                "            p.status as status\n" +
                "        FROM post as p\n" +
                "             left join post_detail as pd on p.post_id=pd.post_id\n" +
                "                 left join result as r on p.post_id=r.post_id\n" +
                "        WHERE p.status='ACTIVE'\n" +
                "        GROUP BY p.post_id, p.point, p.title, p.status\n" +
                "        ORDER BY count(distinct r.user_id) ASC" +
                "        LIMIT 3";
        return this.jdbcTemplate.query(selectParticipantsAscQuery,
                (rs, rowNum) -> new GetParticipantsRes(
                        rs.getLong("postId"),
                        rs.getInt("point"),
                        rs.getString("title"),
                        rs.getInt("qCount"),
                        rs.getInt("dDay"),
                        rs.getString("status")
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
    public boolean checkIsLiked(long postId, long userId) {
        String checkInterestExistQuery = "select exists(select interest_id from interest " +
                "where post_id = ? and user_id = ?)";
        Object[] checkInterestExistParams = new Object[]{postId, userId};;
        int isLike = this.jdbcTemplate.queryForObject(checkInterestExistQuery,
                int.class,
                checkInterestExistParams);
        if(isLike == 0) return false;
        else return true;
    }

    public int checkDuplicateInterest(long postId, long userId) {
        String checkDuplicateInterestQuery = "select exists(select post_id, user_id from interest where post_id = ? and user_id = ?)";
        long checkDuplicateInterestParams1 = postId;
        long checkDuplicateInterestParams2 = userId;
        return this.jdbcTemplate.queryForObject(checkDuplicateInterestQuery, int.class, checkDuplicateInterestParams1, checkDuplicateInterestParams2);

    }

    public GetPostContentRes selectPostContent(long postId) {
        String selectPostContentQuery =
                "select p.user_id as postUserId,\n" +
                        "p.title as title,\n" +
                        "p.content as content,\n" +
                        "count(distinct pd.post_detail_id) as qCount,\n" +
                        "p.d_day as d_day\n" +
                        "from post as p\n" +
                        "left join post_detail as pd on pd.post_id = p.post_id\n" +
                        "where p.post_id = ? and (p.status = 'ACTIVE' or p.status = 'CLOSED')\n";

        long selectPostContentParam = postId;

        return this.jdbcTemplate.queryForObject(selectPostContentQuery,
                (rs, rowNum) -> new GetPostContentRes(
                        rs.getLong("postUserId"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("qCount"),
                        rs.getInt("d_day")
                ), selectPostContentParam);
    }


    public int deletePost(long postId){
        String deletePostQuery = "UPDATE post SET status='INACTIVE' WHERE post_id = ?";
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
        String checkStatusQuery = "select status from post where post_id = ?;";
        long checkStatusParams = postId;
        return this.jdbcTemplate.queryForObject(checkStatusQuery,
                String.class,
                checkStatusParams);
    }

    public long getUserId(long postId){
        String selectUserIdQuery = "select user_id from post where post_id=?" +
                " and (p.status = 'ACTIVE' or p.status = 'CLOSED');";
        long selectUserIdParam = postId;
        return this.jdbcTemplate.queryForObject(selectUserIdQuery,
                Long.class,
                selectUserIdParam);
    }

    public int deleteInterest(long userId, long postId) {
        String deleteInterestQuery = "delete from interest where user_id = ? and post_id = ?;";
        Object[] deleteInterestParams = new Object[]{userId, postId};
        return this.jdbcTemplate.update(deleteInterestQuery,
                deleteInterestParams);
    }

    public int checkPostIdExist(long postId) {
        String checkPostIdExistQuery = "select exists(select post_id from post where post_id= ?)";
        long checkPostIdExistParams = postId;
        return this.jdbcTemplate.queryForObject(checkPostIdExistQuery,
                int.class,
                checkPostIdExistParams);
    }
}