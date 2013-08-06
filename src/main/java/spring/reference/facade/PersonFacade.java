package spring.reference.facade;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import spring.reference.exception.RecordNotFoundException;
import spring.reference.exception.RestClientCallException;
import spring.reference.model.Person;
import spring.reference.model.dto.PersonDto;
import spring.reference.service.RetryOnOptimisticLockException;
import spring.reference.service.api.PersonService;
import spring.reference.util.Logged;

@Logged
@Named
public class PersonFacade {
    @Inject
    private PersonService personService;

    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    public Person getPerson(Long personId) throws RecordNotFoundException {
        return personService.getPerson(personId);
    }

    public Long createPerson(Person person) {
        return personService.createPerson(person);
    }

    @RetryOnOptimisticLockException
    public Person updatePerson(Person person) {
        return personService.updatePerson(person);
    }

    public List<PersonDto> getAllPersonData() {
        return personService.getAllPersonData();
    }

    public PersonDto getPersonData(Long personId) {
        return personService.getPersonData(personId);
    }

    public PersonDto getPersonThroughRestClient(Long personId) throws RestClientCallException {
        return personService.getPersonThroughRestClient(personId);
    }
}
