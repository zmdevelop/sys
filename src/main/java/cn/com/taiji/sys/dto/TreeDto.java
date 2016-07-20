package cn.com.taiji.sys.dto;

import java.io.Serializable;

import cn.com.taiji.sys.domain.Menu;

/** 
 * 描述:树公用dto,以构造函数绑定
 * 作者:zhao
 * 日期:2016年7月8日上午11:28:04
 * 版本:1.0
 */
public class TreeDto implements Serializable{

	private static final long serialVersionUID = -5788316868003995938L;
	
	private String id;
	private String pId;
	private String name;
	

	public TreeDto(Menu menu)
	{
		this.id = menu.getMenuId();
		this.pId = menu.getParent()==null?null:menu.getParent().getMenuId();
		this.name = menu.getMenuName();
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getpId() {
		return pId;
	}


	public void setpId(String pId) {
		this.pId = pId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
}
