package az.fitnest.support.service;

import az.fitnest.support.dto.ContactDetailsDto;
import az.fitnest.support.dto.ContactDetailsUpdateRequest;

public interface ContactDetailsService {

    ContactDetailsDto getContactDetails();

    ContactDetailsDto updateContactDetails(ContactDetailsUpdateRequest request);
}
