//package cn.com.taiji.sys.service;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//import javax.inject.Inject;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.BeanUtils;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.domain.Sort.Direction;
//import org.springframework.data.domain.Sort.Order;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import cn.com.taiji.sys.domain.Groups;
//import cn.com.taiji.sys.domain.GroupsRepository;
//import cn.com.taiji.sys.domain.User;
//import cn.com.taiji.sys.domain.UserGroup;
//import cn.com.taiji.sys.domain.UserGroupPK;
//import cn.com.taiji.sys.domain.UserGroupRepository;
//import cn.com.taiji.sys.domain.UserRepository;
//import cn.com.taiji.sys.dto.GroupDto;
//import cn.com.taiji.sys.dto.UserDto;
//
///**
// * 用户组Group Service
// * @author SunJingyan
// * @date 2015年1月12日
// *
// */
//@Service
//public class GroupService {
//	
//	@Inject
//	private GroupsRepository groupRepository;
//	
//	@Inject
//	private UserRepository userRepository;
//	
//	@Inject
//	private UserGroupRepository userGroupRepository;
//	
//	/**
//	 * 查询用户组信息
//	 * @param searchParameters
//	 *        查询参数的map集合
//	 * @return
//	 *       查询的结果,map类型
//	 *       total:总条数
//	 *       codes:查询结果list集合
//	 */
//	@Transactional(propagation=Propagation.SUPPORTS)
//	public Map getPage(final Map searchParameters,String salt) {
//		Map map = new HashMap();
//		int page = 0;
//		int pageSize = 10;
//		Page<Groups> pageList;
//		if(searchParameters!=null && searchParameters.size()>0 && searchParameters.get("page")!=null)
//		{
//			page = Integer.parseInt(searchParameters.get("page").toString()) - 1;
//		}
//		if(searchParameters!=null && searchParameters.size()>0 && searchParameters.get("pageSize")!=null)
//		{
//			pageSize = Integer.parseInt(searchParameters.get("pageSize").toString());
//		}
//		if (pageSize < 1)
//			pageSize = 1;
//		if (pageSize > 100)
//			pageSize = 100;
//		List<Map> orderMaps = (List<Map>) searchParameters.get("sort");
//		List<Order> orders = new ArrayList<Order>();
//		if (orderMaps != null) {
//			for (Map m : orderMaps) {
//				if (m.get("field") == null)
//					continue;
//				String field = m.get("field").toString();
//				if (!StringUtils.isEmpty(field)) {
//					String dir = m.get("dir").toString();
//					if ("DESC".equalsIgnoreCase(dir)) {
//						orders.add(new Order(Direction.DESC, field));
//					} else {
//						orders.add(new Order(Direction.ASC, field));
//					}
//				}
//			}
//		}
//		PageRequest pageable;
//		if (orders.size() > 0) 
//		{
//			pageable = new PageRequest(page, pageSize, new Sort(orders));
//		} 
//		else 
//		{
//			Sort sort = new Sort(Direction.ASC,"groupIndex");
//			pageable = new PageRequest(page, pageSize,sort);
//		}
//		Map filter = (Map) searchParameters.get("filter");
//		if (filter != null) 
//		{
////			String logic = filter.get("logic").toString();
//			final List<Map> filters = (List<Map>) filter.get("filters");
//			Specification<Groups> spec = new Specification<Groups>() 
//			{
//				@Override
//				public Predicate toPredicate(Root<Groups> root,
//						CriteriaQuery<?> query, CriteriaBuilder cb) 
//				{
//					List<Predicate> pl = new ArrayList<Predicate>();
//					for (Map f : filters) 
//					{
////						String operator = ((String) f.get("operator")).trim();
//						String field = f.get("field").toString().trim();
//						String value = f.get("value").toString().trim();
//						if(value!=null && value.length()>0)
//						{
//							if ("groupIndex".equalsIgnoreCase(field)) 
//							{
//								pl.add(cb.equal(root.<String> get(field),value));
//							} else if ("groupName".equalsIgnoreCase(field)) 
//							{
//								pl.add(cb.like(root.<String> get(field),"%"+ value + "%"));
//							} 
//						}
//					}
//					//查询出未删除的
//					pl.add(cb.equal(root.<Integer> get("flag"),1));
//					return cb.and(pl.toArray(new Predicate[0]));
//				}
//			};
//			pageList =  groupRepository.findAll(spec, pageable);
//		} 
//		else 
//		{
//			Specification<Groups> spec = new Specification<Groups>() 
//					{
//						@Override
//						public Predicate toPredicate(Root<Groups> root,
//								CriteriaQuery<?> query, CriteriaBuilder cb) 
//						{
//							List<Predicate> pl = new ArrayList<Predicate>();
//							//查询出未删除的
//							pl.add(cb.equal(root.<Integer> get("flag"),1));
//							return cb.and(pl.toArray(new Predicate[0]));
//						}
//					};
//			pageList = groupRepository.findAll(spec,pageable);
//
//		}
//		map.put("total", pageList.getTotalElements());
//		List<GroupDto> dtos = new ArrayList<GroupDto>();
//		List<Groups> list = pageList.getContent();
//		if(list!=null && list.size()>0)
//		{
//			for(Groups d:list)
//			{
//				GroupDto dto = new GroupDto();
//				BeanUtils.copyProperties(d, dto);
//				dto.generateToken(salt);
//				dtos.add(dto);
//			}
//		}
//		map.put("groups", dtos);
//		return map;
//	}
//
//	
//	/**
//	 * 添加用户组
//	 * @param list
//	 */
//	@Transactional(propagation=Propagation.REQUIRED)
//	public void addGroup(List<GroupDto> list,UserDetails userDetails)
//	{
//		if(list!=null && list.size()>0)
//		{
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			for(GroupDto dto: list)
//			{
//				if(StringUtils.isEmpty(dto.getToken()))
//				{
//				    Groups group = new Groups();
//					String id = UUID.randomUUID().toString().replaceAll("-", "");
//					dto.setGroupId(id);
//					dto.setFlag(1);//初始化
//					dto.setCreatorId(userDetails.getUsername());
//					dto.setUpdaterId(userDetails.getUsername());
//					String createTimeStr = sdf.format(new Date());
//					String updateTimeStr = sdf.format(new Date());
//					Date createTime;
//					Date updateTime;
//					try {
//						createTime = sdf.parse(createTimeStr);
//						updateTime = sdf.parse(updateTimeStr);
//						dto.setCreateTime(createTime);
//						dto.setUpdateTime(updateTime);
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//					BeanUtils.copyProperties(dto, group);
//					this.groupRepository.saveAndFlush(group);
//				}
//			}
//		}
//	}
//	
//	/**
//	 * 编辑用户组
//	 * @param list
//	 */
//	@Transactional(propagation=Propagation.REQUIRED)
//	public void editGroup(List<GroupDto> list,UserDetails userDetails)
//	{
//		if(list!=null && list.size()>0)
//		{
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			for(GroupDto dto: list)
//			{
//				Groups group = this.groupRepository.findOne(dto.getGroupId());
//				String updateTimeStr = sdf.format(new Date());
//				Date updateTime;
//				try {
//						updateTime = sdf.parse(updateTimeStr);
//						dto.setUpdateTime(updateTime);
//						dto.setUpdaterId(userDetails.getUsername());
//				} catch (ParseException e) {
//						e.printStackTrace();
//				}
//				BeanUtils.copyProperties(dto, group);
//				this.groupRepository.saveAndFlush(group);
//					
//			}
//		}
//	}
//	
//	/**
//	 * 查询出更新后的数据
//	 * @param list
//	 * @return
//	 */
//	@Transactional(propagation=Propagation.SUPPORTS)
//	public List<GroupDto> findGroups(List<GroupDto> list,String salt)
//	{
//		List<GroupDto> newList = new ArrayList<GroupDto>();
//		if(list!=null && list.size()>0)
//		{
//			for(GroupDto d:list)
//			{
//				String id = d.getGroupId();
//				Groups group = this.groupRepository.findOne(id);
//				if(group!=null)
//				{
//					GroupDto dto = new GroupDto();
//					BeanUtils.copyProperties(group, dto);
//					dto.generateToken(salt);
//					newList.add(dto);
//				}
//			}
//		}
//		return newList;
//	}
//	
//	/**
//	 * 多条删除
//	 * @param list
//	 */
//	@Transactional(propagation=Propagation.REQUIRED)
//	public void deleteGroup(List<GroupDto> list,UserDetails userDetails)
//	{
//		if(list!=null && list.size()>0)
//		{
//			for(GroupDto d:list)
//			{
//				String id = d.getGroupId();
//				//标记为已删除-0,未删除-1
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				String updateTimeStr = sdf.format(new Date());
//				Date updateTime = null;
//				try {
//					updateTime = sdf.parse(updateTimeStr);
//				} catch (ParseException e) {
//				}
//			    this.groupRepository.updateFlag(id,updateTime,userDetails.getUsername());
//			}
//				
//		}
//	}
//	
//	/**
//	 * 根据groupId查询出关系表中所有userId字符串
//	 * @param groupId
//	 * @return
//	 */
//	@Transactional(propagation=Propagation.SUPPORTS)
//	public String findUserIdsByGroupId(String groupId)
//	{
//		List<String> list=this.groupRepository.findUserIdsByGroupId(groupId);
//		StringBuilder sb = new StringBuilder();
//		if(list!=null && list.size()>0)
//		{
//			for(String id:list)
//			{
//				sb.append(",").append(id);
//			}
//		}
//		// 去掉首字符“,”
//		if (sb.length() > 0 && sb.indexOf(",") == 0)
//		{
//			sb.replace(0, 1, "");
//		}
//		return sb.toString();
//	}
//	
//	
//	/**
//	 * 根据id查询出映射的用户集合
//	 * @param groupId
//	 *        用户选择的用户组id
//	 * @return
//	 *        映射的用户集合
//	 */
//	@Transactional(propagation=Propagation.SUPPORTS)
//	public List<UserDto> findUsersByGroupId(String groupId)
//	{
//		
//		List<UserDto> users = new ArrayList<UserDto>();
//		Groups group = this.groupRepository.findOne(groupId);
//		List<User>  userList = group.getUsers();
//		if(userList!=null && userList.size()>0)
//		{
//			for(User u:userList)
//			{
//				UserDto userDto = new UserDto();
//				BeanUtils.copyProperties(u, userDto);
//				users.add(userDto);
//			}
//		}
//		return users;
//	}
//	
//	/**
//	 * 保存用户组的分配
//	 * @param userIds
//	 *        分配的用户的id字符串，以逗号分隔
//	 * @param groupId
//	 *        用户选定的用户组id
//	 */
//	@Transactional(propagation=Propagation.REQUIRED)
//	public void saveGroupUsers(String userIds,String preUserIds,String groupId)
//	{
//		if(groupId!=null && groupId.length()>0)
//		{
//			String[] aryUser;
//			String[] aryUserPre;
//			if(userIds!=null && userIds.length()>0 && preUserIds!=null && preUserIds.length()==0)
//			{
//				//新分配的ID数组
//			    aryUser = userIds.split(",");
//			    for(String userId:aryUser)
//				{
//					if(userId!=null && userId.length()>0)
//					{
//						UserGroupPK pk = new UserGroupPK();
//						pk.setGroupId(groupId);
//						pk.setUserId(userId);
//						UserGroup userGroup = new UserGroup();
//						userGroup.setId(pk);
//						this.userGroupRepository.saveAndFlush(userGroup);
//					}
//				}
//			}
//			if(userIds!=null && userIds.length()==0 && preUserIds!=null && preUserIds.length()>0)
//			{
//				aryUserPre = preUserIds.split(",");
//				//删除
//				for(String userIdPre:aryUserPre)
//				{
//					if(userIdPre!=null && userIdPre.length()>0)
//					{
//						UserGroupPK pk = new UserGroupPK();
//						pk.setGroupId(groupId);
//						pk.setUserId(userIdPre);
//						UserGroup userGroup = new UserGroup();
//						userGroup.setId(pk);
//						this.userGroupRepository.delete(userGroup);
//					}
//				}
//			}
//			if(userIds!=null && userIds.length()>0 && preUserIds!=null && preUserIds.length()>0)
//			{
//				//新分配的ID数组
//			    aryUser = userIds.split(",");
//				//原来已分配的ID数组
//				aryUserPre = preUserIds.split(",");
//				//需要插入的list
//				List<String> userIdsList = new ArrayList<String>();
//				for(int i=0;i<aryUser.length;i++)
//				{
//					userIdsList.add(aryUser[i]);
//				}
//				//旧的
//				for(int j=0;j<aryUserPre.length;j++)
//				{
//					String userIdPre = aryUserPre[j];
//					//新的
//					for(int i=0;i<aryUser.length;i++)
//					{
//						String userId = aryUser[i];
//						if(!userIdPre.equals(userId))
//						{
//							if(i!=(aryUser.length-1))
//							{
//								continue;
//							}
//							else
//							{
//								//删除关系表旧记录
//								if(userIdPre!=null && userIdPre.length()>0)
//								{
//									UserGroupPK pk = new UserGroupPK();
//									pk.setGroupId(groupId);
//									pk.setUserId(userIdPre);
//									UserGroup userGroup = new UserGroup();
//									userGroup.setId(pk);
//									this.userGroupRepository.delete(userGroup);
//									break;
//								}
//							}
//						}
//						else if(userId.equals(userIdPre))
//						{
//							userIdsList.remove(userId);
//							break;
//						}
//					}
//					
//				}
//				//插入关系表记录
//				if(userIdsList.size()>0)
//				{
//					for(String userId:userIdsList)
//					{
//						if(userId!=null && userId.length()>0)
//						{
//							UserGroupPK pk = new UserGroupPK();
//							pk.setGroupId(groupId);
//							pk.setUserId(userId);
//							UserGroup userGroup = new UserGroup();
//							userGroup.setId(pk);
//							this.userGroupRepository.saveAndFlush(userGroup);
//						}
//					}
//				}
//			}
//		}
//	}
//	
//}
