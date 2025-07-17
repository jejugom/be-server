package org.scoula.board.service;

import java.util.List;

import org.scoula.board.dto.BoardDTO;
import org.scoula.board.domain.BoardAttachmentVO;
import org.scoula.common.pagination.Page;
import org.scoula.common.pagination.PageRequest;

public interface BoardService {
	//Pagination
	Page<BoardDTO> getPage(PageRequest pageRequest);
	public List<BoardDTO> getList();
	public BoardDTO get(Long no);

	public BoardDTO create(BoardDTO board);
	public BoardDTO update(BoardDTO board);
	public BoardDTO delete(Long no);

	public BoardAttachmentVO getAttachment(Long no);
	public boolean deleteAttachment(Long no);
}
