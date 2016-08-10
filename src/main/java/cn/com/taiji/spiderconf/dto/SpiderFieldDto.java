package cn.com.taiji.spiderconf.dto;

import org.springframework.beans.BeanUtils;

import cn.com.taiji.spiderconf.domain.SpiderField;

public class SpiderFieldDto {

	private String id;
	private String name;
	private String xpath;
	private String filterType;
	private String filterValue;
	private String modelId;
	private String attr;
	private String spiderChannelId;
	private String spiderChannelName;
	private String spiderContentId;
	private String spiderContentName;

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

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getSpiderChannelId() {
		return spiderChannelId;
	}

	public void setSpiderChannelId(String spiderChannelId) {
		this.spiderChannelId = spiderChannelId;
	}

	public String getSpiderChannelName() {
		return spiderChannelName;
	}

	public void setSpiderChannelName(String spiderChannelName) {
		this.spiderChannelName = spiderChannelName;
	}
	

	public String getSpiderContentId() {
		return spiderContentId;
	}

	public void setSpiderContentId(String spiderContentId) {
		this.spiderContentId = spiderContentId;
	}

	public String getSpiderContentName() {
		return spiderContentName;
	}

	public void setSpiderContentName(String spiderContentName) {
		this.spiderContentName = spiderContentName;
	}

	public SpiderFieldDto init(SpiderField field) {
		BeanUtils.copyProperties(field, this);
		if (field.getSpiderChannel() != null) {
			this.spiderChannelId = field.getSpiderChannel().getId();
			this.spiderChannelName = field.getSpiderChannel().getName();
		}
		if (field.getSpiderContent() != null) {
			this.spiderContentId = field.getSpiderContent().getId();
			this.spiderContentName = field.getSpiderContent().getName();
		}
		return this;
	}

}
