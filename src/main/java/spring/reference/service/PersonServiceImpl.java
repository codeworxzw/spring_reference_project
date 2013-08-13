package spring.reference.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import spring.reference.exception.RecordNotFoundException;
import spring.reference.exception.RestClientCallException;
import spring.reference.meta.TODO;
import spring.reference.meta.TODOTag;
import spring.reference.model.Person;
import spring.reference.model.Person_;
import spring.reference.model.dto.PersonDto;
import spring.reference.service.api.PersonService;
import spring.reference.service.decision.PersonDecisionBean;
import spring.reference.service.messaging.MessagingService;
import spring.reference.util.Logged;

@TODO(tags = { TODOTag.JAXRS_2_0 }, value = "Use JAX-RS Client API instead of (deprecated) resteasy specific components.")
@Logged
@Named
@Transactional
public class PersonServiceImpl implements PersonService {
    @PersistenceContext(unitName = "admin")
    private EntityManager entityManager;
    @Inject
    private PersonDecisionBean personDecisionBean;
    @Inject
    private MessagingService messagingService;

    @Value("${rest.recursive.host}")
    private String host;
    @Value("${rest.recursive.port}")
    private Long port;
    @Value("${rest.recursive.path}")
    private String path;

    @Override
    public List<Person> getAllPerson() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = criteriaBuilder.createQuery(Person.class);
        query.distinct(true);
        Root<Person> root = getJoinedPersonRoot(query);

        Predicate predicate = criteriaBuilder.equal(root.get(Person_.deleted), 0);
        query.select(root).where(predicate);

        List<Person> personList = entityManager.createQuery(query).setHint("org.hibernate.cacheable", true).getResultList();

        return personList;
    }

    @Override
    public Person getPerson(Long personId) throws RecordNotFoundException {
        Person person = entityManager.find(Person.class, personId);

        if (personDecisionBean.personDoesntExist(person)) {
            throw new RecordNotFoundException("Couldn't find " + Person.class.getSimpleName() + " with " + Person_.id.getName() + "=" + personId + "!");
        }

        return person;
    }

    @Override
    public Long createPerson(Person person) {
        entityManager.persist(person);
        messagingService.sendTextMessage("Person created (message sent as part of a transaction):" + person);

        return person.getId();
    }

    @Override
    public Person updatePerson(Person person) {
        Person updatedPerson = entityManager.merge(person);

        return updatedPerson;
    }

    @Override
    public List<PersonDto> getAllPersonData() {
        Query query = entityManager.createNamedQuery(Person.NQ_GET_ALL_PERSON_DATA);
        query.setParameter("deleted", 0L);

        @SuppressWarnings("unchecked")
        List<PersonDto> allPersonData = query.getResultList();

        return allPersonData;
    }

    @Override
    public PersonDto getPersonData(Long personId) {
        Query query = entityManager.createNamedQuery(Person.NQ_GET_PERSON_DATA);
        query.setParameter("personId", personId);
        PersonDto personData = (PersonDto) query.getSingleResult();

        return personData;
    }

    @Override
    public PersonDto getPersonThroughRestClient(Long personId) throws RestClientCallException {
        PersonDto personData;

        String url = buildUrl(host, port, path, personId);

        ClientRequest request = new ClientRequest(url);
        request.accept("application/json");

        try {
            ClientResponse<PersonDto> response = request.get(PersonDto.class);
            personData = response.getEntity();
            // JAX-RS 2.0 Client API
            // personData = ClientBuilder.newClient().target(url).request().get().readEntity(PersonDto.class);
        } catch (Exception e) {
            throw new RestClientCallException("Error occurred while calling '" + url + "' for a result of " + PersonDto.class.getSimpleName() + "!", e);
        }

        return personData;
    }

    @TODO(
            tags = { TODOTag.MAY_CHANGE_IN_THE_FUTURE, TODOTag.JPA_2_1 },
            value = "Might be able to use an EntityGraph from JPA 2.1 instead of the many fetches? Not sure that even if it is possible, this level of control shouldn't stay at this level.")
    private Root<Person> getJoinedPersonRoot(CriteriaQuery<Person> query) {
        Root<Person> root = query.from(Person.class);
        root.fetch(Person_.passport, JoinType.LEFT);
        root.fetch(Person_.drivingLicence, JoinType.LEFT);
        root.fetch(Person_.cars, JoinType.LEFT);
        root.fetch(Person_.addresses, JoinType.LEFT);
        root.fetch(Person_.creditCards, JoinType.LEFT);

        return root;
    }

    private String buildUrl(String host, Long port, String path, Long personId) {
        return "http://" + host + ":" + port + path + personId;
    }
}
