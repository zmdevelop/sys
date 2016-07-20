package cn.com.taiji.spiderconfig.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import cn.com.taiji.spiderconfig.domain.SpiderRule;

public interface SpiderRuleRespository extends JpaRepository<SpiderRule, String>,JpaSpecificationExecutor<SpiderRule>,PagingAndSortingRepository<SpiderRule, String>{

}
