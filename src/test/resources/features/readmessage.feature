Feature: Prueba de humo

  @smoke
  Scenario: Verify message with specific key exists in topic
    Given I am connected to kafka, listening to topic "cert_finance_billing_invoice_commands"
    When I listen for a message with key "250011" in topic "cert_finance_billing_invoice_commands"
    Then the message with key "250011" in topic "cert_finance_billing_invoice_commands" should not be empty

  @smokeP
  Scenario: Publish message
    Given I am connected to kafka, listening to topic "cert_finance_billing_invoice_commands"
    When I send a message to topic "flypass_payments.wallet.events" with key "250014"
    Then I should see a message with key "250014" in topic "cert_finance_billing_invoice_commands" with the corresponding information