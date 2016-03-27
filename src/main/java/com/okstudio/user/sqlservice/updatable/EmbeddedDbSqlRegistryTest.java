package com.okstudio.user.sqlservice.updatable;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
	EmbeddedDatabase db;
	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		this.db = new EmbeddedDatabaseBuilder()
						.setType(EmbeddedDatabaseType.HSQL)
						.addScript("classpath:com/okstudio/user/sqlservice/updatable/sqlRegistrySchema.sql")
						.build();
		
		EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
		embeddedDbSqlRegistry.setDataSource(db);
		
		return embeddedDbSqlRegistry;
	}
	
	@Test
	public void transactionalUpdate() {
		this.checkFind("SQL1", "SQL2", "SQL3");
		
		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap.put("KEY1", "Modified1");
		sqlmap.put("KEY9999!@#$", "Modified9999");
		
		try {
			sqlRegistry.updateSql(sqlmap);
//			fail();
		}
		catch(SqlUpdateFailureException e) {}
		
		this.checkFind("SQL1", "SQL2", "SQL3");
	}
	
	@After
	public void tearDown() {
		this.db.shutdown();
	}
}
