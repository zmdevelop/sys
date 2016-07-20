package cn.com.taiji.spiderconfig.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.taiji.spiderconfig.domain.SpiderSeed;

public interface SpiderSeedRespository extends JpaRepository<SpiderSeed, String>,JpaSpecificationExecutor<SpiderSeed>,PagingAndSortingRepository<SpiderSeed, String>{

}
