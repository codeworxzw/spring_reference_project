package jee.reference.facade;

import javax.inject.Inject;
import javax.inject.Named;

import jee.reference.service.PassportService;
import spring.reference.model.Passport;

@Named
public class PassportFacade {
    @Inject
    private PassportService passportService;

    public Passport updatePassport(Long passportId, Passport passport) {
        return passportService.updatePassport(passportId, passport);
    }
}
