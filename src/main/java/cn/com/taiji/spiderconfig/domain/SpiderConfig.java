package cn.com.taiji.spiderconfig.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the spider_config database table.
 * 
 */
@Entity
@Table(name="spider_config")
@NamedQuery(name="SpiderConfig.findAll", query="SELECT s FROM SpiderConfig s")
public class SpiderConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6418432670986637540L;

	@Id
	private String id;

	@Column(name="download_size")
	private int downloadSize;
	
	private String name;

	private int duration;
	
	private boolean status;

	@Column(name="extract_size")
	private int extractSize;

	@Column(name="result_handler")
	private String resultHandler;

	@Column(name="result_size")
	private int resultSize;

	@Column(name="scheduler_period")
	private String schedulerPeriod;

	@Column(name="store_path")
	private String storePath;

	//bi-directional many-to-one association to SpiderSeed
	@OneToMany(mappedBy="spiderConfig")
	private List<SpiderSeed> spiderSeeds;

	public SpiderConfig() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getDownloadSize() {
		return this.downloadSize;
	}

	public void setDownloadSize(int downloadSize) {
		this.downloadSize = downloadSize;
	}

	public int getDuration() {
		return this.duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getExtractSize() {
		return this.extractSize;
	}

	public void setExtractSize(int extractSize) {
		this.extractSize = extractSize;
	}

	public String getResultHandler() {
		return this.resultHandler;
	}

	public void setResultHandler(String resultHandler) {
		this.resultHandler = resultHandler;
	}

	public int getResultSize() {
		return this.resultSize;
	}

	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	public String getSchedulerPeriod() {
		return this.schedulerPeriod;
	}

	public void setSchedulerPeriod(String schedulerPeriod) {
		this.schedulerPeriod = schedulerPeriod;
	}

	public String getStorePath() {
		return this.storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}


	public List<SpiderSeed> getSpiderSeeds() {
		return this.spiderSeeds;
	}

	public void setSpiderSeeds(List<SpiderSeed> spiderSeeds) {
		this.spiderSeeds = spiderSeeds;
	}

	public SpiderSeed addSpiderSeed(SpiderSeed spiderSeed) {
		getSpiderSeeds().add(spiderSeed);
		spiderSeed.setSpiderConfig(this);

		return spiderSeed;
	}

	public SpiderSeed removeSpiderSeed(SpiderSeed spiderSeed) {
		getSpiderSeeds().remove(spiderSeed);
		spiderSeed.setSpiderConfig(null);

		return spiderSeed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}