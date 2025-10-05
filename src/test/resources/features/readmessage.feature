Feature: Prueba de humo

  @smoke
  Scenario: Verify message with specific key exists in topic
    Given I am connected to kafka, listening to topic "cert_finance_billing_invoice_commands"
    When I listen for a message with key "250011" in topic "cert_finance_billing_invoice_commands"
    Then the message with key "250011" in topic "cert_finance_billing_invoice_commands" should not be empty

#  @smoke
#  Scenario: Verify message with specific key exists in topic
#    Given I am connected to kafka
#    When I listen for a message with key "250013" in topic "cert_finance_billing_invoice_commands"
#    Then the message with key "250013" in topic "cert_finance_billing_invoice_commands" should not be empty

