package org.scoula.faq.mapper;

import org.scoula.faq.domain.FaqVO;

import java.util.List;

public interface FaqMapper {
    List<FaqVO> getAllFaqs();
    FaqVO getFaqById(Integer id);
    void insertFaq(FaqVO faq);
    int updateFaq(FaqVO faq);
    int deleteFaq(Integer id);
}
