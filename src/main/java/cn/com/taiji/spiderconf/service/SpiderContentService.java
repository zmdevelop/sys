package cn.com.taiji.spiderconf.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.com.taiji.spiderconf.domain.SpiderContent;
import cn.com.taiji.spiderconf.dto.SpiderContentDto;
import cn.com.taiji.spiderconf.respository.SpiderContentRespository;
import cn.com.taiji.spiderconf.respository.SpiderSiteRespository;
import cn.com.taiji.sys.util.Pagination;
import cn.com.taiji.sys.util.UUIDUtils;

@Service
public class SpiderContentService {
	
	@Autowired
	private SpiderContentRespository contentRespository;
	@Autowired
	private SpiderSiteRespository siteRespository;
	
	public SpiderContentDto findOne(String contentId){
		return new SpiderContentDto()
		.init(contentRespository.findOne(contentId));
	}
	public Pagination<SpiderContentDto> findByPage(Integer pageSize,Integer pageNum,SpiderContentDto searchDto){
		PageRequest request = new PageRequest(pageNum - 1,
				pageSize, null);
		Page<SpiderContent> page ;
		page = this.contentRespository.findAll(new Specification<SpiderContent>() {
			
			@Override
			public Predicate toPredicate(Root<SpiderContent> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> pl = new ArrayList<Predicate>();
				String name = searchDto.getName();
				if(StringUtils.hasText(name)){
					pl.add(cb.like(root.<String> get("name"),
						    "%" + name + "%"));
				}
				String siteId = searchDto.getSpiderSiteId();
				if(StringUtils.hasText(siteId)){
					pl.add(cb.equal(root.<String>get("spiderSiteId"),siteId));
				}
				return cb.and(pl.toArray(new Predicate[0]));
			}
		}, request);
        Pagination<SpiderContentDto> pagination = this.getPagination(page);
		return pagination;
	}

	private Pagination<SpiderContentDto> getPagination(Page<SpiderContent> page) {
		List<SpiderContent> contents = page.getContent();
        List<SpiderContentDto> dtos = getDtoList(contents);
        Pagination<SpiderContentDto> pagination = new Pagination<SpiderContentDto>(page.getNumber()+1, page.getSize(), page.getTotalPages(), page.getTotalElements(), dtos);
        return pagination;
	}
	
	private List<SpiderContentDto> getDtoList(List<SpiderContent> contents) {
		List<SpiderContentDto> dtos = new ArrayList<>();
        for(SpiderContent site:contents)
        {
        	dtos.add(new SpiderContentDto().init(site));
        }
        return dtos;
	}

	public void save(SpiderContentDto contentDto){
		SpiderContent content = dto2domain(contentDto);
		content.setId(UUIDUtils.getUUID32());
		this.contentRespository.save(content);
	}

	private SpiderContent dto2domain(SpiderContentDto contentDto) {
		SpiderContent content = new SpiderContent();
		BeanUtils.copyProperties(contentDto, content);
		String siteId = contentDto.getSpiderSiteId();
		if(StringUtils.hasText(siteId)){
			content.setSpiderSite(this.siteRespository.getOne(siteId));
		}
		return content;
	}
	public void update(SpiderContentDto contentDto){
		this.contentRespository.saveAndFlush(dto2domain(contentDto));
	}
	public SpiderContentDto delete(String contentId){
		SpiderContent content = this.contentRespository.findOne(contentId);
		this.contentRespository.delete(content);
		return new SpiderContentDto().init(content);
		
	}
	public List<SpiderContentDto> ListAll(){
		List<SpiderContent> list = this.contentRespository.findAll();
		List<SpiderContentDto> listAll = getDtoList(list);
		return listAll;
		
	}
}
