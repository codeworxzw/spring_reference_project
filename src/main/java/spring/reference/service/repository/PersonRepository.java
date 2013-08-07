package spring.reference.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.reference.model.Person;

//@Logged
public interface PersonRepository extends JpaRepository<Person, Long> {
    // Person findById(Long id);

    // List<Person> findByDeleted(Long deleted);

    // List<PersonDto> getAllPersonData(Long deleted);

    // PersonDto getPersonData(Long personId);
}
