package org.scoula.board.domain;

import java.util.Date;

import org.scoula.common.util.UploadFiles;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardAttachmentVO {
	private long no;
	private Long bno; // FK : Board의 No
	private String filename; //원본 파일명
	private String path; //서버에 저장된 파일 경로
	private String contentType; //파일 mime-type
	private Long size; //파이 ㄹ크기
	private Date regDate; //등록일

	//팩토리메서드
	public static BoardAttachmentVO of(MultipartFile part, Long bno, String path){
		return builder()
			.bno(bno)
			.filename(part.getOriginalFilename())
			.path(path)
			.contentType(part.getContentType())
			.size(part.getSize())
			.build();
	}
	public String getFileSize(){
		return UploadFiles.getFormatSize(size);
	}
}
