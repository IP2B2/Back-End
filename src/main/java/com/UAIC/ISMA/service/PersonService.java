package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.Person;
import com.UAIC.ISMA.dto.PersonDTO;
import com.UAIC.ISMA.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonDTO> getAllPersons() {
        return personRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PersonDTO> getPersonById(Long id) {
        return personRepository.findById(id).map(this::convertToDTO);
    }

    public PersonDTO savePerson(PersonDTO personDTO) {
        Person person = convertToEntity(personDTO);
        Person savedPerson = personRepository.save(person);
        return convertToDTO(savedPerson);
    }

    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

    private PersonDTO convertToDTO(Person person) {
        return new PersonDTO(person.getId(), person.getUsername(), person.getFirstName(),
                person.getLastName(), person.getRole());
    }

    private Person convertToEntity(PersonDTO personDTO) {
        Person person = new Person();
        person.setId(personDTO.getId());
        person.setUsername(personDTO.getUsername());
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setRole(personDTO.getRole());
        return person;
    }
}
