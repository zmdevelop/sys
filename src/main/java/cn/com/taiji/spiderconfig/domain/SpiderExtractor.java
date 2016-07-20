package cn.com.taiji.spiderconfig.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the spider_extractor database table.
 * 
 */
@Entity
@Table(name="spider_extractor")
@NamedQuery(name="SpiderExtractor.findAll", query="SELECT s FROM SpiderExtractor s")
public class SpiderExtractor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String name;

	//bi-directional many-to-one association to SpiderPage
	@OneToMany(mappedBy="spiderExtractor")
	private List<SpiderPage> spiderPages;

	public SpiderExtractor() {
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

	public List<SpiderPage> getSpiderPages() {
		return this.spiderPages;
	}

	public void setSpiderPages(List<SpiderPage> spiderPages) {
		this.spiderPages = spiderPages;
	}

	public SpiderPage addSpiderPage(SpiderPage spiderPage) {
		getSpiderPages().add(spiderPage);
		spiderPage.setSpiderExtractor(this);

		return spiderPage;
	}

	public SpiderPage removeSpiderPage(SpiderPage spiderPage) {
		getSpiderPages().remove(spiderPage);
		spiderPage.setSpiderExtractor(null);

		return spiderPage;
	}

}