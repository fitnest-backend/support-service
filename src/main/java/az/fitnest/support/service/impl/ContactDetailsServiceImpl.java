package az.fitnest.support.service.impl;

import az.fitnest.support.dto.ContactDetailsDto;
import az.fitnest.support.dto.ContactDetailsUpdateRequest;
import az.fitnest.support.exception.ResourceNotFoundException;
import az.fitnest.support.model.entity.ContactDetails;
import az.fitnest.support.repository.ContactDetailsRepository;
import az.fitnest.support.service.ContactDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactDetailsServiceImpl implements ContactDetailsService {

    private final ContactDetailsRepository contactDetailsRepository;

    @Override
    public ContactDetailsDto getContactDetails() {
        ContactDetails entity = contactDetailsRepository.findSingleton()
                .orElseThrow(() -> new ResourceNotFoundException("Contact details not found"));
        return toDto(entity);
    }

    @Transactional
    @Override
    public ContactDetailsDto updateContactDetails(ContactDetailsUpdateRequest request) {
        ContactDetails entity = contactDetailsRepository.findSingleton()
                .orElseThrow(() -> new ResourceNotFoundException("Contact details not found"));

        entity.setEmail(request.email());
        entity.setMobileNumber(request.mobileNumber());
        contactDetailsRepository.save(entity);

        return toDto(entity);
    }

    private ContactDetailsDto toDto(ContactDetails entity) {
        return new ContactDetailsDto(entity.getEmail(), entity.getMobileNumber());
    }
}

