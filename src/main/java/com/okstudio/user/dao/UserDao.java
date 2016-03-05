package com.okstudio.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.okstudio.user.domain.User;

public class UserDao {	
	private JdbcTemplate jdbcTemplate;
		
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);		
//		this.dataSource = dataSource;
	}

	public void add(final User user) throws SQLException {
		this.jdbcTemplate.update(
			"insert into users(id, name, password) values(?,?,?)",
			user.getId(),
			user.getName(),
			user.getPassword()
		);		
	}

	public User get(String id) throws SQLException {
		return this.jdbcTemplate.queryForObject("select * from users where id = ?",
			new Object[] {id},
			new RowMapper<User>(){
				public User mapRow(ResultSet resultSet, int inx) throws SQLException {
					User user = new User();
					user.setId(resultSet.getString("id"));
					user.setName(resultSet.getString("name"));
					user.setPassword(resultSet.getString("password"));
					return user;
				}
		});
	}

	public void deleteAll() throws SQLException {
		this.jdbcTemplate.update("delete from users");
	}

	public int getCount() throws SQLException  {
		return this.jdbcTemplate.queryForObject("select count(1) from users", null, Integer.class);		
	}
	
	public List<User> getAll() throws SQLException {
		return this.jdbcTemplate.query("select * from users order by id",
			new RowMapper<User>() {
				public User mapRow(ResultSet resultSet, int inx) throws SQLException {
					User user = new User();
					user.setId(resultSet.getString("id"));
					user.setName(resultSet.getString("name"));
					user.setPassword(resultSet.getString("password"));
					return user;
				}
		});
	}
}
