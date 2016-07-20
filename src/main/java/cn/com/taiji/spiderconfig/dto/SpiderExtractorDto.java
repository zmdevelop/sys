package cn.com.taiji.spiderconfig.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import cn.com.taiji.spiderconfig.domain.SpiderConfig;
import cn.com.taiji.spiderconfig.domain.SpiderPage;

public class SpiderExtractorDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3924152986815102456L;

	private String id;

	private String name;

	private String configId;

	private List<SpiderPage> spiderPages;

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

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public List<SpiderPage> getSpiderPages() {
		return spiderPages;
	}

	public void setSpiderPages(List<SpiderPage> spiderPages) {
		this.spiderPages = spiderPages;
	}
	
}
