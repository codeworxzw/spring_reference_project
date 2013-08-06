package spring.reference.facade;

import javax.inject.Inject;
import javax.inject.Named;

import spring.reference.model.Passport;
import spring.reference.service.PassportService;

@Named
public class PassportFacade {
    @Inject
    private PassportService passportService;

    public Passport updatePassport(Long passportId, Passport passport) {
        return passportService.updatePassport(passportId, passport);
    }
}
