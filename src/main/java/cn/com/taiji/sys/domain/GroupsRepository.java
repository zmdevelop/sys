package cn.com.taiji.sys.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 用户组Group dao层
 * @author SunJingyan
 * @date 2015年1月12日
 *
 */
public interface GroupsRepository extends JpaRepository<Groups, String>,JpaSpecificationExecutor<Groups>{

	/**
	 * 标记为删除
	 * @param id
	 * @param updateTime
	 * @param updaterId
	 */
	@Modifying
	@Query("update Groups g set g.flag=0,g.updateTime=:updateTime,g.updaterId=:updaterId where g.groupId=:id ")
	void updateFlag(@Param("id") String id,@Param("updateTime") Date updateTime,@Param("updaterId") String updaterId);
	
	/**
	 * 查询出userId集合
	 * @param groupId
	 * @return
	 */
	@Query("select k.id.userId from UserGroup k where k.id.groupId =:groupId")
	List<String> findUserIdsByGroupId(@Param("groupId") String groupId);
}
