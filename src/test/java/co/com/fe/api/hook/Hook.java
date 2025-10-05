package co.com.fe.api.hook;

import co.com.fe.api.abilities.ConnectToKafka;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

public class Hook {
    @Before
    public void setup(){
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("el empleado").can(CallAnApi.at(""));
        OnStage.theActorCalled("KafkaListener").can(ConnectToKafka.withDefaultConfiguration());
    }
}
