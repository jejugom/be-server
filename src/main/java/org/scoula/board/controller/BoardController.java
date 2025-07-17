package org.scoula.board.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.scoula.board.domain.BoardAttachmentVO;
import org.scoula.board.dto.BoardDTO;
import org.scoula.board.service.BoardService;
import org.scoula.common.pagination.Page;
import org.scoula.common.pagination.PageRequest;
import org.scoula.common.util.UploadFiles;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Log4j2
@Api(tags = "게시글 관리")
public class BoardController {
	private final BoardService service;

	@ApiOperation(value = "게시글 목록",notes = "게시글 목록을 얻는 API")
	@ApiResponses(
		value = {
			@ApiResponse(code = 200 , message = "성공적으로 요청이 처리되었습니다",response = BoardDTO.class),
			@ApiResponse(code = 400, message = "잘못된 요청입니다."),
			@ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
		})
	@GetMapping("")//Pagination으로 변경
	public ResponseEntity<Page> getList(PageRequest pageRequest){
		return ResponseEntity.ok(service.getPage(pageRequest));
 	}
	// @GetMapping("")
	// public ResponseEntity<List<BoardDTO>> getList(){
	// 	return ResponseEntity.ok(service.getList());
	// }


	@ApiOperation(value = "상세정보 얻기",notes = "게시글 상세 정보를 얻는 API")
	@ApiResponses(
		value = {
			@ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.",response = BoardDTO.class),
			@ApiResponse(code = 400, message = "잘못된 요청입니다."),
			@ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
		})
	@GetMapping("/{no}")
	public ResponseEntity<BoardDTO> getById(
		@ApiParam(value = "게시글 ID",required = true,example = "1")
		@PathVariable Long no){
		return ResponseEntity.ok(service.get(no));
	}
	@ApiOperation(value = "게시글 생성",notes = "게시글 생성 APi")
	@ApiResponses(
		value = {
		@ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습.",response = BoardDTO.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다."),
		@ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다")
		})
	@PostMapping("")
	public ResponseEntity<BoardDTO> create(
		@ApiParam(value = "게시글 객체",required = true) BoardDTO board){
		return ResponseEntity.ok(service.create(board));
	}

	@ApiOperation(value = "게시글 수정", notes = "게시글 수정 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다.", response = BoardDTO.class),
		@ApiResponse(code = 400, message = "잘못된 요청입니다."),
		@ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
	})
	@PutMapping("/{no}")
	public ResponseEntity<BoardDTO> update(
		@ApiParam(value = "게시글 ID", required = true, example = "1")
		@PathVariable Long no,
		@ApiParam(value = "게시글 객체", required = true)
		BoardDTO board) {
		return ResponseEntity.ok(service.update(board));
	}
	@ApiOperation(value = "게시글 삭제", notes = "게시글 삭제 API")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "성공적으로 요청이 처리되었습니다."),
		@ApiResponse(code = 400, message = "잘못된 요청입니다."),
		@ApiResponse(code = 500, message = "서버에서 오류가 발생했습니다.")
	})
	@DeleteMapping("/{no}")
	public ResponseEntity<BoardDTO> delete(
		@ApiParam(value = "게시글 ID", required = true, example = "1")
		@PathVariable
		Long no) {
		return ResponseEntity.ok(service.delete(no));
	}
	@GetMapping("/download/{no}")
	public void download(@PathVariable Long no, HttpServletResponse response) throws Exception{
		BoardAttachmentVO attachment = service.getAttachment(no);
		File file = new File(attachment.getPath());
		UploadFiles.download(response,file,attachment.getFilename());
	}
	@DeleteMapping("/deleteAttachment/{no}")
	public ResponseEntity<Boolean> deleteAttachment(@PathVariable Long no) throws Exception{
		return ResponseEntity.ok(service.deleteAttachment(no));
	}
}
