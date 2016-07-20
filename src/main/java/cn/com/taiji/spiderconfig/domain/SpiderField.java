package cn.com.taiji.spiderconfig.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the spider_field database table.
 * 
 */
@Entity
@Table(name="spider_field")
@NamedQuery(name="SpiderField.findAll", query="SELECT s FROM SpiderField s")
public class SpiderField implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String name;

	public SpiderField() {
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

}