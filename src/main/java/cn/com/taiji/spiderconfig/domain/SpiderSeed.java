package cn.com.taiji.spiderconfig.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the spider_seed database table.
 * 
 */
@Entity
@Table(name="spider_seed")
@NamedQuery(name="SpiderSeed.findAll", query="SELECT s FROM SpiderSeed s")
public class SpiderSeed implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	private String seedId;

	@Column(name="name")
	private String seedName;

	private String url;

	//bi-directional many-to-one association to SpiderConfig
	@ManyToOne
	@JoinColumn(name="config_id")
	private SpiderConfig spiderConfig;

	public SpiderSeed() {
	}

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
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public SpiderConfig getSpiderConfig() {
		return this.spiderConfig;
	}

	public void setSpiderConfig(SpiderConfig spiderConfig) {
		this.spiderConfig = spiderConfig;
	}

}