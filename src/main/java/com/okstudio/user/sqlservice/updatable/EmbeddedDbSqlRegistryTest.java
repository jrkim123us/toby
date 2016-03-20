package com.okstudio.user.sqlservice.updatable;

import org.junit.After;
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
	
	@After
	public void tearDown() {
		this.db.shutdown();
	}
}
