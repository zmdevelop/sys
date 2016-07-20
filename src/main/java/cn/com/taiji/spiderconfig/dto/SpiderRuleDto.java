package cn.com.taiji.spiderconfig.dto;


import java.io.Serializable;

import cn.com.taiji.spiderconfig.domain.SpiderPage;

public class SpiderRuleDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3617514718599493763L;

	private String id;

	private byte isNegativeEnabled;

	private String ruleType;

	private String ruleUrl;

	private String pageId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte getIsNegativeEnabled() {
		return isNegativeEnabled;
	}

	public void setIsNegativeEnabled(byte isNegativeEnabled) {
		this.isNegativeEnabled = isNegativeEnabled;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getRuleUrl() {
		return ruleUrl;
	}

	public void setRuleUrl(String ruleUrl) {
		this.ruleUrl = ruleUrl;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	
	
}
