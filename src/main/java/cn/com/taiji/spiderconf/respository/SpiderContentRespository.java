package cn.com.taiji.spiderconf.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.taiji.spiderconf.domain.SpiderContent;


public interface SpiderContentRespository extends JpaRepository<SpiderContent, String>,JpaSpecificationExecutor<SpiderContent>,PagingAndSortingRepository<SpiderContent, String>{

}
