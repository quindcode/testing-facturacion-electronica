package co.com.fe.api.tasks;

import co.com.fe.api.utils.Constants;
import co.com.fe.api.utils.JsonFileReader;
import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static io.restassured.RestAssured.config;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class GetIdToken implements Task {
    @Override
    public <T extends Actor> void performAs(T actor) {
        String requestBody = JsonFileReader.readJson("login/auth_cognito_request.json");

        actor.attemptsTo(
                Post.to(Constants.COGNITO_URL)
                        .with(request -> request
                                .config(config().encoderConfig(encoderConfig().encodeContentTypeAs(
                                        Constants.CONTENT_TYPE_TOKEN,
                                        ContentType.TEXT)))
                                .header("Content-Type", Constants.CONTENT_TYPE_TOKEN)
                                .header("X-Amz-Target", Constants.AMZ_TARGET)
                                .body(requestBody))
        );

        String idToken = SerenityRest.lastResponse().jsonPath().getString("AuthenticationResult.IdToken");
        String bearerToken = "Bearer " + idToken;
        actor.remember("idToken", bearerToken);
    }

    public static GetIdToken fromCognito() {
        return instrumented(GetIdToken.class);
    }
}
