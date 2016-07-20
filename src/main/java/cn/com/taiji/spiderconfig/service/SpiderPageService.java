package cn.com.taiji.spiderconfig.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.taiji.spiderconfig.domain.SpiderConfig;
import cn.com.taiji.spiderconfig.domain.SpiderExtractor;
import cn.com.taiji.spiderconfig.domain.SpiderPage;
import cn.com.taiji.spiderconfig.dto.SpiderPageDto;
import cn.com.taiji.spiderconfig.respository.SpiderConfigRespository;
import cn.com.taiji.spiderconfig.respository.SpiderExtractorRespository;
import cn.com.taiji.spiderconfig.respository.SpiderPageRespository;
import cn.com.taiji.sys.util.Pagination;
import cn.com.taiji.sys.util.UUIDUtils;

@Service
public class SpiderPageService {

	@Inject
	SpiderPageRespository spiderPageRespository;
	
	@Inject
	SpiderExtractorRespository spiderExtractorRespository;
	
	@Inject
	SpiderConfigRespository spiderConfigRespository;
	
	public Pagination<SpiderPageDto> findByPage(Integer pageNum, Integer pageSize, SpiderPageDto dto) {
		// TODO Auto-generated method stub
		
		PageRequest request = new PageRequest(pageNum-1, pageSize);
		Page<SpiderPage> page = spiderPageRespository.findAll(new Specification<SpiderPage>() {
			
			@Override
			public Predicate toPredicate(Root<SpiderPage> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> pl = new ArrayList<Predicate>();
				return cb.and(pl.toArray(new Predicate[0]));
			}
		}, request);
		
		List<SpiderPage> spiderPages = page.getContent();
		List<SpiderPageDto> spiderPageDtos = new ArrayList<>();
		for(SpiderPage spiderPage:spiderPages)
		{
			SpiderPageDto spiderPageDto = toSpiderPageDto(spiderPage);
			spiderPageDtos.add(spiderPageDto);
		}
		Pagination<SpiderPageDto> pagination= new Pagination<>(pageNum, pageSize, page.getTotalPages(), page.getTotalElements(), spiderPageDtos);
	    return pagination;
	}
	
	@Transactional
	public void save(SpiderPageDto dto) {
		// TODO Auto-generated method stub
		dto.setId(UUIDUtils.getUUID32());
		spiderPageRespository.save(toSpiderPage(dto));
	}
	
	@Transactional
	public SpiderPageDto findOne(String pageId) {
		// TODO Auto-generated method stub
		SpiderPage spiderPage = spiderPageRespository.findOne(pageId);
		SpiderPageDto dto = toSpiderPageDto(spiderPage);
		return dto;
	}
	
	@Transactional
	public void update(SpiderPageDto dto) {
		// TODO Auto-generated method stub
		spiderPageRespository.saveAndFlush(toSpiderPage(dto));
	}

	@Transactional
	public void deleteById(String pageId) {
		// TODO Auto-generated method stub
		spiderPageRespository.delete(pageId);
	}
	
	private SpiderPage toSpiderPage(SpiderPageDto spiderPageDto)
	{
		SpiderPage spiderPage = new SpiderPage();
		BeanUtils.copyProperties(spiderPageDto, spiderPage);
		if(!StringUtils.isEmpty(spiderPageDto.getConfigId()))
		{
			SpiderConfig config = spiderConfigRespository.findOne(spiderPageDto.getConfigId());
			spiderPage.setSpiderConfig(config);
		}
		if(!StringUtils.isEmpty(spiderPageDto.getExtractorId()))
		{
			SpiderExtractor extractor = spiderExtractorRespository.findOne(spiderPageDto.getExtractorId());
			spiderPage.setSpiderExtractor(extractor);
		}
		return spiderPage;
	}
	
	private SpiderPageDto toSpiderPageDto(SpiderPage spiderPage)
	{
		SpiderPageDto dto = new SpiderPageDto();
		BeanUtils.copyProperties(spiderPage, dto);
		if(spiderPage.getSpiderConfig()!=null)
		{
			dto.setConfigId(spiderPage.getSpiderConfig().getId());
		}
		if(spiderPage.getSpiderExtractor()!=null)
		{
			dto.setExtractorId(spiderPage.getSpiderExtractor().getId());
		}
		return dto;
	}

}
