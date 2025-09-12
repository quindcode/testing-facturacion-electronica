const AWS = require('aws-sdk');

describe('Prueba usando credenciales temporales de AWS', () => {
  before(() => {
    // Obtiene las credenciales temporales antes de las pruebas
    cy.task("awsSetTemporaryCredentials").then(credentials => {
      cy.log(credentials)
      AWS.config.update({
        accessKeyId: credentials.accessKeyId,
        secretAccessKey: credentials.secretAccessKey,
        sessionToken: credentials.sessionToken,
        region: credentials.region
      });
    });
  });

  it('Prueba de llamada a AWS usando credenciales temporales', () => {
    // Realiza una llamada de prueba a DynamoDB para verificar la conexión
    cy.task("awsSetTemporaryCredentials").then(credentials => {
      AWS.config.update({
        accessKeyId: credentials.accessKeyId,
        secretAccessKey: credentials.secretAccessKey,
        sessionToken: credentials.sessionToken,
        region: credentials.region
      });

      const dynamoDB = new AWS.DynamoDB();

      dynamoDB.listTables({}, (err, data) => {
        if (err) {
          cy.log('Error al listar las tablas:', err);
          throw new Error(`Error al listar las tablas: ${err.message}`);
        } else {
          cy.log('Tablas:', data);
          expect(data).to.have.property('TableNames');
        }
      });
    });
  });
});
