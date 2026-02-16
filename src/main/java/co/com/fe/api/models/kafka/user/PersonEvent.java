package co.com.fe.api.models.kafka.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonEvent {
    private Long personId;
    private String names;
    private String lastNames;
    private String position;
    private String documentNumber;
    private String documentType;
    private Integer personType;
    private String companyName;
    private String checkDigit;
    private String legalRepresentativeName;
    private String docNumberLegalRep;
}
