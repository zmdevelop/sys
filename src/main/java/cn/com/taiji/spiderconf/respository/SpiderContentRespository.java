package cn.com.taiji.spiderconf.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import cn.com.taiji.spiderconf.domain.SpiderContent;


public interface SpiderContentRespository extends JpaRepository<SpiderContent, String>,JpaSpecificationExecutor<SpiderContent>,PagingAndSortingRepository<SpiderContent, String>{

	@Query("select s from SpiderContent s where s.spiderSite.id=:siteId")
	public List<SpiderContent> findBySiteId(@Param("siteId") String siteId);
}
