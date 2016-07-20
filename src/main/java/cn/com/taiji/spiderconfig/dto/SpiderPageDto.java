package cn.com.taiji.spiderconfig.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import cn.com.taiji.spiderconfig.domain.SpiderExtractor;
import cn.com.taiji.spiderconfig.domain.SpiderRule;

public class SpiderPageDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2543601584403569658L;

	private String id;

	private String name;

	private String extractorId;

	private String configId;
	
    private String urlType;
	
	private String url;

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

	public String getExtractorId() {
		return extractorId;
	}

	public void setExtractorId(String extractorId) {
		this.extractorId = extractorId;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
