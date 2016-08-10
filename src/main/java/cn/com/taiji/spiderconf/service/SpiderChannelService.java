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

import cn.com.taiji.spiderconf.domain.SpiderChannel;
import cn.com.taiji.spiderconf.dto.SpiderChannelDto;
import cn.com.taiji.spiderconf.respository.SpiderChannelRespository;
import cn.com.taiji.spiderconf.respository.SpiderSiteRespository;
import cn.com.taiji.sys.util.Pagination;
import cn.com.taiji.sys.util.UUIDUtils;

@Service
public class SpiderChannelService {
	
	@Autowired
	private SpiderChannelRespository channelRespository;
	@Autowired
	private SpiderSiteRespository siteRespository;
	
	public SpiderChannelDto findOne(String channelId){
		return new SpiderChannelDto()
		.init(channelRespository.findOne(channelId));
	}
	public Pagination<SpiderChannelDto> findByPage(Integer pageSize,Integer pageNum,SpiderChannelDto searchDto){
		PageRequest request = new PageRequest(pageNum - 1,
				pageSize, null);
		Page<SpiderChannel> page ;
		page = this.channelRespository.findAll(new Specification<SpiderChannel>() {
			
			@Override
			public Predicate toPredicate(Root<SpiderChannel> root, CriteriaQuery<?> query,
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
        Pagination<SpiderChannelDto> pagination = this.getPagination(page);
		return pagination;
	}

	private Pagination<SpiderChannelDto> getPagination(Page<SpiderChannel> page) {
		List<SpiderChannel> channels = page.getContent();
        List<SpiderChannelDto> dtos = getDtoList(channels);
        Pagination<SpiderChannelDto> pagination = new Pagination<SpiderChannelDto>(page.getNumber()+1, page.getSize(), page.getTotalPages(), page.getTotalElements(), dtos);
        return pagination;
	}
	
	private List<SpiderChannelDto> getDtoList(List<SpiderChannel> channels) {
		List<SpiderChannelDto> dtos = new ArrayList<>();
        for(SpiderChannel site:channels)
        {
        	dtos.add(new SpiderChannelDto().init(site));
        }
        return dtos;
	}

	public void save(SpiderChannelDto channelDto){
		SpiderChannel channel = dto2domain(channelDto);
		channel.setId(UUIDUtils.getUUID32());
		this.channelRespository.save(channel);
	}

	private SpiderChannel dto2domain(SpiderChannelDto channelDto) {
		SpiderChannel channel = new SpiderChannel();
		BeanUtils.copyProperties(channelDto, channel);
		String siteId = channelDto.getSpiderSiteId();
		if(StringUtils.hasText(siteId)){
			channel.setSpiderSite(this.siteRespository.getOne(siteId));
		}
		return channel;
	}
	public void update(SpiderChannelDto channelDto){
		this.channelRespository.saveAndFlush(dto2domain(channelDto));
	}
	public SpiderChannelDto delete(String channelId){
		SpiderChannel channel = this.channelRespository.findOne(channelId);
		this.channelRespository.delete(channel);
		return new SpiderChannelDto().init(channel);
		
	}
	public List<SpiderChannelDto> ListAll(){
		List<SpiderChannel> list = this.channelRespository.findAll();
		List<SpiderChannelDto> listAll = getDtoList(list);
		return listAll;
		
	}
}
