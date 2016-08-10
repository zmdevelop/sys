package cn.com.taiji.spiderconf.dto;

import org.springframework.beans.BeanUtils;

import cn.com.taiji.spiderconf.domain.SpiderChannel;
import cn.com.taiji.spiderconf.domain.SpiderSite;

public class SpiderChannelDto {
	private String id;
	private String name;
	private String extractor;
	private String urlType;
	private String url;
	private String channelType;
	private String spiderSiteId;
	private String spiderSiteName;
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
	public String getExtractor() {
		return extractor;
	}
	public void setExtractor(String extractor) {
		this.extractor = extractor;
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
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
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
	
	public SpiderChannelDto init(SpiderChannel channel){
		BeanUtils.copyProperties(channel,this);
		SpiderSite s = channel.getSpiderSite();
		if(s!=null){
			this.spiderSiteId = s.getId();
			this.spiderSiteName = s.getName();
		}
		return this;
	}
}
