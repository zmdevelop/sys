package cn.com.taiji.spiderconf.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.com.taiji.spiderconf.domain.SpiderChannel;


public interface SpiderChannelRespository extends JpaRepository<SpiderChannel, String>,JpaSpecificationExecutor<SpiderChannel>,PagingAndSortingRepository<SpiderChannel, String>{

}
