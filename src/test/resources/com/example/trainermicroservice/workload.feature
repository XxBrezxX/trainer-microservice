Feature: Obtener workloads

    Scenario: Obtencion de workloads existentes
        Given un usuario con registros de workloads con nombre de usuario "Test.User"
        When el usuario realiza una solicitud mandando el usuario "Test.User"
        Then recibe una respuesta con el workload asociado