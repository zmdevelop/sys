package cn.com.taiji.spiderconfig.dto;

import java.io.Serializable;


public class SpiderFieldDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8388898626718310322L;

	private String id;

	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
