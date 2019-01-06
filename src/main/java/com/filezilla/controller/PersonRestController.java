package com.filezilla.controller;

import com.filezilla.domain.Person;
import com.filezilla.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonRestController {

    private final PersonService personService;

    @Autowired
    public PersonRestController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{id}")
    public Person findById(@PathVariable Long id) {
        Person person = personService.findById(id);
        if (person != null) {
            return personService.findById(id);
        } else {
            throw new IllegalArgumentException("Sorry. person not found: id = " + id);
        }
    }

    @PostMapping("/save")
    public Person save(@RequestBody Person person) {
        return personService.saveOrUpdate(person);
    }

    @GetMapping("/findAll")
    public List<Person> findAll() {
        return personService.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public List<Person> delete(@PathVariable Long id) {
        Person person = personService.findById(id);
        if (person != null) {
            personService.delete(id);
        } else {
            throw new IllegalArgumentException("Sorry. Person not found: id=  " + id);
        }
        return personService.findAll();
    }
}
