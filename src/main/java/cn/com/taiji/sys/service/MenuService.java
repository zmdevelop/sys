package cn.com.taiji.sys.service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hazelcast.util.UuidUtil;

import cn.com.taiji.sys.domain.Menu;
import cn.com.taiji.sys.domain.MenuRepository;
import cn.com.taiji.sys.domain.Role;
import cn.com.taiji.sys.domain.RoleRepository;
import cn.com.taiji.sys.dto.MenuDto;
import cn.com.taiji.sys.dto.TreeDto;
import cn.com.taiji.sys.exception.BusinessException;

/**
 * 菜单service
 * @author Sunjingyan
 * @date 2014-05-10
 */
@Service
public class MenuService {
	
	private static final Logger log = LoggerFactory.getLogger(MenuService.class);
	
	@Inject
	private MenuRepository menuRepository;
	
	@Inject
	private RoleRepository roleRepository;


	/**
	 * 查询菜单信息
	 * @param searchParameters
	 *        查询参数的map集合
	 * @return
	 *       查询的结果,map类型
	 *       total:总条数
	 *       menus:查询结果list集合
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public Map getPage(final Map searchParameters,String salt) {
		Map map = new HashMap();
		int page = 0;
		int pageSize = 8;
		Page<Menu> pageList;
//		Page<MenuDto> pageDtoList;
		if(searchParameters!=null && searchParameters.size()>0 && searchParameters.get("page")!=null)
		{
			page = Integer.parseInt(searchParameters.get("page").toString()) - 1;
		}
		if(searchParameters!=null && searchParameters.size()>0 && searchParameters.get("pageSize")!=null)
		{
			pageSize = Integer.parseInt(searchParameters.get("pageSize").toString());
		}
		if (pageSize < 1)
			pageSize = 1;
		if (pageSize > 100)
			pageSize = 100;
		List<Map> orderMaps = (List<Map>) searchParameters.get("sort");
		List<Order> orders = new ArrayList<Order>();
		if (orderMaps != null) {
			for (Map m : orderMaps) {
				if (m.get("field") == null)
					continue;
				String field = m.get("field").toString();
				if (!StringUtils.isEmpty(field)) {
					String dir = m.get("dir").toString();
					if ("DESC".equalsIgnoreCase(dir)) {
						orders.add(new Order(Direction.DESC, field));
					} else {
						orders.add(new Order(Direction.ASC, field));
					}
				}
			}
		}
		PageRequest pageable;
		if (orders.size() > 0) 
		{
			pageable = new PageRequest(page, pageSize, new Sort(orders));
		} 
		else 
		{
			Sort s = new Sort(Direction.ASC,"menuIndex");
			pageable = new PageRequest(page, pageSize,s);
		}
		Map filter = (Map) searchParameters.get("filter");
		if (filter != null) 
		{
//			String logic = filter.get("logic").toString();
			final List<Map> filters = (List<Map>) filter.get("filters");
			Specification<Menu> spec = new Specification<Menu>() 
			{
				@Override
				public Predicate toPredicate(Root<Menu> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) 
				{
					List<Predicate> pl = new ArrayList<Predicate>();
					for (Map f : filters) 
					{
//						String operator = ((String) f.get("operator")).trim();
						String field = f.get("field").toString().trim();
						String value = f.get("value").toString().trim();
						if(value!=null && value.length()>0)
						{
							if ("menuIndex".equalsIgnoreCase(field)) 
							{
								pl.add(cb.equal(root.<String> get(field),value));
							} 
							if ("menuName".equalsIgnoreCase(field)) 
							{
								pl.add(cb.like(root.<String> get(field),"%"+ value + "%"));
							} 
							else if ("menuUrl".equalsIgnoreCase(field)) 
							{
								pl.add(cb.like(root.<String> get(field),"%"+ value + "%"));
							} else if ("menuDesc".equalsIgnoreCase(field)) 
							{
								pl.add(cb.like(root.<String> get(field),"%"+ value + "%"));
							}
						}
						
					}
					//查询出未删除的
					pl.add(cb.equal(root.<Integer> get("flag"),1));
					return cb.and(pl.toArray(new Predicate[0]));
				}
			};
			pageList =  menuRepository.findAll(spec, pageable);

		} 
		else 
		{
			Specification<Menu> spec = new Specification<Menu>() 
					{
						public Predicate toPredicate(Root<Menu> root,
								CriteriaQuery<?> query, CriteriaBuilder cb) 
						{
							List<Predicate> list = new ArrayList<Predicate>();
							//查询出未删除的
							list.add(cb.equal(root.<Integer> get("flag"),1));
							return cb.and(list.toArray(new Predicate[0]));
						}
					};
			pageList = menuRepository.findAll(spec, pageable);

		}
		map.put("total", pageList.getTotalElements());
		List<MenuDto> dtos = new ArrayList<MenuDto>();
		List<Menu> list = pageList.getContent();
		if(list!=null && list.size()>0)
		{
			for(Menu d:list)
			{
				MenuDto dto = new MenuDto();
				BeanUtils.copyProperties(d, dto);
				if(d.getParent()!=null && d.getParent().getMenuId()!=null)
				{
					dto.setParentId(d.getParent().getMenuId());
					dto.setParentName(d.getParent().getMenuName());
				}
				dto.generateToken(salt);
				dtos.add(dto);
			}
		}
		map.put("menus", dtos);
		return map;
	}
	
	/**
	 * 添加菜单信息
	 * @param list
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void add(List<MenuDto> list,UserDetails userDetails)
	{
		if(list!=null && list.size()>0)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(MenuDto dto: list)
			{
				if(StringUtils.isEmpty(dto.getToken()))
				{
					//新增
					Menu menu = new Menu();
					String id = UUID.randomUUID().toString().replaceAll("-", "");
					dto.setMenuId(id);
					dto.setFlag(1);//初始化
					dto.setCreatorId(userDetails.getUsername());
					dto.setUpdaterId(userDetails.getUsername());
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
					BeanUtils.copyProperties(dto, menu);
					if(dto.getParentId()!=null)
					{
						Menu parent = this.menuRepository.findOne(dto.getParentId());
						menu.setParent(parent);
					}
					this.menuRepository.saveAndFlush(menu);
				}
			}
		}
	}
	
	/**
	 * 编辑菜单信息
	 * @param list
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void edit(List<MenuDto> list,UserDetails userDetails)
	{
		if(list!=null && list.size()>0)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(MenuDto dto: list)
			{
				Menu menu = this.menuRepository.findOne(dto.getMenuId());
				String updateTimeStr = sdf.format(new Date());
				Date updateTime;
				try {
						updateTime = sdf.parse(updateTimeStr);
						dto.setUpdateTime(updateTime);
						dto.setUpdaterId(userDetails.getUsername());
				} catch (ParseException e) {
				}
				BeanUtils.copyProperties(dto, menu);
				if(dto.getParentId()!=null)
				{
					Menu parent = this.menuRepository.findOne(dto.getParentId());
					menu.setParent(parent);
				}
				this.menuRepository.saveAndFlush(menu);
					
			}
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void editOne(MenuDto dto)
	{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Menu menu = this.menuRepository.findOne(dto.getMenuId());
				String updateTimeStr = sdf.format(new Date());
				Date updateTime;
				try {
						updateTime = sdf.parse(updateTimeStr);
						dto.setUpdateTime(updateTime);
				} catch (ParseException e) {
				}
				menu = convertToMenu(dto);
				this.menuRepository.saveAndFlush(menu);
	}
	
	/**
	 * 添加和编辑菜单信息-备份
	 * @param list
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveMenu(List<MenuDto> list)
	{
		if(list!=null && list.size()>0)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(MenuDto d:list)
			{
				Menu menu;
				if(d!=null && d.getMenuId()!=null && !d.getMenuId().equals(""))
				{
					//修改
					menu = this.menuRepository.findOne(d.getMenuId());
					String updateTimeStr = sdf.format(new Date());
					Date updateTime;
					try {
						updateTime = sdf.parse(updateTimeStr);
						d.setUpdateTime(updateTime);
					} catch (ParseException e) {
					}
				}
				else
				{
					//新增
					menu = new Menu();
					String id = UUID.randomUUID().toString().replaceAll("-", "");
					d.setMenuId(id);
					d.setFlag(1);//初始化
					String createTimeStr = sdf.format(new Date());
					String updateTimeStr = sdf.format(new Date());
					Date createTime;
					Date updateTime;
					try {
						createTime = sdf.parse(createTimeStr);
						updateTime = sdf.parse(updateTimeStr);
						d.setCreateTime(createTime);
						d.setUpdateTime(updateTime);
					} catch (ParseException e) {
					}
				}
				BeanUtils.copyProperties(d, menu);
				if(d.getParentId()!=null)
				{
					Menu parent = this.menuRepository.findOne(d.getParentId());
					menu.setParent(parent);
				}
				if(d.getMenuIndex()==null)
				{
					menu.setMenuIndex(0);
				}
//				if(d.getCodeDto()!=null && d.getCodeDto().getCode()!=null)
//				{
//					Menu parent = this.menuRepository.findOne(d.getCodeDto().getCode());
//					menu.setParent(parent);
//				}
				this.menuRepository.saveAndFlush(menu);
			}
		}
	}
	
	/**
	 * 多条删除
	 * @param list
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteMenu(List<MenuDto> list,UserDetails userDetails)
	{
		if(list!=null && list.size()>0)
		{
			for(MenuDto d:list)
			{
				if(d.getChildren()!=null && d.getChildren().size()>0)
				{
					throw new BusinessException("该菜单下面有子菜单，不能删除!");
				}
				else if(d.getRoles()!=null && d.getRoles().size()>0)
				{
					throw new BusinessException("该菜单与角色关联，不能删除!");
				}
				else{
					    String id = d.getMenuId();
					    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String updateTimeStr = sdf.format(new Date());
						Date updateTime = null;
						try {
								updateTime = sdf.parse(updateTimeStr);
						} catch (ParseException e) {
						}
				       this.menuRepository.updateFlag(id,updateTime,userDetails.getUsername());
				}
			}
		}
	}
	
	
//	/**
//	 * 查询所有的菜单
//	 * @return
//	 */
//	public List<CodeDto> findAllMenus()
//	{
//		List<CodeDto> codes = new ArrayList<CodeDto>();
//		List<Menu> menus = this.menuRepository.findAll();
//		if(menus!=null && menus.size()>0)
//		{
//			for(Menu d:menus)
//			{
//				CodeDto dto = new CodeDto();
//				dto.setCode(d.getMenuId());
//				dto.setName(d.getMenuName());
//				codes.add(dto);
//			}
//		}
//		return codes;
//	}
	
