package org.scoula.travel.controller;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import org.scoula.common.pagination.Page;
import org.scoula.common.pagination.PageRequest;
import org.scoula.common.util.UploadFiles;
import org.scoula.travel.dto.TravelDTO;
import org.scoula.travel.dto.TravelImageDTO;
import org.scoula.travel.service.TravelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/travel")
public class TravelController {
	final TravelService service;

	@GetMapping("")
	public ResponseEntity<Page> getTravels(PageRequest pageRequest){
		return ResponseEntity.ok(service.getPage(pageRequest));
	}
	@GetMapping("/{no}")
	public ResponseEntity<TravelDTO> getTravels(@PathVariable("no") Long no){
		return ResponseEntity.ok(service.get(no));
	}
	@GetMapping("/image/{no}")
	public void viewImage(@PathVariable Long no, HttpServletResponse response){
		TravelImageDTO image = service.getImaage(no);
		File file = new File(image.getPath());
		UploadFiles.downloadImage(response,file);
	}

}
