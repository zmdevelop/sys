package cn.com.taiji.sys.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaBuilder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

import cn.com.taiji.sys.domain.Dept;
import cn.com.taiji.sys.domain.User;
import cn.com.taiji.sys.domain.UserRepository;
import cn.com.taiji.sys.dto.UserDto;
import cn.com.taiji.sys.exception.BusinessException;
import cn.com.taiji.sys.util.Pagination;

/**
 * 用户Service
 * @author zhangjb
 * @date 2016-06-06
 */

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	
    public Pagination<UserDto> findAllUser(final UserDto dto,Pagination<UserDto> pag) {

	List<Order> orders = new ArrayList<Sort.Order>();
	Order order = new Order(Direction.DESC, "createTime");
	orders.add(order);
	Sort sort = new Sort(orders);
	PageRequest request = new PageRequest(pag.getPageCurrentNum() - 1,
		pag.getPageSize(), sort);
	Page<User> page;
	page = userRepository.findAll(new Specification<User>() {
	    @Override
	    public Predicate toPredicate(Root<User> root,
		    CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> pl = new ArrayList<Predicate>();
//		pl.add(cb.isNotNull(root.get("parentname")));
//		pl.add(cb.equal(root.get("flag"), "1"));
//		if (dto.getFormName() != null && !"".equals(dto.getFormName())) {
//		    pl.add(cb.like(root.<String> get("formName"),
//			    "%" + dto.getFormName() + "%"));
//		}
//		if (dto.getFyear() != null && !"".equals(dto.getFyear())) {
//		    pl.add(cb.like(root.<String> get("fyear"),
//			    "%" + dto.getFyear() + "%"));
//		}
//		if (dto.getStatus() != null && !"".equals(dto.getStatus())) {
//		    pl.add(cb.equal(root.get("status"), dto.getStatus()));
//		}
//		if (dto.getFormType() != null && !"".equals(dto.getFormType())) {
//		    pl.add(cb.equal(root.get("formType"), dto.getFormType()));
//		}
		return cb.and(pl.toArray(new Predicate[0]));
	    }

	}, request);

	List<UserDto> dtoList = findDTOByPage(page);
	pag.setPageResult(dtoList);
	pag.setPageCount(page.getTotalPages());
	pag.setPageTotal((int) page.getTotalElements());

	return pag;
    }
	
	public List<UserDto> among(){
		List<User> userlist= userRepository.findAllUsers2();
		List<UserDto> ude = new ArrayList<UserDto>();
		
		UserDto user= new UserDto();
		for(User us:userlist){
			user.setUserId(us.getUserId());
			user.setCreateTime(us.getCreateTime());
			user.setCreatorId(us.getCreatorId());
			user.setEmail(us.getEmail());
			user.setFlag(us.getFlag());
			user.setLoginName(us.getLoginName());
			user.setPassword(us.getPassword());
			user.setPhoneNum(us.getPhoneNum());
			user.setRemark(us.getRemark());
			user.setState(us.getState());
			user.setUpdateTime(us.getUpdateTime());
			user.setUpdaterId(us.getUpdaterId());
			user.setUserIndex(us.getUserIndex());
			user.setUserName(us.getUserName());
			ude.add(user);
		}
		
		return ude;
	}
	
	/**
	 * 添加用户信息
	 * @param list
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void add(UserDto usdt)
	{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtils.isEmpty(usdt.getToken()))
		{
			if(!findByName(usdt.getLoginName()))
			{
				throw new BusinessException("用户名["+usdt.getLoginName()+"]已存在！");
			}
			//新增
			User user = new User();
			UserDto dto = new UserDto();
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			dto.setUserId(id); 
			dto.setFlag(1);//初始化 
//			dto.setState("1");  //用户状态
			//有登录要放开****
//			dto.setCreatorId(userDetails.getUsername());
//			dto.setUpdaterId(userDetails.getUsername());
			dto.setLoginName(usdt.getLoginName());
			dto.setPassword(usdt.getPassword());
			dto.setEmail(usdt.getEmail());
			dto.setUserName(usdt.getUserName());
			dto.setPhoneNum(usdt.getPhoneNum());
			dto.setRemark(usdt.getRemark());
			String createTimeStr = sdf.format(new Date());
			String updateTimeStr = sdf.format(new Date());
			Date createTime;
			Date updateTime;
			try {
				createTime = sdf.parse(createTimeStr);
				updateTime = sdf.parse(updateTimeStr);
				dto.setCreateTime(createTime);
				dto.setUpdateTime(updateTime);
			} catch (ParseException e) {
			}
			BeanUtils.copyProperties(dto, user);
			this.userRepository.saveAndFlush(user);
		}
	}
	
	/**
	 * 编辑用户信息
	 * @param list
	 */
	public UserDto findUserDtoByUserId(String id){
		UserDto dto = new UserDto();
		BeanUtils.copyProperties(this.userRepository.findOne(id), dto);
		return dto;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void edit(UserDto usdt)
	{
		if(!(usdt.getUserId()).equals("") && !(usdt.getLoginName()).equals(""))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			if(!findByName(usdt.getLoginName()))
//			{
//				throw new BusinessException("用户名["+usdt.getLoginName()+"]已存在！");
//			}
			User user = new User();
//			UserDto dto = new UserDto();
//			dto.setLoginName(usdt.getLoginName());
//			dto.setUserName(usdt.getUserName());
//			dto.setEmail(usdt.getEmail());
//			dto.setPhoneNum(usdt.getPhoneNum());
//			dto.setRemark(usdt.getRemark());
			String updateTimeStr = sdf.format(new Date());
			Date updateTime;
			try {
					updateTime = sdf.parse(updateTimeStr);
					usdt.setUpdateTime(updateTime);
//					dto.setUpdaterId(userDetails.getUsername());
			} catch (ParseException e) {
			}
			BeanUtils.copyProperties(usdt, user);
			this.userRepository.saveAndFlush(user);
				
		}
	}
	
	
	/**
	 * 查询出更新后的数据
	 * @return
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<UserDto> findUsers(List<UserDto> list,String salt)
	{
		List<UserDto> newList = new ArrayList<UserDto>();
		if(list!=null && list.size()>0)
		{
			for(UserDto d:list)
			{
				String id = d.getUserId();
				User user = this.userRepository.findOne(id);
				if(user!=null)
				{
					UserDto dto = new UserDto();
					BeanUtils.copyProperties(user, dto);
					dto.generateToken(salt);
					newList.add(dto);
				}
			}
		}
		return newList;
	}
	
	
	/**
	 * 根据用户名查询
	 * @param 
	 * @return
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public boolean findByName(String loginName)
	{
		List<User> users = new ArrayList<User>();
		if(loginName!=null && loginName.length()>0)
		{
			users = this.userRepository.findByUserName2(loginName);
			if(users.size()>0)
			{
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 根据用户名查询用户
	 * @param loginName
	 * @return
	 */
	 @Cacheable(value = "usersCache")
	//@Transactional(propagation=Propagation.SUPPORTS)
	public User findByLoginName(String loginName)
	{
		User user=this.userRepository.findByLoginName(loginName);
		return user;
	}
	
	
	/**
	 * 登录查询
	 * @param loginName
	 * @param password
	 * @return
	 */
	 @Cacheable(value="userCache",key="#loginName.concat(#password)") 
	//@Transactional(propagation=Propagation.SUPPORTS)
	public User login(String loginName,String password)
	{
		 System.out.println(loginName+"===="+password);
	    return this.userRepository.login(loginName, password);	
	}
	
	
    public List<UserDto> findDTOByPage(Page<User> page) {
	List<UserDto> dtoList = new ArrayList<UserDto>();
	List<User> listp = page.getContent();
	for (User p : listp) {
		UserDto pdto = new UserDto();
	    if (p != null) {
		BeanUtils.copyProperties(p, pdto);
		dtoList.add(pdto);
	    }
	}
	return dtoList;
    }
}
