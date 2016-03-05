package com.okstudio.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class JdbcContext {
	private DataSource datasource;
	
	public void setDataSource(DataSource dataSource) {
		this.datasource = dataSource;
	}
	
	public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = this.datasource.getConnection();
			
			preparedStatement = stmt.makePreparedStatement(connection);
			
			preparedStatement.executeQuery();
		} catch(SQLException e) {
			throw e;
		} finally {
			if (preparedStatement != null) { try{ preparedStatement.close();} catch(SQLException e){}}
			if (connection != null) {try{connection.close();} catch(SQLException e) {}}
		}		
	}
	
	public void executeSql(final String query) throws SQLException {
		this.workWithStatementStrategy(
			new StatementStrategy() {				
				@Override
				public PreparedStatement makePreparedStatement(Connection c) throws SQLException {					
					return c.prepareStatement(query);
				}
			}
		);
	}

}
