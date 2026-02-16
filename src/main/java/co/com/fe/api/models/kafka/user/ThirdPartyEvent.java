package co.com.fe.api.models.kafka.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyEvent {
    private String flykey;
    private Long mandateId;
    private String firstName;
    private String secondName;
    private String firstSurname;
    private String secondSurname;
    private String email;
    private String cityName;
    private String cityCode;
    private String departmentName;
    private String departmentCode;
    private String postalCode;
    private String address;
    private String document;
    private Integer documentType;
    private String telephone;
    private String cellphone;
    private Integer personType;
    private Integer taxRegime;
    private String entryDate;
    private String validFromDate;
    private String validToDate;
    private Boolean isMandate;
    private Boolean status;
}
