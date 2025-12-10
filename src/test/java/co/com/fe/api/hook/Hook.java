package co.com.fe.api.hook;

import co.com.fe.api.abilities.ConnectToKafka;
import co.com.fe.api.abilities.UseDatabase;
import co.com.fe.api.utils.DatabaseConnector;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

import java.sql.SQLException;

public class Hook {
    @Before
    public void setup(){
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("admin").can(ConnectToKafka.withDefaultConfiguration());
        OnStage.theActorCalled("admin").can(UseDatabase.connect());
    }

    @After
    static void tearDown() throws SQLException {
        DatabaseConnector.closeConnection();
    }
}
