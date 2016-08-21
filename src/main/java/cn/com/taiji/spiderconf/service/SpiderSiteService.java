package cn.com.taiji.spiderconf.service;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.kernal.spiderman.Config;
import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.kit.JdbcUtils;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.kit.Seed;
import net.kernal.spiderman.kit.TomcatDataSource;
import net.kernal.spiderman.logger.Logger;
import net.kernal.spiderman.logger.Loggers;
import net.kernal.spiderman.worker.extract.ExtractTask;
import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.schema.Field;
import net.kernal.spiderman.worker.extract.schema.Model;
import net.kernal.spiderman.worker.extract.schema.filter.ScriptableFilter;
import net.kernal.spiderman.worker.extract.schema.filter.TrimFilter;
import net.kernal.spiderman.worker.extract.schema.filter.URLNormalizer;
import net.kernal.spiderman.worker.extract.schema.rule.RegexRule;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.com.taiji.spiderconf.domain.SpiderChannel;
import cn.com.taiji.spiderconf.domain.SpiderContent;
import cn.com.taiji.spiderconf.domain.SpiderField;
import cn.com.taiji.spiderconf.domain.SpiderSite;
import cn.com.taiji.spiderconf.dto.SpiderSiteDto;
import cn.com.taiji.spiderconf.respository.SpiderContentRespository;
import cn.com.taiji.spiderconf.respository.SpiderFieldRespository;
import cn.com.taiji.spiderconf.respository.SpiderSiteRespository;
import cn.com.taiji.sys.util.Pagination;
import cn.com.taiji.sys.util.UUIDUtils;

@Service
public class SpiderSiteService {
	Logger log = Loggers.getLogger(SpiderSiteService.class);

	@Autowired
	private SpiderSiteRespository siteRespository;
	@Autowired
	private SpiderFieldRespository fieldRespository;
	@Autowired
	private SpiderContentRespository spiderContentRespository;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	private final Map<String, Spiderman> spiderManMap = new HashMap<String, Spiderman>();

	public SpiderSiteDto findOne(String siteId) {
		SpiderSite site = this.siteRespository.findOne(siteId);
		return new SpiderSiteDto().init(site);
	}

	public Pagination<SpiderSiteDto> findByPage(Integer pageSize,
			Integer pageNum, SpiderSiteDto searchDto) {
		PageRequest request = new PageRequest(pageNum - 1, pageSize, null);
		Page<SpiderSite> page;
		page = this.siteRespository.findAll(new Specification<SpiderSite>() {

			@Override
			public Predicate toPredicate(Root<SpiderSite> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> pl = new ArrayList<Predicate>();
				if (searchDto.getName() != null
						&& !searchDto.getName().equals("")) {
					pl.add(cb.like(root.<String> get("name"),
							"%" + searchDto.getName() + "%"));
				}
				return cb.and(pl.toArray(new Predicate[0]));
			}
		}, request);
		Pagination<SpiderSiteDto> pagination = this.getPagination(page);
		return pagination;

	}

	private Pagination<SpiderSiteDto> getPagination(Page<SpiderSite> page) {
		List<SpiderSite> sites = page.getContent();
		List<SpiderSiteDto> dtos = getDtoList(sites);
		Pagination<SpiderSiteDto> pagination = new Pagination<SpiderSiteDto>(
				page.getNumber() + 1, page.getSize(), page.getTotalPages(),
				page.getTotalElements(), dtos);
		return pagination;
	}

	public void update(SpiderSiteDto siteDto) {
		this.siteRespository.saveAndFlush(this.dto2Domain(siteDto));
	}

	private SpiderSite dto2Domain(SpiderSiteDto siteDto) {
		SpiderSite spiderSite = new SpiderSite();
		BeanUtils.copyProperties(siteDto, spiderSite);
		return spiderSite;
	}

	public void save(SpiderSiteDto siteDto) {
		SpiderSite spiderSite = this.dto2Domain(siteDto);
		spiderSite.setId(UUIDUtils.getUUID32());
		spiderSite = this.siteRespository.save(spiderSite);
	}

	public SpiderSiteDto delete(String siteId) {
		SpiderSite site = this.siteRespository.findOne(siteId);
		this.siteRespository.delete(site);
		return new SpiderSiteDto().init(site);
	}

	public List<SpiderSiteDto> findAll() {
		List<SpiderSite> list = this.siteRespository.findAll();
		return getDtoList(list);
	}

