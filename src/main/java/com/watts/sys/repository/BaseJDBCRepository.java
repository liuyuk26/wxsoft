package com.watts.sys.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
 
@Repository
public class BaseJDBCRepository {
	@Autowired
	public JdbcTemplate jdbcTemplate;
	protected static Logger logger = LoggerFactory.getLogger(BaseJDBCRepository.class);

	public List jdbcQueryList(String sql, Class model) {
		Table tableModel = (Table) model.getAnnotation(Table.class);
		sql = "SELECT * FROM " + tableModel.name() + " " + sql;
		logger.info("jdbcQueryList:" + sql);
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(model));
	}

	public List jdbcSqlQueryList(String sql, Class model) {
		logger.info("jdbcQueryList:" + sql);
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(model));
	}

	public Integer jdbcSqlSize(String sql) {
		logger.info("jdbcSqlSize:" + sql);
		return jdbcTemplate.queryForObject(sql, null, Integer.class);
	}

	public String jdbcSqlField(String sql) {
		logger.info("jdbcSqlField:" + sql);
		return jdbcTemplate.queryForObject(sql, null, String.class);
	}

	public String[] jdbcSqlFieldList(String sql) {
		logger.info("jdbcSqlFieldList:" + sql);
		return jdbcTemplate.queryForObject(sql, null, String[].class);
	}
	
	public List<JSONObject> getJSONObject(String sql) {
		List<JSONObject> jsonLst = null;

		logger.info("getJSONObject:" + sql);
		jsonLst = jdbcTemplate.query(sql, new ResultRowMapper());
		 
		return (jsonLst != null && jsonLst.size() > 0) ? jsonLst : null;
	}

	public class ResultRowMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			JSONObject json = new JSONObject(true);
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				//System.out.println(rs.getMetaData().getColumnName(i));
				if (rs.getObject(i) == null) {
					json.put(rs.getMetaData().getColumnName(i), "NaN");
				} else {
					json.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
				}
			}
			return json;
		}

	}
}
