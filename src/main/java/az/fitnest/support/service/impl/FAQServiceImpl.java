package az.fitnest.support.service.impl;

import az.fitnest.support.dto.FAQDto;
import az.fitnest.support.dto.FAQRequest;
import az.fitnest.support.dto.PaginatedResponse;
import az.fitnest.support.model.entity.SupportFAQ;
import az.fitnest.support.exception.ResourceNotFoundException;
import az.fitnest.support.repository.SupportFAQRepository;
import az.fitnest.support.service.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService {

    private final SupportFAQRepository faqRepository;

    @Override
    public PaginatedResponse<FAQDto> getAllFAQs(int page, int size) {
        PageRequest pageable = PageRequest.of(Math.max(0, page - 1), size);
        Page<SupportFAQ> faqPage = faqRepository.findAll(pageable);
        
        List<FAQDto> items = faqPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        
        return PaginatedResponse.<FAQDto>builder()
                .items(items)
                .total(faqPage.getTotalElements())
                .page(page)
                .pageSize(size)
                .build();
    }

    @Override
    public FAQDto getFAQById(Long id) {
        SupportFAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ not found with id: " + id));
        return mapToDto(faq);
    }

    @Override
    @Transactional
    public FAQDto createFAQ(FAQRequest request) {
        SupportFAQ faq = new SupportFAQ();
        faq.setQuestion(request.getQuestion());
        faq.setAnswer(request.getAnswer());
        SupportFAQ saved = faqRepository.save(faq);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public FAQDto updateFAQ(Long id, FAQRequest request) {
        SupportFAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ not found with id: " + id));
        
        faq.setQuestion(request.getQuestion());
        faq.setAnswer(request.getAnswer());
        SupportFAQ saved = faqRepository.save(faq);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public void deleteFAQ(Long id) {
        if (!faqRepository.existsById(id)) {
            throw new ResourceNotFoundException("FAQ not found with id: " + id);
        }
        faqRepository.deleteById(id);
    }

    private FAQDto mapToDto(SupportFAQ faq) {
        return FAQDto.builder()
                .id(faq.getId())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .build();
    }
}
