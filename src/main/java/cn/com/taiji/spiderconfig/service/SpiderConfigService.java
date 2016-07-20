package cn.com.taiji.spiderconfig.service;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.kernal.spiderman.Config;
import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.kit.Seed;
import net.kernal.spiderman.worker.extract.ExtractTask;
import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.schema.Model;
import net.kernal.spiderman.worker.extract.schema.rule.EqualsRule;
import net.kernal.spiderman.worker.extract.schema.rule.RegexRule;
import net.kernal.spiderman.worker.extract.schema.rule.StartsWithRule;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

import cn.com.taiji.spiderconfig.domain.SpiderConfig;
import cn.com.taiji.spiderconfig.domain.SpiderPage;
import cn.com.taiji.spiderconfig.domain.SpiderSeed;
import cn.com.taiji.spiderconfig.dto.SpiderConfigDto;
import cn.com.taiji.spiderconfig.dto.SpiderSeedDto;
import cn.com.taiji.spiderconfig.respository.SpiderConfigRespository;
import cn.com.taiji.spiderconfig.respository.SpiderPageRespository;
import cn.com.taiji.spiderconfig.respository.SpiderSeedRespository;
import cn.com.taiji.sys.util.Pagination;
import cn.com.taiji.sys.util.UUIDUtils;

@Service
public class SpiderConfigService {

	@Inject
	SpiderConfigRespository spiderConfigRespository;
	
	@Inject
	SpiderSeedRespository spiderSeedRespository;
	
	@Inject
	SpiderPageRespository spiderPagedRespository;
	
	private final Map<String,Spiderman> spiderManMap = new HashMap<String,Spiderman>();
	
	@Transactional
	 public  Pagination<SpiderConfigDto> findByPage(int pageSize,int pageNum,SpiderConfigDto searchDto) {

		PageRequest request = new PageRequest(pageNum - 1,
			pageSize, null);
		Page<SpiderConfig> page;
		page = spiderConfigRespository.findAll(new Specification<SpiderConfig>() {
		    @Override
		    public Predicate toPredicate(Root<SpiderConfig> root,
			    CriteriaQuery<?> query, CriteriaBuilder cb) {
			List<Predicate> pl = new ArrayList<Predicate>();
			if (searchDto.getName() != null && !"".equals(searchDto.getName())) {
			    pl.add(cb.like(root.<String> get("name"),
				    "%" + searchDto.getName() + "%"));
			}
			return cb.and(pl.toArray(new Predicate[0]));
		    }

		}, request);

        List<SpiderConfig> configs = page.getContent();
        List<SpiderConfigDto> dtos = new ArrayList<>();
        for(SpiderConfig config:configs)
        {
        	SpiderConfigDto dto = toSpiderConfigDto(config);
        	dtos.add(dto);
        }
        Pagination<SpiderConfigDto> pagination = new Pagination<SpiderConfigDto>(pageNum, pageSize, page.getTotalPages(), page.getTotalElements(), dtos);
		return pagination;
	}
	 
	@Transactional
	 public void save(SpiderConfigDto configDto) {
			// TODO Auto-generated method stub
		SpiderConfig config = toSpiderConfig(configDto);
		config.setId(UUIDUtils.getUUID32());
        spiderConfigRespository.save(config);
	}
	
	 
		@Transactional
		 public void update(SpiderConfigDto configDto) {
				// TODO Auto-generated method stub
			SpiderConfig config = toSpiderConfig(configDto);
		     spiderConfigRespository.saveAndFlush(config);
		}


	public void delete(String spiderConfigId) {
		// TODO Auto-generated method stub
		SpiderConfig spiderConfig = spiderConfigRespository.findOne(spiderConfigId);
		spiderConfigRespository.delete(spiderConfig);
	}

