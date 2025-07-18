package org.scoula.faq.service;

import org.scoula.faq.dto.FaqDTO;

import java.util.List;

public interface FaqService {
    List<FaqDTO> getAllFaqs();
    FaqDTO getFaqById(Integer id);
    void addFaq(FaqDTO faqDTO);
    void updateFaq(FaqDTO faqDTO);
    void deleteFaq(Integer id);
}
