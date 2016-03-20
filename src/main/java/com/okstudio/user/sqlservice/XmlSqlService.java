package com.okstudio.user.sqlservice;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.okstudio.user.dao.UserDao;
import com.okstudio.user.sqlservice.jaxb.SqlType;
import com.okstudio.user.sqlservice.jaxb.Sqlmap;

public class XmlSqlService implements SqlService {
	private Map<String, String> sqlMap = new HashMap<String, String>();
	
	public XmlSqlService() {
		String contextPath = Sqlmap.class.getPackage().getName();
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream inputStream = UserDao.class.getResourceAsStream("sqlmap.xml");
			Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(inputStream);
			
			for(SqlType sql : sqlmap.getSql()) {
				sqlMap.put(sql.getKey(), sql.getValue());
			}
		} catch(JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getSql(String key) throws SqlRetrieveFailureException {
		String sql = sqlMap.get(key);
		if(sql == null) {
			 throw new SqlRetrieveFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
		} else {
			return sql;			
		}
	}

}
