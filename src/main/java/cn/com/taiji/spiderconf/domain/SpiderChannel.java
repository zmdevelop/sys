package cn.com.taiji.spiderconf.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "spider_channel")
@NamedQuery(name = "SpiderChannel.findAll", query = "SELECT s FROM SpiderChannel s")
public class SpiderChannel implements Serializable {
	private static final long serialVersionUID = -8940099423487427415L;

	@Id
	private String id;
	private String name;
	private String extractor;
	@Column(name = "url_type")
	private String urlType;
	private String url;
	@Column(name = "channel_type")
	private String channelType;
	@ManyToOne
	@JoinColumn(name="site_id")
	private SpiderSite spiderSite;
	@OneToMany(mappedBy="spiderChannel")
	private List<SpiderField> spiderFields;
	
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
	public SpiderSite getSpiderSite() {
		return spiderSite;
	}
	
	public void setSpiderSite(SpiderSite spiderSite) {
		this.spiderSite = spiderSite;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public List<SpiderField> getSpiderFields() {
		return spiderFields;
	}
	public void setSpiderFields(List<SpiderField> spiderFields) {
		this.spiderFields = spiderFields;
	}
	
	

}
