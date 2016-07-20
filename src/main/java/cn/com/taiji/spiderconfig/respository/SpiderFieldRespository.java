package cn.com.taiji.spiderconfig.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.taiji.spiderconfig.domain.SpiderField;

public interface SpiderFieldRespository extends JpaRepository<SpiderField, String>,JpaSpecificationExecutor<SpiderField>,PagingAndSortingRepository<SpiderField, String>{

}
