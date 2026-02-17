package co.com.fe.api.utils;

import co.com.fe.api.models.kafka.common.BodyDto;
import co.com.fe.api.models.kafka.common.HeaderDto;
import co.com.fe.api.models.kafka.common.KafkaEventDto;
import co.com.fe.api.models.kafka.user.*;
import com.github.javafaker.Faker;

import java.time.Instant;

public class KafkaEventFactory {
        private static final Faker faker = new Faker();

        public static KafkaEventDto<PersonEvent> buildPersonEvent(UserTestContext ctx) {
                return KafkaEventDto.<PersonEvent>builder()
                                .header(HeaderDto.builder().correlationId(faker.internet().uuid()).build())
                                .body(BodyDto.<PersonEvent>builder()
                                                .eventName("CHANGED_PERSON")
                                                .domainEntity("CUSTOMER_PERSON")
                                                .eventTimestamp(Instant.now().toString())
                                                .eventType("I")
                                                .eventData(PersonEvent.builder()
                                                                .personId(ctx.getPersonId())
                                                                .names(faker.name().firstName())
                                                                .lastNames(faker.name().lastName())
                                                                .documentNumber(ctx.getDocumentNumber())
                                                                .documentType("CC")
                                                                .personType(1)
                                                                .companyName(faker.company().name())
                                                                .build())
                                                .build())
                                .build();
        }

        public static KafkaEventDto<AccountEvent> buildAccountEvent(UserTestContext ctx) {
                return KafkaEventDto.<AccountEvent>builder()
                                .header(HeaderDto.builder().correlationId(faker.internet().uuid()).build())
                                .body(BodyDto.<AccountEvent>builder()
                                                .eventName("CHANGED_CUSTOMER_ACCOUNT")
                                                .domainEntity("CUSTOMER_ACCOUNT")
                                                .eventTimestamp(Instant.now().toString())
                                                .eventType("I")
                                                .eventData(AccountEvent.builder()
                                                                .accountId(ctx.getSubAccountId())
                                                                .personId(ctx.getPersonId())
                                                                .subAccountId(ctx.getSubAccountId())
                                                                .walletId(ctx.getSubAccountId())
                                                                .subAccountState("REGISTERED")
                                                                .administrativeArea("05001")
                                                                .build())
                                                .build())
                                .build();
        }

        public static KafkaEventDto<ContactEvent> buildContactEvent(UserTestContext ctx) {
                return KafkaEventDto.<ContactEvent>builder()
                                .header(HeaderDto.builder().correlationId(faker.internet().uuid()).build())
                                .body(BodyDto.<ContactEvent>builder()
                                                .eventName("CHANGED_CUSTOMER_ACCOUNT_CONTACT")
                                                .domainEntity("CUSTOMER_ACCOUNT_CONTACT")
                                                .eventTimestamp(Instant.now().toString())
                                                .eventType("I")
                                                .eventData(ContactEvent.builder()
                                                                .contactId((long) faker.number().numberBetween(1000,
                                                                                9999))
                                                                .subAccountId(ctx.getSubAccountId()) // LINK TO ACCOUNT
                                                                .names(faker.name().firstName())
                                                                .email(faker.internet().emailAddress())
                                                                .type("PRIMARY")
                                                                .contactState("ACTIVE")
                                                                .build())
                                                .build())
                                .build();
        }

        public static KafkaEventDto<InvoiceDataEvent> buildBillingDataEvent(UserTestContext ctx) {
                return KafkaEventDto.<InvoiceDataEvent>builder()
                                .header(HeaderDto.builder().correlationId(faker.internet().uuid()).build())
                                .body(BodyDto.<InvoiceDataEvent>builder()
                                                .eventName("CHANGED_INVOICE_GENERATION_DATA")
                                                .eventData(InvoiceDataEvent.builder()
                                                                .subAccountId(ctx.getSubAccountId())
                                                                .documentNumber(ctx.getDocumentNumber())
                                                                .documentType("CC")
                                                                .personType("NATURAL")
                                                                .companyName(faker.company().name())
                                                                .address(faker.address().fullAddress())
                                                                .mandateInvoice(true)
                                                                .build())
                                                .build())
                                .build();
        }

        public static KafkaEventDto<ThirdPartyEvent> buildThirdPartyEvent(UserTestContext ctx, boolean isMandate) {
                return KafkaEventDto.<ThirdPartyEvent>builder()
                                .header(HeaderDto.builder().version(1).traceId(faker.internet().uuid()).build())
                                .body(BodyDto.<ThirdPartyEvent>builder()
                                                .eventName("UPDATED_THIRD_PARTY_DATA")
                                                .eventData(ThirdPartyEvent.builder()
                                                                .flykey(ctx.getPlate())
                                                                .firstName(faker.name().firstName())
                                                                .firstSurname(faker.name().lastName())
                                                                .email(faker.internet().emailAddress())
                                                                .address(faker.address().streetAddress())
                                                                .document(faker.idNumber().valid())
                                                                .documentType(22)
                                                                .isMandate(isMandate)
                                                                .status(true)
                                                                .build())
                                                .build())
                                .build();
        }
}
