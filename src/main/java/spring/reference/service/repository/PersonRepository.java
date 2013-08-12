package spring.reference.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import spring.reference.model.Person;
import spring.reference.model.dto.PersonDto;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByDeleted(Long deleted);

    List<PersonDto> getAllPersonData(Long deleted);

    PersonDto getPersonData(@Param("deleted") Long personId);
}
