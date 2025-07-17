package org.scoula.common.pagination;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//이 생성자는 Private 접근제한으로 생성해라.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageRequest {
	private int page; //요청 페이지
	private int amount; //한 페이지 당 데이터 건수

	//default Page
	public PageRequest(){
		page =1 ;
		amount = 10;
	}
	public static PageRequest of(int page, int amount){
		return new PageRequest(page,amount);
	}

	//계산된 읽기 전용의 Property 전용, xml에서 ${Offset} 가능. (한 번만 사요)
	public int getOffset(){ //offset 값 계산.
		return (page-1) * amount;
	}

}
