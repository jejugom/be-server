package org.scoula.bank.service;

import lombok.RequiredArgsConstructor;
import org.scoula.bank.dto.BankDTO;
import org.scoula.bank.mapper.BankMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankMapper bankMapper;

    @Override
    public List<BankDTO> getAllBanks() {
        return bankMapper.getAllBanks().stream()
                .map(BankDTO::of)
                .collect(Collectors.toList());
    }

    @Override
    public BankDTO getBankByCode(String code) {
        return Optional.ofNullable(bankMapper.getBankByCode(code))
                .map(BankDTO::of)
                .orElseThrow(() -> new NoSuchElementException("Bank not found with code: " + code));
    }

    @Override
    public void addBank(BankDTO bankDTO) {
        bankMapper.insertBank(bankDTO.toVO());
    }

    @Override
    public void updateBank(BankDTO bankDTO) {
        if (bankMapper.updateBank(bankDTO.toVO()) == 0) {
            throw new NoSuchElementException("Bank not found with code: " + bankDTO.getCode());
        }
    }

    @Override
    public void deleteBank(String code) {
        if (bankMapper.deleteBank(code) == 0) {
            throw new NoSuchElementException("Bank not found with code: " + code);
        }
    }
}
