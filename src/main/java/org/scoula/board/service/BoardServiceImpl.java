package org.scoula.board.service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.scoula.board.domain.BoardVO;
import org.scoula.board.dto.BoardDTO;
import org.scoula.board.mapper.BoardMapper;
import org.scoula.board.domain.BoardAttachmentVO;
import org.scoula.common.pagination.Page;
import org.scoula.common.pagination.PageRequest;
import org.scoula.common.util.UploadFiles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	final private BoardMapper mapper;
	private final static String BASE_DIR = "/Users/dong2/upload/board";
	@Override
	public Page<BoardDTO> getPage(PageRequest pageRequest) {
		List<BoardVO> boards = mapper.getPage(pageRequest);
		int totalCount = mapper.getTotalCount();

		return Page.of(pageRequest,totalCount,
			boards.stream().map(BoardDTO::of).toList());
	}
	@Override
	public List<BoardDTO> getList() {
		log.info("getList ...... ");
		return mapper.getList().stream() //BoardVO의 스트립
			.map(BoardDTO::of) //BoardDTO의 스트립
			.toList(); //List<BoardDTO> 변환
	}

	@Override
	public BoardDTO get(Long no) {
		log.info("get......" + no);
		BoardDTO board = BoardDTO.of(mapper.get(no));

		log.info("=".repeat(10) + board);
		return Optional.ofNullable(board)
			.orElseThrow(NoSuchElementException::new);
	}

	private void upload(Long bno, List<MultipartFile> files) {
		for (MultipartFile part : files) {
			if (part.isEmpty())
				continue;
			try {
				String uploadPath = UploadFiles.upload(BASE_DIR, part);
				BoardAttachmentVO attachmentVO = BoardAttachmentVO.of(part, bno, uploadPath);
				mapper.createAttachment(attachmentVO);
			} catch (IOException e) {
				throw new RuntimeException(e); //@Transaction에서 감지, 자동rollback;
			}
		}
	}

	@Transactional
	//2개 이상의 inssert문이 실행될 수 있으므로 트랜잭션 처리 필요
	//RuntimeException인 경우만 자동 rollback
	@Override
	public BoardDTO create(BoardDTO board) {
		log.info("Create ....." + board);

		BoardVO vo = board.toVO();
		mapper.create(vo);

		log.info(vo.getNo());

		//파일 업로드 처리
		List<MultipartFile> files = board.getFiles();
		if (files != null && !files.isEmpty()) {
			upload(vo.getNo(), files);
		}
		return get(vo.getNo());
	}

	@Override
	public BoardDTO update(BoardDTO board) {
		log.info("update ........" + board);
		BoardVO boardVO = board.toVO();
		log.info("update....." + boardVO);

		mapper.update(boardVO);
		//파일 업로드 처리
		List<MultipartFile> files = board.getFiles();
		if(files != null && !files.isEmpty()){
			upload(board.getNo(),files);
		}
		return get(board.getNo());
	}

	@Override
	public BoardDTO delete(Long no) {
		log.info("delete ...." + no);
		BoardDTO boardDTO = get(no);

		mapper.delete(no);
		return boardDTO;
	}

	//첨부파일 한 개 얻기
	@Override
	public BoardAttachmentVO getAttachment(Long no) {

		return mapper.getAttachment(no);
	}

	//첨부파일 삭제
	@Override
	public boolean deleteAttachment(Long no) {
		return mapper.deleteAttachment(no) == 1;
	}
}
