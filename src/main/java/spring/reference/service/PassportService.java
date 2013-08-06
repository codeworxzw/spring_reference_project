package spring.reference.service;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import spring.reference.meta.POI;
import spring.reference.model.Passport;
import spring.reference.model.Person;
import spring.reference.util.Logged;

@Logged
@Named
public class PassportService {
    @PersistenceContext(unitName = "admin")
    private EntityManager entityManager;

    @POI("Explicit optimistic lock")
    public Passport updatePassport(Long passportId, Passport passport) {
        Passport unupdatedPassport = entityManager.find(Passport.class, passportId);

        issueOptimisiticLockOnOwner(unupdatedPassport);

        passport.setId(passportId);
        Passport updatedPassport = entityManager.merge(passport);

        return updatedPassport;
    }

    private void issueOptimisiticLockOnOwner(Passport unupdatedPassport) {
        Person owner = unupdatedPassport.getOwner();
        entityManager.lock(owner, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
    }
}
