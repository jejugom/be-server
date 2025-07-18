package org.scoula.bank.controller;

import lombok.RequiredArgsConstructor;
import org.scoula.bank.dto.BankDTO;
import org.scoula.bank.service.BankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/banks")
public class BankController {

    private final BankService bankService;

    @GetMapping
    public ResponseEntity<List<BankDTO>> getAllBanks() {
        return ResponseEntity.ok(bankService.getAllBanks());
    }

    @GetMapping("/{code}")
    public ResponseEntity<BankDTO> getBankByCode(@PathVariable String code) {
        return ResponseEntity.ok(bankService.getBankByCode(code));
    }

    @PostMapping
    public ResponseEntity<Void> addBank(@RequestBody BankDTO bankDTO) {
        bankService.addBank(bankDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{code}")
    public ResponseEntity<Void> updateBank(@PathVariable String code, @RequestBody BankDTO bankDTO) {
        bankDTO.setCode(code);
        bankService.updateBank(bankDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteBank(@PathVariable String code) {
        bankService.deleteBank(code);
        return ResponseEntity.ok().build();
    }
}
