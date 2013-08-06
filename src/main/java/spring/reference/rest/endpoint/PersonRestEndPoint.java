package spring.reference.rest.endpoint;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import spring.reference.exception.RecordNotFoundException;
import spring.reference.exception.RestClientCallException;
import spring.reference.facade.PersonFacade;
import spring.reference.meta.QND;
import spring.reference.model.Person;
import spring.reference.model.dto.PersonDto;
import spring.reference.util.Logged;

@Logged
@Controller
@RequestMapping("/person")
public class PersonRestEndPoint {
    @Inject
    private PersonFacade personFacade;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<Person> getAllPerson() {
        return personFacade.getAllPersons();
    }

    @RequestMapping(value = "/{personId}", method = RequestMethod.GET)
    public @ResponseBody
    Person getPerson(@PathVariable("personId") Long personId) throws RecordNotFoundException {
        return personFacade.getPerson(personId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    Long createPerson(@RequestBody Person person) {
        return personFacade.createPerson(person);
    }

    @RequestMapping(value = "/{personId}", method = RequestMethod.PUT)
    public @ResponseBody
    Person updatePerson(@PathVariable("personId") Long personId, @RequestBody Person person) {
        return personFacade.updatePerson(personId, person);
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public @ResponseBody
    List<PersonDto> getAllPersonData() throws RecordNotFoundException {
        return personFacade.getAllPersonData();
    }

    @RequestMapping(value = "/data/{personId}", method = RequestMethod.GET)
    public @ResponseBody
    PersonDto getPersonData(@PathVariable("personId") Long personId) throws RecordNotFoundException {
        return personFacade.getPersonData(personId);
    }

    @RequestMapping(value = "/restclient/{personId}", method = RequestMethod.GET)
    public @ResponseBody
    PersonDto getPersonThroughRestClient(@PathVariable("personId") Long personId) throws RecordNotFoundException, RestClientCallException {
        return personFacade.getPersonThroughRestClient(personId);
    }

    @QND
    @RequestMapping(value = "/qnd/{firstName}", method = RequestMethod.GET)
    public Long qndCreatePerson(@PathVariable("firstName") String firstName) {
        Person person = new Person();
        person.setFirstName(firstName);

        return personFacade.createPerson(person);
    }

    @QND
    @RequestMapping(value = "/qnd/{personId}/{firstName}", method = RequestMethod.GET)
    public Person qndUpdatePerson(@PathVariable("personId") Long personId, @PathVariable("firstName") String firstName) {
        Person person = new Person();
        person.setFirstName(firstName);

        return personFacade.updatePerson(personId, person);
    }
}
