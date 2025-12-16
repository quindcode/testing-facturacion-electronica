package co.com.fe.api.models.commandevents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceRecipient {
    private String personType;
    private String companyName;
    private String fullName;
    private String firstName;
    private String secondName;
    private String firstLastName;
    private String secondLastName;
    private String countryName;
    private String countryCode;
    private String countrySubentity;
    private String city;
    private String address;
    private String documentType;
    private String documentNumber;
    private String checkDigit;
    private String telephone;
    private String email;
    private String fiscalTypeOperation;
    private String taxObligation;
    private Boolean taxPayer;
    private Boolean selfRetainer;
    private Boolean retainerIva;
    private Boolean fiscalTypeOperationIsSimple;
    private Boolean mandateInvoice;
    private String administrativeArea;
}