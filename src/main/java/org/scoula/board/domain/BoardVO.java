package org.scoula.board.domain;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardVO {

	/**
	 * join 처리 -> resultMap 구성
	 * setAttaches() 호출로 처리
	 */
	private List<BoardAttachmentVO> attaches;


	private Long no;
	private String title;
	private String content;
	private String writer;
	private Date regDate;
	private Date updateDate;

}
