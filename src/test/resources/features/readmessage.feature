Feature: Prueba de humo

  @smoke
  Scenario: Publish with changes
    Given I am prepared to listen the topic "cert_finance_billing_invoice_commands"
    When I send a message to topic "flypass_payments.wallet.events" with key "40312"
    Then I should see a message with uniqueId "prueba-autom-004" in topic "cert_finance_billing_invoice_commands"