package org.scoula.bank.service;

import org.scoula.bank.dto.BankDTO;

import java.util.List;

public interface BankService {
    List<BankDTO> getAllBanks();
    BankDTO getBankByCode(String code);
    void addBank(BankDTO bankDTO);
    void updateBank(BankDTO bankDTO);
    void deleteBank(String code);
}
