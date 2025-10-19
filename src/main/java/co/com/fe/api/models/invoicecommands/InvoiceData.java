package co.com.fe.api.models.invoicecommands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceData {
    private String personType; // "NATURAL || JURIDICA ",
    private String companyName; //Si es juridica",
    private String fullName;
    private String firstName;
    private String secondName;
    private String firstLastName;
    private String secondLastName;

    private String country;
    private String city;
    private String address;

    private String documentType;
    private String documentNumber;
    private String checkDigit;

    private String telephone;
    private String email;

    private String fiscalTypeOperation; // "RESPONSIBLE, NO_RESPONSIBLE"
    private String taxObligation; // "LARGE_CONTRIBUTOR, SELF_RETAINER, RETAINER_IVA, OTHER"

    private boolean taxPayer;
    private boolean selfRetainer;
    private boolean retainerIva;
    private boolean fiscalTypeOperationIsSimple;
    private boolean mandateInvoice;

    private String administrativeArea; // "123"
}
