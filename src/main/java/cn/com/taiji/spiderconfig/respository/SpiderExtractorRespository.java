package cn.com.taiji.spiderconfig.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.taiji.spiderconfig.domain.SpiderExtractor;

public interface SpiderExtractorRespository extends JpaRepository<SpiderExtractor, String>,JpaSpecificationExecutor<SpiderExtractor>,PagingAndSortingRepository<SpiderExtractor, String>{

}
