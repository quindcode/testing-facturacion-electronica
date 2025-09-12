describe ("Variabilización de datos en los fixture", ()=>{
    
    it("imprimir data resultante", ()=>{
        cy.fixture('usuarios/crearUsuarioRequestVariable.json').as('crearUsuarioRequest');
        const create_user_detail = {
            name: "jhon",
            job: 'tester'
          };
          cy.get("@crearUsuarioRequest").then((originalJson) => {
            cy.replaceVariables(originalJson, create_user_detail).then((result) => { 
                cy.log(result);
            })
          }) 
    })
});