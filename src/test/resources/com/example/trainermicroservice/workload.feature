Feature: Pruebas del controlador Trainer, responsable de los workloads

  Background: Eliminacion de todos los workloads
    When el usuario desea borrar los workloads
    Then la respuesta tiene codigo "OK"

  Scenario: Creacion de un workload
    Given el usuario "Test.User" ingresa sus datos en el formulario
    When el usuario envia sus datos a la url "/trainer/modify"
    Then la respuesta tiene codigo "Accepted"

  Scenario: Obtencion de los workloads de un usuario
    Given el usuario "Test.User" ingresa sus datos en el formulario
    And el usuario envia sus datos a la url "/trainer/modify"
    When el usuario desea obtener el workload del usuario "Test.User" mediante la url "/trainer/workload"
    Then la respuesta tiene codigo "Accepted"

  Scenario: Obtencion de workloads inexistentes
    When el usuario desea obtener el workload del usuario "Test.User" mediante la url "/trainer/workload"
    Then la respuesta tiene codigo "Bad Request"

  Scenario: La cantidad de horas corresponde a la suma
    Given el usuario "Test.User" ingresa sus datos en el formulario
    And el usuario envia sus datos a la url "/trainer/modify"
    But se debe acumular las horas del response
    When el usuario envia sus datos a la url "/trainer/modify"
    And se debe acumular las horas del response
    Then la suma debe corresponder al 2 envios
