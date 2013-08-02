package jee.reference.service.decision;

import javax.inject.Named;

import spring.reference.model.Person;
import spring.reference.util.Logged;

@Logged
@Named
public class PersonDecisionBean {
    public boolean personDoesntExist(Person person) {
        return person == null;
    }
}
