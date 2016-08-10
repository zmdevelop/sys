package cn.com.taiji.spiderconf.dto;

import org.springframework.beans.BeanUtils;

import cn.com.taiji.spiderconf.domain.SpiderSite;

public class SpiderSiteDto {

	private String id;
	private String name;
	private String duration;
	private String schedulerPeriod;
	private String handlerSize;
	private String resultHandler;
	private Boolean status;
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
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	
	public SpiderSiteDto init(SpiderSite site){
		 BeanUtils.copyProperties(site,this);
		/*this.id =site.getId();
		this.duration = site.getDuration();
		this.handlerSize = site.getHandlerSize();
		this.name = site.getName();
		this.resultHandler = site.getResultHandler();
		this.schedulerPeriod = site.getSchedulerPeriod();
		this.status = site.getStatus();*/
		return this;
	}

}
