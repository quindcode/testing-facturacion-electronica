package co.com.fe.api.models.provider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceData {
    @JsonProperty("intID")
    private String intId;

    private List<Item> items;

    private String note1;
    private String note2;
    private String field1;

    // Este field2 parece ser el uniqueTransactionId o similar
    private String field2;

    private String prefix;
    private Amounts amounts;
    private String dueDate;
    private String bucketS3;
    private Customer customer;
    private String rangeKey;
    private String issueDate;
    private String issueTime;
    private String additional;
    private String equivalente;
    private String paymentCode;
    private String paymentType;
}