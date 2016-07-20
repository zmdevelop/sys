package cn.com.taiji.spiderconfig.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the spider_rule database table.
 * 
 */
@Entity
@Table(name="spider_rule")
@NamedQuery(name="SpiderRule.findAll", query="SELECT s FROM SpiderRule s")
public class SpiderRule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="is_negative_enabled")
	private byte isNegativeEnabled;

	@Column(name="rule_type")
	private String ruleType;

	@Column(name="rule_url")
	private String ruleUrl;

	//bi-directional many-to-one association to SpiderPage
	@ManyToOne
	@JoinColumn(name="page_id")
	private SpiderPage spiderPage;

	public SpiderRule() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte getIsNegativeEnabled() {
		return this.isNegativeEnabled;
	}

	public void setIsNegativeEnabled(byte isNegativeEnabled) {
		this.isNegativeEnabled = isNegativeEnabled;
	}

	public String getRuleType() {
		return this.ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getRuleUrl() {
		return this.ruleUrl;
	}

	public void setRuleUrl(String ruleUrl) {
		this.ruleUrl = ruleUrl;
	}

	public SpiderPage getSpiderPage() {
		return this.spiderPage;
	}

	public void setSpiderPage(SpiderPage spiderPage) {
		this.spiderPage = spiderPage;
	}

}