package org.scoula.common.pagination;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Page<T> { //게시판, 유저, Travel 등등 다 받기 위해 제너릭 처리
	private int totalCount; // 전체 데이터 건수
	private int totalPage; //전체 페이지 수

	@JsonIgnore
	private PageRequest pageRequest;//JSON 직렬화에서 뺴라.
	private List<T> list; //데이터 목록

	public static <T>Page of(PageRequest pageRequest, int totalCount, List<T> list){
		//전체 페이지 수 계산
		int totalPage = (int)Math.ceil((double)totalCount/pageRequest.getAmount());

		return new Page(totalCount, totalPage, pageRequest,list);
	}
	//Getter, Propertiy : PageNUm
	public int getPageNum(){return pageRequest.getPage();}

	//Getter, Property: Amount
	public int getAmount(){return pageRequest.getAmount();}

}
