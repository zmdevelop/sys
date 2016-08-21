package cn.com.taiji.spiderconf.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import cn.com.taiji.spiderconf.domain.SpiderField;

public interface SpiderFieldRespository extends JpaRepository<SpiderField, String>,JpaSpecificationExecutor<SpiderField>,PagingAndSortingRepository<SpiderField, String>{

	@Query("select s from SpiderField s where s.spiderContent.id = :contentId")
	List<SpiderField> findByContentId(@Param("contentId") String contentId);

}
