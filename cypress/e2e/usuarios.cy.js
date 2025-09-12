import Ajv from 'ajv';
const ajv = new Ajv();

describe ("Obtener usuarios del sistema", ()=>{
    beforeEach(() => {
        cy.fixture('usuarios/crearUsuarioRequest.json').as('crearUsuarioRequest');
        cy.fixture('usuarios/listadoUsuariosEscheme.json').as('listadoUsuariosEscheme');
        cy.fixture('usuarios/obtenerUsuarioResponse.json').as('obtenerUsuarioResponse');
        cy.fixture('usuarios/obtenerUsuarioScheme.json').as('obtenerUsuarioScheme');
      });
    it("usando id del usuario", ()=>{
        cy.request({
            method: 'GET',
            url: '/users/2',
          }).then((response) => {
            // Realiza las aserciones necesarias sobre la respuesta recibida
            expect(response.status).to.eq(200);
            cy.get('@obtenerUsuarioResponse').then((fixtureData) => {
                // Realiza una aserción para verificar si la respuesta cumple con los valores esperados
                expect(response.body).to.deep.equal(fixtureData);
            });   
            // Realiza la validación del esquema utilizando ajv
            cy.get('@obtenerUsuarioScheme').then((schema) => {
                cy.schemeValidation(response, schema)
            });         
          });
          
    })
    it("por listas paginadas", ()=>{
        cy.request({
            method: 'GET',
            url: '/users?page=2',
          }).then((response) => {
            // Realiza las aserciones necesarias sobre la respuesta recibida
            expect(response.status).to.eq(200);
            // Realiza la validación del esquema utilizando ajv
            cy.get('@listadoUsuariosEscheme').then((schema) => {
                cy.schemeValidation(response, schema)
            });                    
          });
    })
})