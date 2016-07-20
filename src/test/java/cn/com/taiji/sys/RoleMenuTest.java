package cn.com.taiji.sys;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.com.taiji.sys.domain.Menu;
import cn.com.taiji.sys.domain.MenuRepository;
import cn.com.taiji.sys.domain.Role;
import cn.com.taiji.sys.domain.RoleRepository;
import cn.com.taiji.sys.dto.RoleDto;
import cn.com.taiji.sys.service.RoleService;

/** 
 * 描述:menu,role单元测试
 * 作者:zhao
 * 日期:2016年7月8日下午2:51:56
 * 版本:1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SysApplication.class)
public class RoleMenuTest {

	@Inject
	RoleRepository roleRepository;
	
	@Inject
	RoleService roleService;
	@Inject
	MenuRepository menuRepository;
	
	@Test
	@Transactional(propagation=Propagation.REQUIRED)
	public void setMenus()
	{
		
		//role.getMenus().add(menu);
		//roleRepository.save(role);
		//System.out.println(menu);
		
		RoleDto rolenew = new RoleDto();
		//BeanUtils.copyProperties(role, rolenew);
		roleService.save(rolenew);
		/*List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		menu.getRoles().add(role);
		menuRepository.saveAndFlush(menu);*/
	}
}
