package co.com.fe.api.tasks;

import co.com.fe.api.utils.Constants;
import co.com.fe.api.utils.JsonFileReader;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.io.PrintStream;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CreateTransaction implements Task {

    private final String filePath;

    public CreateTransaction(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String requestBody = JsonFileReader.readJson(filePath);
        String idToken = actor.recall("idToken"); // Recupera el token del actor

//        PrintStream requestLog = System.out; TODO eliminar

        actor.attemptsTo(
                Post.to(Constants.BASE_URL + "transactions")
                        .with(request -> request
//                                .filter(new RequestLoggingFilter(requestLog)) // <-- LÍNEA CLAVE TODO eliminar
                                .header("authorization", idToken)
                                .contentType(ContentType.JSON)
                                .body(requestBody))
        );
    }

    public static CreateTransaction withDataFromFile(String filePath) {
        return Tasks.instrumented(CreateTransaction.class, filePath);
    }
}