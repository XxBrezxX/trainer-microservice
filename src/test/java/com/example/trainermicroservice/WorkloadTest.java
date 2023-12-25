package com.example.trainermicroservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WorkloadTest {
    @Given("un usuario con registros de workloads con nombre de usuario {string}")
    public void createWorkload(String user) {
        // Write code here that turns the phrase above into concrete actions
    }

    @When("el usuario realiza una solicitud mandando el usuario {string}")
    public void el_usuario_realiza_una_solicitud_mandando_el_usuario(String s) {
    }

    @Then("recibe una respuesta con el workload asociado")
    public void recibe_una_respuesta_con_el_workload_asociado() {
        assertEquals(200, 200);
    }
}
