package cn.com.taiji.sys.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserGroupRepository extends JpaRepository<UserGroup, String>,JpaSpecificationExecutor<UserGroup>{

}
