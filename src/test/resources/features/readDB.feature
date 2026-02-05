Feature: Read postgresql database

  @e2e
  Scenario: Billing for service consumption
    Given I send a message to topic "flypass_payments.wallet.events" with key "40312"
    When I wait 3 seconds for the event to process
    Then I can see the invoice in billing database with status "ACCEPTED_IN_PROCESS" or "ISSUED"

  @e2e
  Scenario: Verify invoice audit trail for successful generated invoice
    Given I send a message to topic "flypass_payments.wallet.events" with key "40312"
    When I wait until the invoice for has status "ISSUED"
    Then I can verify the audit trail contains the expected transitions