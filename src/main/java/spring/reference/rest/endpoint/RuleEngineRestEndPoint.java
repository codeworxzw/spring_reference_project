package spring.reference.rest.endpoint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import spring.reference.exception.InternalServerErrorException;
import spring.reference.meta.POI;
import spring.reference.meta.POITag;
import spring.reference.meta.QND;
import spring.reference.model.Address;
import spring.reference.model.Car;
import spring.reference.model.Person;
import spring.reference.service.drools.DroolsService;
import spring.reference.util.Logged;

@Logged
@Controller
@RequestMapping("/rules")
public class RuleEngineRestEndPoint {
    @Inject
    private DroolsService droolsService;

    @POI(tags = { POITag.USEFUL }, value = "Able to copy one POJO to another.")
    private ModelMapper modelMapper = new ModelMapper();

    @QND
    @RequestMapping(value = "/qnd/{firstName}/{deleted}", method = RequestMethod.GET)
    public @ResponseBody
    Set<spring.reference.external.drools.fact.Person> qndRunRules(@PathVariable("firstName") String firstName, @PathVariable("deleted") Long deleted)
            throws InternalServerErrorException {
        Person person = createPerson(firstName, deleted);

        spring.reference.external.drools.fact.Person personFact = convertToFact(person);

        Set<spring.reference.external.drools.fact.Person> personFactSet = new HashSet<spring.reference.external.drools.fact.Person>(Arrays.asList(personFact));
        List<Object> factList = Arrays.asList(personFactSet, personFact);

        droolsService.executeInStatelessKnowledgeSession(factList);

        convertFromFact(personFact);

        return personFactSet;
    }

    private Person createPerson(String firstName, Long deleted) {
        Person person = new Person();
        person.setDeleted(deleted);
        person.setFirstName(firstName);

        Set<Car> cars = new HashSet<Car>();
        person.setCars(cars);

        Set<Address> addresses = new HashSet<Address>();
        person.setAddresses(addresses);

        return person;
    }

    private spring.reference.external.drools.fact.Person convertToFact(Person person) {
        return modelMapper.map(person, spring.reference.external.drools.fact.Person.class);
    }

    private Person convertFromFact(spring.reference.external.drools.fact.Person personFact) {
        return modelMapper.map(personFact, Person.class);
    }
}