	/**
	 * 查询出sys菜单树的根(parent为null)
	 * @return
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public Menu findTreeSys()
	{
		List<Menu> list = this.menuRepository.findRoots("sys");
		if(list!=null && list.size()>0)
		{
			if(list.size()>1)
			{
				return list.get(0);
			}
			return list.get(0);
		}
		return null;
	}
	/**
	 * 查询出portal菜单树的根(parent为null)
	 * @return
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public Menu findTreePortal()
	{
		List<Menu> list = this.menuRepository.findRoots("portal");
		if(list!=null && list.size()>0)
		{
			if(list.size()>1)
			{
				return list.get(0);
			}
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据ID查询菜单
	 * @param id
	 * @return
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public MenuDto findById(String id)
	{
		Menu menu = this.menuRepository.findOne(id);
		System.out.println(id+"--"+id.length());
		System.out.println(menu);
		MenuDto dto = convertToMenuDto(menu);
		return dto;
	}
	
	/**
	 * 查询出所有的菜单集合
	 * @return
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<MenuDto> findAllMenus()
	{
		List<Menu> AllMenus = this.menuRepository.findAllMenus();
		List<MenuDto> list = new ArrayList<MenuDto>();
		if(AllMenus!=null && AllMenus.size()>0)
		{
			for(Menu u:AllMenus)
			{
				MenuDto dto = convertToMenuDto(u);
				list.add(dto);
			}
		}
		return list;
	}
	
	/**
	 * 查询出所有的菜单集合
	 * @return
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<MenuDto> findAllMenusGrant()
	{
		List<Menu> AllMenus = this.menuRepository.findAllMenus();
		List<MenuDto> list = new ArrayList<MenuDto>();
		if(AllMenus!=null && AllMenus.size()>0)
		{
			for(Menu u:AllMenus)
			{
				MenuDto dto = new MenuDto();
				BeanUtils.copyProperties(u, dto);
				if(u!=null && u.getParent()!=null && u.getParent().getMenuId()!=null)
				{
					dto.setParentId(u.getParent().getMenuId());
					dto.setParentName(u.getParent().getMenuName());
				}
				list.add(dto);
			}
		}
		return list;
	}
	
	/**
	 * 上传菜单图标文件
	 * @param menuId
	 * @param iconPath
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void uploadPic(String menuId,byte[] iconPath,UserDetails userDetails){
		
		Menu menu = null;
		if(menuId!=null)
		{
			menu = this.menuRepository.findOne(menuId);
		}
		if(menu!=null && iconPath!=null && iconPath.length>0)
		{
			menu.setIconPath(iconPath);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String updateTimeStr = sdf.format(new Date());
			Date updateTime;
			try {
				updateTime = sdf.parse(updateTimeStr);
				menu.setUpdateTime(updateTime);
				menu.setUpdaterId(userDetails.getUsername());
			} catch (ParseException e) {
			}
			this.menuRepository.saveAndFlush(menu);
		}
	}
	
	
	/**
	 * 获得根节点菜单
	 * @return
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<Menu> findRootTree() 
	{
           return this.menuRepository.findRoots();
	}
	
	/**
	 * 查询出更新后的数据
	 * @param list
	 * @return
	 */
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<MenuDto> findMenus(List<MenuDto> list,String salt)
	{
		List<MenuDto> newList = new ArrayList<MenuDto>();
		if(list!=null && list.size()>0)
		{
			for(MenuDto d:list)
			{
				String id = d.getMenuId();
				Menu menu = null;
				if(id!=null && id.length()>0)
				{
				    menu = this.menuRepository.findOne(id);
				}
				if(menu!=null)
				{
					MenuDto dto = new MenuDto();
					BeanUtils.copyProperties(menu, dto);
//					if(menu.getParent()!=null && menu.getParent().getMenuId()!=null)
//					{
//						dto.setParentId(menu.getParent().getMenuId());
//					}
					if(menu.getParent()!=null && menu.getParent().getMenuId()!=null)
					{
						dto.setParentId(menu.getParent().getMenuId());
						dto.setParentName(menu.getParent().getMenuName());
					}
					dto.generateToken(salt);
					newList.add(dto);
				}
			}
		}
		return newList;
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<MenuDto> findMenusByTreeOrder()
	{
		List<Menu> rootMenus = menuRepository.findRoots();
		List<Menu> allMenu = new ArrayList<>();
		for(Menu m:rootMenus)
		{
			allMenu.add(m);
			List<Menu> children = menuRepository.findRoots(m.getMenuId());
			allMenu.addAll(children);
		}
		List<MenuDto> dtos = new ArrayList<>();
		for(Menu m : allMenu)
		{
			dtos.add(convertToMenuDto(m));
		}
		return dtos;
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<TreeDto> findMenusByTreeDto()
	{
		List<Menu> rootMenus = menuRepository.findRoots();
		List<TreeDto> dtos = new ArrayList<>();
		for(Menu root:rootMenus)
		{
			TreeDto dto = new TreeDto(root);
			dtos.add(dto);
			for(Menu child:root.getChildren())
			{
				TreeDto cDto = new TreeDto(child);
				dtos.add(cDto);
			}
		}
		return dtos;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean save(MenuDto menuDto)
	{
		menuDto.setMenuId(cn.com.taiji.sys.util.UUIDUtils.getUUID32());
		menuRepository.save(convertToMenu(menuDto));
		return true;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean delete(String menuId)
	{
		menuRepository.delete(menuId);
		return true;
	}
	
	private Menu convertToMenu(MenuDto dto)
	{
		Menu menu = new Menu();
		BeanUtils.copyProperties(dto, menu);
		Menu parent = menuRepository.findOne(dto.getParentId());
		menu.setParent(parent);
		return menu;
	}
	
	private MenuDto convertToMenuDto(Menu menu)
	{
		MenuDto dto = new MenuDto();
		BeanUtils.copyProperties(menu, dto);
		dto.setParentId(menu.getParent()==null?null:menu.getParent().getMenuId());
		return dto;
	}

	/**
	 * @param userId
	 * @return
	 */
	
	public List<Menu> findMenuByRoleId(String roleId)
	{
	  List<Menu> menus = menuRepository.findMenuByRoleId(roleId);
	  return menus;
	}
	
	public Set<MenuDto> findByUser(String userId) {
		// TODO Auto-generated method stub
		List<Role> roles = roleRepository.findByUserId(userId);
		Set<MenuDto> menuDtos = new HashSet<>();
		for(Role role:roles)
		{
			List<Menu> menus = roleRepository.findMenuByRoleId(role.getRoleId());
			for(Menu m :menus)
			{
				MenuDto dto = convertToMenuDto(m);
				menuDtos.add(dto);
			}
		}
		
		return menuDtos;
	}
}
