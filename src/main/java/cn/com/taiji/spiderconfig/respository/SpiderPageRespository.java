package cn.com.taiji.spiderconfig.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import cn.com.taiji.spiderconfig.domain.SpiderPage;

public interface SpiderPageRespository extends JpaRepository<SpiderPage, String>,JpaSpecificationExecutor<SpiderPage>,PagingAndSortingRepository<SpiderPage, String>{

	@Query("select p from SpiderPage p join p.spiderConfig c where c.id=:configId")
	List<SpiderPage> findByConfigId(@Param("configId") String id);

	

}
