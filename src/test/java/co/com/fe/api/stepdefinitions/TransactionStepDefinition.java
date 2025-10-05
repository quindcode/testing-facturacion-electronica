package co.com.fe.api.stepdefinitions;

import co.com.fe.api.questions.TheResponseBodyValue;
import co.com.fe.api.questions.TheResponseStatus;
import co.com.fe.api.tasks.CreateTransaction;
import co.com.fe.api.tasks.GetIdToken;
import co.com.fe.api.utils.Constants;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;

public class TransactionStepDefinition {

    @Given("{string} obtiene el token de autenticación")
    public void anActorObtainsTheAuthenticationToken(String actorName) {
        OnStage.theActorCalled(actorName).attemptsTo(
                GetIdToken.fromCognito()
        );
    }

    @When("crea una transacción con los datos del archivo {string}")
    public void createATransactionWithTheFileData(String filePath) {
        theActorInTheSpotlight().attemptsTo(
                CreateTransaction.withDataFromFile(filePath)
        );
    }

    @Then("la respuesta tiene un código de estado de {int}")
    public void theResponseHasAStatusCodeOf(int code) {
        OnStage.theActorInTheSpotlight().should(
                seeThat("el código de estado",
                        TheResponseStatus.ofTheRequest(), equalTo(Constants.STATUS_OK))
        );
    }

    @And("el cuerpo de la respuesta coincide con los datos esperados de la transacción")
    public void theResponseBodyMatchesTheExpectedTransactionData() {
        String expectedEmployeeId = "employee-1152440509";
        String expectedPlate = "JTM000";
        int expectedStatusId = 0;

        theActorInTheSpotlight().should(
                seeThat("el ID del empleado es el correcto", TheResponseBodyValue.at("employee.id"), equalTo(expectedEmployeeId)),
                seeThat("la placa del vehículo es la correcta", TheResponseBodyValue.at("plate"), equalTo(expectedPlate)),
                seeThat("el ID de estado es el correcto", TheResponseBodyValue.at("status.id"), equalTo(expectedStatusId))
        );
    }
}
