package cn.com.taiji.spiderconfig.dto;

import java.io.Serializable;


public class SpiderConfigDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5343968326680134532L;

	private String id;

	private int downloadSize;
	
	private String name;

	private int duration;

	private int extractSize;

	private String resultHandler;

	private int resultSize;

	private String schedulerPeriod;

	private String storePath;
	
	private boolean status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getDownloadSize() {
		return downloadSize;
	}

	public void setDownloadSize(int downloadSize) {
		this.downloadSize = downloadSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getExtractSize() {
		return extractSize;
	}

	public void setExtractSize(int extractSize) {
		this.extractSize = extractSize;
	}

	public String getResultHandler() {
		return resultHandler;
	}

	public void setResultHandler(String resultHandler) {
		this.resultHandler = resultHandler;
	}

	public int getResultSize() {
		return resultSize;
	}

	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	public String getSchedulerPeriod() {
		return schedulerPeriod;
	}

	public void setSchedulerPeriod(String schedulerPeriod) {
		this.schedulerPeriod = schedulerPeriod;
	}

	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
