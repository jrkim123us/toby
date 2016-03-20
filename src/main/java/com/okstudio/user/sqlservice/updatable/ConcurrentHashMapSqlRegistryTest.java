package com.okstudio.user.sqlservice.updatable;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest{
	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {		
		return new ConcurrentHashMapSqlRegistry();
	}
}
