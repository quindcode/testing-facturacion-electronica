package co.com.fe.api.utils;

import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTestContext {
    private final Long personId;
    private final Long subAccountId;
    private final String documentNumber;
    private final String plate;
    private String personType = "NATURAL";
    private String documentType = "CC";

    public UserTestContext() {
        Faker faker = new Faker();
        this.personId = (long) faker.number().numberBetween(100000, 999999);
        this.subAccountId = (long) faker.number().numberBetween(200000, 299999);
        this.documentNumber = faker.idNumber().valid();
        this.plate = faker.bothify("???###").toUpperCase();
    }
}
