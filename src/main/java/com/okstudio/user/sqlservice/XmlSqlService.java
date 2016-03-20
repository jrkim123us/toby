package com.okstudio.user.sqlservice;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.okstudio.user.dao.UserDao;
import com.okstudio.user.sqlservice.jaxb.SqlType;
import com.okstudio.user.sqlservice.jaxb.Sqlmap;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {
	private SqlReader sqlReader;
	private SqlRegistry sqlRegistry;
	
	private Map<String, String> sqlMap = new HashMap<String, String>();
	private String sqlmapFile;
	
	public XmlSqlService() {
	}	
	
	@PostConstruct
	public void loadSql() {
		this.sqlReader.read(this.sqlRegistry);	
	}
	
	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}
	
	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}		
	
	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}
	
	public String findSql(String key) throws SqlNotFoundException {
		String sql = this.sqlMap.get(key);
		if(sql == null) {
			throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다.");
		} else {
			return sql;
		}
	}
	
	public void registerSql(String key, String sql) {
		this.sqlMap.put(key, sql);
	};
	
	public void read(SqlRegistry sqlRegistry) {
		String contextPath = Sqlmap.class.getPackage().getName();
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream inputStream = UserDao.class.getResourceAsStream(this.sqlmapFile);
			Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(inputStream);
			
			for(SqlType sql : sqlmap.getSql()) {
				this.sqlRegistry.registerSql(sql.getKey(), sql.getValue());
			}
		} catch(JAXBException e) {
			throw new RuntimeException(e);
		}
	};

	@Override
	public String getSql(String key) throws SqlRetrieveFailureException {
		try {
			return this.sqlRegistry.findSql(key);
		} catch(SqlNotFoundException e) {
			throw new SqlRetrieveFailureException(e.getMessage(), e.getCause());
		}		
	}

}
