describe('Register spec', () => {
    it('Register succesfull', () => {

        cy.intercept('POST', '/api/auth/register', {
            body: {
                email: 'Jean.Dupont@Gmail.com',
                firstName: 'Jean',
                lastName: 'Dupont',
                password: "test!1234"
            },
        }).as('registerRequest');

        cy.visit('')
        cy.get('span[routerLink="register"]').click();
        cy.url().should('include', '/register');

        cy.get('input[formControlName=firstName]').type("Jean")
        cy.get('input[formControlName=lastName]').type("Dupont")
        cy.get('input[formControlName=email]').type("Jean.Dupont@Gmail.com")
        cy.get('input[formControlName=password]').type("test!1234")
        cy.get('button[type="submit"]').click();
        cy.url().should('include', '/login')

    })
    it('Register error', () => {

        cy.intercept('POST', '/api/auth/register', {
            statusCode: 400,
            body: {
                message: "Error: Email is already taken!"
            },
        }).as('registerRequestError');

        

        cy.visit('')
        cy.get('span[routerLink="register"]').click();
        cy.url().should('include', '/register');
        cy.get('input[formControlName=firstName]').type("Jean")
        cy.get('input[formControlName=lastName]').type("Dupont")
        cy.get('input[formControlName=email]').type("Jean.Dupont@Gmail.com")
        cy.get('input[formControlName=password]').type("test!1234")
        cy.get('button[type="submit"]').click();
        cy.wait('@registerRequestError').then(({ response }) => {
            expect(response.statusCode).to.eq(400);
            expect(response.body.message).to.eq('Error: Email is already taken!');
          });
          cy.get('span').contains('An error occurred').should('exist');
    })

});