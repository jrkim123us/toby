package com.okstudio.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.okstudio.user.domain.Level;
import com.okstudio.user.domain.User;

public class UserDaoJdbc implements UserDao {

	private JdbcTemplate jdbcTemplate;
	private String sqlAdd;
	
	public void setSqlAdd(String sqlAdd) {
		this.sqlAdd = sqlAdd;
	}
	
	private RowMapper<User> userMapper = 
		new RowMapper<User>() {
			public User mapRow(ResultSet resultSet, int inx) throws SQLException {
				User user = new User();
				user.setId(resultSet.getString("id"));
				user.setName(resultSet.getString("name"));
				user.setPassword(resultSet.getString("password"));
				user.setLevel(Level.valueOf(resultSet.getInt("level")));
				user.setLogin(resultSet.getInt("login"));
				user.setRecommend(resultSet.getInt("recommend"));
				user.setEmail(resultSet.getString("email"));
				return user;
			}
		};
		
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void add(final User user) {
		this.jdbcTemplate.update(
			this.sqlAdd,
//			"insert into users(id, name, password, level, login, recommend, email) values(?,?,?,?,?,?,?)",
			user.getId(),
			user.getName(),
			user.getPassword(),
			user.getLevel().intValue(),
			user.getLogin(),
			user.getRecommend(),
			user.getEmail()
		);		
	}

	public User get(String id) {
		return this.jdbcTemplate.queryForObject("select * from users where id = ?",
			new Object[] {id},
			this.userMapper
		);
	}
	
	public void update(User user) {
		this.jdbcTemplate.update(
			"update users set name=?,password=?,level=?,login=?,recommend=?,email=? where id=?",
			user.getName(),
			user.getPassword(),
			user.getLevel().intValue(),
			user.getLogin(),
			user.getRecommend(),
			user.getEmail(),
			user.getId());
	}

	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}

	public int getCount()  {
		return this.jdbcTemplate.queryForObject("select count(1) from users", null, Integer.class);		
	}
	
	public List<User> getAll(){
		return this.jdbcTemplate.query("select * from users order by id", this.userMapper);
	}

}
