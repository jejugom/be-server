package org.scoula.faq.controller;

import lombok.RequiredArgsConstructor;
import org.scoula.faq.dto.FaqDTO;
import org.scoula.faq.service.FaqService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faqs")
public class FaqController {

    private final FaqService faqService;

    @GetMapping
    public ResponseEntity<List<FaqDTO>> getAllFaqs() {
        return ResponseEntity.ok(faqService.getAllFaqs());
    }

    @GetMapping("/{faqId}")
    public ResponseEntity<FaqDTO> getFaqById(@PathVariable Integer faqId) {
        return ResponseEntity.ok(faqService.getFaqById(faqId));
    }

    @PostMapping
    public ResponseEntity<Void> addFaq(@RequestBody FaqDTO faqDTO) {
        faqService.addFaq(faqDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{faqId}")
    public ResponseEntity<Void> updateFaq(@PathVariable Integer faqId, @RequestBody FaqDTO faqDTO) {
        faqDTO.setFaqId(faqId);
        faqService.updateFaq(faqDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{faqId}")
    public ResponseEntity<Void> deleteFaq(@PathVariable Integer faqId) {
        faqService.deleteFaq(faqId);
        return ResponseEntity.ok().build();
    }
}
