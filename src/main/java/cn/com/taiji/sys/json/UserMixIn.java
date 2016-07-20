package cn.com.taiji.sys.json;



import java.util.List;
import java.util.Set;

import cn.com.taiji.sys.domain.Dept;
import cn.com.taiji.sys.domain.Groups;
import cn.com.taiji.sys.domain.Role;
import cn.com.taiji.sys.domain.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserMixIn {
	
	@JsonIgnore
	List<Role> roles;
	
	@JsonIgnore
	List<Dept> depts;
	
	@JsonIgnore
	List<Groups> groups;
}
