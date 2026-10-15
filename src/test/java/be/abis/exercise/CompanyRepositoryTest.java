package be.abis.exercise;

import be.abis.exercise.model.Address;
import be.abis.exercise.model.Company;
import be.abis.exercise.repository.CompanyJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CompanyRepositoryTest {

    @Autowired
    CompanyJpaRepository companyRepository;

    @Test
    public void findCompanyById() {
        Company c = companyRepository.findById(3).orElseThrow();
        assertEquals("ABIS N.V.", c.getName().trim());
        assertEquals("LEUVEN", c.getAddress().getTown().trim());
    }

    @Test
    public void findCompanyByName() {
        assertEquals(1, companyRepository.findByNameIgnoreCase("abis n.v.").size());
    }

    @Test
    public void findCompanyByNameAndTown() {
        Company c = companyRepository.findByNameAndTown("Abis N.V.", "Leuven");
        assertNotNull(c);
        assertEquals(3, c.getCompanyNr());
    }

    @Test
    public void noCompanyWithNameInOtherTown() {
        assertNull(companyRepository.findByNameAndTown("Abis N.V.", "Gent"));
    }

    @Test
    @Transactional
    public void addCompany() {
        Company c = companyRepository.save(new Company("JPA TEST CORP", "01/234567", "BE123",
                new Address("Teststraat", "1", "3000", "Leuven", "B")));
        assertTrue(c.getCompanyNr() > 0);
        assertNotNull(companyRepository.findByNameAndTown("JPA TEST CORP", "LEUVEN"));
    }

    @Test
    @Transactional
    public void updateCompany() {
        Company c = companyRepository.findById(3).orElseThrow();
        c.setTelephoneNumber("016/000000");
        companyRepository.flush();
        assertEquals("016/000000", companyRepository.findById(3).orElseThrow().getTelephoneNumber());
    }

    @Test
    @Transactional
    public void deleteCompany() {
        Company c = companyRepository.save(new Company("JPA TEST CORP", "01/234567", "BE123",
                new Address("Teststraat", "1", "3000", "Leuven", "B")));
        companyRepository.deleteById(c.getCompanyNr());
        assertFalse(companyRepository.findById(c.getCompanyNr()).isPresent());
    }
}
