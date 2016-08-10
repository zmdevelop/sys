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
@Table(name = "spider_content_conf")
@NamedQuery(name = "SpiderContent.findAll", query = "SELECT s FROM SpiderContent s")
public class SpiderContent implements Serializable {
	private static final long serialVersionUID = -894009942464427415L;

	@Id
	private String id;
	private String name;
	private String title;
	@Column(name = "sub_title")
	private String subTitle;
	private String url;
	@Column(name="url_type")
	private String urlType;
	@Column(name = "publish_time")
	private String publishTime;
	private String content;
	private String author;
	private String origin;
	@Column(name="content_type")
	private String contentType;
	@ManyToOne
	@JoinColumn(name="site_id")
	private SpiderSite spiderSite;
	@OneToMany(mappedBy="spiderContent")
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
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public SpiderSite getSpiderSite() {
		return spiderSite;
	}
	
	public void setSpiderSite(SpiderSite spiderSite) {
		this.spiderSite = spiderSite;
	}
	public List<SpiderField> getSpiderFields() {
		return spiderFields;
	}
	public void setSpiderFields(List<SpiderField> spiderFields) {
		this.spiderFields = spiderFields;
	}
	

}
