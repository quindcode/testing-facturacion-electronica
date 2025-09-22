Feature: Transacciones en Flypay

  Yo como Ingeniero de QA
  Quiero utilizar los servicios de flypay
  Para realizar transacciones

  Scenario: Crear una transacción exitosa
    Given "el empleado" obtiene el token de autenticación
    When crea una transacción con los datos del archivo "transaction/transaction_request.json"
    Then la respuesta tiene un código de estado de 200
    And el cuerpo de la respuesta coincide con los datos esperados de la transacción