	public Pagination<SpiderSeedDto> findSeedByPage(Integer pageSize,
			Integer pageNum, String configId) {
		PageRequest request = new PageRequest(pageNum - 1,
				pageSize, null);
			Page<SpiderSeed> page;
			page = spiderSeedRespository.findAll(new Specification<SpiderSeed>() {
			    @Override
			    public Predicate toPredicate(Root<SpiderSeed> root,
				    CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> pl = new ArrayList<Predicate>();
				return cb.and(pl.toArray(new Predicate[0]));
			    }

			}, request);

	        List<SpiderSeed> seeds = page.getContent();
	        List<SpiderSeedDto> dtos = new ArrayList<>();
	        for(SpiderSeed seed:seeds)
	        {
	        	SpiderSeedDto dto = toSpiderSeedDto(seed);
	        	dtos.add(dto);
	        }
	        Pagination<SpiderSeedDto> pagination = new Pagination<SpiderSeedDto>(pageNum, pageSize, page.getTotalPages(), page.getTotalElements(), dtos);
			return pagination;
	}
	

	public SpiderConfigDto findOne(String configId) {
		// TODO Auto-generated method stub
		return  toSpiderConfigDto(spiderConfigRespository.findOne(configId));
	}
	
	@Transactional
	public void saveSeed(SpiderSeedDto dto) {
		// TODO Auto-generated method stub
		dto.setSeedId(UUIDUtils.getUUID32());
		spiderSeedRespository.save(toSpiderSeed(dto));
	}
	
	@Transactional
	 public void updateSeed(SpiderSeedDto dto) {
			// TODO Auto-generated method stub
			spiderSeedRespository.saveAndFlush(toSpiderSeed(dto));
		}
	
	@Transactional
	public void deleteSeed(String seedId) {
		// TODO Auto-generated method stub
		spiderSeedRespository.delete(seedId);;
	}
	
	public List<SpiderConfigDto> findAll() {
		// TODO Auto-generated method stub
		List<SpiderConfig> configs =  spiderConfigRespository.findAll();
		List<SpiderConfigDto> configDtos = new ArrayList<>();
		for(SpiderConfig config:configs)
		{
			SpiderConfigDto dto = toSpiderConfigDto(config);
			configDtos.add(dto);
		}
		return configDtos;
	}
	
	private SpiderConfigDto toSpiderConfigDto(SpiderConfig config)
    {
   	 SpiderConfigDto dto = new SpiderConfigDto();
   	 BeanUtils.copyProperties(config,dto);
   	 return dto;
    }
    
    private SpiderConfig toSpiderConfig(SpiderConfigDto configDto)
    {
   	SpiderConfig config = new SpiderConfig();
   	 BeanUtils.copyProperties(configDto,config);
   	 return config;
    }
    
    private SpiderSeedDto toSpiderSeedDto(SpiderSeed spiderSeed)
    {
     SpiderSeedDto dto = new SpiderSeedDto();
   	 BeanUtils.copyProperties(spiderSeed,dto);
   	 if(spiderSeed.getSpiderConfig()!=null)
   	 {
       dto.setConfigId(spiderSeed.getSpiderConfig().getId());
   	 }
   	 return dto;
    }
    
    private SpiderSeed toSpiderSeed(SpiderSeedDto seedDto)
    {
     SpiderSeed spiderSeed = new SpiderSeed();
   	 BeanUtils.copyProperties(seedDto,spiderSeed);
   	 if(seedDto.getConfigId()!=null)
   	 {
   		 SpiderConfig config = spiderConfigRespository.findOne(seedDto.getConfigId());
   		 spiderSeed.setSpiderConfig(config);
   	 }
   	 return spiderSeed;
    }
    

	public void startSpider(String spiderConfigId,boolean status) {
		// TODO Auto-generated method stub
		
		SpiderConfig spiderConfig = spiderConfigRespository.findOne(spiderConfigId);
		spiderConfig.setStatus(status);
		spiderConfigRespository.saveAndFlush(spiderConfig);
		if(status)
		{
		 pramToConvert(spiderConfig);
		}
		else
		{
			spiderManMap.get(spiderConfigId).stop();
		}
	}
	
