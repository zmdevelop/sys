package cn.com.taiji.sys.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user_group database table.
 * 
 */
@Entity
@Table(name="user_group")
@NamedQuery(name="UserGroup.findAll", query="SELECT u FROM UserGroup u")
public class UserGroup implements Serializable {

	private static final long serialVersionUID = -1382925073246209455L;

	@EmbeddedId
	private UserGroupPK id;

//	bi-directional many-to-one association to Group
//	@ManyToOne
//	@JoinColumn(name="group_id")
//	private Group group;

	public UserGroup() {
	}

	public UserGroupPK getId() {
		return this.id;
	}

	public void setId(UserGroupPK id) {
		this.id = id;
	}

//	public Group getGroup() {
//		return this.group;
//	}
//
//	public void setGroup(Group group) {
//		this.group = group;
//	}

}