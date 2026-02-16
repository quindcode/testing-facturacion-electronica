package co.com.fe.api.models.kafka.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactEvent {
    private Long contactId;
    private Long subAccountId;
    private String names;
    private String lastNames;
    private String position;
    private String email;
    private String prefix;
    private String phone;
    private String cellphone;
    private String type;
    private String contactState;
}
