package co.com.fe.api.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

import static net.serenitybdd.rest.SerenityRest.lastResponse;

public class TheResponseBodyValue implements Question<Object> {

    private final String jsonPath;

    public TheResponseBodyValue(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public static TheResponseBodyValue at(String jsonPath) {
        return new TheResponseBodyValue(jsonPath);
    }

    @Override
    public Object answeredBy(Actor actor) {
        return lastResponse().jsonPath().get(jsonPath);
    }
}