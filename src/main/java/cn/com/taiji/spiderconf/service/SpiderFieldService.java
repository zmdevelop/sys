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

import cn.com.taiji.spiderconf.domain.SpiderField;
import cn.com.taiji.spiderconf.dto.SpiderFieldDto;
import cn.com.taiji.spiderconf.respository.SpiderChannelRespository;
import cn.com.taiji.spiderconf.respository.SpiderContentRespository;
import cn.com.taiji.spiderconf.respository.SpiderFieldRespository;
import cn.com.taiji.sys.util.Pagination;
import cn.com.taiji.sys.util.UUIDUtils;

@Service
public class SpiderFieldService {
	
	@Autowired
	private SpiderFieldRespository fieldRespository;
	@Autowired
	private SpiderChannelRespository channelRespository;
	@Autowired
	private SpiderContentRespository contentRespository;
	
	public SpiderFieldDto findOne(String fieldId){
		return new SpiderFieldDto()
		.init(fieldRespository.findOne(fieldId));
	}
	public Pagination<SpiderFieldDto> findByPage(Integer pageSize,Integer pageNum,SpiderFieldDto searchDto){
		PageRequest request = new PageRequest(pageNum - 1,
				pageSize, null);
		Page<SpiderField> page ;
		page = this.fieldRespository.findAll(new Specification<SpiderField>() {
			
			@Override
			public Predicate toPredicate(Root<SpiderField> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> pl = new ArrayList<Predicate>();
				String name = searchDto.getName();
				if(StringUtils.hasText(name)){
					pl.add(cb.like(root.<String> get("name"),
						    "%" + name + "%"));
				}
				String channelId = searchDto.getSpiderChannelId();
				if(StringUtils.hasText(channelId)){
					pl.add(cb.equal(root.<String>get("spiderChannel").get("id"),channelId));
				}
				return cb.and(pl.toArray(new Predicate[0]));
			}
		}, request);
        Pagination<SpiderFieldDto> pagination = this.getPagination(page);
		return pagination;
	}

	private Pagination<SpiderFieldDto> getPagination(Page<SpiderField> page) {
		List<SpiderField> fields = page.getContent();
        List<SpiderFieldDto> dtos = getDtoList(fields);
        Pagination<SpiderFieldDto> pagination = new Pagination<SpiderFieldDto>(page.getNumber()+1, page.getSize(), page.getTotalPages(), page.getTotalElements(), dtos);
        return pagination;
	}
	
	private List<SpiderFieldDto> getDtoList(List<SpiderField> fields) {
		List<SpiderFieldDto> dtos = new ArrayList<>();
        for(SpiderField site:fields)
        {
        	dtos.add(new SpiderFieldDto().init(site));
        }
        return dtos;
	}

	public void save(SpiderFieldDto fieldDto){
		SpiderField field = dto2domain(fieldDto);
		field.setId(UUIDUtils.getUUID32());
		this.fieldRespository.save(field);
	}

	private SpiderField dto2domain(SpiderFieldDto fieldDto) {
		SpiderField field = new SpiderField();
		BeanUtils.copyProperties(fieldDto, field);
		String channelId = fieldDto.getSpiderChannelId();
		if(StringUtils.hasText(channelId)){
			field.setSpiderChannel(this.channelRespository.getOne(channelId));
		}
		String contentId = fieldDto.getSpiderContentId();
		if(StringUtils.hasText(contentId)){
			field.setSpiderContent(this.contentRespository.getOne(contentId));
		}
		return field;
	}
	public void update(SpiderFieldDto fieldDto){
		this.fieldRespository.saveAndFlush(dto2domain(fieldDto));
	}
	public SpiderFieldDto delete(String fieldId){
		SpiderField field = this.fieldRespository.findOne(fieldId);
		this.fieldRespository.delete(field);
		return new SpiderFieldDto().init(field);
		
	}
	public List<SpiderFieldDto> ListAll(){
		List<SpiderField> list = this.fieldRespository.findAll();
		List<SpiderFieldDto> listAll = getDtoList(list);
		return listAll;
		
	}
}