	private void pramToConvert(SpiderConfig spiderConfig)
	{
		Config cnf = new Config();
		List<SpiderSeed> spiderSeeds = spiderConfig.getSpiderSeeds();
		for(SpiderSeed spiderSeed:spiderSeeds)
		{
			Seed seed = new Seed(spiderSeed.getSeedName(),spiderSeed.getUrl());
			cnf.addSeed(seed);
		}
		cnf.set("logger.level", "ERROR");
		cnf.set("duration", spiderConfig.getDuration());
		cnf.set("worker.download.size", spiderConfig.getDownloadSize());
		cnf.set("worker.extract.size", spiderConfig.getExtractSize());
		cnf.set("worker.result.size", spiderConfig.getResultSize());
		if(spiderConfig.getResultHandler().equals("0"))
		cnf.set("worker.result.handler", "net.kernal.spiderman.worker.result.handler.impl.DbResultHandler");
		if(spiderConfig.getResultHandler().equals("1"))
			cnf.set("worker.result.handler", "net.kernal.spiderman.worker.result.handler.impl.FileBodyResultHandler");
		if(spiderConfig.getResultHandler().equals("2"))
			cnf.set("worker.result.handler", "net.kernal.spiderman.worker.result.handler.impl.FileJsonResultHandler");
	    List<SpiderPage> pages = spiderPagedRespository.findByConfigId(spiderConfig.getId());
		for(SpiderPage spiderPage:pages)
		{
			Class<Extractor> cls = null;
			if(spiderPage.getSpiderExtractor().getId().equals("0"))
			{
				 cls = K.loadClass("net.kernal.spiderman.worker.extract.extractor.impl.TextExtractor");
			}
			else if(spiderPage.getSpiderExtractor().getId().equals("1"))
			{
			     cls = K.loadClass("net.kernal.spiderman.worker.extract.extractor.impl.LinksExtractor");
			}
			else if(spiderPage.getSpiderExtractor().getId().equals("2"))
			{
				 cls = K.loadClass("net.kernal.spiderman.worker.extract.extractor.impl.HtmlCleanerExtractor");
			}
			cnf.registerExtractor(spiderPage.getSpiderExtractor().getName(),cls);
			net.kernal.spiderman.worker.extract.schema.Page page = new net.kernal.spiderman.worker.extract.schema.Page(spiderPage.getName()){
				public void config(UrlMatchRules rules, Models models) {}
			};
			if(spiderPage.getUrlType().equals("regex"))
			{
			  page.getRules().add(new RegexRule(spiderPage.getUrl()).setNegativeEnabled(false));
			}
			else if(spiderPage.getUrlType().equals("equal"))
			{
			  page.getRules().add(new EqualsRule(spiderPage.getUrl()).setNegativeEnabled(false));
			}
			else if(spiderPage.getUrlType().equals("startsWith"))
			{
				page.getRules().add(new StartsWithRule(spiderPage.getUrl()).setNegativeEnabled(false));
			}
			final Constructor<? extends Extractor> ct;
			try {
				ct = cls.getConstructor(ExtractTask.class, String.class, Model[].class);
			} catch (Exception e) {
				throw new Spiderman.Exception("", e);
			}
			page.setExtractorBuilder((t, p, mds) -> {
				try {
					return ct.newInstance(t, p, mds);
				} catch (Exception e) {
					throw new Spiderman.Exception("", e);
				}
			});
			cnf.addPage(page);
		}
		Spiderman spiderman = new Spiderman(cnf);
		spiderManMap.put(spiderConfig.getId(),spiderman);
		startSpider(spiderman);
	}
	
	@Async
	private void startSpider(Spiderman spiderman)
	{
		spiderman.go();
	}

	public SpiderSeedDto findOneSeed(String seedId) {
		// TODO Auto-generated method stub
		SpiderSeed seed =  spiderSeedRespository.findOne(seedId);
		return toSpiderSeedDto(seed);
	}


}
