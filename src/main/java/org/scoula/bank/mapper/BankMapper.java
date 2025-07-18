package org.scoula.bank.mapper;

import org.scoula.bank.domain.BankVO;

import java.util.List;

public interface BankMapper {
    List<BankVO> getAllBanks();
    BankVO getBankByCode(String code);
    void insertBank(BankVO bank);
    int updateBank(BankVO bank);
    int deleteBank(String code);
}
