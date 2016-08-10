package cn.com.taiji.spiderconf.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "spider_site")
@NamedQuery(name = "SpiderSite.findAll", query = "SELECT s FROM SpiderSite s")
public class SpiderSite implements Serializable {
	private static final long serialVersionUID = -8940169488614427415L;

	@Id
	private String id;
	private String name;
	private String duration;
	@Column(name = "scheduler_period")
	private String schedulerPeriod;
	@Column(name = "handler_size")
	private String handlerSize;
	@Column(name = "result_handler")
	private String resultHandler;
	private String url;
	private Boolean status;
	@OneToMany(mappedBy = "spiderSite")
	private List<SpiderChannel> spiderChannels;
	@OneToMany(mappedBy = "spiderSite")
	private List<SpiderContent> spiderContents;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getSchedulerPeriod() {
		return schedulerPeriod;
	}

	public void setSchedulerPeriod(String schedulerPeriod) {
		this.schedulerPeriod = schedulerPeriod;
	}

	public String getHandlerSize() {
		return handlerSize;
	}

	public void setHandlerSize(String handlerSize) {
		this.handlerSize = handlerSize;
	}

	public String getResultHandler() {
		return resultHandler;
	}

	public void setResultHandler(String resultHandler) {
		this.resultHandler = resultHandler;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public List<SpiderChannel> getSpiderChannels() {
		return spiderChannels;
	}

	public void setSpiderChannels(List<SpiderChannel> spiderChannels) {
		this.spiderChannels = spiderChannels;
	}

	public List<SpiderContent> getSpiderContents() {
		return spiderContents;
	}

	public void setSpiderContents(List<SpiderContent> spiderContents) {
		this.spiderContents = spiderContents;
	}
	
	
}
