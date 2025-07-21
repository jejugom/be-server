package org.scoula.faq.service;

import org.scoula.faq.dto.FaqDTO;

import java.util.List;

public interface FaqService {
    List<FaqDTO> getAllFaqs();
    FaqDTO getFaqById(Integer faqId);
    void addFaq(FaqDTO faqDTO);
    void updateFaq(FaqDTO faqDTO);
    void deleteFaq(Integer faqId);
}
