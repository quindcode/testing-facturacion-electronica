describe ("Inicio de sesión de usuarios", ()=>{
    beforeEach(() => {
        cy.fixture('login/loginExitosoRequest.json').as('loginExitosoRequest');
        cy.fixture('login/loginExitosoResponse.json').as('loginExitosoResponse');
        cy.fixture('login/loginExitosoScheme.json').as('loginExitosoScheme');
        cy.fixture('login/loginFallidoRequest.json').as('loginFallidoRequest');
        cy.fixture('login/loginFallidoResponse.json').as('loginFallidoResponse');
        cy.fixture('login/loginFallidoScheme.json').as('loginFallidoScheme');
      });
    it("con credenciales válidas", ()=>{
        cy.get('@loginExitosoRequest').then((requestBody) => {
            cy.request({
              method: 'POST',
              url: '/login',
              body: requestBody
            }).then((response) => {
                cy.log(response)
                expect(response.status).to.eq(200);
                cy.get('@loginExitosoResponse').then((fixtureData) => {
                    // Realiza una aserción para verificar si la respuesta cumple con los valores esperados
                    expect(response.body).to.deep.equal(fixtureData);
                });
              // Realiza la validación del esquema utilizando ajv
                cy.get('@loginExitosoScheme').then((schema) => {
                  cy.schemeValidation(response, schema)
                });
            });
          });
    })
    it("sin contraseña", ()=>{
        cy.get('@loginFallidoRequest').then((requestBody) => {
            cy.request({
              method: 'POST',
              url: '/login',
              failOnStatusCode: false,
              body: requestBody
            }).then((response) => {
              cy.log(response)
              expect(response.status).to.eq(400);
              // Realizar aserciones sobre la respuesta obtenida
                cy.get('@loginFallidoResponse').then((fixtureData) => {
                    // Realiza una aserción para verificar si la respuesta cumple con los valores esperados
                    expect(response.body).to.deep.equal(fixtureData);
                });
                cy.get('@loginFallidoScheme').then((schema) => {
                  cy.schemeValidation(response, schema)
                });
            });
          });
    })
})