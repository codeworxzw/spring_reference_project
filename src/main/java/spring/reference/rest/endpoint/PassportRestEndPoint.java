package spring.reference.rest.endpoint;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import spring.reference.facade.PassportFacade;
import spring.reference.model.Passport;
import spring.reference.util.Logged;

@Logged
@Controller
@RequestMapping("/passport")
public class PassportRestEndPoint {
    @Inject
    private PassportFacade passportFacade;

    @RequestMapping(value = "/{passportId}", method = RequestMethod.PUT)
    public @ResponseBody
    Passport updatePassport(@PathVariable("passportId") Long passportId, @RequestBody Passport passport) {
        return passportFacade.updatePassport(passportId, passport);
    }
}
