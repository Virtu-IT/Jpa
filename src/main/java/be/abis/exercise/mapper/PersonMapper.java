package be.abis.exercise.mapper;

import be.abis.exercise.dto.PersonDTO;
import be.abis.exercise.form.PersonForm;
import be.abis.exercise.model.Address;
import be.abis.exercise.model.Company;
import be.abis.exercise.model.Person;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonMapper {

    public PersonDTO toDto(Person p) {
        PersonDTO dto = new PersonDTO();
        dto.setPersonId(p.getPersonId());
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setEmailAddress(p.getEmailAddress());
        dto.setBirthDate(p.getBirthDate());
        if (p.getCompany() != null) {
            dto.setCompanyName(p.getCompany().getName());
            if (p.getCompany().getAddress() != null) {
                dto.setCompanyTown(p.getCompany().getAddress().getTown());
            }
        }
        return dto;
    }

    public List<PersonDTO> toDtos(List<Person> persons) {
        return persons.stream().map(this::toDto).toList();
    }

    public PersonForm toForm(Person p) {
        PersonForm f = new PersonForm();
        f.setPersonId(p.getPersonId());
        f.setFirstName(p.getFirstName());
        f.setLastName(p.getLastName());
        f.setBirthDate(p.getBirthDate());
        f.setEmailAddress(p.getEmailAddress());
        f.setPassword(p.getPassword());
        f.setLanguage(p.getLanguage());
        Company c = p.getCompany();
        if (c != null) {
            f.setCompanyName(c.getName());
            f.setTelephoneNumber(c.getTelephoneNumber());
            f.setVatNr(c.getVatNr());
            Address a = c.getAddress();
            if (a != null) {
                f.setStreet(a.getStreet());
                f.setNr(a.getNr());
                f.setZipcode(a.getZipcode());
                f.setTown(a.getTown());
                f.setCountryCode(a.getCountryCode());
            }
        }
        return f;
    }

    public Person toEntity(PersonForm f) {
        Person p = new Person();
        p.setPersonId(f.getPersonId());
        p.setFirstName(f.getFirstName());
        p.setLastName(f.getLastName());
        p.setBirthDate(f.getBirthDate());
        p.setEmailAddress(f.getEmailAddress());
        p.setPassword(f.getPassword());
        p.setLanguage(f.getLanguage());
        if (f.getCompanyName() != null && !f.getCompanyName().isBlank()) {
            Address a = new Address(f.getStreet(), f.getNr(), f.getZipcode(), f.getTown(), f.getCountryCode());
            p.setCompany(new Company(f.getCompanyName(), f.getTelephoneNumber(), f.getVatNr(), a));
        }
        return p;
    }
}
