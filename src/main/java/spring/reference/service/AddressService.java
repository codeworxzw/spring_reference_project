package spring.reference.service;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import spring.reference.model.Address;
import spring.reference.model.Address_;
import spring.reference.model.Passport;
import spring.reference.model.Passport_;
import spring.reference.model.Person;
import spring.reference.model.Person_;
import spring.reference.util.Logged;

@Logged
@Named
public class AddressService {
    @PersistenceContext(unitName = "admin")
    private EntityManager entityManager;

    public List<Address> getOwnersAddresses(String passportIssuingCountry) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Address> addressQuery = criteriaBuilder.createQuery(Address.class);
        addressQuery.distinct(true);

        Root<Address> addressRoot = addressQuery.from(Address.class);

        Join<Person, Passport> join = addressRoot.join(Address_.residents).join(Person_.passport);

        addressQuery.select(addressRoot).where(criteriaBuilder.equal(join.get(Passport_.issuingCountry), passportIssuingCountry));
        addressRoot.fetch(Address_.residents);

        List<Address> addresses = entityManager.createQuery(addressQuery).getResultList();

        return addresses;
    }
}
