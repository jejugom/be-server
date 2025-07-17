package org.scoula.travel.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@Builder@NoArgsConstructor@AllArgsConstructor
public class TravelVO {
	private Long no;
	private String district;
	private String title;
	private String description;
	private String address;
	private String phone;
	private List<TravelImageVO> images;
}
