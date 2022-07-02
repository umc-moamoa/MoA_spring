package com.springboot.moa.involve;

import com.springboot.moa.involve.model.GetInvolvesRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class InvolveDao {
    private JdbcTemplate jdbcTemplate;



    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetInvolvesRes> selectInvolve(int postId) {
        String selectInvolveQuery = "\n" +
                "        SELECT \n" +
                "            pd.post_detail_id as post_detail_id,\n" +
                "            pd.question as question,\n" +
                "            pd.type as type\n" +
                "        FROM post_detail as pd\n" +
                "where pd.post_id=?";
        int selectInvolveParam = postId;


        return this.jdbcTemplate.query(selectInvolveQuery,
                (rs, rowNum) -> new GetInvolvesRes(
                        rs.getInt("post_detail_id"),
                        rs.getString("question"),
                        rs.getInt("type")
                ),selectInvolveParam);

    }

    public int checkInvolveExist(int postId) {
        String checkPostExistQuery = "select exists(select post_id from post_detail where post_id = ?)";
        int checkInvolveExistParams = postId;
        return this.jdbcTemplate.queryForObject(checkPostExistQuery,
                int.class,
                checkInvolveExistParams);

    }



}
