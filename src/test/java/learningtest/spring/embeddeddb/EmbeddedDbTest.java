package learningtest.spring.embeddeddb;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

public class EmbeddedDbTest {
	EmbeddedDatabase db;
	JdbcTemplate template;
	
	@Before
	public void setUp() {
		this.db = new EmbeddedDatabaseBuilder()
				.setType(HSQL)
				.addScript("classpath:/learningtest/spring/embeddeddb/schema.sql")
				.addScript("classpath:/learningtest/spring/embeddeddb/data.sql")
				.build();
		
		this.template = new JdbcTemplate(this.db);
	}
	
	@After
	public void tearDown() {
		this.db.shutdown();
	}
	
	@Test
	public void initData() {
		assertThat(this.template.queryForObject("select count(1) from sqlmap", null, Integer.class), is(2));
		
		List<Map<String, Object>> list = this.template.queryForList("select * from sqlmap order by key_");
		
		assertThat((String)list.get(0).get("key_"), is("KEY1"));
		assertThat((String)list.get(0).get("sql_"), is("SQL1"));
		assertThat((String)list.get(1).get("key_"), is("KEY2"));
		assertThat((String)list.get(1).get("sql_"), is("SQL2"));
	}
	
	@Test
	public void insert() {
		this.template.update("insert into sqlmap(key_, sql_) values (?,?)", "KEY3", "SQL3");
		
		assertThat(this.template.queryForObject("select count(1) from sqlmap", Integer.class), is(3));
	}
}
