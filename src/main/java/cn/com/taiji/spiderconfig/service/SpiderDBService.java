package cn.com.taiji.spiderconfig.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.inject.Inject;

import net.kernal.spiderman.kit.Properties;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.taiji.sys.util.UUIDUtils;

@Service
public class SpiderDBService {

	
	
	@Inject
	JdbcTemplate jdbcTemplate;
	
	@Transactional
	public void insertDb(Properties pro)
	{
		String id = UUIDUtils.getUUID32();
		jdbcTemplate.update("insert into spider_content (id,title,content) values (?,?,?)",new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, id);
				ps.setString(2, pro.getString("title"));
				ps.setString(3, pro.getString("text"));
			}
			
		});
	}
	
	
}
