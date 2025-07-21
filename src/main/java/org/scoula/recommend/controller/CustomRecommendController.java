package org.scoula.recommend.controller;

import lombok.RequiredArgsConstructor;
import org.scoula.recommend.dto.CustomRecommendDTO;
import org.scoula.recommend.service.CustomRecommendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/custom-recommends")
public class CustomRecommendController {

    private final CustomRecommendService customRecommendService;

    @GetMapping("/user/{email}")
    public ResponseEntity<List<CustomRecommendDTO>> getCustomRecommendsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(customRecommendService.getCustomRecommendsByEmail(email));
    }

    @PostMapping
    public ResponseEntity<Void> addCustomRecommend(@RequestBody CustomRecommendDTO customRecommendDTO) {
        customRecommendService.addCustomRecommend(customRecommendDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{email}/{prdtId}")
    public ResponseEntity<Void> updateCustomRecommend(@PathVariable String email, @PathVariable String prdtId, @RequestBody CustomRecommendDTO customRecommendDTO) {
        customRecommendDTO.setEmail(email);
        customRecommendDTO.setPrdtId(prdtId);
        customRecommendService.updateCustomRecommend(customRecommendDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{email}/{prdtId}")
    public ResponseEntity<Void> deleteCustomRecommend(@PathVariable String email, @PathVariable String prdtId) {
        customRecommendService.deleteCustomRecommend(email, prdtId);
        return ResponseEntity.ok().build();
    }
}
