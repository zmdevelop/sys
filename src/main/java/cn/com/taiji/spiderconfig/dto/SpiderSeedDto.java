package cn.com.taiji.spiderconfig.dto;

import java.io.Serializable;


public class SpiderSeedDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 933158554078021622L;

	private String seedId;

	private String seedName;

	private String url;

	private String configId;

	public String getSeedId() {
		return seedId;
	}

	public void setSeedId(String seedId) {
		this.seedId = seedId;
	}

	public String getSeedName() {
		return seedName;
	}

	public void setSeedName(String seedName) {
		this.seedName = seedName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

}
