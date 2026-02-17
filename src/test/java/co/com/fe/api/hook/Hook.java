package co.com.fe.api.hook;

import co.com.fe.api.utils.KafkaClient;

import co.com.fe.api.tasks.PrepareUserData;
import co.com.fe.api.utils.DatabaseConnector;
import co.com.fe.api.utils.enums.UserProfile;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

import java.sql.SQLException;

public class Hook {
    @Before
    public void setup(){
        OnStage.setTheStage(new OnlineCast());

        OnStage.theActorCalled("admin").attemptsTo(
                PrepareUserData.withProfile(UserProfile.BASIC_USER)
                        .rememberContextAs("BASIC_USER"),

                PrepareUserData.withProfile(UserProfile.THIRD_PARTY_MANDATE)
                        .rememberContextAs("MANDATE_USER")
        );
    }

    @After
    public void tearDown() throws SQLException {
        DatabaseConnector.closeConnection();
        KafkaClient.getInstance().close();
    }
}
