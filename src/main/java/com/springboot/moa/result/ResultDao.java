package com.springboot.moa.result;

import com.springboot.moa.result.model.PostDetailResultReq;
import com.springboot.moa.result.model.PostResultReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ResultDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}

    // 앞 인자는 다른 곳에서 받아올 수 있는 PK (다른 Res 에서 반환 된 값)
    public int insertResults(int postId, PostResultReq postResultReq) {
        String insertResultQuery = "INSERT INTO result (post_id, user_id) VALUES (?, ?)";
        Object[] insertResultsParams = new Object[]{postId, postResultReq.getUserId()};
        this.jdbcTemplate.update(insertResultQuery, insertResultsParams);
        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    public int insertResultDetails(int resultId, PostDetailResultReq postDetailResultReq) {
        String insertResultDetailsQuery = "INSERT INTO result_detail (result_id, result) VALUES (?, ?)";
        Object[] insertResultDetailsParams = new Object[]{resultId, postDetailResultReq.getResult()};
        this.jdbcTemplate.update(insertResultDetailsQuery, insertResultDetailsParams);
        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

}
