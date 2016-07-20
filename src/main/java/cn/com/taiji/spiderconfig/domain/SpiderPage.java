package cn.com.taiji.spiderconfig.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the spider_page database table.
 * 
 */
@Entity
@Table(name="spider_page")
@NamedQuery(name="SpiderPage.findAll", query="SELECT s FROM SpiderPage s")
public class SpiderPage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String name;
	
	@Column(name="url_type")
	private String urlType;
	
	@Column
	private String url;
	
	@ManyToOne
	@JoinColumn(name="config_id")
	private SpiderConfig spiderConfig;

	//bi-directional many-to-one association to SpiderExtractor
	@ManyToOne
	@JoinColumn(name="extractor_id")
	private SpiderExtractor spiderExtractor;

	public SpiderPage() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SpiderExtractor getSpiderExtractor() {
		return this.spiderExtractor;
	}

	public void setSpiderExtractor(SpiderExtractor spiderExtractor) {
		this.spiderExtractor = spiderExtractor;
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

	public SpiderConfig getSpiderConfig() {
		return spiderConfig;
	}

	public void setSpiderConfig(SpiderConfig spiderConfig) {
		this.spiderConfig = spiderConfig;
	}

}