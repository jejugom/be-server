package org.scoula.faq.service;

import lombok.RequiredArgsConstructor;
import org.scoula.faq.dto.FaqDTO;
import org.scoula.faq.mapper.FaqMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqMapper faqMapper;

    @Override
    public List<FaqDTO> getAllFaqs() {
        return faqMapper.getAllFaqs().stream()
                .map(FaqDTO::of)
                .collect(Collectors.toList());
    }

    @Override
    public FaqDTO getFaqById(Integer faqId) {
        return Optional.ofNullable(faqMapper.getFaqById(faqId))
                .map(FaqDTO::of)
                .orElseThrow(() -> new NoSuchElementException("FAQ not found with id: " + faqId));
    }

    @Override
    public void addFaq(FaqDTO faqDTO) {
        faqMapper.insertFaq(faqDTO.toVO());
    }

    @Override
    public void updateFaq(FaqDTO faqDTO) {
        if (faqMapper.updateFaq(faqDTO.toVO()) == 0) {
            throw new NoSuchElementException("FAQ not found with id: " + faqDTO.getFaqId());
        }
    }

    @Override
    public void deleteFaq(Integer faqId) {
        if (faqMapper.deleteFaq(faqId) == 0) {
            throw new NoSuchElementException("FAQ not found with id: " + faqId);
        }
    }
}
