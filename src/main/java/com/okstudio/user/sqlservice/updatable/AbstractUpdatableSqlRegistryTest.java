package com.okstudio.user.sqlservice.updatable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.okstudio.user.sqlservice.SqlNotFoundException;

public abstract class AbstractUpdatableSqlRegistryTest {
	UpdatableSqlRegistry sqlRegistry;
	
	abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();
	
	@Before
	public void setUp() {
		this.sqlRegistry = this.createUpdatableSqlRegistry();
		sqlRegistry.registerSql("KEY1", "SQL1");
		sqlRegistry.registerSql("KEY2", "SQL2");
		sqlRegistry.registerSql("KEY3", "SQL3");		
	}
	
	@Test
	public void find() {
		this.checkFind("SQL1", "SQL2", "SQL3");
	}
	
	protected void checkFind(String expected1, String expected2, String expected3) {
		assertThat(this.sqlRegistry.findSql("KEY1"), is(expected1));
		assertThat(this.sqlRegistry.findSql("KEY2"), is(expected2));
		assertThat(this.sqlRegistry.findSql("KEY3"), is(expected3));		
	}
	
	@Test(expected=SqlNotFoundException.class)
	public void unknownKey() {
		this.sqlRegistry.findSql("SQL9999!@#$");
	}
	
	@Test
	public void updateSingle() {
		this.sqlRegistry.updateSql("KEY2", "Modified2");
		this.checkFind("SQL1", "Modified2", "SQL3");
	}
	
	@Test
	public void updateMulti() {
		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap.put("KEY1", "Modified1");
		sqlmap.put("KEY3", "Modified3");
		
		this.sqlRegistry.updateSql(sqlmap);
		this.checkFind("Modified1", "SQL2", "Modified3");
	}
	
	@Test(expected=SqlUpdateFailureException.class)
	public void updateWithNotExistingKey() {
		this.sqlRegistry.updateSql("SQL9999!@#$", "Modified2");
	}
}
