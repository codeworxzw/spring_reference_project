package spring.reference.model.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import spring.reference.meta.POI;
import spring.reference.meta.POITag;
import spring.reference.util.DateSerializer;

public class PersonDto {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String drivingLicenceId;
    private String passportId;

    @POI(tags = { POITag.STRANGE_BEHAVIOUR }, value = "Default constructor also needed for when used in a NamedQuery")
    public PersonDto() {
    }

    public PersonDto(String firstName, String lastName, Date dateOfBirth, String drivingLicenceId, String passportId) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.drivingLicenceId = drivingLicenceId;
        this.passportId = passportId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonSerialize(using = DateSerializer.class)
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDrivingLicenceId() {
        return drivingLicenceId;
    }

    public void setDrivingLicenceId(String drivingLicenceId) {
        this.drivingLicenceId = drivingLicenceId;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

}
