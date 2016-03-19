package com.okstudio.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.okstudio.user.domain.Level;
import com.okstudio.user.domain.User;
import com.okstudio.user.sqlservice.SqlService;

public class UserDaoJdbc implements UserDao {

	private JdbcTemplate jdbcTemplate;
	private SqlService sqlService;
	
	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}
//	private Map<String, String> sqlMap;
//	
//	public void setSqlMap(Map<String, String> sqlMap) {
//		this.sqlMap = sqlMap;
//	}
	
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
			this.sqlService.getSql("userAdd"),
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
		return this.jdbcTemplate.queryForObject(
			this.sqlService.getSql("userGet"),			
			new Object[] {id},
			this.userMapper
		);
	}
	
	public void update(User user) {
		this.jdbcTemplate.update(
			this.sqlService.getSql("userUpdate"),			
			user.getName(),
			user.getPassword(),
			user.getLevel().intValue(),
			user.getLogin(),
			user.getRecommend(),
			user.getEmail(),
			user.getId());
	}

	public void deleteAll() {
		this.jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));
	}

	public int getCount()  {
		return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGetCount"), null, Integer.class);		
	}
	
	public List<User> getAll(){
		return this.jdbcTemplate.query(this.sqlService.getSql("userGetAll"), this.userMapper);
	}
}
