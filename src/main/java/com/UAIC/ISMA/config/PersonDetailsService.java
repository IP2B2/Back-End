package com.UAIC.ISMA.config;

import com.UAIC.ISMA.dao.Person;
import com.UAIC.ISMA.config.PersonDetails;
import com.UAIC.ISMA.repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting login for user: " + username);
        try {
            Person person = personRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            System.out.println("User found: " + person.getUsername());
            return new PersonDetails(person);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("Eroare interna", e);
        }
    }

}
