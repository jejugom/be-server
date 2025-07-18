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

    @GetMapping("/{id}")
    public ResponseEntity<FaqDTO> getFaqById(@PathVariable Integer id) {
        return ResponseEntity.ok(faqService.getFaqById(id));
    }

    @PostMapping
    public ResponseEntity<Void> addFaq(@RequestBody FaqDTO faqDTO) {
        faqService.addFaq(faqDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateFaq(@PathVariable Integer id, @RequestBody FaqDTO faqDTO) {
        faqDTO.setId(id);
        faqService.updateFaq(faqDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaq(@PathVariable Integer id) {
        faqService.deleteFaq(id);
        return ResponseEntity.ok().build();
    }
}
