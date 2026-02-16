package co.com.fe.api.models.kafka.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDataEvent {
    private Long invoiceGenDataId;
    private Long accountId;
    private Long subAccountId;
    private String documentNumber;
    private String partnerDocument;
    private String documentType;
    private String checkDigit;
    private String personType;
    private String fiscalTypeOperation;
    private String taxObligation;
    private String fiscalResponsibility;
    private String firstName;
    private String secondName;
    private String firstLastName;
    private String secondLastName;
    private String email;
    private String companyName;
    private String companyBrand;
    private String city;
    private String department;
    private String address;
    private String extraData;
    private String postalCode;
    private String otherEmailNotification;
    private Boolean taxPayer;
    private Boolean selfRetainer;
    private Boolean retainerIva;
    private Boolean fiscalTypeOperationIsSimple;
    private Boolean mandateInvoice;
    private String administrativeArea;
}
