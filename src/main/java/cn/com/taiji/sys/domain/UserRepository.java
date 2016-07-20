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
 * 用户dao
 * @author SunJingyan
 * @date 2014年4月21日
 *
 */
public interface UserRepository extends JpaRepository<User, String>,JpaSpecificationExecutor<User>,PagingAndSortingRepository<User,String>{

	/**
	 * 根据登录名称查询用户
	 * @param loginName
	 * @return
	 */
	@Query("select u from User u where u.loginName =:loginName and u.flag=1")
	User findByLoginName(@Param("loginName") String loginName);

	/**
	 * 根据登录名称和密码查询用户
	 * @param loginName
	 * @param password
	 * @return
	 */
	@Query("SELECT u FROM User u WHERE u.loginName = :loginName and u.password =:password and u.flag=1")
	User login(@Param("loginName") String loginName,@Param("password") String password);

	/**
	 * 标记为删除
	 * @param id
	 */
	@Modifying
	@Query("update User u set u.flag=0,u.updateTime=:updateTime,u.updaterId=:updaterId where u.userId=:id")
	void updateFlag(@Param("id") String id,@Param("updateTime") Date updateTime,@Param("updaterId") String updaterId);

	/**
	 * 查询所有未标记为删除的用户集合
	 * @return
	 */
	@Query("select u from User u where u.flag=1 order by u.userIndex")
	List<User> findAllUsers();
	
	@Query("select u from User u where u.flag=1 order by createTime desc")
	List<User> findAllUsers2();
	
	/**
	 * 查询所有未标记为删除并且状态为激活的用户集合
	 * @return
	 */
	@Query("select u from User u where u.flag=1 and u.state=1 order by u.userIndex")
	List<User> findActivationAllUsers();

	/**
	 * 修改密码
	 * @param userId
	 * @param pwd
	 */
	@Modifying
	@Query("update User u set u.password=:pwd where u.userId=:userId")
	void updatePwd(@Param("userId") String userId,@Param("pwd") String pwd);

	/**
	 * 根据loginName查询出记录
	 * @param loginName
	 * @return
	 */
	@Query("select u from User u where u.loginName = :loginName and u.userId!= :id and u.flag=1")
	List<User> findByUserName(@Param("loginName") String loginName,@Param("id") String id);
	
	@Query("select u from User u where u.loginName = :loginName and u.flag=1")
	List<User> findByUserName2(@Param("loginName") String loginName);
	/**
	 * 根据用户手机号码查询用户
	 * @param phone
	 */
	@Query("select u from User u where u.phoneNum = :phoneNum  and u.flag=1")
	List<User>  findUserByPhone(@Param("phoneNum")  String phoneNum);

}
