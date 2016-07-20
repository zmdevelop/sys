package cn.com.taiji.sys.json;



import cn.com.taiji.sys.domain.Code;
import cn.com.taiji.sys.domain.Dept;
import cn.com.taiji.sys.domain.Groups;
import cn.com.taiji.sys.domain.Menu;
import cn.com.taiji.sys.domain.Role;
import cn.com.taiji.sys.domain.User;
//import cn.com.taiji.sys.domain.UserAccount;
//import cn.com.taiji.sys.domain.UserRole;


import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class SysModule extends SimpleModule {


	private static final long serialVersionUID = -1176170151835577922L;

	public SysModule() {
		super("sysModule", new Version(0, 0, 1, null, "cn.com.taiji", "sys"));
	}

	@Override
	public void setupModule(SetupContext context) {
//		context.setMixInAnnotations(Org.class, OrgMixIn.class);
//		context.setMixInAnnotations(UserAccount.class, UserAccountMixIn.class);
//		context.setMixInAnnotations(UserRole.class, UserRoleMixIn.class);
		
		context.setMixInAnnotations(Menu.class, MenuMixIn.class);
		context.setMixInAnnotations(Dept.class, DeptMixIn.class);
		context.setMixInAnnotations(User.class, UserMixIn.class);
		context.setMixInAnnotations(Role.class, RoleMixIn.class);
		context.setMixInAnnotations(Groups.class, GroupMixIn.class);
		context.setMixInAnnotations(Code.class, CodeMixIn.class);
	}

}
