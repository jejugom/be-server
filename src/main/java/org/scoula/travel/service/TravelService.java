package org.scoula.travel.service;

import java.util.List;

import org.scoula.common.pagination.Page;
import org.scoula.common.pagination.PageRequest;
import org.scoula.travel.dto.TravelDTO;
import org.scoula.travel.dto.TravelImageDTO;

public interface TravelService {
	Page<TravelDTO> getPage(PageRequest pageRequest);
	List<TravelDTO> getList();
	TravelDTO get(Long no);
	TravelImageDTO getImaage(Long no);


}
