package be.abis.exercise;

import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.exception.PersonCanNotBeDeletedException;
import be.abis.exercise.exception.PersonNotFoundException;
import be.abis.exercise.model.Address;
import be.abis.exercise.model.Company;
import be.abis.exercise.model.Person;
import be.abis.exercise.repository.CompanyJpaRepository;
import be.abis.exercise.service.PersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class PersonServiceTest {

	@Autowired
    PersonService personService;

	@Autowired
	CompanyJpaRepository companyRepository;

	private static final String NEW_MAIL = "sschillebeeckx@abis.be";

	// these tests really commit, so every test cleans up after itself
	@AfterEach
	public void cleanUp() throws PersonNotFoundException {
		try {
			Person p = personService.findPerson(NEW_MAIL, "abis123");
			personService.deletePerson(p.getPersonId());
		} catch (PersonNotFoundException | PersonCanNotBeDeletedException e) {
			// nothing to clean
		}
		try {
			Person p = personService.findPerson(NEW_MAIL, "blabla");
			personService.deletePerson(p.getPersonId());
		} catch (PersonNotFoundException | PersonCanNotBeDeletedException e) {
			// nothing to clean
		}
		companyRepository.findByNameIgnoreCase("NEW TEST COMPANY").forEach(companyRepository::delete);
	}

	private Person newPerson(Company c) {
		return new Person(0, "Sandy", "Schillebeeckx", LocalDate.of(1978, 4, 10), NEW_MAIL, "abis123", "nl", c);
	}

	@Test
	public void person1ShouldBeCalledJan() throws PersonNotFoundException {
		assertEquals("JAN", personService.findPerson(1).getFirstName().trim());
	}

	@Test
	public void thereShouldBe45PersonsInTheDB(){
		assertEquals(45, personService.getAllPersons().size());
	}

	@Test
	public void findPersonByMailAndPassword() throws PersonNotFoundException {
		Person p = personService.findPerson("jan.smiths@abis.be", "js123");
		assertEquals(1, p.getPersonId());
		assertEquals("ABIS N.V.", p.getCompany().getName().trim());
	}

	@Test
	public void wrongPasswordThrowsException() {
		assertThrows(PersonNotFoundException.class, () -> personService.findPerson("jan.smiths@abis.be", "wrong"));
	}

	@Test
	public void unknownIdThrowsException() {
		assertThrows(PersonNotFoundException.class, () -> personService.findPerson(-5));
	}

	@Test
	public void findPersonsOfCompany() {
		assertEquals(3, personService.findPersonsByCompanyName("abis n.v.").size());
	}

	@Test
	public void addPersonWorkingForNewCompany() throws PersonAlreadyExistsException {
		Address a = new Address("Teststraat", "1", "3000", "Leuven", "B");
		Person p = personService.addPerson(newPerson(new Company("NEW TEST COMPANY", "016/455610", "BE12345678", a)));
		assertTrue(p.getPersonId() > 0);
		assertTrue(p.getCompany().getCompanyNr() > 0);
		assertEquals(1, companyRepository.findByNameIgnoreCase("NEW TEST COMPANY").size());
	}

	@Test
	public void addPersonWorkingForExistingCompany() throws PersonAlreadyExistsException {
		Address a = new Address("Diestsevest", "32", "3000", "Leuven", "B");
		int companiesBefore = companyRepository.findAll().size();
		Person p = personService.addPerson(newPerson(new Company("Abis N.V.", "016/455610", "BE12345678", a)));
		assertEquals(3, p.getCompany().getCompanyNr());
		assertEquals(companiesBefore, companyRepository.findAll().size());
	}

	@Test
	public void addPersonWithoutCompany() throws PersonAlreadyExistsException, PersonNotFoundException {
		Person p = personService.addPerson(newPerson(null));
		assertNull(personService.findPerson(p.getPersonId()).getCompany());
	}

	@Test
	public void addingSamePersonTwiceThrowsException() throws PersonAlreadyExistsException {
		personService.addPerson(newPerson(null));
		assertThrows(PersonAlreadyExistsException.class, () -> personService.addPerson(newPerson(null)));
	}

	@Test
	public void changePassWordOfAddedPerson() throws PersonAlreadyExistsException, PersonNotFoundException {
		Person p = personService.addPerson(newPerson(null));
		Person changed = personService.changePassword(p, "blabla");
		assertEquals("blabla", changed.getPassword());
		assertEquals(p.getPersonId(), personService.findPerson(NEW_MAIL, "blabla").getPersonId());
	}

	@Test
	public void deleteAddedPerson() throws PersonAlreadyExistsException, PersonCanNotBeDeletedException {
		Person p = personService.addPerson(newPerson(null));
		personService.deletePerson(p.getPersonId());
		assertThrows(PersonNotFoundException.class, () -> personService.findPerson(p.getPersonId()));
	}

	@Test
	public void deleteUnknownPersonThrowsException() {
		assertThrows(PersonCanNotBeDeletedException.class, () -> personService.deletePerson(-5));
	}

	@Test
	public void personWithEnrolmentsCanNotBeDeleted() {
		// person 25 is enrolled for several sessions
		assertThrows(PersonCanNotBeDeletedException.class, () -> personService.deletePerson(25));
	}

}
