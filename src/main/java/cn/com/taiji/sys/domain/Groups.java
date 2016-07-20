package cn.com.taiji.sys.domain;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the group database table.
 * 
 */
@Entity
@Table(name="groups")
@NamedQuery(name="Groups.findAll", query="SELECT g FROM Groups g")
public class Groups implements Serializable {

	private static final long serialVersionUID = 4186756588977538061L;

	@Id
	@Column(name="group_id")
	private String groupId;
	
	@Column(name="group_index")
	private Integer groupIndex;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime;

	@Column(name="creator_id")
	private String creatorId;

	private Integer flag;

	@Column(name="group_name")
	private String groupName;

	private String remark;

	private String state;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_time")
	private Date updateTime;

	@Column(name="updater_id")
	private String updaterId;

	//bi-directional many-to-one association to UserGroup
//	@OneToMany(mappedBy="group")
//	private List<UserGroup> userGroups;

	//bi-directional many-to-many association to User
//	@ManyToMany(mappedBy="groups")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_group", joinColumns = {@JoinColumn(name = "group_id")},
			inverseJoinColumns = {@JoinColumn(name = "user_id")})
	private List<User> users;

	public Groups() {
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdaterId() {
		return this.updaterId;
	}

	public void setUpdaterId(String updaterId) {
		this.updaterId = updaterId;
	}

//	public List<UserGroup> getUserGroups() {
//		return this.userGroups;
//	}
//
//	public void setUserGroups(List<UserGroup> userGroups) {
//		this.userGroups = userGroups;
//	}

//	public UserGroup addUserGroup(UserGroup userGroup) {
//		getUserGroups().add(userGroup);
//		userGroup.setGroup(this);
//
//		return userGroup;
//	}
//
//	public UserGroup removeUserGroup(UserGroup userGroup) {
//		getUserGroups().remove(userGroup);
//		userGroup.setGroup(null);
//
//		return userGroup;
//	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Integer getGroupIndex() {
		return groupIndex;
	}

	public void setGroupIndex(Integer groupIndex) {
		this.groupIndex = groupIndex;
	}

	
}