	private List<SpiderSiteDto> getDtoList(List<SpiderSite> list) {
		List<SpiderSiteDto> listAll = new ArrayList<SpiderSiteDto>();
		for (SpiderSite site : list) {
			listAll.add(new SpiderSiteDto().init(site));
		}
		return listAll;
	}

	public void startSpider(String siteId, Boolean status) {
		SpiderSite site = this.siteRespository.findOne(siteId);
		site.setStatus(status);
		this.siteRespository.saveAndFlush(site);
		if(status)
		createTable(siteId);
		if (status) {
			pramToConvert(site);
		} else {
			if (spiderManMap.get(site.getId()) != null) {
				spiderManMap.get(site.getId()).stop();
			}
		}

	}
	
	private void createTable(String siteId)
	{
		List<SpiderContent> spiderContents = spiderContentRespository.findBySiteId(siteId);
		//jdbcTemplate.update(sql);
		StringBuffer sb = new StringBuffer();
		for(SpiderContent sc:spiderContents)
		{
			if(isExitTable(sc.getTableName()))
			{
				continue;
			}
			sb.append("CREATE TABLE `");
			List<SpiderField> fields = fieldRespository.findByContentId(sc.getId());
			sb.append(sc.getTableName()+"`( ");
			sb.append("`id` varchar(50) NOT NULL,");
			for(SpiderField sf : fields)
			{
				sb.append("`"+sf.getAttr()+"` varchar(2000) DEFAULT NULL,");
			}
			sb.append(" PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		    log.info(sb.toString());
			jdbcTemplate.update(sb.toString());
		}
		
	}
	
	private boolean isExitTable(String tableName)
	{
		 Connection conn = null;
		 ResultSet tabs = null;  
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
		        DatabaseMetaData dbMetaData = conn.getMetaData();  
		        String[]   types   =   { "TABLE" };  
		        tabs = dbMetaData.getTables(null, null, tableName, types);  
		        if (tabs.next()) {  
		            return true;  
		        }  
		    } catch (Exception e) {  
		        e.printStackTrace();  
		    }finally{  
		        try {
					tabs.close();
					 conn.close(); 
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
		    }  
		    return false;  
	}

	private void pramToConvert(SpiderSite spiderConfig) {
		Config cnf = new Config();
		Seed seed = new Seed(spiderConfig.getName(), spiderConfig.getUrl());
		cnf.addSeed(seed);
		String handlerSize = spiderConfig.getHandlerSize();
		cnf.set("worker.download.size", handlerSize);
		cnf.set("worker.extract.size", handlerSize);
		cnf.set("worker.result.size", handlerSize);
		cnf.set("worker.result.store", "/result/" + spiderConfig.getName()
				+ "/content");
		cnf.set("queue.store.path", "/result/" + spiderConfig.getName()
				+ "/queue");
		cnf.set("duration", spiderConfig.getDuration());
		if (spiderConfig.getResultHandler().equals("0"))
			cnf.set("worker.result.handler",
					"net.kernal.spiderman.worker.result.handler.impl.DbResultHandler");
		if (spiderConfig.getResultHandler().equals("1"))
			cnf.set("worker.result.handler",
					"net.kernal.spiderman.worker.result.handler.impl.FileBodyResultHandler");
		if (spiderConfig.getResultHandler().equals("2"))
			cnf.set("worker.result.handler",
					"net.kernal.spiderman.worker.result.handler.impl.FileJsonResultHandler");

		Class<Extractor> cls = K
				.loadClass("net.kernal.spiderman.worker.extract.extractor.impl.HtmlCleanerExtractor");
		Class<Extractor> clsTT = K
				.loadClass("net.kernal.spiderman.worker.extract.extractor.impl.LinksExtractor");
		cnf.registerExtractor("HtmlCleaner", cls);
		cnf.registerExtractor("Links", clsTT);

		List<SpiderChannel> pages = spiderConfig.getSpiderChannels();
		for (SpiderChannel page : pages) {
			page.setName(page.getName()+"_LinkList");//resultHandler处理以"_LinkList"结尾的不处理(DB,Solr)
			if (null != page.getChannelType()
					&& "0".equals(page.getChannelType())) {
				this.setDefaultPage(page, cnf);// 默认列表
			} else if (null != page.getChannelType()
					&& "1".equals(page.getChannelType())){
				this.setCustomPage(page, cnf);// 自定义连接抽取
			}

		}
		List<SpiderContent> contents = spiderConfig.getSpiderContents();
		for (SpiderContent page : contents) {
			if(page.getContentType()!=null && page.getContentType().equals("0")){
				this.setContentPage(page, cnf);// 标准内容抽取
			}else if(page.getContentType()!=null && page.getContentType().equals("1")){
				this.setCustomContentPage(page,cnf);//自定义内容抽取
			}
		}
		Spiderman spiderman = new Spiderman(cnf);
		spiderManMap.put(spiderConfig.getId(), spiderman);
		startSpider(spiderman);
	}
	/**
	 * 自定义 内容抽取
	 * @param page
	 * @param cnf
	 */
	private void setCustomContentPage(SpiderContent page, Config cnf) {
		List<SpiderField> fieldList = page.getSpiderFields();
		net.kernal.spiderman.worker.extract.schema.Page schemapage = new net.kernal.spiderman.worker.extract.schema.Page(
				page.getName()) {
           
			public void config(UrlMatchRules rules, Models models) {
				rules.add(new RegexRule(page.getUrl())
						.setNegativeEnabled(false));

				Model model = models.addModel(page.getName());
				for (SpiderField field : fieldList) {
					Field f = model.addField(field.getName())//
							.set("xpath", field.getXpath());
						f.set("isSorted", true);
					String attr = field.getAttr();
					if (attr != null && !attr.equals("")) {
						f.set("attr", attr);
					}
					String filterValue = field.getFilterValue();
					if(filterValue!=null && !"".equals(filterValue)){
						Field.ValueFilter filter = new ScriptableFilter(
								filterValue);
						/*switch (field.getFilterType()) {
						case "script":
							break;
						case "trim":
							new TrimFilter();
							break;
						case "urlNormalizer":
							new URLNormalizer();
							break;
						}*/
						f.addFilter(filter);
					}
				}
			}
		};
		schemapage.setContentType(page.getContentType()+"-"+page.getTableName());
		final Map<String, Class<Extractor>> extractors = cnf.getExtractors()
				.all();
		final Class<? extends Extractor> extractorClass = extractors
				.get("HtmlCleaner");
		if (extractorClass == null) {
			throw new Spiderman.Exception("");
		}
		final Constructor<? extends Extractor> ct;
		try {
			ct = extractorClass.getConstructor(ExtractTask.class, String.class,
					Model[].class);
		} catch (Exception e) {
			throw new Spiderman.Exception("", e);
		}
		schemapage.setExtractorBuilder((t, p, mds) -> {
			try {
				return ct.newInstance(t, p, mds);
			} catch (Exception e) {
				throw new Spiderman.Exception("", e);
			}
		});
		cnf.addPage(schemapage);
	}

	/**
	 * 标准内容抽取
	 * 
	 * @param content
	 * @param cnf
	 */
	private void setContentPage(SpiderContent content, Config cnf) {
		net.kernal.spiderman.worker.extract.schema.Page schemapage = new net.kernal.spiderman.worker.extract.schema.Page(
				content.getName()) {
			public void config(UrlMatchRules rules, Models models) {
				rules.add(new RegexRule(content.getUrl())
						.setNegativeEnabled(false));
				Model model = models.addModel(content.getName());
				model.addField("title")// 标题
						.set("xpath", content.getTitle());
				model.addField("content")// 文章内容
						.set("xpath", content.getContent());
				String publishTime = content.getPublishTime();
				if (StringUtils.hasText(publishTime)) {
					model.addField("publishTime")// 发布时间
							.set("xpath", publishTime).addFilter(cxt -> {
								String timestr = cxt.getValue();
								String time = K.findOneDate(timestr);
								if (K.isNotBlank(time)) {
									return time;
								}
								log.info("发布时间字段没有取到");
								return null;
							});
				}
				String subtitle = content.getSubTitle();
				if (StringUtils.hasText(subtitle)) {
					model.addField("subTitle")// 子标题
							.set("xpath", subtitle);
				}
				String author = content.getAuthor();
				if (StringUtils.hasText(author)) {
					model.addField("author")// 作者
							.set("xpath", author);
				}
				String origin = content.getOrigin();
				if (StringUtils.hasText(origin)) {
					model.addField("origin")// 作者
							.set("xpath", origin);
				}

			}
		};
		final Map<String, Class<Extractor>> extractors = cnf.getExtractors()
				.all();
		final Class<? extends Extractor> extractorClass = extractors
				.get("HtmlCleaner");
		if (extractorClass == null) {
			throw new Spiderman.Exception("");
		}
		final Constructor<? extends Extractor> ct;
		try {
			ct = extractorClass.getConstructor(ExtractTask.class, String.class,
					Model[].class);
		} catch (Exception e) {
			throw new Spiderman.Exception("", e);
		}
		schemapage.setExtractorBuilder((t, p, mds) -> {
			try {
				return ct.newInstance(t, p, mds);
			} catch (Exception e) {
				throw new Spiderman.Exception("", e);
			}
		});
		cnf.addPage(schemapage);

	}

	/**
	 * 自定义连接抽取
	 * 
	 * @param page
	 * @param cnf
	 */
	private void setCustomPage(SpiderChannel page, Config cnf) {
		/*
		 * List<SpiderField> fieldList = this.fieldRespository.findAll(new
		 * Specification<SpiderField>() {
		 * 
		 * @Override public Predicate toPredicate(Root<SpiderField> root,
		 * CriteriaQuery<?> query, CriteriaBuilder cb) { List<Predicate> pl =
		 * new ArrayList<Predicate>(); pl.add(cb.equal(root.<String>
		 * get("channel_id"), page.getId() )); return cb.and(pl.toArray(new
		 * Predicate[0])); } });
		 */
		List<SpiderField> fieldList = page.getSpiderFields();
		net.kernal.spiderman.worker.extract.schema.Page schemapage = new net.kernal.spiderman.worker.extract.schema.Page(
				page.getName()) {

			public void config(UrlMatchRules rules, Models models) {
				rules.add(new RegexRule(page.getUrl())
						.setNegativeEnabled(false));

				Model model = models.addModel(page.getName());
				for (SpiderField field : fieldList) {
					Field f = model.addField(field.getName())//
							.set("xpath", field.getXpath());
					if (page.getChannelType() != null
							&& page.getChannelType().equals("1")) {
						f.set("isArray", true).set("isDistinct", true)
								.set("isForNewTask", true)
								.set("isSorted", true);
					}
					if (page.getChannelType() != null
							&& page.getChannelType().equals("2")) {
						f.set("isSorted", true);
					}
					String attr = field.getAttr();
					if (attr != null && !attr.equals("")) {
						f.set("attr", attr);
					}
					String filterValue = field.getFilterValue();
					if(filterValue!=null && !"".equals(filterValue)){
						Field.ValueFilter filter = new ScriptableFilter(
								filterValue);
						/*switch (field.getFilterType()) {
						case "script":
							break;
						case "trim":
							new TrimFilter();
							break;
						case "urlNormalizer":
							new URLNormalizer();
							break;
						}*/
						f.addFilter(filter);
					}
				}
			}
		};
		final Map<String, Class<Extractor>> extractors = cnf.getExtractors()
				.all();
		final Class<? extends Extractor> extractorClass = extractors
				.get("HtmlCleaner");
		if (extractorClass == null) {
			throw new Spiderman.Exception("");
		}
		final Constructor<? extends Extractor> ct;
		try {
			ct = extractorClass.getConstructor(ExtractTask.class, String.class,
					Model[].class);
		} catch (Exception e) {
			throw new Spiderman.Exception("", e);
		}
		schemapage.setExtractorBuilder((t, p, mds) -> {
			try {
				return ct.newInstance(t, p, mds);
			} catch (Exception e) {
				throw new Spiderman.Exception("", e);
			}
		});
		cnf.addPage(schemapage);

	}

	/**
	 * 添加默认连接modelpage
	 * 
	 * @param page
	 * @param cnf
	 */
	private void setDefaultPage(SpiderChannel page, Config cnf) {
		net.kernal.spiderman.worker.extract.schema.Page schemapage = new net.kernal.spiderman.worker.extract.schema.Page(
				page.getName()) {
			public void config(UrlMatchRules rules, Models models) {
				rules.add(new RegexRule(page.getUrl())
						.setNegativeEnabled(false));
			}
		};
		final Map<String, Class<Extractor>> extractors = cnf.getExtractors()
				.all();
		final Class<? extends Extractor> extractorClass = extractors
				.get("Links");
		if (extractorClass == null) {
			throw new Spiderman.Exception("");
		}
		final Constructor<? extends Extractor> ct;
		try {
			ct = extractorClass.getConstructor(ExtractTask.class, String.class,
					Model[].class);
		} catch (Exception e) {
			throw new Spiderman.Exception("", e);
		}
		schemapage.setExtractorBuilder((t, p, mds) -> {
			try {
				return ct.newInstance(t, p, mds);
			} catch (Exception e) {
				throw new Spiderman.Exception("", e);
			}
		});
		cnf.addPage(schemapage);

	}

	@Async
	private void startSpider(Spiderman spiderman) {
		spiderman.go();
	}

}
