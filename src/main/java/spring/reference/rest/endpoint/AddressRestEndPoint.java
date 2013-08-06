package spring.reference.rest.endpoint;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import spring.reference.exception.RecordNotFoundException;
import spring.reference.model.Address;
import spring.reference.service.AddressService;
import spring.reference.util.Logged;

@Logged
@Controller
@RequestMapping("/address")
public class AddressRestEndPoint {
    @Inject
    private AddressService addressService;

    @RequestMapping(value = "/{passportIssuingCountry}", method = RequestMethod.GET)
    public @ResponseBody
    List<Address> getOwnersAddresses(@PathVariable("passportIssuingCountry") String passportIssuingCountry) throws RecordNotFoundException {
        return addressService.getOwnersAddresses(passportIssuingCountry);
    }

}
