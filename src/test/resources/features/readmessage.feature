Feature: Prueba de humo

  @smoke
  Scenario: Publish message
    Given I am connected to kafka, listening to topic "cert_finance_billing_invoice_commands"
    When I send a message to topic "flypass_payments.wallet.events" with key "40312"
    Then I should see a message with key "40312" in topic "cert_finance_billing_invoice_commands" with the corresponding information