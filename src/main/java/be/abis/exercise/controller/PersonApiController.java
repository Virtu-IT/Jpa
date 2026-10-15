package be.abis.exercise.controller;

import be.abis.exercise.dto.PersonDTO;
import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.exception.PersonCanNotBeDeletedException;
import be.abis.exercise.exception.PersonNotFoundException;
import be.abis.exercise.form.LoginForm;
import be.abis.exercise.form.PersonForm;
import be.abis.exercise.mapper.PersonMapper;
import be.abis.exercise.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("persons")
public class PersonApiController {

    // BEFORE: two fields (personService and ps) for the same bean, and Person was used directly in the API:
    //   public List<Person> getAllPersons()       public Person findPerson(int id)
    //   public Person findPersonByMailAndPwd(...) public List<Person> findPersonsByCompanyName(...)
    //   public void addPerson(@RequestBody Person p)
    //   public void changePassword(int id, @RequestBody Person person) { ps.changePassword(person, person.getPassword()); }
    // NOW: PersonDTO goes out, PersonForm comes in, PersonMapper converts.
    @Autowired
    PersonService ps;

    @Autowired
    PersonMapper mapper;

    @GetMapping("")
    public List<PersonDTO> getAllPersons(){
        return mapper.toDtos(ps.getAllPersons());
    }

    @GetMapping("{id}")
    public PersonDTO findPerson(@PathVariable("id") int id) throws PersonNotFoundException {
       return mapper.toDto(ps.findPerson(id));
    }

    @PostMapping("/login")
    public PersonDTO findPersonByMailAndPwd(@RequestBody LoginForm login) throws PersonNotFoundException {
        return mapper.toDto(ps.findPerson(login.getEmail(), login.getPassword()));
    }

    @GetMapping(path="/compquery")
    public List<PersonDTO> findPersonsByCompanyName(@RequestParam("compname") String compName) {
        return mapper.toDtos(ps.findPersonsByCompanyName(compName));
    }

    @PostMapping(path="")
    public PersonDTO addPerson(@RequestBody PersonForm form) throws PersonAlreadyExistsException {
        return mapper.toDto(ps.addPerson(mapper.toEntity(form)));
    }

    @DeleteMapping("{id}")
    public void deletePerson(@PathVariable("id") int id) throws PersonCanNotBeDeletedException {
        ps.deletePerson(id);
    }

    @PutMapping("{id}")
    public PersonDTO changePassword(@PathVariable("id") int id, @RequestBody PersonForm form) throws PersonNotFoundException {
        form.setPersonId(id);
        return mapper.toDto(ps.changePassword(mapper.toEntity(form), form.getPassword()));
    }

}
