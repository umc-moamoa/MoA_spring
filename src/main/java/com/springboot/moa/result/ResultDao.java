package com.springboot.moa.result;

import com.springboot.moa.post.model.GetPostDetailRes;
import com.springboot.moa.result.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ResultDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}

    // 앞 인자는 다른 곳에서 받아올 수 있는 PK (다른 Res 에서 반환 된 값)
    public long insertResults(PostResultReq postResultReq) {
        String insertResultQuery = "INSERT INTO result (post_id, user_id) VALUES (?, ?)";
        Object[] insertResultsParams = new Object[]{postResultReq.getPostId(), postResultReq.getUserId()};
        this.jdbcTemplate.update(insertResultQuery, insertResultsParams);
        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, long.class);
    }

    public void insertResultDetails(long resultId, long postDetailId, String result) {
        String insertResultDetailsQuery = "INSERT INTO result_detail (result_id, post_detail_id, result) VALUES (?, ?, ?)";
        Object[] insertResultDetailsParams = new Object[]{resultId, postDetailId, result};
        this.jdbcTemplate.update(insertResultDetailsQuery, insertResultDetailsParams);
        String lastInsertIdxQuery = "select last_insert_id()";
        this.jdbcTemplate.queryForObject(lastInsertIdxQuery, long.class);
    }

    public int selectPostPoint(long postId){
        String selectPostPointQuery = "select point from post where post_id = ? and status = 'ACTIVE'";
        long selectPostPointParam = postId;
        try {
            return this.jdbcTemplate.queryForObject(selectPostPointQuery, int.class, selectPostPointParam);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public List<GetResultStatisticsRes> selectResult(long postDetailId) {

        String selectResultQuery = "select result from result_detail where post_detail_id = ? order by result";
        long selectResultParams = postDetailId;

        return this.jdbcTemplate.query(selectResultQuery,
                (rs, rowNum) -> new GetResultStatisticsRes(
                        rs.getString("result")
                ), selectResultParams);
    }

    // postDetailId가 존재하는지 확인 (질문의 번호가 올바른가)
    public int checkResultPostDetailId (long postDetailId){
        String checkResultPostDetailIdQuery = "select exists(select post_detail_id from post_detail where post_detail_id = ?);";
        long checkResultPostDetailIdParams = postDetailId;
        return this.jdbcTemplate.queryForObject(checkResultPostDetailIdQuery,
                int.class,
                checkResultPostDetailIdParams);
    }

    // 중복 답변 방지 (동일한 유저가 동일한 설문조사에 답변 달지 못하도록)
    public int checkDuplicatedResult(long postId, long userId) {
        String checkDuplicatedResultQuery = "select exists(select post_id, user_id from result where post_id = ? and user_id = ?)";
        Object[] checkDuplicatedResultParams = new Object[]{postId, userId};
        return this.jdbcTemplate.queryForObject(checkDuplicatedResultQuery,
                int.class,
                checkDuplicatedResultParams);
    }

    // 등록된 답변의 개수 출력
    public int countResult(long postDetailId) {
        String countResultResultQuery = "select count(result) from result_detail where post_detail_id = ?";
        long countResultParams = postDetailId;
        return this.jdbcTemplate.queryForObject(countResultResultQuery,
                int.class,
                countResultParams);
    }

    // 설문조사 문항 개수
    public int countPostDetail(long postId) {
        String countPostDetailQuery = "select count(post_detail_id) from post_detail where post_id = ?;";
        long countPostDetailParams = postId;
        return this.jdbcTemplate.queryForObject(countPostDetailQuery,
                int.class,
                countPostDetailParams);
    }

    // 설문조사 문항 시작 postDetailId
    public int startPostDetailId(long postId) {
        String startPostDetailIdQuery = "select post_detail_id from post_detail where post_id = ? group by post_id;";
        long startPostDetailIdParams = postId;
        return this.jdbcTemplate.queryForObject(startPostDetailIdQuery,
                int.class,
                startPostDetailIdParams);
    }

    // 설문조사 type 반환
    public int checkResultType(long postDetailId) {
        String checkResultTypeQuery = "select distinct format " +
                "from post_detail, result_detail " +
                "where post_detail.post_detail_id = result_detail.post_detail_id " +
                "and result_detail.post_detail_id = ?";
        long checkResultTypeParams = postDetailId;
        return this.jdbcTemplate.queryForObject(checkResultTypeQuery,
                int.class,
                checkResultTypeParams);
    }

    public GetResultNumberRes countResultByPostId(long postId){
        String countResultByPostIdQuery = "select count(result_id) as number from result where post_id = ?";
        long countResultByPostIdParams = postId;

        GetResultNumberRes getResultNumberRes = this.jdbcTemplate.queryForObject(countResultByPostIdQuery,
                (rs, rowNum) -> new GetResultNumberRes(
                        rs.getInt("number")),
                countResultByPostIdParams);

        return getResultNumberRes;
    }

    public int checkResultPostId (long postId){
        String checkResultPostIdQuery = "select exists(select post_id from result where post_id = ?);";
        long checkResultPostIdParams = postId;
        return this.jdbcTemplate.queryForObject(checkResultPostIdQuery,
                int.class,
                checkResultPostIdParams);
    }

    public int checkPostPostId (long postId){
        String checkPostPostIdQuery = "select exists(select post_id from post where post_id = ?);";
        long checkPostPostIdParams = postId;
        return this.jdbcTemplate.queryForObject(checkPostPostIdQuery,
                int.class,
                checkPostPostIdParams);
    }

    public String selectQuestion (long postDetailId) {
        String selectQuestionQuery = "select question from post_detail where post_detail_id =?";
        long selectQuestionParams = postDetailId;
        return this.jdbcTemplate.queryForObject(selectQuestionQuery,
                String.class,
                selectQuestionParams);
    }

    public List<GetResultItems> selectItems (long postDetailId) {
        String selectItemsQuery = "select item from question_detail where post_detail_id = ?";
        long selectItemsParams = postDetailId;
        return this.jdbcTemplate.query(selectItemsQuery,
                (rs, rowNum) -> new GetResultItems(
                        rs.getString("item")
                ), selectItemsParams);

    }
}