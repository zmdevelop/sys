package cn.com.taiji.sys.domain;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 数据字典dao
 * @author SunJingyan
 * @date 2014年8月27日
 *
 */
public interface CodeRepository  extends JpaRepository<Code, String>,JpaSpecificationExecutor<Code>{

	/**
	 * 根据代码类型查询出业务代码
	 * @param codeType
	 * @return
	 */
	@Query("select c.codeCode from Code  c where c.codeType=:codeType") 
	List<String> findCodeByType(@Param("codeType") String codeType);
	
	/**
	 * 标记为删除
	 * @param id
	 */
	@Modifying
	@Query("update Code c set c.state=0,c.updateTime=:updateTime,c.updaterId=:updaterId where c.id=:id ")
	void updateState(@Param("id") String id,@Param("updateTime") Date updateTime,@Param("updaterId") String updaterId);
	
	/**
	 * 删除树节点
	 * @param id
	 */
	@Modifying
	@Query("update Code c set c.state=0,c.updateTime=:updateTime,c.updaterId=:updaterId where c.id= :id")
	void deleteTreeNode(@Param("id") String id,@Param("updateTime") Date updateTime,@Param("updaterId") String updaterId);
	
	/**
	 * 查询出未删除的所有数据字典记录集合
	 * @return
	 */
	@Query("select c from Code c where c.state=1") 
	List<Code> findAllCodes();
	
	/**
	 * 根据codeCode查询出记录
	 * @param codeCode
	 * @return
	 */
	@Query("select c from Code c where c.codeCode = :codeCode and c.id!= :id and c.state=1")
	Code findByCodeCode(@Param("codeCode") String codeCode,@Param("id") String id);
	


	/**
	 * 查询统一机构下创建的人
	 * @param code
	 * @return
	 */
	@Query("select c from Code c where c.code = :parent and   c.deptId=:deptId    and  c.state=1")
	List<Code> findAllChildCodesByDeptId(@Param("parent") Code code,@Param("deptId") String deptId);
	
	
	/**
	 * 查询出子节点集合
	 * @param code
	 * @return
	 */
	@Query("select c from Code c where c.code = :parent and c.state=1")
	List<Code> findAllChildCodes(@Param("parent") Code code);
	
	@Query("select c from Code c where c.code = null and c.category = 1 and c.state=1")
	List<Code> findRoots();
	/**
	 * 获得单级数据字典
	 * @return
	 */
	@Query("select c from Code c where  c.category = 0 and c.state=1   ")
	List<Code> findAllSingle();
	/**
	 * 按创建机构获得单级数据字典
	 * @param deptId
	 * @return
	 */
	@Query("select c from Code c where  c.category = 0 and c.state=1  and     (c.deptId in (:deptIds)) ")
	List<Code> findAllSingleByDeptId(@Param("deptIds") List<String> deptIds);
	
	
}
