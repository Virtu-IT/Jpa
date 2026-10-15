package be.abis.exercise.repository;

import be.abis.exercise.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
 * BEFORE (no JPA): PersonRepository was implemented by FilePersonRepository, which read and
 * rewrote a CSV file (readFile(), writePersons(), parsePerson()) and looped over a List<Person>:
 *
 *   public interface PersonRepository {
 *       List<Person> getAllPersons();
 *       Person findPerson(int id) throws PersonNotFoundException;
 *       Person findPerson(String emailAddress, String passWord) throws PersonNotFoundException;
 *       void addPerson(Person p) throws PersonAlreadyExistsException;
 *       void deletePerson(int id) throws PersonCanNotBeDeletedException;
 *       void changePassword(Person p, String newPswd);
 *       List<Person> findPersonsByCompanyName(String compName);
 *   }
 *
 *   // e.g. in FilePersonRepository:
 *   public Person findPerson(int id) throws PersonNotFoundException {
 *       this.readFile();
 *       return allPersons.stream()
 *               .filter(p->p.getPersonId()==id).findFirst()
 *               .orElseThrow(()-> new PersonNotFoundException("person with id " + id + " not found"));
 *   }
 *   public List<Person> findPersonsByCompanyName(String compName) {
 *       return allPersons.stream()
 *               .filter(p->p.getCompany().getName().equalsIgnoreCase(compName))
 *               .collect(Collectors.toList());
 *   }
 *
 * NOW: no file handling at all. getAllPersons -> findAll(), findPerson(id) -> findById(),
 * addPerson/changePassword -> save(), deletePerson -> delete(); the other lookups are query methods.
 */
public interface PersonJpaRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByEmailAddressIgnoreCase(String emailAddress);

    Optional<Person> findByEmailAddressIgnoreCaseAndPassword(String emailAddress, String password);

    List<Person> findByCompanyNameIgnoreCase(String companyName);

}
