package cn.com.taiji.sys.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * 角色表dao
 * @author SunJingyan
 * @date 2014年4月21日
 *
 */
public interface  RoleRepository extends JpaRepository<Role, String>,
		JpaSpecificationExecutor<Role>,PagingAndSortingRepository<Role, String>{
	
	/**
	 * 根据角色id查询菜单集合
	 * @param roleId
	 * @return
	 */
	@Query("SELECT m FROM Role u join u.menus m WHERE u.roleId = :roleId order by m.menuIndex")
	List<Menu> findMenuByRoleId(@Param("roleId") String roleId);

	/**
	 * 标记为删除
	 * @param id
	 */
	@Modifying
	@Query("update Role m set m.flag=0,m.updateTime=:updateTime,m.updaterId=:updaterId where m.roleId=:id")
	void updateFlag(@Param("id") String id,@Param("updateTime") Date updateTime,@Param("updaterId") String updaterId);
	
	/**
	 * 查询所有未标记为删除的角色集合
	 * @return
	 */
	@Query("select r from Role r")
	List<Role> findAllRoles();
	
	/**
	 * 根据roleName查询出记录
	 * @param roleName
	 * @return
	 */
	@Query("select r from Role r where r.roleName = :roleName and r.roleId!= :id")
	List<Role> findByRoleName(@Param("roleName") String roleName,@Param("id") String id);

	/**
	 * @param userId
	 * @return
	 */
	@Query("select r from Role r join r.users u where u.userId=:userId")
	List<Role> findByUserId(@Param("userId") String userId);
}
