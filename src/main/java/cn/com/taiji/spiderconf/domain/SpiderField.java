package cn.com.taiji.spiderconf.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "spider_field")
@NamedQuery(name = "SpiderField.findAll", query = "SELECT s FROM SpiderField s")
public class SpiderField implements Serializable {
	private static final long serialVersionUID = -8940099488614427415L;

	@Id
	private String id;
	private String name;
	private String xpath;
	@Column(name = "filter_type")
	private String filterType;
	@Column(name = "filter_value")
	private String filterValue;
	private String attr;
	@Column(name = "model_id")
	private String modelId;
	@ManyToOne
	@JoinColumn(name = "channel_id")
	private SpiderChannel spiderChannel;
	@ManyToOne
	@JoinColumn(name="content_id")
	private SpiderContent spiderContent;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	
	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public void setName(String name) {
		this.name = name;
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

	public SpiderChannel getSpiderChannel() {
		return spiderChannel;
	}

	public void setSpiderChannel(SpiderChannel spiderChannel) {
		this.spiderChannel = spiderChannel;
	}

	public SpiderContent getSpiderContent() {
		return spiderContent;
	}

	public void setSpiderContent(SpiderContent spiderContent) {
		this.spiderContent = spiderContent;
	}
	
	
}
