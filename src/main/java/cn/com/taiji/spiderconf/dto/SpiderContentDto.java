package cn.com.taiji.spiderconf.dto;

import org.springframework.beans.BeanUtils;

import cn.com.taiji.spiderconf.domain.SpiderContent;
import cn.com.taiji.spiderconf.domain.SpiderSite;


public class SpiderContentDto{

	private String id;
	private String name;
	private String title;
	private String subTitle;
	private String url;
	private String urlType;
	private String publishTime;
	private String content;
	private String origin;
	private String author;
	private String contentType;
	private String spiderSiteId;
	private String spiderSiteName;
	private String tableName;
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
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getUrlType() {
		return urlType;
	}
	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getSpiderSiteId() {
		return spiderSiteId;
	}
	public void setSpiderSiteId(String spiderSiteId) {
		this.spiderSiteId = spiderSiteId;
	}
	public String getSpiderSiteName() {
		return spiderSiteName;
	}
	public void setSpiderSiteName(String spiderSiteName) {
		this.spiderSiteName = spiderSiteName;
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public SpiderContentDto init(SpiderContent content){
		BeanUtils.copyProperties(content, this);
		SpiderSite site = content.getSpiderSite();
		if(site!=null){
			this.spiderSiteId=site.getId();
			this.spiderSiteName=site.getName();
		}
		return this;
	}

}
