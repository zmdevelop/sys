package cn.com.taiji.spiderconfig.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.taiji.spiderconfig.domain.SpiderConfig;

public interface SpiderConfigRespository extends JpaRepository<SpiderConfig, String>,JpaSpecificationExecutor<SpiderConfig>,PagingAndSortingRepository<SpiderConfig, String>{

}
