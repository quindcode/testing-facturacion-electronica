package co.com.fe.api.models.dbmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Invoice {
    private String id;
    private String invoiceNumber;
    private String cufe;
    private String uniqueTransactionId;
    private String aggregateRootId;
    private BigDecimal subtotalAmount;
    private BigDecimal totalTaxes;
    private BigDecimal totalAmount;
    private String status;
    private Timestamp appliedDate;
    private Timestamp createdDate;
    private Timestamp acceptanceDate;
    private Timestamp rejectedDate;
    private Timestamp issuedDate;
    private Timestamp cancelledDate;
}