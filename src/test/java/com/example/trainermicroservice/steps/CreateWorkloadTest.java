package com.example.trainermicroservice.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.trainermicroservice.model.TrainerWorkload;
import com.example.trainermicroservice.model.TrainerWorkloadRequest;
import com.example.trainermicroservice.model.TrainerWorkloadRequest.ActionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CreateWorkloadTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private TrainerWorkloadRequest trainerWorkloadRequest = new TrainerWorkloadRequest();
    private ResponseEntity<String> response;
    private Integer totalHours = 0;

    @Given("el usuario {string} ingresa sus datos en el formulario")
    public void el_usuario_ingresa_sus_datos_en_el_formulario(String user) {
        trainerWorkloadRequest.setActionType(ActionType.ADD);
        trainerWorkloadRequest.setActive(true);
        trainerWorkloadRequest.setTrainerFirstName("name");
        trainerWorkloadRequest.setTrainerLastName("last");
        trainerWorkloadRequest.setTrainerUsername(user);
        trainerWorkloadRequest.setTrainingDate(LocalDate.of(2000, 1, 1));
        trainerWorkloadRequest.setTrainingDuration(20);
    }

    @When("el usuario envia sus datos a la url {string}")
    public void el_usuario_da_clic_en_enviar(String url) throws Exception {
        response = restTemplate.exchange(url,
                HttpMethod.PUT,
                new HttpEntity<>(trainerWorkloadRequest, new HttpHeaders()),
                String.class);
    }

    @When("el usuario desea obtener el workload del usuario {string} mediante la url {string}")
    public void el_usuario_desea_obtener_el_workload_del_usuario(String user, String url) {
        response = restTemplate.exchange(url.concat("?trainerUsername=").concat(user),
                HttpMethod.GET,
                new HttpEntity<>(trainerWorkloadRequest, new HttpHeaders()),
                String.class);
    }

    @When("el usuario desea borrar los workloads")
    public void el_usuario_desea_borrar_los_workloads() {
        response = restTemplate.exchange("/trainer/delete",
                HttpMethod.DELETE,
                null,
                String.class);
    }

    @Then("verifica que se haya generado correctamente")
    public void verifica_que_se_haya_generado_correctamente() {
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Then("la respuesta tiene codigo {string}")
    public void la_respuesta_debe_ser_erronea_con_codigo(String codigo) {
        switch (codigo) {
            case "Bad Request":
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                break;
            case "Accepted":
                assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
                break;
            case "OK":
                assertEquals(HttpStatus.OK, response.getStatusCode());
                break;
            case "Not Acceptable":
                assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
                break;
            default:
                break;
        }
    }

    @Then("la suma debe corresponder al {int} envios")
    public void la_suma_debe_corresponder_al_envios(Integer times)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        TrainerWorkload trainerWorkload = null;
        trainerWorkload = objectMapper.readValue(response.getBody(), TrainerWorkload.class);
        assertEquals(trainerWorkload.getTrainingDuration() * times, totalHours);
    }

    @When("se debe acumular las horas del response")
    public void se_debe_acumular_las_horas_del_response() throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        TrainerWorkload trainerWorkload = null;
        trainerWorkload = objectMapper.readValue(response.getBody(), TrainerWorkload.class);

        totalHours += trainerWorkload.getTrainingDuration();
    }
}
