package org.scoula.board.mapper;

import java.util.List;

import org.scoula.board.domain.BoardVO;
import org.scoula.board.domain.BoardAttachmentVO;
import org.scoula.common.pagination.PageRequest;

public interface  BoardMapper {

	int getTotalCount();

	List<BoardVO> getPage(PageRequest pageRequest);

	public List<BoardVO> getList();
	public BoardVO get(Long no);
	public void create(BoardVO board);
	public int update(BoardVO board);
	public int delete(long no);

	public void createAttachment(BoardAttachmentVO attach);
	public List<BoardAttachmentVO> getAttachmentList(Long bno);
	public BoardAttachmentVO getAttachment(Long no);
	public int deleteAttachment(Long no);
}
