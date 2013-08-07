package spring.reference.rest.endpoint;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import spring.reference.exception.RecordNotFoundException;
import spring.reference.model.Person;
import spring.reference.service.repository.PersonRepository;
import spring.reference.util.Logged;

@Logged
@Controller
@RequestMapping("/personRepo")
public class PersonRestEndPointToJpaRepostory {
    @Inject
    private PersonRepository personRepository;

    // @RequestMapping(method = RequestMethod.GET)
    // public @ResponseBody
    // List<Person> getAllPerson() {
    // return personRepository.findByDeleted(0L);
    // }

    @RequestMapping(value = "/{personId}", method = RequestMethod.GET)
    public @ResponseBody
    Person getPerson(@PathVariable("personId") Long personId) throws RecordNotFoundException {
        return personRepository.findOne(personId);
    }

    // @RequestMapping(method = RequestMethod.POST)
    // public @ResponseBody
    // Long createPerson(@RequestBody Person person) {
    // return personRepository.save(person).getId();
    // }
    //
    // @RequestMapping(value = "/{personId}", method = RequestMethod.PUT)
    // public @ResponseBody
    // Person updatePerson(@PathVariable("personId") Long personId, @RequestBody Person person) {
    // return personRepository.save(person);
    // }

    // @RequestMapping(value = "/data", method = RequestMethod.GET)
    // public @ResponseBody
    // List<PersonDto> getAllPersonData() throws RecordNotFoundException {
    // return personRepository.getAllPersonData(0L);
    // }
    //
    // @RequestMapping(value = "/data/{personId}", method = RequestMethod.GET)
    // public @ResponseBody
    // PersonDto getPersonData(@PathVariable("personId") Long personId) throws RecordNotFoundException {
    // return personRepository.getPersonData(personId);
    // }

    // @QND
    // @RequestMapping(value = "/qnd/{firstName}", method = RequestMethod.GET)
    // public Long qndCreatePerson(@PathVariable("firstName") String firstName) {
    // Person person = new Person();
    // person.setFirstName(firstName);
    //
    // return personRepository.save(person).getId();
    // }
    //
    // @QND
    // @RequestMapping(value = "/qnd/{personId}/{firstName}", method = RequestMethod.GET)
    // public Person qndUpdatePerson(@PathVariable("personId") Long personId, @PathVariable("firstName") String firstName) {
    // Person person = new Person();
    // person.setId(personId);
    // person.setFirstName(firstName);
    //
    // return personRepository.save(person);
    // }
}
