package cn.com.taiji.spiderconf.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.taiji.spiderconf.domain.SpiderSite;


public interface SpiderSiteRespository extends JpaRepository<SpiderSite, String>,JpaSpecificationExecutor<SpiderSite>,PagingAndSortingRepository<SpiderSite, String>{

}
