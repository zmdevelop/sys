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
 * 菜单表dao
 * @author SunJingyan
 * @date 2014-04-21
 *
 */
public interface  MenuRepository extends JpaRepository<Menu, String>,JpaSpecificationExecutor<Menu>,PagingAndSortingRepository<Menu, String>{

	/**
	 * 查询菜单树
	 * @return
	 */
	@Query("select o from Menu o left  join fetch  o.children c  where c.flag=1")
	public List<Menu> findMenuTree();
	
//	@Query("select o from Menu o where o.parent is null and o.parent.menuId=:parent_id")
//	List<Menu> findMenuByRootId(@Param("parent_id") String parent_id);

	/**
	 * 查询出树根
	 * @return
	 */
	@Query("select o from Menu o where o.parent is null order by o.menuIndex")
	List<Menu> findRoots();
	
	/**
	 * 标记为删除
	 * @param id
	 */
	@Modifying
	@Query("update Menu m set m.flag=0,m.updateTime=:updateTime,m.updaterId=:updaterId where m.menuId=:id")
	void updateFlag(@Param("id") String id,@Param("updateTime") Date updateTime,@Param("updaterId") String updaterId);
	
	/**
	 * 根据描述查询出树根
	 * @param menuDesc
	 * @return
	 */
	@Query("select o from Menu o where o.parent.menuId=:menuId order by o.menuIndex")
	List<Menu> findRoots(@Param("menuId") String menuId);
	
	/**
	 * 查询所有菜单（未标记未删除的）
	 * @return
	 */
	@Query("select m from Menu m")
	List<Menu> findAllMenus();

	/**
	 * @param userId
	 * @return
	 */
	@Query("select m from Menu m")
	public List<Menu> findByUser(String userId);

	/**
	 * @param roleId
	 * @return
	 */
	@Query("select m from Menu m join m.roles r where r.roleId=:roleId")
	List<Menu> findMenuByRoleId(@Param("roleId") String roleId);

}
