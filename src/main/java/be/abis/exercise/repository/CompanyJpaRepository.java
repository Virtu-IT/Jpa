package be.abis.exercise.repository;

import be.abis.exercise.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyJpaRepository extends JpaRepository<Company, Integer> {

    List<Company> findByNameIgnoreCase(String name);

    @Query("select c from Company c where upper(c.name) = upper(:name) and upper(c.address.town) = upper(:town)")
    Company findByNameAndTown(@Param("name") String name, @Param("town") String town);

}
